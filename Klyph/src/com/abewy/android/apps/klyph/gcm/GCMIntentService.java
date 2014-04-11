package com.abewy.android.apps.klyph.gcm;

import static com.abewy.android.apps.klyph.gcm.CommonUtilities.SENDER_ID;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.service.FriendRequestService;
import com.abewy.android.apps.klyph.service.NotificationService;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService
{
	private static final String	TAG	= "GCMIntentService";

	public GCMIntentService()
	{
		super(SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId)
	{
		//Log.i(TAG, "Device registered: regId = " + registrationId);

		KlyphPreferences.setGCMRegId(registrationId);

		new RegisterOnServerTask(registrationId).execute(context);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId)
	{
		Log.i(TAG, "Device unregistered");

		new UnregisterOnServerTask(registrationId).execute(context);

		KlyphPreferences.setGCMRegId("");
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(final Context context, Intent intent)
	{
		//String message = intent.getExtras().getString("fb_field");
		//Log.i(TAG, "Received message " + message);

		if (KlyphPreferences.notifyNotifications() == true)
		{
			startService(new Intent(this, NotificationService.class));
			
			if (KlyphPreferences.notifyFriendRequest())
				startService(new Intent(this, FriendRequestService.class));
		}
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total)
	{
		Log.i(TAG, "Received deleted messages notification");
		// String message = getString(R.string.gcm_deleted, total);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId)
	{
		Log.i(TAG, "Received error: " + errorId);
	}

	@Override
	protected boolean  onRecoverableError(Context context, String errorId)
	{
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);

		return super.onRecoverableError(context, errorId);
	}
	
	private static class RegisterOnServerTask extends AsyncTask<Context, Void, Void>
	{
		final String	regId;

		public RegisterOnServerTask(String regId)
		{
			this.regId = regId;
		}

		@Override
		protected Void doInBackground(Context... params)
		{
			ServerUtilities.register(params[0], regId);
			return null;
		}
	}

	private static class UnregisterOnServerTask extends AsyncTask<Context, Void, Void>
	{
		final String	regId;

		public UnregisterOnServerTask(String regId)
		{
			this.regId = regId;
		}

		@Override
		protected Void doInBackground(Context... params)
		{
			ServerUtilities.unregister(params[0], regId);
			return null;
		}
	}
}
