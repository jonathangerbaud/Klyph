/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.gcm;

import android.content.Context;

public class GCMBroadcastReceiver extends com.google.android.gcm.GCMBroadcastReceiver
{

	@Override
	protected String getGCMIntentServiceClassName(Context context)
	{
		return "com.abewy.android.apps.klyph.gcm.GCMIntentService";
	}

}
