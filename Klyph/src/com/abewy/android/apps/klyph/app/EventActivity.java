package com.abewy.android.apps.klyph.app;

import android.os.Bundle;
import com.abewy.android.apps.klyph.KlyphApplication;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.fragment.EventFragment;

public class EventActivity extends TitledFragmentActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Let the Application class know that the first launch is complete
		// If we come from a notification, then do not show the ads
		// When going back to main activity
		KlyphApplication.getInstance().launchComplete();
		
		String id = getIntent().getStringExtra(KlyphBundleExtras.EVENT_ID);
		String name = getIntent().getStringExtra(KlyphBundleExtras.EVENT_NAME);

		setTitle(name);
		
		EventFragment eventFragment = (EventFragment) getFragmentManager().findFragmentById(R.id.event_fragment);
		eventFragment.setElementId(id);
		eventFragment.load();
		
		enableAds(true);
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_event;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
