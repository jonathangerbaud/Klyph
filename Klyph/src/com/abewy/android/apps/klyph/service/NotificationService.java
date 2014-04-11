package com.abewy.android.apps.klyph.service;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphNotification;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.fql.Notification;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.imageloader.SimpleFakeImageLoaderListener;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.crashlytics.android.Crashlytics;
import com.facebook.Session;
import com.squareup.picasso.Picasso.LoadedFrom;

public class NotificationService extends Service
{
	private static final String TAG = "NotificationService";
	
	private Looper			mServiceLooper;
	private ServiceHandler	mServiceHandler;

	@Override
	public void onCreate()
	{
		// Why initialize device values ?
		// Because we need the density to calculate image size
		// In notification request
		KlyphDevice.initDeviceValues(this);
		
		HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper, new WeakReference<Service>(this));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		mServiceHandler.sendMessage(msg);

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy()
	{

	}

	// Handler that receives messages from the thread
	private static final class ServiceHandler extends Handler
	{
		private final WeakReference<Service>	service;

		public ServiceHandler(Looper looper, WeakReference<Service> service)
		{
			super(looper);
			this.service = service;
		}

		@Override
		public void handleMessage(Message msg)
		{
			launchRequest();

			Intent intent = new Intent(KlyphBundleExtras.NOTIFICATION_EVENT);
			// Add data
			intent.putExtra(KlyphBundleExtras.NOTIFICATION_EVENT_DATA, "test broadcast");
			LocalBroadcastManager.getInstance(service.get()).sendBroadcast(intent);
			
			// Force a reload of the notification pane even if there isn't any update
			KlyphPreferences.setNotificationReadStatusChanged(true);
		}

		private void launchRequest()
		{
			Log.d("NotificationService.ServiceHandler", "launchRequest: ");
			if (service.get() == null)
				return;

			try
			{
				if (Session.getActiveSession() == null)
				{
					Session.openActiveSessionFromCache(service.get());
				}
			}
			catch (UnsupportedOperationException e)
			{
				Log.d("NotificationService.ServiceHandler", "launchRequest: exception " + e.getMessage());
			}

			if (Session.getActiveSession() != null && Session.getActiveSession().getPermissions().contains("manage_notifications"))
			{
				AsyncRequest request = new AsyncRequest(Query.PERIODIC_NOTIFICATIONS, "me()",
						/*KlyphPreferences.getNotificationServiceOffset()*/"", new AsyncRequest.Callback() {

							@Override
							public void onComplete(Response response)
							{
								if (response.getError() == null)
								{
									onRequestSuccess(response.getGraphObjectList());
								}
								else
								{
									if (service.get() != null)
										service.get().stopSelf();
								}
							}
						});

				request.execute();
			}
			else
			{
				if (service.get() != null)
					service.get().stopSelf();
			}
		}

