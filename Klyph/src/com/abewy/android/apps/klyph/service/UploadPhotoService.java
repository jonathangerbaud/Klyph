/**
 * Copyright   2013 Abewy. All rights reserved
 * 
 * @date 2 avr. 2013
 * @author Jonathan
 */
package com.abewy.android.apps.klyph.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.core.util.NotificationUtil;
import com.abewy.android.apps.klyph.R;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Session;

public class UploadPhotoService extends Service
{
	private static final String	MY_PHOTOS		= "me/photos";
	private static final String	MY_VIDEOS		= "me/videos";
	private static final String	PICTURE_PARAM	= "picture";

	private Looper				mServiceLooper;
	private ServiceHandler		mServiceHandler;

	public UploadPhotoService()
	{
		super();
	}

	@Override
	public void onCreate()
	{
		HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper, new WeakReference<Service>(this));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (intent != null)
		{
			Message msg = mServiceHandler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putStringArrayList(KlyphBundleExtras.UPLOAD_PHOTO_URIS,
					intent.getStringArrayListExtra(KlyphBundleExtras.UPLOAD_PHOTO_URIS));
			bundle.putString(KlyphBundleExtras.UPLOAD_PHOTO_CAPTION,
					intent.getStringExtra(KlyphBundleExtras.UPLOAD_PHOTO_CAPTION));
			bundle.putString(KlyphBundleExtras.UPLOAD_PHOTO_PLACE,
					intent.getStringExtra(KlyphBundleExtras.UPLOAD_PHOTO_PLACE));
			bundle.putString(KlyphBundleExtras.UPLOAD_PHOTO_PRIVACY,
					intent.getStringExtra(KlyphBundleExtras.UPLOAD_PHOTO_PRIVACY));
			bundle.putString(KlyphBundleExtras.UPLOAD_PHOTO_ALBUM,
					intent.getStringExtra(KlyphBundleExtras.UPLOAD_PHOTO_ALBUM));
			bundle.putStringArrayList(KlyphBundleExtras.UPLOAD_PHOTO_TAGS,
					intent.getStringArrayListExtra(KlyphBundleExtras.UPLOAD_PHOTO_TAGS));
			msg.setData(bundle);
			mServiceHandler.sendMessage(msg);	
		}

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	static class Image
	{
		private String	uri;
		private String	album;
		private String	caption;
		private String	place;
		private String	privacy;
		private List<String> tags;

		public Image(String uri, String album, String caption, String place, String privacy, List<String> tags)
		{
			this.uri = uri;
			this.album = album;
			this.caption = caption;
			this.place = place;
			this.privacy = privacy;
			this.tags = tags;
		}

		public String getUri()
		{
			return uri;
		}

		public String getAlbum()
		{
			return album;
		}

		public String getCaption()
		{
			return caption;
		}

		public String getPlace()
		{
			return place;
		}

		public String getPrivacy()
		{
			return privacy;
		}
		
		public List<String> getTags()
		{
			return tags;
		}
	}

	// Handler that receives messages from the thread
	private static final class ServiceHandler extends Handler
	{
		private final WeakReference<Service>		service;
		private List<Image>							images;
		private int									total		= 0;
		private int									errors		= 0;
		private boolean								isLoading	= false;
		private final int							notificationId;
		private final NotificationCompat.Builder	builder;

		public ServiceHandler(Looper looper, WeakReference<Service> service)
		{
			super(looper);

			this.service = service;
			images = new ArrayList<UploadPhotoService.Image>();
			notificationId = (int) System.currentTimeMillis();

			builder = new NotificationCompat.Builder(service.get()).setSmallIcon(R.drawable.ic_notification)
					.setContentTitle(service.get().getString(R.string.uploading_images))
					.setTicker(service.get().getString(R.string.uploading_images)).setAutoCancel(true)
					.setOnlyAlertOnce(false);
			
			NotificationUtil.setDummyIntent(service.get(), builder);
		}

		@Override
		public void handleMessage(Message msg)
		{
			Bundle bundle = msg.getData();

			List<String> path = bundle.getStringArrayList(KlyphBundleExtras.UPLOAD_PHOTO_URIS);
			String caption = bundle.getString(KlyphBundleExtras.UPLOAD_PHOTO_CAPTION);
			String place = bundle.getString(KlyphBundleExtras.UPLOAD_PHOTO_PLACE);
			String privacy = bundle.getString(KlyphBundleExtras.UPLOAD_PHOTO_PRIVACY);
			String album = bundle.getString(KlyphBundleExtras.UPLOAD_PHOTO_ALBUM);
			List<String> tags = bundle.getStringArrayList(KlyphBundleExtras.UPLOAD_PHOTO_TAGS);
			
			if (album == null)
			{
				album = "me";
			}

			if (path != null && path.size() > 0)
			{
				for (String uri : path)
				{
					Image image = new Image(uri, album, caption, place, privacy, tags);
					images.add(image);
					total++;
				}

				if (isLoading == false)
				{
					uploadNext();
				}
			}
		}

