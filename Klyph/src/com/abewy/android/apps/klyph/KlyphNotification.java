/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph;

import java.util.List;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.abewy.android.apps.klyph.app.AlbumPhotosActivity;
import com.abewy.android.apps.klyph.app.EventActivity;
import com.abewy.android.apps.klyph.app.GroupActivity;
import com.abewy.android.apps.klyph.app.ImageActivity;
import com.abewy.android.apps.klyph.app.MainActivity;
import com.abewy.android.apps.klyph.app.PageActivity;
import com.abewy.android.apps.klyph.app.StreamActivity;
import com.abewy.android.apps.klyph.app.UserActivity;
import com.abewy.android.apps.klyph.core.fql.Notification;
import com.abewy.android.apps.klyph.service.NotificationDeletedReceiver;
import com.abewy.android.apps.klyph.service.NotificationGroupDeletedReceiver;
import com.abewy.util.PhoneUtil;

public class KlyphNotification
{

	public static Builder getBuilder(Context context, boolean  alert)
	{
		Builder builder = new Builder(context).setSmallIcon(R.drawable.ic_notification).setAutoCancel(true);

		if (alert == true)
		{
			int defaults = 0;

			if (KlyphPreferences.getNotificationRingtone() != null && KlyphPreferences.getNotificationRingtone().equals("default"))
			{
				defaults |= android.app.Notification.DEFAULT_SOUND;
			}
			else if (KlyphPreferences.getNotificationRingtoneUri() == null)
			{
				builder.setSound(null);
			}
			else
			{
				builder.setSound(Uri.parse(KlyphPreferences.getNotificationRingtoneUri()));
			}

			if (KlyphPreferences.isNotificationVibrationEnabled() == true)
				defaults |= android.app.Notification.DEFAULT_VIBRATE;

			defaults |= android.app.Notification.DEFAULT_LIGHTS;
			
			builder.setDefaults(defaults);
			builder.setOnlyAlertOnce(true);
		}

		return builder;
	}

	public static void setInboxStyle(Builder builder, String title, List<String> lines)
	{
		builder.setNumber(lines.size());
		InboxStyle inboxStyle = new InboxStyle();

		inboxStyle.setBigContentTitle(title);

		for (String line : lines)
		{
			inboxStyle.addLine(line);
		}

		builder.setStyle(inboxStyle);
	}

	public static void setNoSound(Builder builder)
	{
		builder.setSound(null);
	}

	public static void setNoVibration(Builder builder)
	{
		builder.setVibrate(null);
	}

	public static Intent getIntentForNotification(Context context, Notification notification)
	{
		final String id = notification.getObject_id();
		final String name = notification.getObject_name();
		final String type = notification.getObject_type();
		final String href = notification.getHref();

		Intent intent = null;

		if (type.equals(Notification.TYPE_FRIEND) || type.equals(Notification.TYPE_POKE)  || type.equals(Notification.TYPE_USER))
		{
			intent = Klyph.getIntentForParams(context, id, name, Notification.TYPE_FRIEND);
		}
		else if (type.equals(Notification.TYPE_EVENT) || type.equals(Notification.TYPE_CANCELED_EVENT))
		{
			int start = href.indexOf("events/") + 7;
			int end = href.indexOf("/", start);
			final String eid = end > start ? href.substring(start, end) : href.substring(start);
			intent = Klyph.getIntentForParams(context, eid, name, type);
		}
		else if (type.equals(Notification.TYPE_PAGE))
		{
			intent = Klyph.getIntentForParams(context, id, name, type);
		}
		else if (type.equals(Notification.TYPE_GROUP))
		{
			if (notification.getGroup().getGid().length() == 0)
			{
				// That is not a group but a post in a group
				intent = new Intent(context, StreamActivity.class);
				intent.putExtra(KlyphBundleExtras.STREAM_GROUP, true);
				intent.putExtra(KlyphBundleExtras.STREAM_ID, id);
			}
			else
			{
				intent = Klyph.getIntentForParams(context, id, name, type);
			}
		}
		else if (type.equals(Notification.TYPE_PHOTO))
		{
			intent = new Intent(context, ImageActivity.class);
			intent.putExtra(KlyphBundleExtras.PHOTO_ID, id);
		}
		else if (type.equals(Notification.TYPE_ALBUM))
		{
			intent = new Intent(context, AlbumPhotosActivity.class);
			intent.putExtra(KlyphBundleExtras.ALBUM_ID, id);
			intent.putExtra(KlyphBundleExtras.ALBUM_NAME, name);
		}
		else if (type.equals(Notification.TYPE_APP_REQUEST) || type.equals(Notification.TYPE_WEB_APP))
		{
			PhoneUtil.openURL(context, notification.getHref());
		}
		else if (type.equals(Notification.TYPE_VIDEO))
		{
			// ???
			// PhoneUtil.openURL(context, notification.getHref());
		}
		else
		{
			// Stream
			intent = new Intent(context, StreamActivity.class);

			intent.putExtra(KlyphBundleExtras.STREAM_ID, notification.getObject_id());
		}

		return intent;
	}

