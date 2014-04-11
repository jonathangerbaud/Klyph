/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.core.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.StrictMode;
import com.android.debug.hv.ViewServer;

public class HierachyViewUtil
{
	@TargetApi(9)
	public static void connectHierarchyView(Activity activity)
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// Set content view, etc.
		ViewServer.get(activity).addWindow(activity);
	}
}
