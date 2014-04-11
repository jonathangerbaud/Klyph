package com.abewy.android.apps.klyph.app;

import java.util.List;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.abewy.app.BaseViewPagerActivity;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class TitledViewPagerActivity extends BaseViewPagerActivity implements IFbPermissionWorker, IActionbarSpinner
{
	private static final int		SETTINGS_CODE	= 674;

	private int						currentTheme;
	private String					currentLocale;
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
	private boolean					userHasDonated	= false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(getCustomTheme());
		KlyphLocale.defineLocale(getBaseContext());
		KlyphDevice.initDeviceValues(this, true);

		super.onCreate(savedInstanceState);

		currentTheme = getCustomTheme();
		currentLocale = KlyphLocale.getDeviceLocale();

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		userHasDonated = KlyphPreferences.hasUserDonated();

		KlyphDevice.initDeviceValues(this);

		setAppIconBackToHomeEnabled(true);
		displayBackArrow(true);

		getViewPager().setOffscreenPageLimit(3);

		setLoadingView(findViewById(R.id.progressBar));

		// if (KlyphPreferences.areBannerAdsEnabled())
		// initAds();

		if (!KlyphSession.isLogged())
		{
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

	protected void initAds()
	{
		ViewGroup adContainer = (ViewGroup) findViewById(R.id.ad);
		manageAdView(adContainer, KlyphPreferences.areBannerAdsEnabled());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);

		if (requestCode == IFbPermissionWorker.REAUTH_ACTIVITY_CODE && resultCode == Activity.RESULT_CANCELED)
		{
			if (callbackObject != null)
			{
				callbackObject.onCancelPermissions();
				callbackObject = null;
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

		if (Session.getActiveSession().isOpened())
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

		super.onOptionsItemSelected(item);

		return false;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		KlyphDevice.initDeviceValues(this, true);
		KlyphLocale.defineLocale(getBaseContext());
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
	protected ViewPager getViewPager()
	{
		return (ViewPager) findViewById(R.id.pager);
	}

	@Override
	protected PageIndicator getPageIndicator()
	{
		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.title_indicator);
		TabPageIndicator tabIndicator = (TabPageIndicator) findViewById(R.id.tab_indicator);

		if (KlyphPreferences.showTabPageIndicator())
			return tabIndicator;
		else
			return titleIndicator;
	}

	@Override
	protected PagerAdapter getPagerAdapter()
	{
		return null;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// No call for super(). Bug on API Level > 11.
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		uiHelper.onResume();

		if (userHasDonated != KlyphPreferences.hasUserDonated())
		{
			userHasDonated = KlyphPreferences.hasUserDonated();
			invalidateOptionsMenu();

			// initAds();
		}
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

	public void displaySpinnerInActionBar(int array, int position, OnNavigationListener listener)
	{
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(this, array, android.R.layout.simple_dropdown_item_1line);
		list.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getActionBar().setListNavigationCallbacks(list, listener);
		getActionBar().setSelectedNavigationItem(position);
	}

	public void removeSpinnerInActionBar()
	{
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActionBar().setListNavigationCallbacks(null, null);
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
}