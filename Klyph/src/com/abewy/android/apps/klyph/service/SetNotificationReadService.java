/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.service;

import java.lang.ref.WeakReference;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.core.request.BaseAsyncRequest;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class SetNotificationReadService extends Service
{

	public SetNotificationReadService()
	{
		
	}

	private Looper			mServiceLooper;
	private ServiceHandler	mServiceHandler;

	@Override
	public void onCreate()
	{
		HandlerThread thread = new HandlerThread("SetNotificationReadServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper, new WeakReference<Service>(this));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Message msg = mServiceHandler.obtainMessage();
		
		if (intent != null)
		{
			msg.obj = intent.getStringExtra(KlyphBundleExtras.NOTIFICATION_ID);
			mServiceHandler.sendMessage(msg);
			stopSelf();
		}

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy()
	{

	}

	// Handler that receives messages from the thread
	private static final class ServiceHandler extends Handler
	{
		private final WeakReference<Service>	service;
		private int inProgressActions = 0;

		public ServiceHandler(Looper looper, WeakReference<Service> service)
		{
			super(looper);
			this.service = service;
		}

		@Override
		public void handleMessage(Message msg)
		{
			inProgressActions++;
			
			String notficationId = (String) msg.obj;
			new AsyncRequest(Query.POST_READ_NOTIFICATION, notficationId, "", new BaseAsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					inProgressActions--;
					
					KlyphPreferences.setNotificationReadStatusChanged(true);
					
					if (service.get() != null)
					{
						Intent intent = new Intent();
						intent.setAction("com.abewy.android.apps.klyph.action.NOTIFICATION_STATUS_CHANGE");
						service.get().sendBroadcast(intent);
					}
					
					if (inProgressActions == 0)
					{
						if (service.get() != null)
							service.get().stopSelf();
					}
				}
			}).execute();
		}
	}
}
