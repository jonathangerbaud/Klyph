package com.abewy.android.apps.klyph;

import com.abewy.android.apps.klyph.gcm.CommonUtilities;
import com.abewy.android.apps.klyph.gcm.ServerUtilities;
import com.google.android.gcm.GCMRegistrar;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class KlyphGCM
{
	private static RegisterTask			registerTask;
	private static RegisterOnServerTask	registerOnServerTask;

	public static void registerIfNecessary()
	{
		final Context context = KlyphApplication.getInstance();
		
		try
		{
			GCMRegistrar.checkDevice(context);
		}
		catch (UnsupportedOperationException e)
		{
			return;
		}
		// GCMRegistrar.checkManifest(context);

		final String regId = KlyphPreferences.getGCMRegId();

		// Register GCM
		if (regId.length() == 0)
		{
			Log.d("KlyphGCM", "register GCM");

			registerTask = new RegisterTask();
			registerTask.execute(context);
		}
		else
		{
			// Register on server, maybe last attempt failed
			Log.d("KlyphGCM", "register on server");
				
			registerOnServerTask = new RegisterOnServerTask();
			registerOnServerTask.execute(context);
		}
	}
	
	public static void unregister(Context context)
	{
		new UnregisterTask().execute(context);
	}

	private static class RegisterTask extends AsyncTask<Context, Void, Void>
	{
		@Override
		protected Void doInBackground(Context... params)
		{
			GCMRegistrar.register(params[0], CommonUtilities.SENDER_ID);
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			registerTask = null;
		}
	}

	private static class RegisterOnServerTask extends AsyncTask<Context, Void, Void>
	{
		@Override
		protected Void doInBackground(Context... params)
		{
			ServerUtilities.register(params[0], KlyphPreferences.getGCMRegId());
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			registerOnServerTask = null;
		}
	}
	
	private static class UnregisterTask extends AsyncTask<Context, Void, Void>
	{
		@Override
		protected Void doInBackground(Context... params)
		{
			GCMRegistrar.unregister(params[0]);
			return null;
		}
	}
}