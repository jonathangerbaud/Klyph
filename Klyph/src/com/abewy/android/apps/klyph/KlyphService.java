package com.abewy.android.apps.klyph;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.abewy.android.apps.klyph.core.KlyphFlags;
import com.abewy.android.apps.klyph.service.BirthdayService;
import com.abewy.android.apps.klyph.service.NotificationService;
import com.amazon.device.messaging.ADM;

public class KlyphService
{
	public static void startBirthdayService()
	{
		Log.d("KlyphService", "Start birthday");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		int targetHour = KlyphPreferences.getBirthdayNotificationTime();
		
		calendar.set(Calendar.HOUR_OF_DAY, targetHour);
		
		if (currentHour > targetHour)
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		
		Intent service = new Intent(KlyphApplication.getInstance(), BirthdayService.class);
		PendingIntent pendingService = PendingIntent.getService(KlyphApplication.getInstance(), 0, service,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) KlyphApplication.getInstance()
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingService);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingService);
	}

	public static void stopBirthdayService()
	{
		Log.d("KlyphService", "Stop birthday");
		Intent service = new Intent(KlyphApplication.getInstance(), BirthdayService.class);
		PendingIntent pendingService = PendingIntent.getService(KlyphApplication.getInstance(), 0, service,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) KlyphApplication.getInstance()
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingService);
	}

	public static void startSelectedServices()
	{
		if (KlyphPreferences.areNotificationsEnabled() == true)
		{
			if (KlyphPreferences.arePushNotificationsEnabled())
			{
				startPushNotificationsService();
			}
			
			if (KlyphPreferences.arePeriodicNotificationsEnabled())
			{
				KlyphService.startPeriodicNotificationService();
			}
			
			if (KlyphPreferences.areBirthdayNotificationsEnabled())
			{
				KlyphService.startBirthdayService();
			}
		}
	}
	
	public static void startPushNotificationsService()
	{
		Log.d("KlyphService", "Start push");
		if (KlyphFlags.IS_AMAZON_VERSION)
		{
			Log.d("KlyphService", "startPushNotificationsService: starting ADM");
			KlyphADM.registerIfNecessary();
		}
		else
		{
			Log.d("KlyphService", "startPushNotificationsService: starting GCM");
			KlyphGCM.registerIfNecessary();
		}
	}
	
	public static void stopPushNotificationsService()
	{
		Log.d("KlyphService", "Stop push");
		
		if (KlyphFlags.IS_AMAZON_VERSION)
		{
			Log.d("KlyphService", "stopPushNotificationsService: stopping ADM");
			KlyphADM.unregister(KlyphApplication.getInstance());
		}
		else
		{
			Log.d("KlyphService", "stopPushNotificationsService: stopping GCM");
			KlyphGCM.unregister(KlyphApplication.getInstance());
		}
		
	}
	
	public static void startPeriodicNotificationService()
	{
		Log.d("KlyphService", "Start periodic");
		int interval = KlyphPreferences.getPeriodicNotificationsInterval();

		Intent service = new Intent(KlyphApplication.getInstance(), NotificationService.class);
		PendingIntent pendingService = PendingIntent.getService(KlyphApplication.getInstance(), 0, service,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) KlyphApplication.getInstance()
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingService);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval * 60 * 1000,
				pendingService);
	}

	public static void stopPeriodicNotificationService()
	{
		Log.d("KlyphService", "Stop periodic");
		Intent service = new Intent(KlyphApplication.getInstance(), NotificationService.class);
		PendingIntent pendingService = PendingIntent.getService(KlyphApplication.getInstance(), 0, service,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = (AlarmManager) KlyphApplication.getInstance()
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingService);
	}
	
	public static void stopServices()
	{
		Log.d("KlyphService", "Stop services");
		stopPushNotificationsService();
		stopBirthdayService();
		stopPeriodicNotificationService();
	}
}
