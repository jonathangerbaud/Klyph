package com.abewy.android.apps.klyph.service;

import java.util.Date;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.abewy.android.apps.klyph.KlyphPreferences;

public class NotificationGroupDeletedReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d("NotificationGroupDeletedReceiver", "onReceive: ");
		KlyphPreferences.setLastCheckedNotificationTime(new Date().getTime() / 1000);		
	}
}