		private void uploadNext()
		{
			isLoading = true;

			final Image image = images.get(0);
			images.remove(0);

			Bundle parameters = new Bundle();

			if (image.getCaption() != null && image.getCaption().length() > 0)
			{
				parameters.putString("caption", image.getCaption());
			}

			if (image.getPlace() != null && image.getPlace().length() > 0)
			{
				parameters.putString("place", image.getPlace());
			}

			if (image.getPrivacy() != null && image.getPrivacy().length() > 0)
			{
				parameters.putString("privacy", image.getPrivacy());
			}
			
			if (image.getTags() != null && image.getTags().size() > 0)
			{
				JSONArray array = new JSONArray();
				
				for (String id : image.getTags())
				{
					Log.d("UpldoadService", "tag id " + id);
					JSONObject object = new JSONObject();
					try
					{
						object.putOpt("tag_uid", id);
					}
					catch (JSONException e)
					{
						continue;
					}
					
					array.put(object);
				}
				
				parameters.putString("tags", array.toString());
			}

			File file = new File(image.getUri());
			ParcelFileDescriptor descriptor;
			try
			{
				descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				showFileNotFoundNotification(image.getUri());
				return;
			}

			parameters.putParcelable(PICTURE_PARAM, descriptor);
			
			Request request = new Request(Session.getActiveSession(), image.getAlbum() + "/photos", parameters, HttpMethod.POST,
					new Request.Callback() {

						@Override
						public void onCompleted(com.facebook.Response response)
						{
							if (response.getError() != null)
							{
								errors++;
								Log.d("UploadService", response.getError().toString());
								showErrorNotification(response.getError().getErrorMessage());
							}
							
							if (images.size() > 0)
							{
								uploadNext();
							}
							else
							{
								showEndNotification();
								service.get().stopSelf();
							}
						}
					});

			showNotification();
			Request.executeBatchAsync(request);
		}

		private void showFileNotFoundNotification(String uri)
		{
			if (service.get() == null)
				return;

			Service s = service.get();
			
			final int notificationId = (int) System.currentTimeMillis();

			NotificationCompat.Builder builder = new NotificationCompat.Builder(service.get()).setSmallIcon(R.drawable.ic_notification)
					.setTicker(service.get().getString(R.string.upload_error)).setAutoCancel(true)
					.setOnlyAlertOnce(true);
			NotificationUtil.setDummyIntent(service.get(), builder);

			builder.setContentTitle(service.get().getString(R.string.upload_error));
			builder.setContentText(service.get().getString(R.string.file_not_found, uri));

			final NotificationManager mNotificationManager = (NotificationManager) s
					.getSystemService(Context.NOTIFICATION_SERVICE);

			mNotificationManager.notify(AttrUtil.getString(s, R.string.app_name), notificationId, builder.build());
		}

		private void showNotification()
		{
			if (service.get() == null)
				return;

			Service s = service.get();

			builder.setTicker(s.getString(R.string.uploading_image_of, total - images.size(), total));
			builder.setContentText(s.getString(R.string.uploading_image_of, total - images.size(), total));
			builder.setProgress(100, 0, true);

			final NotificationManager mNotificationManager = (NotificationManager) s
					.getSystemService(Context.NOTIFICATION_SERVICE);

			mNotificationManager.notify(AttrUtil.getString(s, R.string.app_name), notificationId, builder.build());
		}

		private void showEndNotification()
		{
			if (service.get() == null)
				return;

			Service s = service.get();

			builder.setTicker(service.get().getString(R.string.upload_complete));
			builder.setContentTitle(service.get().getString(R.string.upload_complete));
			builder.setContentText(s.getString(R.string.n_uploaded_images, total - errors));
			builder.setProgress(0, 0, false);

			final NotificationManager mNotificationManager = (NotificationManager) s
					.getSystemService(Context.NOTIFICATION_SERVICE);

			mNotificationManager.notify(AttrUtil.getString(s, R.string.app_name), notificationId, builder.build());
		}
		
		private void showErrorNotification(String fbError)
		{
			if (service.get() == null || fbError == null)
				return;

			Service s = service.get();
			
			final int notificationId = (int) System.currentTimeMillis();

			NotificationCompat.Builder builder = new NotificationCompat.Builder(service.get()).setSmallIcon(R.drawable.ic_notification)
					.setTicker(service.get().getString(R.string.upload_error)).setAutoCancel(true)
					.setOnlyAlertOnce(true);
			
			NotificationUtil.setDummyIntent(service.get(), builder);

			builder.setContentTitle(service.get().getString(R.string.upload_error));
			builder.setContentText(fbError);

			final NotificationManager mNotificationManager = (NotificationManager) s
					.getSystemService(Context.NOTIFICATION_SERVICE);

			mNotificationManager.notify(AttrUtil.getString(s, R.string.app_name), notificationId, builder.build());
		}
	}
}
