package com.abewy.android.apps.klyph.core;

import android.annotation.TargetApi;
import android.app.Application;
import android.util.Log;
import com.crashlytics.android.Crashlytics;

public abstract class BaseApplication extends Application
{
	private static BaseApplication instance;
	
	@Override
	public void onCreate()
	{
		instance = this;
		
		initGlobals();
		initBugReport();
		initPreferences();
		initAds();
		initOthers();

		super.onCreate();
	}
	
	public static BaseApplication getInstance()
	{
		return instance;
	}

	private void initBugReport()
	{
		if (KlyphFlags.ENABLE_BUG_REPORT)
		{
			Crashlytics.start(this);
		}
	}

	protected abstract void initPreferences();

	protected abstract void initGlobals();
	
	protected abstract void initAds();

	protected abstract void initOthers();
	
	public abstract void onLogout();

	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
		Log.i("BaseApplication", "onLowMemory");
	}

	@Override
	@TargetApi(14)
	public void onTrimMemory(int level)
	{
		super.onTrimMemory(level);
		Log.i("BaseApplication", "onTrimMemory");
	}
}

