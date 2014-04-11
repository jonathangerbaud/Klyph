package com.abewy.android.apps.klyph.core;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

public class KlyphDevice
{
	private static int		deviceWidth;
	private static int		deviceHeight;
	private static int		deviceDPI;
	private static float	deviceDensity;
	private static float	deviceXdpi;
	private static float	deviceYdpi;
	private static boolean 	valuesInitialized	= false;
	private static int		orientation;

	public static void initDeviceValues(Context context)
	{
		initDeviceValues(context, false);
	}

	public static void initDeviceValues(Context context, boolean  reset)
	{
		if (reset == true || valuesInitialized == false)
		{
			final DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
			
			deviceWidth = displaymetrics.widthPixels;
			deviceHeight = displaymetrics.heightPixels;
			deviceDPI = displaymetrics.densityDpi;
			deviceDensity = displaymetrics.density;
			deviceXdpi = displaymetrics.xdpi;
			deviceYdpi = displaymetrics.ydpi;
			
			orientation = context.getResources().getConfiguration().orientation;

			valuesInitialized = true;
		}
	}

	public static int getDeviceWidth()
	{
		return deviceWidth;
	}

	public static int getDeviceHeight()
	{
		return deviceHeight;
	}

	/**
	 * The dpi type of screen : DisplayMetrics.DENSITY_LOW, MEDIUM or HIGH
	 */
	public static int getDeviceDPI()
	{
		return deviceDPI;
	}

	/**
	 * The dpi factor of the screen (1.0 for a mdpi screen, 1.5 for hdpi, 2 for
	 * xhdpi, ...)
	 */
	public static float getDeviceDensity()
	{
		return deviceDensity;
	}

	public static float getDeviceXdpi()
	{
		return deviceXdpi;
	}

	public static float getDeviceYdpi()
	{
		return deviceYdpi;
	}
	
	public static boolean  isPortraitMode()
	{
		return orientation == Configuration.ORIENTATION_PORTRAIT;
	}
	
	public static boolean  isLandscapeMode()
	{
		return orientation == Configuration.ORIENTATION_LANDSCAPE;
	}
}