	public static void sendNotification(Context context, Builder builder)
	{
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra(KlyphBundleExtras.SHOW_NOTIFICATION_MENU, true);

		stackBuilder.addNextIntentWithParentStack(resultIntent);

		int intentCode = (int) Math.round(Math.random() * 1000000);

		// Gets a PendingIntent containing the entire back stack
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(intentCode, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		
		Intent intent = new Intent(context, NotificationGroupDeletedReceiver.class);
		builder.setDeleteIntent(PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

		final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		final String tag = context.getPackageName() + "_grouped";
		final int id = 0;

		// pair (tag, id) must be unique
		// tag is the unique key
		mNotificationManager.notify(tag, id, builder.build());

	}

	public static void sendNotification(Context context, Builder builder, Notification notification)
	{
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		Intent resultIntent = null;

		final String id = notification.getObject_id();
		final String name = notification.getObject_name();
		final String type = notification.getObject_type();

		if (type.equals(Notification.TYPE_FRIEND) || type.equals(Notification.TYPE_POKE) || type.equals(Notification.TYPE_USER))
		{
			resultIntent = Klyph.getIntentForParams(context, id, name, Notification.TYPE_FRIEND);
			stackBuilder.addParentStack(UserActivity.class);
		}
		else if (type.equals(Notification.TYPE_EVENT))
		{
			resultIntent = Klyph.getIntentForParams(context, id, name, type);
			stackBuilder.addParentStack(EventActivity.class);
		}
		else if (type.equals(Notification.TYPE_PAGE))
		{
			resultIntent = Klyph.getIntentForParams(context, id, name, type);
			stackBuilder.addParentStack(PageActivity.class);
		}
		else if (type.equals(Notification.TYPE_GROUP))
		{
			if (notification.getGroup().getGid().length() == 0)
			{
				// That is not a group but a post in a group
				resultIntent = new Intent(context, StreamActivity.class);
				resultIntent.putExtra(KlyphBundleExtras.STREAM_GROUP, true);
				resultIntent.putExtra(KlyphBundleExtras.STREAM_ID, id);

				stackBuilder.addParentStack(StreamActivity.class);
			}
			else
			{
				resultIntent = Klyph.getIntentForParams(context, id, name, type);
				stackBuilder.addParentStack(GroupActivity.class);
			}
		}
		else if (type.equals(Notification.TYPE_PHOTO))
		{
			resultIntent = new Intent(context, ImageActivity.class);
			resultIntent.putExtra(KlyphBundleExtras.PHOTO_ID, id);
			stackBuilder.addParentStack(ImageActivity.class);
		}
		else if (type.equals(Notification.TYPE_ALBUM))
		{
			resultIntent = new Intent(context, AlbumPhotosActivity.class);
			resultIntent.putExtra(KlyphBundleExtras.ALBUM_ID, id);
			resultIntent.putExtra(KlyphBundleExtras.ALBUM_NAME, name);
		}
		else if (type.equals(Notification.TYPE_APP_REQUEST))
		{
			resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notification.getHref()));
		}
		else if (type.equals(Notification.TYPE_VIDEO))
		{
			resultIntent = new Intent(context, MainActivity.class);
			// ???
			// resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notification.getHref()));
		}
		else
		{
			// Stream
			resultIntent = new Intent(context, StreamActivity.class);

			resultIntent.putExtra(KlyphBundleExtras.STREAM_ID, id);

			stackBuilder.addParentStack(StreamActivity.class);
		}

		resultIntent.putExtra(KlyphBundleExtras.SET_NOTIFICATION_AS_READ, true);
		resultIntent.putExtra(KlyphBundleExtras.NOTIFICATION_ID, notification.getNotification_id());

		// Adds the Intent to the top of the stack
		stackBuilder.addNextIntentWithParentStack(resultIntent);

		int intentCode = (int) Math.round(Math.random() * 1000000);

		// Gets a PendingIntent containing the entire back stack
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(intentCode, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		
		Intent intent = new Intent(context, NotificationDeletedReceiver.class);
		intent.putExtra(KlyphBundleExtras.NOTIFICATION_ID, notification.getNotification_id());
		int random = (int) Math.round(Math.random() * 100000);
		builder.setDeleteIntent(PendingIntent.getBroadcast(context.getApplicationContext(), random, intent, 0));

		final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Log.d("NotificationService", "Notify " + notification.getNotification_id());
		final String tag = context.getPackageName() + "_" + notification.getNotification_id();

		// pair (tag, id) must be unique
		// because notificationId may not be converted to an int
		// tag is the unique key
		mNotificationManager.notify(tag, 0, builder.build());

	}
}
