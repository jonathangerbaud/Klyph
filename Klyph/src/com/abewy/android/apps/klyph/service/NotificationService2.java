/**
 * Copyright 2013 Abewy. All rights reserved
 * 
 * @date 2 avr. 2013
 * @author Jonathan
 */
package com.abewy.android.apps.klyph.service;

import android.app.Service;
import android.os.Looper;

public class NotificationService2 /* extends Service*/
{
	private Looper			mServiceLooper;
	//private ServiceHandler	mServiceHandler;

	/*@Override
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
		private boolean							isFirst	= true;

		public ServiceHandler(Looper looper, WeakReference<Service> service)
		{
			super(looper);
			this.service = service;
		}

		@Override
		public void handleMessage(Message msg)
		{
			isFirst = true;

			launchRequest();

			Intent intent = new Intent(KlyphBundleExtras.NOTIFICATION_EVENT);
			// Add data
			intent.putExtra(KlyphBundleExtras.NOTIFICATION_EVENT_DATA, "test broadcast");
			LocalBroadcastManager.getInstance(service.get()).sendBroadcast(intent);
		}

		private void launchRequest()
		{
			Log.d("Notificationservice", "launchRequest " + service.get());
			if (service.get() == null)
				return;

			try
			{
				if (Session.getActiveSession() == null)
					Session.openActiveSessionFromCache(service.get());
			}
			catch (UnsupportedOperationException e)
			{

			}

			if (Session.getActiveSession() != null && Session.getActiveSession().getPermissions().contains("manage_notifications"))
			{
				AsyncRequest request = new AsyncRequest(Query.PERIODIC_NOTIFICATIONS, KlyphPreferences.getNotificationServiceOffset(),
						KlyphPreferences.getNotificationServiceOffset(), new AsyncRequest.Callback() {

							@Override
							public void onComplete(Response response)
							{
								if (response.getError() == null)
								{
									Log.d("NotificationsService", "onRequestSuccess " + response.getGraphObjectList());
									Log.d("NotificationsService", "onRequestSuccess " + response.getGraphObject());
									onRequestSuccess(response.getGraphObjectList());
								}
								else
								{
									Log.d("NotificationsService", "onRequestError " + response.getError());
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
			Log.d("NotificationsService", "Num notifications : " + list.size());
			Log.d("NotificationsService", "service : " + service.get());

			Log.d("Notificationservice", "onRequestSuccess " + service.get());
			if (service.get() == null)
				return;

			Service s = service.get();

			if (list.size() > 0)
			{
				boolean notifyStreams = KlyphPreferences.notifyStreams();
				boolean notifyEvents = KlyphPreferences.notifyEvents();
				boolean notifyPhotos = KlyphPreferences.notifyPhotos();
				boolean notifyVideos = KlyphPreferences.notifyVideos();
				boolean notifyPageInvite = KlyphPreferences.notifyPageInvites();
				boolean notifyGroupInvite = KlyphPreferences.notifyGroupInvites();

				// Order notifications by object_type
				HashMap<String, List<GraphObject>> map = new HashMap<String, List<GraphObject>>();

				int count = 0;
				for (GraphObject graphObject : list)
				{
					Notification n = (Notification) graphObject;
					String objectType = n.getObject_type();

					// Check notifications preferences
					if (objectType.equals("stream") && notifyStreams == false)
						continue;

					if (objectType.equals("event") && notifyEvents == false)
						continue;

					if (objectType.equals("photo") && notifyPhotos == false)
						continue;

					if (objectType.equals("video") && notifyVideos == false)
						continue;

					if (objectType.equals("page") && notifyPageInvite == false)
						continue;

					if (objectType.equals("group") && notifyGroupInvite == false)
						continue;

					String objectId = n.getObject_id();

					// Preferences compliant, add to the list
					if (map.get(objectId) == null)
						map.put(objectId, new ArrayList<GraphObject>());

					List<GraphObject> l = map.get(objectId);
					l.add(graphObject);
					count++;
				}

				if (KlyphPreferences.mustGroupNotifications() && count > 1)
				{
					sendNotification(map);
				}
				else
				{
					// For each object (post, photo, video, ...), send
					// notification
					for (String key : map.keySet())
					{
						List<GraphObject> l = map.get(key);
						sendNotification(l);
					}
				}

				Notification n0 = (Notification) list.get(0);
				KlyphPreferences.setNotificationServiceOffset(n0.getUpdated_time());

				s.stopSelf();
			}
		}

		private void sendNotification(HashMap<String, List<GraphObject>> map)
		{
			Log.d("Notificationservice", "sendNotification " + service.get());
			if (service.get() == null)
				return;

			List<GraphObject> list = new ArrayList<GraphObject>();
			for (String key : map.keySet())
			{
				list.addAll(map.get(key));
			}

			Service s = service.get();

			final NotificationCompat.Builder builder = KlyphNotification.getBuilder(service.get(), isFirst);
			builder.setContentTitle(s.getString(R.string.app_large_name)).setContentText(s.getString(R.string.new_notifications, list.size()))
					.setTicker(s.getString(R.string.new_notifications, list.size()));

			isFirst = false;

			// Big notification style
			if (list.size() > 1)
			{
				List<String> lines = new ArrayList<String>();
				for (GraphObject graphObject : list)
				{
					lines.add(((Notification) graphObject).getTitle_text());
				}

				KlyphNotification.setInboxStyle(builder, s.getString(R.string.app_large_name), lines);
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

		private void sendNotification(List<GraphObject> list)
		{
			Log.d("NotificationsService", "Send notification : " + list.size());
			Log.d("NotificationsService", "Send notification service : " + service.get());

			Log.d("Notificationservice", "sendNotification2 " + service.get());
			if (service.get() == null)
				return;

			Service s = service.get();

			Notification n = (Notification) list.get(0);

			final NotificationCompat.Builder builder = getBuilder(list);

			if (builder != null)
			{
				final NotificationManager mNotificationManager = (NotificationManager) s.getSystemService(Context.NOTIFICATION_SERVICE);

				final String tag = AttrUtil.getString(service.get(), R.string.app_name) + n.getObject_id();
				final int id = 0;

				// pair (tag, id) must be unique
				// because n.getObject_id() may not be converted to an int
				// tag is the unique key
				mNotificationManager.notify(tag, id, builder.build());
			}
		}

		private NotificationCompat.Builder getBuilder(List<GraphObject> list)
		{
			Log.d("Notificationservice", "getBuilder " + service.get());
			if (service.get() == null)
				return null;

			Notification n0 = (Notification) list.get(0);

			NotificationCompat.Builder builder = KlyphNotification.getBuilder(service.get(), isFirst);

			builder.setContentTitle(n0.getSender_name()).setContentText(n0.getTitle_text()).setTicker(n0.getTitle_text());

			// Big notification style
			if (list.size() > 1)
			{
				List<String> lines = new ArrayList<String>();
				for (GraphObject graphObject : list)
				{
					lines.add(((Notification) graphObject).getTitle_text());
				}

				KlyphNotification.setInboxStyle(builder, n0.getSender_name(), lines);
			}

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(service.get());
			Intent resultIntent;

			final String id = n0.getObject_id();
			final String name = n0.getObject_name();
			final String type = n0.getObject_type();

			if (type.equals(Notification.FRIEND) || type.equals(Notification.POKE))
			{
				resultIntent = Klyph.getIntentForParams(service.get(), id, name, Notification.FRIEND);
				stackBuilder.addParentStack(UserActivity.class);
			}
			else if (type.equals(Notification.EVENT))
			{
				resultIntent = Klyph.getIntentForParams(service.get(), id, name, type);
				stackBuilder.addParentStack(EventActivity.class);
			}
			else if (type.equals(Notification.PAGE))
			{
				resultIntent = Klyph.getIntentForParams(service.get(), id, name, type);
				stackBuilder.addParentStack(PageActivity.class);
			}
			else if (type.equals(Notification.GROUP))
			{
				resultIntent = Klyph.getIntentForParams(service.get(), id, name, type);
				stackBuilder.addParentStack(GroupActivity.class);
			}
			else if (type.equals(Notification.PHOTO))
			{

				resultIntent = new Intent(service.get(), ImageActivity.class);
				resultIntent.putExtra(KlyphBundleExtras.PHOTO_ID, n0.getObject_id());
				stackBuilder.addParentStack(ImageActivity.class);
			}
			else if (type.equals(Notification.ALBUM))
			{
				resultIntent = new Intent(service.get(), AlbumPhotosActivity.class);
				resultIntent.putExtra(KlyphBundleExtras.ALBUM_ID, id);
				resultIntent.putExtra(KlyphBundleExtras.ALBUM_NAME, name);
			}
			else if (type.equals(Notification.APP_REQUEST))
			{
				resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(n0.getHref()));
			}
			else
			{
				// Stream
				resultIntent = new Intent(service.get(), StreamActivity.class);

				if (type.equals(Notification.GROUP))
					resultIntent.putExtra(KlyphBundleExtras.STREAM_GROUP, true);

				resultIntent.putExtra(KlyphBundleExtras.STREAM_ID, n0.getObject_id());

				stackBuilder.addParentStack(StreamActivity.class);
			}

			resultIntent.putExtra(KlyphBundleExtras.SET_NOTIFICATION_AS_READ, true);
			resultIntent.putExtra(KlyphBundleExtras.NOTIFICATION_ID, n0.getNotification_id());

			// Adds the Intent to the top of the stack
			stackBuilder.addNextIntentWithParentStack(resultIntent);

			int intentCode = (int) Math.round(Math.random() * 1000000);

			// Gets a PendingIntent containing the entire back stack
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(intentCode, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(resultPendingIntent);

			return builder;
		}
	}*/
}
