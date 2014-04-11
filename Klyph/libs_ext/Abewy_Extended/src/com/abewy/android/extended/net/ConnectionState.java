package com.abewy.android.extended.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionState 
{
	private static ConnectionState instance = new ConnectionState();
	private static Context context;
	
	private ConnectivityManager connectivityManager;
	private boolean connected = false;

	public static ConnectionState getInstance(Context ctx) 
	{
		context = ctx;
		return instance;
	}

	public Boolean isOnline() 
	{
		try
		{
			connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
			return connected;
		}
		catch (Exception e)
		{
			Log.v("connectivity", e.toString());
		}

		return connected;
	}
}
