package com.abewy.android.extended.util;

import java.util.UUID;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class Android extends Build.VERSION_CODES
{
	/**
	 * Check the api level of the device we're running on
	 * 
	 * @param level API level
	 * @return true if same or higher
	 */
	public static boolean isMinAPI(int level)
	{
		return false;//Build.VERSION.SDK_INT >= level;
	}

	private Android()
	{}

	public static String getDeviceUDID(Context ctx)
	{
		final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;

		PackageManager pm = ctx.getPackageManager();
		int hasPerm = pm.checkPermission(android.Manifest.permission.READ_PHONE_STATE, ctx.getPackageName());
		if (hasPerm == PackageManager.PERMISSION_GRANTED)
		{
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
		}
		else
		{
			tmDevice = Settings.Secure.ANDROID_ID;
			tmSerial = android.os.Build.SERIAL;
		}

		androidId = "" + android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();

		return deviceId;
	}
}
