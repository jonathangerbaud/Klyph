package com.abewy.android.apps.klyph.app;

import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.KlyphService;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphFlags;
import com.abewy.android.apps.klyph.core.KlyphLocale;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private static final String			ABOUT_KEY				= "preference_about";
	private static final String			CHANGELOG_KEY			= "preference_changelog";
	private static final String			BUY_PRO_VERSION_KEY		= "preference_buy_pro_version";
	public static final String			NOTIFICATIONS_SYSTEM	= "preference_notifications_system";

	private String						previousRingtone;
	private UiLifecycleHelper			uiHelper;
	private boolean						pendingAnnounce			= false;
	private static final int			RINGTONE_CODE			= 159;
	private static final int			SONG_CODE				= 167;
	private static final int			REAUTH_ACTIVITY_CODE	= 100;
	private static final List<String>	PERMISSIONS				= Arrays.asList("manage_notifications");

	private Session.StatusCallback		callback				= new Session.StatusCallback() {
																	@Override
																	public void call(final Session session, final SessionState state,
																			final Exception exception)
																	{
																		onSessionStateChange(session, state, exception);
																	}
																};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setTheme(KlyphPreferences.getPreferencesTheme());
		KlyphLocale.defineLocale(getBaseContext());

		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		final Preference p = findPreference(KlyphPreferences.PREFERENCE_IMAGE_CLEAR_CACHE);

		p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean  onPreferenceClick(Preference preference)
			{
				p.setEnabled(false);
				Toast.makeText(PreferencesActivity.this, R.string.preference_images_cache_clearing, Toast.LENGTH_SHORT).show();

				new ClearCacheTask().execute();

				return true;
			}
		});

		setNotificationsIntervalEnabled();
		refreshRingtoneSummary();
		refreshInterval();
		refreshAppLanguage();
		refreshFbLanguage();

		previousRingtone = KlyphPreferences.getNotificationRingtone();

		Preference aboutPref = findPreference(ABOUT_KEY);
		Preference changelogPref = findPreference(CHANGELOG_KEY);

		aboutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean  onPreferenceClick(Preference preference)
			{
				Intent intent = new Intent(PreferencesActivity.this, AboutActivity.class);
				startActivity(intent);
				return true;
			}
		});

		changelogPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean  onPreferenceClick(Preference preference)
			{
				Intent intent = new Intent(PreferencesActivity.this, ChangeLogActivity.class);
				startActivity(intent);
				return true;
			}
		});

		if (KlyphPreferences.arePushNotificationsAvailable() == false)
		{
			Preference notificationSystemPref = findPreference(NOTIFICATIONS_SYSTEM);
			notificationSystemPref.setEnabled(false);
			notificationSystemPref.setShouldDisableView(true);
		}

		if (KlyphFlags.IS_PRO_VERSION == true || KlyphFlags.IS_AMAZON_VERSION == true)
		{
			Preference buyProPref = findPreference(BUY_PRO_VERSION_KEY);
			buyProPref.setEnabled(false);
			buyProPref.setShouldDisableView(true);
		}

		/*
		 * Preference p = findPreference(KlyphPreferences.PREFERENCE_IMAGE_SAVE_DIRECTORY);
		 * p.setSummary("hi/ha/yalala");
		 * 
		 * p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
		 * 
		 * @Override
		 * public boolean  onPreferenceClick(Preference preference)
		 * {
		 * Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		 * intent.setType("folder://");
		 * intent.addCategory(Intent.CATEGORY_OPENABLE);
		 * 
		 * try {
		 * startActivityForResult(
		 * Intent.createChooser(intent, "Select a File to Upload"),
		 * 12);
		 * } catch (android.content.ActivityNotFoundException ex) {
		 * // Potentially direct the user to the Market with a Dialog
		 * Toast.makeText(PreferencesActivity.this, "Please install a File Manager.",
		 * Toast.LENGTH_SHORT).show();
		 * }
		 * return false;
		 * }
		 * });
		 */
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		if (key.equals(KlyphPreferences.PREFERENCE_THEME))
		{
			restart();
		}
		else 
		if (key.equals(KlyphPreferences.PREFERENCE_VIEW_PAGER_INDICATOR_STYLE) || key.equals(KlyphPreferences.PERFORMANCES_CARD_ANIMATION)
					|| key.equals(KlyphPreferences.PREFERENCE_LEFT_DRAWER_MENU_CLASSES))
		{
			AlertUtil.showAlert(this, R.string.restart, R.string.option_need_restart, R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
									| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

					startActivity(intent);
					finish();
				}
			});
		}
		else if (key.equals(KlyphPreferences.PREFERENCE_APP_LANGUAGE))
		{
			String l = sharedPreferences.getString(key, "default");

			KlyphLocale.setAppLocale(l);

			restart();

		}
		else if (key.equals(KlyphPreferences.PREFERENCE_NOTIFICATIONS))
		{
			boolean isEnabled = sharedPreferences.getBoolean(key, true);

			if (isEnabled == true)
			{
				handleSetNotifications();
			}
			else
			{
				KlyphService.stopServices();
				setNotificationsIntervalEnabled();
			}
		}
		else if (key.equals(KlyphPreferences.PREFERENCE_NOTIFICATIONS_SYSTEM))
		{
			startOrStopNotificationsServices();
			setNotificationsIntervalEnabled();
		}
		else if (key.equals(KlyphPreferences.PREFERENCE_NOTIFICATIONS_BIRTHDAY)
					|| key.equals(KlyphPreferences.PREFERENCE_NOTIFICATIONS_BIRTHDAY_TIME))
		{
			if (sharedPreferences.getBoolean(KlyphPreferences.PREFERENCE_NOTIFICATIONS_BIRTHDAY, false) == true)
				KlyphService.startBirthdayService();
			else
				KlyphService.stopBirthdayService();
		}
		else if (key.equals(KlyphPreferences.PREFERENCE_NOTIFICATIONS_RINGTONE))
		{
			if (KlyphPreferences.getNotificationRingtone().equals("ringtone"))
			{
				Intent ringtoneManager = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

				// specifies what type of tone we want, in this case "ringtone", can be notification if you want
				ringtoneManager.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);

				// gives the title of the RingtoneManager picker title
				ringtoneManager.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.preference_notification_ringtone_chooser));

				// returns true shows the rest of the songs on the device in the default location
				ringtoneManager.getBooleanExtra(RingtoneManager.EXTRA_RINGTONE_INCLUDE_DRM, true);

				startActivityForResult(ringtoneManager, RINGTONE_CODE);
			}
			else if (KlyphPreferences.getNotificationRingtone().equals("song"))
			{
				Intent intent = new Intent();
				intent.setType("audio/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, getString(R.string.preference_notification_ringtone_chooser)), SONG_CODE);
			}
			else
			{
				KlyphPreferences.setNotificationRingtoneUri(null);
				refreshRingtoneSummary();
			}
		}
		else if (key.equals(KlyphPreferences.PREFERENCE_NOTIFICATIONS_INTERVAL))
		{
			refreshInterval();
		}
		else if (key.equals(KlyphPreferences.PREFERENCE_FB_LANGUAGE))
		{
			refreshFbLanguage();
		}
		else if (key.equals(KlyphPreferences.PERFORMANCES_PHOTO_EFFECT))
		{
			ImageLoader.FADE_ENABLED = KlyphPreferences.isPhotoEffectEnabled();
		}
	}

	private void restart()
	{
		Intent localIntent = new Intent(getApplicationContext(), PreferencesActivity.class);
		localIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(localIntent, Activity.RESULT_CANCELED);
		finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	private void setNotificationsIntervalEnabled()
	{
		Preference p1 = findPreference(KlyphPreferences.PREFERENCE_NOTIFICATIONS_INTERVAL);
		p1.setEnabled(KlyphPreferences.areNotificationsEnabled() && !KlyphPreferences.getNotificationSystem().equals("push"));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REAUTH_ACTIVITY_CODE)
		{
			uiHelper.onActivityResult(requestCode, resultCode, data);
		}
		else if (requestCode == RINGTONE_CODE)
		{
			if (resultCode == Activity.RESULT_CANCELED)
			{
				KlyphPreferences.setNotificationRingtone(previousRingtone);
			}
			else
			{
				Uri ringtoneURI = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
				Log.d("PreferencesActivity", "Returned uri " + ringtoneURI);
				if (ringtoneURI != null)
				{
					String ringtoneString = null;
					try
					{
						ringtoneString = RingtoneManager.getRingtone(this, ringtoneURI).getTitle(this);

					}
					catch (final Exception e)
					{
						Log.d("PreferencesActivity", "error " + e.getMessage());
						ringtoneString = "unknown";
					}
					Log.d("PreferencesActivity", "ringtoneString " + ringtoneString);
					KlyphPreferences.setNotificationRingtone(ringtoneString);
					KlyphPreferences.setNotificationRingtoneUri(ringtoneURI.toString());
				}
				else
				{
					KlyphPreferences.setNotificationRingtone(getString(R.string.none));
					KlyphPreferences.setNotificationRingtoneUri(null);

				}
				refreshRingtoneSummary();
			}
		}
		else if (requestCode == SONG_CODE)
		{
			if (resultCode == Activity.RESULT_CANCELED)
			{
				KlyphPreferences.setNotificationRingtone(previousRingtone);
			}
			else
			{
				String path = data.getDataString();
				String name = path;
				int index = name.lastIndexOf("/");
				if (index != -1)
					name = name.substring(index + 1);
				KlyphPreferences.setNotificationRingtone(name);
				KlyphPreferences.setNotificationRingtoneUri(path);
				refreshRingtoneSummary();
			}
		}
	}

	private void onSessionStateChange(final Session session, SessionState state, Exception exception)
	{
		if (session != null && session.isOpened())
		{
			if (state.equals(SessionState.OPENED_TOKEN_UPDATED))
			{
				tokenUpdated();
			}
		}
	}

	/**
	 * Notifies that the session token has been updated.
	 */
	private void tokenUpdated()
	{
		if (pendingAnnounce)
		{
			handleSetNotifications();
		}
	}

	private void refreshRingtoneSummary()
	{
		Preference p = findPreference(KlyphPreferences.PREFERENCE_NOTIFICATIONS_RINGTONE);
		p.setSummary(KlyphPreferences.getNotificationRingtone());
	}

	private void refreshInterval()
	{
		ListPreference p = (ListPreference) findPreference(KlyphPreferences.PREFERENCE_NOTIFICATIONS_INTERVAL);
		p.setSummary(p.getEntry());
	}

	private void refreshAppLanguage()
	{
		ListPreference p = (ListPreference) findPreference(KlyphPreferences.PREFERENCE_APP_LANGUAGE);
		p.setSummary(p.getEntry());
	}

	private void refreshFbLanguage()
	{
		ListPreference p = (ListPreference) findPreference(KlyphPreferences.PREFERENCE_FB_LANGUAGE);
		p.setSummary(p.getEntry());
	}

	private void handleSetNotifications()
	{
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		@SuppressWarnings("deprecation") CheckBoxPreference cpref = (CheckBoxPreference) findPreference("preference_notifications");

		pendingAnnounce = false;
		final Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingAnnounce = true;
			editor.putBoolean(KlyphPreferences.PREFERENCE_NOTIFICATIONS, false);
			editor.commit();
			cpref.setChecked(false);

			AlertUtil.showAlert(this, R.string.preferences_notifications_permissions_title, R.string.preferences_notifications_permissions_message,
					R.string.ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							requestPublishPermissions(session);
						}
					}, R.string.cancel, null);
			return;
		}

		editor.putBoolean(KlyphPreferences.PREFERENCE_NOTIFICATIONS, true);
		editor.commit();
		cpref.setChecked(true);

		startOrStopNotificationsServices();

		if (sharedPreferences.getBoolean(KlyphPreferences.PREFERENCE_NOTIFICATIONS_BIRTHDAY, false) == true)
			KlyphService.startBirthdayService();
	}

	private void startOrStopNotificationsServices()
	{
		if (KlyphPreferences.arePushNotificationsEnabled())
		{
			KlyphService.startPushNotificationsService();
		}
		else
		{
			KlyphService.stopPushNotificationsService();
		}

		if (KlyphPreferences.arePeriodicNotificationsEnabled())
		{
			KlyphService.startPeriodicNotificationService();
		}
		else
		{
			KlyphService.stopPeriodicNotificationService();
		}
	}

	private void requestPublishPermissions(Session session)
	{
		Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
		newPermissionsRequest.setRequestCode(REAUTH_ACTIVITY_CODE);
		Session.getActiveSession().requestNewPublishPermissions(newPermissionsRequest);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume()
	{
		super.onResume();
		uiHelper.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause()
	{
		super.onPause();
		uiHelper.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);

		uiHelper.onSaveInstanceState(bundle);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
		uiHelper = null;
		callback = null;
	}

	private class ClearCacheTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params)
		{
			ImageLoader.clearImageCache();
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			Toast.makeText(PreferencesActivity.this, R.string.preference_images_cache_cleared, Toast.LENGTH_SHORT).show();
		}
	}
}
