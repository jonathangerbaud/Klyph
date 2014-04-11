package com.abewy.android.apps.klyph.gcm;

import static com.abewy.android.apps.klyph.gcm.CommonUtilities.REGISTER_URL;
import static com.abewy.android.apps.klyph.gcm.CommonUtilities.TAG;
import static com.abewy.android.apps.klyph.gcm.CommonUtilities.UNREGISTER_URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import android.content.Context;
import android.util.Log;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.R;
import com.abewy.util.Android;
import com.google.android.gcm.GCMRegistrar;

public final class ServerUtilities
{
	private static final int	MAX_ATTEMPTS			= 5;
	private static final int	BACKOFF_MILLI_SECONDS	= 2000;
	private static final Random	random					= new Random();

	/**
	 * Register this account/device pair within the server.
	 * 
	 */
	public static boolean register(final Context context, final String regId)
	{
		Log.i(TAG, "registering device (regId = " + regId + ")");

		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		params.put("facebook_user_id", KlyphSession.getSessionUserId());
		params.put("udid", Android.getDeviceUDID(context));
		
		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++)
		{
			Log.d(TAG, "Attempt #" + i + " to register");
			
			try
			{
				Log.d("ServerUtilities", context.getString(R.string.gcm_server_registering, i, MAX_ATTEMPTS));
				
				post(REGISTER_URL, params);
				GCMRegistrar.setRegisteredOnServer(context, true);
				
				Log.d("ServerUtilities", context.getString(R.string.gcm_server_registered));
				
				return true;
			}
			catch (IOException e)
			{
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS)
				{
					Log.e(TAG, "Max attempt reached, giving up registration on server");
					GCMRegistrar.setRegisteredOnServer(context, false);
					break;
				}
				try
				{
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				}
				catch (InterruptedException e1)
				{
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					GCMRegistrar.setRegisteredOnServer(context, false);
					return false;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		
		Log.d("ServerUtilities", context.getString(R.string.gcm_server_register_error, MAX_ATTEMPTS));
		
		return false;
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	public static void unregister(final Context context, final String regId)
	{
		Log.i(TAG, "unregistering device (regId = " + regId + ")");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		
		try
		{
			post(UNREGISTER_URL, params);
			GCMRegistrar.setRegisteredOnServer(context, false);
			
			Log.d("ServerUtilities", context.getString(R.string.gcm_server_unregistered));
		}
		catch (IOException e)
		{
			// At this point the device is unregistered from GCM, but still
			// registered in the server.
			// We could try to unregister again, but it is not necessary:
			// if the server tries to send a message to the device, it will get
			// a "NotRegistered" error message and should unregister the device.
			Log.d("ServerUtilities", context.getString(R.string.gcm_server_unregister_error, e.getMessage()));
		}
	}

	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint POST address.
	 * @param params request parameters.
	 * 
	 * @throws IOException propagated from POST.
	 */
	private static void post(String endpoint, Map<String, String> params) throws IOException
	{
		URL url;
		
		try
		{
			url = new URL(endpoint);
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		
		// constructs the POST body using the parameters
		while (iterator.hasNext())
		{
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
			
			if (iterator.hasNext())
			{
				bodyBuilder.append('&');
			}
		}
		
		String body = bodyBuilder.toString();
		//Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		 
		HttpURLConnection conn = null;
		try
		{
			//Log.e("URL", "> " + url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200)
			{
				//throw new IOException("Post failed with error code " + status);
				Log.e(TAG, "Post failed with error code " + status);
			}
			else
			{
				StringBuilder json = new StringBuilder();
				try 
				{
				    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				    try {
				        String str;
				        while ((str = in.readLine()) != null) {
				            json.append(str).append("\n");
				        }
				    } finally {
				        in.close();
				    }
				} catch (Exception e) {
					Log.e(TAG, "Failed to read JSON from stream" + e);
				    //throw new RuntimeException("Failed to read JSON from stream", e);
				}
				Log.d(TAG, "GCM Server response : " + json.toString());
			}
		}
		finally
		{
			if (conn != null)
			{
				conn.disconnect();
			}
		}
	}
}
