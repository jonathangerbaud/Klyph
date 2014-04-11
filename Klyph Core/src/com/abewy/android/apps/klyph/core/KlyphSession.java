package com.abewy.android.apps.klyph.core;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.abewy.android.apps.klyph.core.fql.User;
import com.facebook.Session;

public class KlyphSession
{
	private static final String	SESSION_USER_ID				= "sessionUserId";
	private static final String	SESSION_USER_NAME			= "sessionUserName";
	private static final String	SESSION_USER_LOCALE			= "sessionUserLocale";
	
	private static User			sessionUser;
	
	public static User getSessionUser()
	{
		if (sessionUser != null)
		{
			return sessionUser;
		}
		else
		{
			String id = getSessionUserId();
			String name = getSessionUserName();
			String locale = getSessionUserLocale();
			
			if (name != null && id != null)
			{
				User user = new User();
				user.setUid(id);
				user.setName(name);
				user.setLocale(locale);
				return user;
			}
			
			return null;
		}
	}
	
	public static void setSessionUser(User sessionUser)
	{
		KlyphSession.sessionUser = sessionUser;

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
		SharedPreferences.Editor editor = sp.edit();

		if (sessionUser != null)
		{
			editor.putString(SESSION_USER_ID, sessionUser.getUid());
			editor.putString(SESSION_USER_NAME, sessionUser.getName());
			editor.putString(SESSION_USER_LOCALE, sessionUser.getLocale());
		}
		else
		{
			editor.remove(SESSION_USER_ID);
			editor.remove(SESSION_USER_NAME);
			editor.remove(SESSION_USER_LOCALE);
		}

		editor.commit();
	}
	
	public static String getSessionUserId()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
		return sp.getString(SESSION_USER_ID, null);
	}

	public static String getSessionUserName()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
		return sp.getString(SESSION_USER_NAME, null);
	}
	
	public static String getSessionUserLocale()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
		return sp.getString(SESSION_USER_LOCALE, null);
	}
	
	public static boolean isLogged()
	{
		return getSessionUserId() != null && getSessionUserId().length() > 0;
	}

	public static void logout()
	{		
		BaseApplication.getInstance().onLogout();
		
		NotificationManager nm = (NotificationManager) BaseApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancelAll();
		
		setSessionUser(null);
		Session.getActiveSession().closeAndClearTokenInformation();
	}
}
