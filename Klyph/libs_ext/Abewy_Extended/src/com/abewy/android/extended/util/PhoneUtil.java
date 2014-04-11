package com.abewy.android.extended.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class PhoneUtil
{

	public static void openDialActivity(Context context, String phoneNumber)
	{
		try
		{
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException activityException)
		{
			Log.d("PhoneUtil", "openDialActivity: ", activityException);
		}
	}

	public static void callNumber(Context context, String phoneNumber)
	{
		try
		{
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + phoneNumber));
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException activityException)
		{
			Log.d("PhoneUtil", "callNumber: ", activityException);
		}
	}

	public static void sendSMS(Context context, String phoneNumber)
	{
		try
		{
			Uri uri = Uri.parse("sms:" + phoneNumber);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException activityException)
		{
			Log.d("PhoneUtil", "sendSMS: ", activityException);
		}
	}

	public static void sendMail(Context context, String email)
	{
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));

		try
		{
			context.startActivity(Intent.createChooser(intent, "Send mail..."));
		}
		catch (android.content.ActivityNotFoundException activityException)
		{
			Log.d("PhoneUtil", "sendMail: ", activityException);
		}
	}

	public static void openURL(Context context, String url)
	{
		if (url.indexOf("http://") != 0)
		{
			if (url.indexOf("https://") != 0)
				url = "http://" + url;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(intent);
	}
}
