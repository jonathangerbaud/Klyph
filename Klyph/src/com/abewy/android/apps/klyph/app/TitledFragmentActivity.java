package com.abewy.android.apps.klyph.app;

import java.util.List;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphData;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.KlyphLocale;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.facebook.IFbPermissionCallback;
import com.abewy.android.apps.klyph.facebook.IFbPermissionWorker;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.app.BaseFragmentActivity;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class TitledFragmentActivity extends BaseFragmentActivity implements IFbPermissionWorker, IActionbarSpinner
{
	private static final int	SETTINGS_CODE	= 674;

	private int					currentTheme;
	private String				currentLocale;
	private boolean				userHasDonated	= false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(getCustomTheme());
		KlyphLocale.defineLocale(getBaseContext());

		super.onCreate(savedInstanceState);

		currentTheme = getCustomTheme();
		currentLocale = KlyphLocale.getDeviceLocale();

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		KlyphDevice.initDeviceValues(this);

		userHasDonated = KlyphPreferences.hasUserDonated();

		if (!(this instanceof MainActivity))
		{
			setAppIconBackToHomeEnabled(true);
			displayBackArrow(true);
		}
		else
		{
			setAppIconBackToHomeEnabled(false);
		}

		/*
		 * getSupportActionBar().setStackedBackgroundDrawable(
		 * getResources().getDrawable(
		 * R.drawable.stacked_transparent_light_holo));
		 */

		//enableAds(true);

		if (!KlyphSession.isLogged())
		{
			if (!(this instanceof MainActivity))
				logout();
		}
		else if (Session.getActiveSession() == null)
		{
			Session.openActiveSessionFromCache(this);
		}

		if (getIntent().getBooleanExtra(KlyphBundleExtras.SET_NOTIFICATION_AS_READ, false) == true)
		{
			String id = getIntent().getStringExtra(KlyphBundleExtras.NOTIFICATION_ID);

			if (id != null && id.length() > 0)
			{
				new AsyncRequest(Query.POST_READ_NOTIFICATION, id, "", null).execute();
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		KlyphDevice.initDeviceValues(this, true);
		KlyphLocale.defineLocale(getBaseContext());
	}

	protected void enableAds(boolean enabled)
	{
		ViewGroup adContainer = (ViewGroup) findViewById(R.id.ad);
		manageAdView(adContainer, KlyphPreferences.areBannerAdsEnabled() && enabled);
	}

	@Override
	protected int getLayout()
	{
		return 0;
	}

	@Override
	protected int getCustomTheme()
	{
		return KlyphPreferences.getTheme();
	}

	@Override
	public boolean  onCreateOptionsMenu(Menu menu)
	{
		// menu.clear();

		if (Session.getActiveSession() != null && Session.getActiveSession().isOpened())
		{
			// menu.add(Menu.NONE, R.id.menu_chat, 7, R.string.menu_chat);
			menu.add(Menu.NONE, R.id.menu_feedback, 7, R.string.menu_feedback);

			/*
			 * if (!KlyphPreferences.hasUserDonated())
			 * {
			 * menu.add(Menu.NONE, R.id.menu_donate, 8, R.string.menu_donate);
			 * }
			 */

			menu.add(Menu.NONE, R.id.menu_settings, 9, R.string.menu_preferences);
			menu.add(Menu.NONE, R.id.menu_logout, 10, R.string.menu_logout);
			return true;
		}

		return false;
	}

	@Override
	public boolean  onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_feedback)
		{
			Intent intent = new Intent(this, FeedBackActivity.class);
			startActivity(intent);

			return true;
		}
		/*
		 * else if (item.getItemId() == R.id.menu_donate)
		 * {
		 * Intent intent = new Intent(this, DonateActivity.class);
		 * startActivity(intent);
		 * 
		 * return true;
		 * }
		 */
		else if (item.getItemId() == R.id.menu_settings)
		{
			Intent intent = new Intent(this, PreferencesActivity.class);
			startActivityForResult(intent, SETTINGS_CODE);

			return true;
		}
		if (item.getItemId() == R.id.menu_logout)
		{
			AlertUtil.showAlert(this, R.string.menu_logout, R.string.logout_confirmation, R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					logout();
				}
			}, R.string.cancel, null);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void logout()
	{
		KlyphSession.logout();
		KlyphData.reset();

		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		startActivity(intent);
	}

	/*
	 * @Override protected Drawable getActionBarBackground() { BitmapDrawable bg
	 * = (BitmapDrawable) getResources().getDrawable( R.drawable.action_bar_bg);
	 * bg.setTileModeX(TileMode.REPEAT); return bg; }
	 */

	@Override
	protected Class<? extends Activity> getHomeClass()
	{
		return MainActivity.class;
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		uiHelper.onResume();

		if (userHasDonated != KlyphPreferences.hasUserDonated())
		{
			userHasDonated = KlyphPreferences.hasUserDonated();
			supportInvalidateOptionsMenu();

			enableAds(true);
		}
	}

	// ___ Fb Permissions stuff ________________________________________________
	private boolean					pendingAnnounce	= false;
	private UiLifecycleHelper		uiHelper;
	private IFbPermissionCallback	callbackObject;
	private Session.StatusCallback	callback		= new Session.StatusCallback() {
														@Override
														public void call(final Session session, final SessionState state, final Exception exception)
														{
															onSessionStateChange(session, state, exception);
														}

													};

	protected void onSessionStateChange(final Session session, SessionState state, Exception exception)
	{
		if (session != null && session.isOpened())
		{
			if (state.equals(SessionState.OPENED_TOKEN_UPDATED))
			{
				tokenUpdated();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data);

		if (requestCode == IFbPermissionWorker.REAUTH_ACTIVITY_CODE && resultCode == Activity.RESULT_CANCELED)
		{
			if (callbackObject != null)
			{
				callbackObject.onCancelPermissions();
			}
		}
		else if (requestCode == SETTINGS_CODE && resultCode == 0)
		{
			if (KlyphPreferences.getTheme() != currentTheme || !KlyphLocale.getDeviceLocale().equals(currentLocale))
			{
				restart();
			}
		}
	}

	private void restart()
	{
		Intent localIntent = new Intent(this, MainActivity.class);
		localIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(localIntent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}

	/**
	 * Notifies that the session token has been updated.
	 */
	private void tokenUpdated()
	{
		if (pendingAnnounce)
		{
			pendingAnnounce = false;
			if (callbackObject != null)
			{
				callbackObject.onPermissionsChange();
				callbackObject = null;
			}
		}
	}

	public void requestPublishPermissions(IFbPermissionCallback callback, List<String> permissions)
	{
		callbackObject = callback;
		pendingAnnounce = true;

		Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, permissions);
		newPermissionsRequest.setRequestCode(IFbPermissionWorker.REAUTH_ACTIVITY_CODE);
		Session.getActiveSession().requestNewPublishPermissions(newPermissionsRequest);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// No call for super(). Bug on API Level > 11.
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
		uiHelper = null;
		callback = null;
		callbackObject = null;
	}

	@Override
	public void displaySpinnerInActionBar(int array, int position, OnNavigationListener listener)
	{
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(this, array, android.R.layout.simple_dropdown_item_1line);
		list.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setListNavigationCallbacks(list, listener);
		getActionBar().setSelectedNavigationItem(position);
	}
	
	@Override
	public void displaySpinnerInActionBar(List<GraphObject> data, int position, OnNavigationListener listener)
	{
		MultiObjectAdapter adapter = new MultiObjectAdapter(null, SpecialLayout.DROP_DOWN_ITEM);
		adapter.addAll(data);
		
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setListNavigationCallbacks(adapter, listener);
		getActionBar().setSelectedNavigationItem(position);
	}

	@Override
	public void removeSpinnerInActionBar()
	{
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActionBar().setListNavigationCallbacks(null, null);
	}
}