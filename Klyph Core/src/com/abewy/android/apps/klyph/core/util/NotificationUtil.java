package com.abewy.android.apps.klyph.core.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationUtil
{
	public static void setDummyIntent(Context context, NotificationCompat.Builder builder)
	{
		PendingIntent pendingIntent = PendingIntent.getActivity(
			      context, 
			      0, 
			      new Intent(),  //Dummy Intent do nothing 
			      Intent.FLAG_ACTIVITY_NEW_TASK);
		
		builder.setContentIntent(pendingIntent);
	}
}
