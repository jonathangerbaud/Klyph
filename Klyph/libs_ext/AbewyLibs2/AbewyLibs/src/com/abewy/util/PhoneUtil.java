package com.abewy.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class PhoneUtil {

	public static void callNumber(Activity activity, String phoneNumber)
	{
		Intent intent = new Intent("android.intent.action.DIAL",
				Uri.parse("tel:" + phoneNumber));
		activity.startActivity(intent);
	}
	
	public static void sendMail(Activity activity, String email)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		
		try 
		{
			activity.startActivity(Intent.createChooser(intent, "Send mail..."));
		} 
		catch (android.content.ActivityNotFoundException ex) 
		{
			Toast.makeText(activity,
					"Aucun client email installé.", Toast.LENGTH_SHORT)
					.show();
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
