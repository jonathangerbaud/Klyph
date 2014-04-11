package com.abewy.android.apps.klyph.adm;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.abewy.android.apps.klyph.KlyphApplication;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.service.FriendRequestService;
import com.abewy.android.apps.klyph.service.NotificationService;
import com.amazon.device.messaging.ADMMessageHandlerBase;
import com.amazon.device.messaging.ADMMessageReceiver;

public class ADMMessageHandler extends ADMMessageHandlerBase
{

	public ADMMessageHandler(String arg0)
	{
		super(arg0);
	}
	
	public ADMMessageHandler()
	{
		super(ADMMessageHandler.class.getName());
	}

	public static class Receiver extends ADMMessageReceiver
	{
		public Receiver()
		{
			super(ADMMessageHandler.class);
		}

		// Nothing else is required here; your broadcast receiver automatically
		// forwards intents to your service for processing.
	}

	@Override
	protected void onRegistered(final String newRegistrationId)
	{
		Log.d("ADMMessageHandler", "onRegistered: ");
		// You start the registration process by calling startRegister() in your Main
		// Activity. When the registration ID is ready, ADM calls onRegistered() on
		// your app. Transmit the passed-in registration ID to your server, so your
		// server can send messages to this app instance. onRegistered() is also
		// called if your registration ID is rotated or changed for any reason; your
		// app should pass the new registration ID to your server if this occurs.
		// Your server needs to be able to handle a registration ID up to 1536 characters
		// in length.
		new RegisterOnServerTask(newRegistrationId).execute(KlyphApplication.getInstance());

	}

	@Override
	protected void onUnregistered(final String registrationId)
	{
		Log.d("ADMMessageHandler", "onUnregistered: ");
		// If your app is unregistered on this device, inform your server that
		// this app instance is no longer a valid target for messages.
		new UnregisterOnServerTask(registrationId).execute(KlyphApplication.getInstance());
	}

	@Override
	protected void onRegistrationError(final String errorId)
	{
		// You should consider a registration error fatal. In response, your app may
		// degrade gracefully, or you may wish to notify the user that this part of
		// your app's functionality is not available.
	}

	@Override
	protected void onMessage(final Intent intent)
	{
		Log.d("ADMMessageHandler", "onMessage: ");
		if (KlyphPreferences.notifyNotifications() == true)
		{
			startService(new Intent(this, NotificationService.class));
			
			if (KlyphPreferences.notifyFriendRequest())
				startService(new Intent(this, FriendRequestService.class));
		}
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