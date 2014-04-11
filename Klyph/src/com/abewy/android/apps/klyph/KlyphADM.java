package com.abewy.android.apps.klyph;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.abewy.android.apps.klyph.adm.ServerUtilities;
import com.amazon.device.messaging.ADM;
import com.google.android.gcm.GCMRegistrar;

public class KlyphADM
{
	private static RegisterTask			registerTask;
	private static RegisterOnServerTask	registerOnServerTask;

	public static void registerIfNecessary()
	{
		final Context context = KlyphApplication.getInstance();
		Log.d("KlyphADM", "registerIfNecessary: ");
		try
		{
		    Class.forName( "com.amazon.device.messaging.ADM" );
		}
		catch (ClassNotFoundException e)
		{
		    return;
		}
		
		final ADM adm = new ADM(context);
        if (adm.isSupported())
        {
        	Log.d("KlyphADM", "registerIfNecessary: 1");
            if(adm.getRegistrationId() == null)
            {
            	Log.d("KlyphADM", "registerIfNecessary: 2");
                registerTask = new RegisterTask();
    			registerTask.execute(context);
            } 
            else 
            {
            	Log.d("KlyphADM", "registerIfNecessary: 3");
                /* Send the registration ID for this app instance to your server. */
                /* This is a redundancy since this should already have been performed at registration time from the onRegister() callback */
                /* but we do it because our python server doesn't save registration IDs. */
                registerOnServerTask = new RegisterOnServerTask();
    			registerOnServerTask.execute(context);
            }
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
			final ADM adm = new ADM(KlyphApplication.getInstance());
			adm.startRegister();
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
			final ADM adm = new ADM(KlyphApplication.getInstance());
			ServerUtilities.register(params[0], adm.getRegistrationId());
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
			try
			{
			    Class.forName( "com.amazon.device.messaging.ADM" );
			}
			catch (ClassNotFoundException e)
			{
			    return null;
			}
			final ADM adm = new ADM(KlyphApplication.getInstance());
			adm.startUnregister();
			return null;
		}
	}
}