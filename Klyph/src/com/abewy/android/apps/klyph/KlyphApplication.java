package com.abewy.android.apps.klyph;

import android.preference.PreferenceManager;
import com.abewy.android.apps.klyph.core.BaseApplication;
import com.abewy.android.apps.klyph.core.KlyphLocale;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;

public class KlyphApplication extends BaseApplication
{
	private boolean  mIsFirstLaunch = true;
	
	public static KlyphApplication getInstance()
	{
		return (KlyphApplication) BaseApplication.getInstance();
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	protected void initPreferences()
	{
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		setTheme(KlyphPreferences.getTheme());
		getBaseContext().setTheme(KlyphPreferences.getTheme());
	}

	@Override
	protected void initGlobals()
	{
		Klyph.defineFacebookId();
		
		KlyphLocale.setAppLocale(KlyphLocale.getAppLocale());
	}
	
	@Override
	protected void initAds()
	{
		
	}

	@Override
	protected void initOthers()
	{
		ImageLoader.initImageLoader(this);
		ImageLoader.FADE_ENABLED = KlyphPreferences.isPhotoEffectEnabled();
	}
	
	@Override
	public void onLogout()
	{
		KlyphData.setLastStreams(null);
		KlyphService.stopServices();
		KlyphPreferences.setLastStories("{}");
	}
	
	public boolean  isFirstLaunch()
	{
		return mIsFirstLaunch;
	}
	
	public void launchComplete()
	{
		mIsFirstLaunch = false;
	}
}