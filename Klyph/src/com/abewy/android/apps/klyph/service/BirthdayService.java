/**
 * Copyright 2013 Abewy. All rights reserved
 * 
 * @date 2 avr. 2013
 * @author Jonathan GERBAUD
 */
package com.abewy.android.apps.klyph.service;

import java.lang.ref.WeakReference;
import java.util.List;
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
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.app.MainActivity;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.R;
import com.facebook.Session;

public class BirthdayService extends Service
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
					Log.e("BirthdayService", e.getMessage());
				}
			}

			AsyncRequest request = new AsyncRequest(Query.BIRTHDAY_NOTIFICATIONS, "", "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					if (response.getError() == null)
					{
						onRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						Log.d("BirthdayService", "onError " + response.getError());
						if (service.get() != null)
							service.get().stopSelf();
					}
				}
			});

			request.execute();
		}

		private void onRequestSuccess(List<GraphObject> list)
		{
			Log.d("BirthdayService", "onRequestSuccess " + list.size() + " " + service.get());

			if (service.get() == null)
				return;

			Service s = service.get();

			if (list.size() > 0)
			{
				final NotificationCompat.Builder builder = new NotificationCompat.Builder(s)
						.setSmallIcon(R.drawable.ic_notification)
						.setOnlyAlertOnce(true)
						.setAutoCancel(true)
						.setDefaults(
								android.app.Notification.DEFAULT_SOUND | android.app.Notification.DEFAULT_VIBRATE
										| android.app.Notification.FLAG_ONLY_ALERT_ONCE);

				final NotificationManager mNotificationManager = (NotificationManager) s.getSystemService(Context.NOTIFICATION_SERVICE);

				if (KlyphPreferences.mustGroupNotifications() && list.size() > 1)
				{
					sendNotification(list);
				}
				else
				{
					boolean isFirst = true;
					for (GraphObject graphObject : list)
					{
						Friend friend = (Friend) graphObject;

						TaskStackBuilder stackBuilder = TaskStackBuilder.create(service.get());
						Intent intent = Klyph.getIntentForGraphObject(service.get(), friend);

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

						builder.setContentTitle(friend.getName());
						builder.setContentText(s.getString(R.string.notification_birthday_today, friend.getName()));
						builder.setTicker(s.getString(R.string.notification_birthday_today, friend.getName()));

						if (isFirst == false)
						{
							builder.setDefaults(android.app.Notification.DEFAULT_VIBRATE | android.app.Notification.FLAG_ONLY_ALERT_ONCE);
							builder.setSound(null);
						}

						final String tag = AttrUtil.getString(service.get(), R.string.app_name) + friend.getUid();
						final int id = (int) System.currentTimeMillis();

						mNotificationManager.notify(tag, id, builder.build());

						isFirst = false;
					}
				}
			}

			s.stopSelf();
		}

		private void sendNotification(List<GraphObject> list)
		{
			if (service.get() == null)
				return;

			Service s = service.get();

			final NotificationCompat.Builder builder = new NotificationCompat.Builder(service.get()).setSmallIcon(R.drawable.ic_notification)
					.setContentTitle(s.getString(R.string.app_large_name)).setContentText(s.getString(R.string.friends_birthday_today, list.size()))
					.setTicker(s.getString(R.string.friends_birthday_today, list.size())).setOnlyAlertOnce(true).setAutoCancel(true)
					.setOnlyAlertOnce(true);

			builder.setDefaults(android.app.Notification.DEFAULT_SOUND | android.app.Notification.DEFAULT_VIBRATE
								| android.app.Notification.FLAG_ONLY_ALERT_ONCE);

			// Big notification style
			if (list.size() > 1)
			{
				builder.setNumber(list.size());
				NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

				inboxStyle.setBigContentTitle(s.getString(R.string.friends_birthday_today, list.size()));

				for (int i = 0; i < list.size(); i++)
				{
					inboxStyle.addLine(((Friend) list.get(i)).getName());
				}

				builder.setStyle(inboxStyle);
			}

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(service.get());

			Intent resultIntent = new Intent(service.get(), MainActivity.class);
			resultIntent.putExtra(KlyphBundleExtras.SHOW_BIRTHDAYS, true);
			// stackBuilder.addParentStack(MainActivity.class);

			// Adds the Intent to the top of the stack
			stackBuilder.addNextIntentWithParentStack(resultIntent);

			int intentCode = (int) Math.round(Math.random() * 1000000);

			// Gets a PendingIntent containing the entire back stack
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(intentCode, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(resultPendingIntent);

			if (builder != null)
			{
				final NotificationManager mNotificationManager = (NotificationManager) s.getSystemService(Context.NOTIFICATION_SERVICE);

				final String tag = AttrUtil.getString(service.get(), R.string.app_name) + Math.round(Math.random() * 1000000);
				final int id = 0;

				// pair (tag, id) must be unique
				// because n.getObject_id() may not be converted to an int
				// tag is the unique key
				mNotificationManager.notify(tag, id, builder.build());

			}
		}
	}
}