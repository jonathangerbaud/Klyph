package com.abewy.android.apps.klyph.service;

import com.abewy.android.apps.klyph.KlyphBundleExtras;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationDeletedReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d("NotificationDeletedReceiver", "onReceive: " + intent.getStringExtra(KlyphBundleExtras.NOTIFICATION_ID));
		Intent service = new Intent(context, SetNotificationReadService.class);
		service.putExtra(KlyphBundleExtras.NOTIFICATION_ID, intent.getStringExtra(KlyphBundleExtras.NOTIFICATION_ID));
		context.startService(service);
	}
}
