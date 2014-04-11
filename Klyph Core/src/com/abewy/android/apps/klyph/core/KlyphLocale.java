package com.abewy.android.apps.klyph.core;

import java.util.Locale;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

public class KlyphLocale
{
	private static final String APP_LOCALE_KEY = "preference_app_language";
	private static final String FB_LOCALE_KEY = "preference_fb_language";
	private static final String DEFAULT = "default";
	
	public static void setAppLocale(String language)
	{
		if (language == null || language.length() == 0 || language.equals(DEFAULT))
		{
			language = getDeviceLocale();
		}

		Locale locale;
		if (language.contains("_"))
		{
			/* handle special language code, in language-country format */
			String array[] = language.split("_");
			locale = new Locale(array[0], array[1]);
		}
		else
		{
			locale = new Locale(language);
		}

		Locale.setDefault(locale);

		Configuration config = BaseApplication.getInstance().getResources().getConfiguration();
		config.locale = locale;
		BaseApplication.getInstance().getResources().updateConfiguration(config, BaseApplication.getInstance().getResources().getDisplayMetrics());
	}
	
	public static String getAppLocale()
	{
		return getPreferences().getString(APP_LOCALE_KEY, DEFAULT);
	}
	
	public static void defineLocale(Context context)
	{
		String language = getPreferences().getString(APP_LOCALE_KEY, DEFAULT);
		
		if (language == null || language.length() == 0 || language.equals(DEFAULT))
		{
			language = getDeviceLocale();
		}

		Locale locale;
		if (language.contains("_"))
		{
			/* handle special language code, in language-country format */
			String array[] = language.split("_");
			locale = new Locale(array[0], array[1]);
		}
		else
		{
			locale = new Locale(language);
		}

		Locale.setDefault(locale);

		Configuration config = BaseApplication.getInstance().getResources().getConfiguration();
		config.locale = locale;
		context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
	}

	public static String getFbLocale()
	{
		String locale = getPreferences().getString(FB_LOCALE_KEY, DEFAULT);

		if (locale.equals(DEFAULT))
			return KlyphSession.getSessionUser().getLocale();

		return locale;
	}

	public static String getDeviceLocale()
	{
		Locale locale = Locale.getDefault();

		return locale.getLanguage();
	}
	
	private static SharedPreferences getPreferences()
	{
		return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
	}
}
