/**
 * @author Jonathan
 */

package com.abewy.android.extended.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PlayStoreUtil
{
	private static final String MY_PUBLISHER_NAME = "Abewy";
	
	private static final String MARKET = "market://";
	private static final String STORE = "http://play.google.com/store/";
	private static final String MARKET_APP_ID = "details?id=";
	private static final String STORE_APP_ID = "apps/details?id=";
	private static final String MARKET_SEARCH_PUBLISHER = "search?q=pub:";
	private static final String STORE_SEARCH_PUBLISHER = "search?q=pub:";
	
	/**
	 * Open an app in the Play Store (app or browser according to user's device)
	 * 
	 * @param context
	 * @param appPackageName The app's package on the Play Store (ex: com.example.appexample)
	 */
	public static void openApp(Context context, String appPackageName)
	{
		try
		{
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET + MARKET_APP_ID + appPackageName)));
		}
		catch (android.content.ActivityNotFoundException anfe)
		{
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(STORE + STORE_APP_ID + appPackageName)));
		}
	}
	
	
	/**
	 * Open the Play Store (app or browser according to user's device) listing all my apps
	 * 
	 * @param context
	 */
	public static void openMyApps(Context context)
	{
		openDeveloperApps(context, MY_PUBLISHER_NAME);
	}
	
	/**
	 * Open the Play Store (app or browser according to user's device) listing all apps of the publisher
	 * 
	 * @param context
	 * @param publisherName The name of the publisher on the Play Store
	 */
	public static void openDeveloperApps(Context context, String publisherName)
	{
		try
		{
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET + MARKET_SEARCH_PUBLISHER + publisherName)));
		}
		catch (android.content.ActivityNotFoundException anfe)
		{
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(STORE + STORE_SEARCH_PUBLISHER + publisherName)));
		}
	}
}