		private void onRequestSuccess(List<GraphObject> list)
		{
			Log.d("NotificationService.ServiceHandler", "onRequestSuccess: " + list.size());
			if (service.get() == null)
				return;

			if (list.size() > 0)
			{
				boolean notifyAppRequests = KlyphPreferences.notifyAppRequests();
				boolean notifyAlbums = KlyphPreferences.notifyAlbums();
				boolean notifyEvents = KlyphPreferences.notifyEvents();
				boolean notifyFriends = KlyphPreferences.notifyFriendRequest();
				boolean notifyGroups = KlyphPreferences.notifyGroups();
				boolean notifyPages = KlyphPreferences.notifyPages();
				boolean notifyPhotos = KlyphPreferences.notifyPhotos();
				boolean notifyPokes = KlyphPreferences.notifyPokes();
				boolean notifyStreams = KlyphPreferences.notifyStreams();
				boolean notifyVideos = KlyphPreferences.notifyVideos();


				List<Notification> filteredNotifications = new ArrayList<Notification>();
				
				List<String> types = new ArrayList<String>();
				types.add(Notification.TYPE_ALBUM);
				types.add(Notification.TYPE_APP_REQUEST);
				types.add(Notification.TYPE_COMMENT);
				types.add(Notification.TYPE_EVENT);
				types.add(Notification.TYPE_CANCELED_EVENT);
				types.add(Notification.TYPE_FRIEND);
				types.add(Notification.TYPE_GROUP);
				types.add(Notification.TYPE_PAGE);
				types.add(Notification.TYPE_PHOTO);
				types.add(Notification.TYPE_POKE);
				types.add(Notification.TYPE_STREAM);
				types.add(Notification.TYPE_VIDEO);
				types.add(Notification.TYPE_WEB_APP);
				types.add(Notification.TYPE_USER);
				
				boolean  mustGroup = KlyphPreferences.mustGroupNotifications();
				long lastCheckedTime = KlyphPreferences.getLastCheckedNotificationTime();

				for (GraphObject graphObject : list)
				{
					Notification n = (Notification) graphObject;
					String objectType = n.getObject_type();
					
					// Trying to discover unreferenced types
					if (!types.contains(objectType))
					{
						Crashlytics.setString("Notification_type ", objectType);
						Crashlytics.setString("Object_name ", n.getObject_name());
						Crashlytics.setString("Notification_text ", n.getTitle_text());
						Crashlytics.setString("Notification_url ", n.getHref());

						try
						{
							throw new Exception("NotificationService unknown notification type : " + objectType);
						}
						catch (Exception e)
						{
							Crashlytics.logException(e);
						}
					}

					// Filtering
					if (objectType.equals(Notification.TYPE_ALBUM) && notifyAlbums == false)
						continue;
					
					if (objectType.equals(Notification.TYPE_APP_REQUEST) && notifyAppRequests == false)
						continue;
					
					if ((objectType.equals(Notification.TYPE_EVENT) || objectType.equals(Notification.TYPE_CANCELED_EVENT)) && notifyEvents == false)
						continue;
					
					if ((objectType.equals(Notification.TYPE_FRIEND) || objectType.equals(Notification.TYPE_USER))&& notifyFriends == false)
						continue;
					
					if (objectType.equals(Notification.TYPE_GROUP) && notifyGroups == false)
						continue;

					if (objectType.equals(Notification.TYPE_PAGE) && notifyPages == false)
						continue;
					
					if (objectType.equals(Notification.TYPE_PHOTO) && notifyPhotos == false)
						continue;
					
					if (objectType.equals(Notification.TYPE_POKE) && notifyPokes == false)
						continue;
					
					if (objectType.equals(Notification.TYPE_STREAM) && notifyStreams == false)
						continue;

					if (objectType.equals(Notification.TYPE_VIDEO) && notifyVideos == false)
						continue;
					
					if (mustGroup)
					{
						// Add only if newer than last checked time
						try
						{
							long time = Long.parseLong(n.getUpdated_time());
							
							if (time > lastCheckedTime)
								filteredNotifications.add(n);
						}
						catch (NumberFormatException e)
						{
							filteredNotifications.add(n);
						}
					}
					else
					{
						filteredNotifications.add(n);
					}
				}
				
				if (filteredNotifications.size() > 0)
				{
					sendNotifications(filteredNotifications);
				}
				else
				{
					service.get().stopSelf();
				}
			}
			else
			{
				service.get().stopSelf();
			}
		}
		
		private void sendNotifications(List<Notification> notifications)
		{
			Service s = service.get();
			
			if (KlyphPreferences.mustGroupNotifications())
			{
				final Builder builder = KlyphNotification.getBuilder(service.get(), true);
				
				if( notifications.size() > 1)
				{
					builder.setContentTitle(s.getString(R.string.app_large_name));
					builder.setContentText(s.getString(R.string.new_notifications, notifications.size()));
					builder.setTicker(s.getString(R.string.new_notifications, notifications.size()));
					
					List<String> lines = new ArrayList<String>();
					for (Notification notification : notifications)
					{
						lines.add(notification.getTitle_text());
					}

					KlyphNotification.setInboxStyle(builder, s.getString(R.string.app_large_name), lines);
					KlyphNotification.sendNotification(s, builder);
				}
				else
				{
					sendNotification(notifications.get(0));
				}
			}
			else
			{
				Collections.reverse(notifications);
				for (Notification notification : notifications)
				{
					sendNotification(notification);
				}
			}
		}
		
		private void sendNotification(final Notification notification)
		{
			sendNotification(notification, true);
		}
		
		private void sendNotification(final Notification notification, final boolean  sendDetails)
		{
			final Builder builder = KlyphNotification.getBuilder(service.get(), true);
			
			builder.setContentTitle(notification.getSender_name());
			builder.setContentText(notification.getTitle_text());
			builder.setTicker(String.format("%1$s\n%2$s", notification.getSender_name(), notification.getTitle_text()));
			
			ImageLoader.loadImage(notification.getSender_pic(), new SimpleFakeImageLoaderListener() {

				@Override
				public void onBitmapFailed(Drawable drawable)
				{
					if (sendDetails)
						KlyphNotification.sendNotification(service.get(), builder, notification);
					else
						KlyphNotification.sendNotification(service.get(), builder);
				}

				@Override
				public void onBitmapLoaded(Bitmap bitmap, LoadedFrom arg1)
				{
					builder.setLargeIcon(bitmap);
					if (sendDetails)
						KlyphNotification.sendNotification(service.get(), builder, notification);
					else
						KlyphNotification.sendNotification(service.get(), builder);
				}
			});
		}		
	}
}