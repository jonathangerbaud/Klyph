/**
 * Copyright 2013 Abewy. All rights reserved
 * 
 * @date 2 avr. 2013
 * @author Jonathan
 */
package com.abewy.android.apps.klyph.service;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphNotification;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.app.MainActivity;
import com.abewy.android.apps.klyph.core.fql.FriendRequest;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.R;
import com.facebook.Session;

public class FriendRequestService extends Service
{
	private Looper			mServiceLooper;
	private ServiceHandler	mServiceHandler;

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
		}

		private void launchRequest()
		{
			if (service.get() == null)
				return;

			Service s = service.get();

			if (Session.getActiveSession() == null)
			{
				try
				{
					Session.openActiveSessionFromCache(s);
				}
				catch (UnsupportedOperationException e)
				{

				}
			}

			if (Session.getActiveSession() != null)
			{
				AsyncRequest request = new AsyncRequest(Query.FRIEND_REQUEST_NOTIFICATION, KlyphPreferences.getNotificationServiceOffset(),
						KlyphPreferences.getFriendRequestServiceOffset(), new AsyncRequest.Callback() {

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
		}

		private void onRequestSuccess(List<GraphObject> list)
		{
			Log.d("FriendRequestService", "Num friend request : " + list.size());
			Log.d("FriendRequestService", "Service : " + service.get());
			if (service.get() == null)
				return;

			Service s = service.get();

			if (list.size() > 0)
			{
				FriendRequest fq = (FriendRequest) list.get(0);
				KlyphPreferences.setFriendRequestServiceOffset(fq.getTime());

				final Builder builder = KlyphNotification.getBuilder(s, true);
				builder.setContentTitle(fq.getUid_from_name()).setContentText(
						s.getString(R.string.notification_friendrequest_message, fq.getUid_from_name()));

				if (KlyphPreferences.mustGroupNotifications() && list.size() > 1)
				{
					sendNotification(list);
				}
				else
				{
					boolean isFirst = true;
					for (GraphObject graphObject : list)
					{
						FriendRequest fr = (FriendRequest) graphObject;

						TaskStackBuilder stackBuilder = TaskStackBuilder.create(service.get());
						Intent intent = Klyph.getIntentForGraphObject(service.get(), fr);

						// stackBuilder.addParentStack(UserActivity.class);
						Intent mainIntent = new Intent(service.get(), MainActivity.class);
						mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
											| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
											| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

						stackBuilder.addNextIntent(mainIntent);
						stackBuilder.addNextIntent(intent);

						int intentCode = (int) Math.round(Math.random() * 1000000);

						// Gets a PendingIntent containing the entire back stack
						PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(intentCode, PendingIntent.FLAG_UPDATE_CURRENT);
						builder.setContentIntent(resultPendingIntent);

						builder.setContentTitle(fr.getUid_from_name());
						builder.setContentText(s.getString(R.string.notification_friendrequest_message, fr.getUid_from_name()));
						builder.setTicker(s.getString(R.string.notification_friendrequest_message, fr.getUid_from_name()));
						
						if (isFirst == false)
						{
							KlyphNotification.setNoSound(builder);
							KlyphNotification.setNoVibration(builder);
						}
						
						KlyphNotification.sendNotification(s, builder);

						isFirst = false;
					}
				}

				service.get().stopSelf();
			}
			else
			{
				s.stopSelf();
			}
		}

		private void sendNotification(List<GraphObject> list)
		{
			if (service.get() == null)
				return;

			Service s = service.get();

			final Builder builder = KlyphNotification.getBuilder(s, true);

			builder.setContentTitle(s.getString(R.string.app_large_name)).setContentText(s.getString(R.string.new_friend_requests, list.size()))
					.setTicker(s.getString(R.string.new_friend_requests, list.size()));

			// Big notification style
			if (list.size() > 1)
			{
				List<String> lines = new ArrayList<String>();
				for (int i = 0; i < list.size(); i++)
				{
					lines.add(((FriendRequest) list.get(i)).getUid_from_name());
				}

				KlyphNotification.setInboxStyle(builder, s.getString(R.string.new_friend_requests, list.size()), lines);
			}

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(service.get());

			Intent resultIntent = new Intent(service.get(), MainActivity.class);
			resultIntent.putExtra(KlyphBundleExtras.SHOW_NOTIFICATION_MENU, true);
			// stackBuilder.addParentStack(MainActivity.class);

			// Adds the Intent to the top of the stack
			stackBuilder.addNextIntentWithParentStack(resultIntent);

			int intentCode = (int) Math.round(Math.random() * 1000000);

			// Gets a PendingIntent containing the entire back stack
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(intentCode, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(resultPendingIntent);

			if (builder != null)
			{
				KlyphNotification.sendNotification(s, builder);
			}
		}
	}
}
