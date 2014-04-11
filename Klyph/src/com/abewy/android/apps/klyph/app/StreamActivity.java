package com.abewy.android.apps.klyph.app;

import android.os.Bundle;
import android.util.Log;
import com.abewy.android.apps.klyph.KlyphApplication;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.fragment.StreamFragment;
import com.abewy.android.apps.klyph.R;
import com.facebook.Session;
import com.facebook.SessionState;

public class StreamActivity extends TitledFragmentActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d("StreamActivity", "onCreate: ");
		super.onCreate(savedInstanceState);
Log.d("StreamActivity", "onCreate: 2");
		// Let the Application class know that the first launch is complete
		// If we come from a notification, then do not show the ads
		// When going back to main activity
		KlyphApplication.getInstance().launchComplete();
		
		if (Session.getActiveSession() == null)
		{
			Log.d("StreamActivity", "Session is null ");
			Session.openActiveSessionFromCache(this);
		}

		StreamFragment streamFragment = (StreamFragment) getFragmentManager().findFragmentById(R.id.stream_fragment);

		// setTitle("Messages");

		Stream stream = getIntent().getParcelableExtra(KlyphBundleExtras.STREAM_PARCELABLE);

		if (stream != null)
		{
			Log.d("StreamActivity", "setStreamAndLoad ");
			streamFragment.setStreamAndLoad(stream);
		}
		else
		{
			Log.d("StreamAct",
					"Received id " + getIntent().getStringExtra(KlyphBundleExtras.STREAM_ID) + " "
							+ getIntent().getBooleanExtra(KlyphBundleExtras.STREAM_GROUP, false));
			streamFragment.setElementId(getIntent().getStringExtra(KlyphBundleExtras.STREAM_ID));

			if (getIntent().getBooleanExtra(KlyphBundleExtras.STREAM_GROUP, false) == true)
			{
				Log.d("StreamAct", "is group ");
				streamFragment.setIsStreamGroup();
			}
			Log.d("StreamActivity", "load ");
			streamFragment.load();
		}
	}

	@Override
	protected void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		super.onSessionStateChange(session, state, exception);

		Log.d("StreamActivity", "onSessionStateChange " + state);
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_stream;
	}
}
