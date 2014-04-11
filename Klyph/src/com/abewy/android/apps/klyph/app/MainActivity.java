package com.abewy.android.apps.klyph.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.abewy.android.ads.BannerAdManager;
import com.abewy.android.apps.klyph.KlyphApplication;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphData;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.KlyphService;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.KlyphFlags;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.User;
import com.abewy.android.apps.klyph.core.fql.User.Cover;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.core.util.FacebookUtil;
import com.abewy.android.apps.klyph.core.util.HierachyViewUtil;
import com.abewy.android.apps.klyph.fragment.IKlyphFragment;
import com.abewy.android.apps.klyph.fragment.LoginFragment;
import com.abewy.android.apps.klyph.fragment.LoginFragment.LoginFragmentCallBack;
import com.abewy.android.apps.klyph.fragment.Notifications;
import com.abewy.android.apps.klyph.fragment.Notifications.NotificationsListener;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.util.KlyphUtil;
import com.abewy.android.apps.klyph.widget.ProfileImageView;
import com.abewy.android.apps.klyph.widget.coverImage.UserCoverImageView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;

public class MainActivity extends TitledFragmentActivity implements LoginFragmentCallBack, NotificationsListener
{
	private static final String		BASE64_PUBLIC_KEY			= "[KEY]";

	private static final byte[]		SALT						= new byte[] {
					105,
					33,
					-46,
					96,
					-12,
					-47,
					-38,
					114,
					19,
					87,
					63,
					-18,
					41,
					-84,
					-6,
					23,
					-74,
					56,
					32,
					-105										};

	private static final String		KLYPH_PRO_PLAY_STORE_URI	= "https://play.google.com/store/apps/details?id=com.abewy.klyph.pro";
	private static final String		MESSENGER_PLAY_STORE_URI	= "https://play.google.com/store/apps/details?id=com.abewy.android.apps.klyph.messenger";
	private static final String		MESSENGER_PACKAGE_NAME		= "com.abewy.android.apps.klyph.messenger";

	private LicenseChecker			mChecker;
	private LicenseCheckerCallback	mLicenseCheckerCallback;
	// A handler on the UI thread.
	private Handler					mHandler;

	private static final String		FRAGMENT_TAG				= "MainActivityFragment";
	private DrawerLayout			drawer;
	private ActionBarDrawerToggle	drawerToggle;
	private List<String>			classes;
	private Notifications			notificationsFragment;
	private DrawerLayoutAdapter		navAdapter;
	private Cover					userCover;
	private String					userPicUrl;

	private class MyLicenseCheckerCallback implements LicenseCheckerCallback
	{
		public void allow(int policyReason)
		{
			if (isFinishing())
			{
				// Don't update UI if Activity is finishing.
				return;
			}
			// Should allow user access.
			// displayResult(getString(R.string.allow));
		}

		public void dontAllow(int policyReason)
		{
			if (isFinishing())
			{
				// Don't update UI if Activity is finishing.
				return;
			}
			// displayResult(getString(R.string.dont_allow));
			// Should not allow access. In most cases, the app should assume
			// the user has access unless it encounters this. If it does,
			// the app should inform the user of their unlicensed ways
			// and then either shut down the app or limit the user to a
			// restricted set of features.
			// In this example, we show a dialog that takes the user to Market.
			// If the reason for the lack of license is that the service is
			// unavailable or there is another problem, we display a
			// retry button on the dialog and a different message.
			// displayDialog(policyReason == Policy.RETRY);
		}

		public void applicationError(int errorCode)
		{
			if (isFinishing())
			{
				// Don't update UI if Activity is finishing.
				return;
			}
			// This is a polite way of saying the developer made a mistake
			// while setting up or calling the license checker library.
			// Please examine the error code and fix the error.
			// String result = String.format(getString(R.string.application_error), errorCode);
			// displayResult(result);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		/*
		 * if (KlyphSession.getSessionUserName() != null)
		 * {
		 * loggedIn = true;
		 * setTitle(KlyphSession.getSessionUserName());
		 * }
		 * else
		 * {
		 * if (KlyphFlags.IS_PRO_VERSION == true)
		 * setTitle(R.string.app_pro_name);
		 * else
		 * setTitle(R.string.app_name);
		 * }
		 */
		setTitle("");

		if (Session.getActiveSession() == null || KlyphSession.getSessionUserId() == null
			|| (Session.getActiveSession() != null && Session.getActiveSession().isOpened() == false))
		{
			getActionBar().hide();
			getFragmentManager().beginTransaction().add(R.id.main, new LoginFragment(), FRAGMENT_TAG).commit();
		}

		// notificationsFragment.setHasOptionsMenu(false);
		adContainer = (ViewGroup) findViewById(R.id.ad);

		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		drawerToggle = new ActionBarDrawerToggle(this, drawer, AttrUtil.getResourceId(this, R.attr.drawerIcon), R.string.open, R.string.close) {
			@Override
			public void onDrawerOpened(View view)
			{
				Log.d("MainActivity.onCreate(...).new ActionBarDrawerToggle() {...}", "onDrawerOpened: ");
				super.onDrawerOpened(view);

				Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG);

				if (drawer.isDrawerOpen(Gravity.RIGHT))
				{
					// drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, Gravity.RIGHT);

					if (notificationsFragment != null)
					{
						notificationsFragment.setHasOptionsMenu(true);
						notificationsFragment.onOpenPane();
					}

					if (fragment != null)
						fragment.setHasOptionsMenu(false);
				}
				else if (drawer.isDrawerOpen(Gravity.LEFT))
				{

					if (notificationsFragment != null)
					{
						notificationsFragment.setHasOptionsMenu(false);
					}

					if (fragment != null)
						fragment.setHasOptionsMenu(true);
				}

				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View view)
			{
				super.onDrawerClosed(view);

				drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);

				if (!drawer.isDrawerOpen(Gravity.RIGHT))
				{
					if (notificationsFragment != null)
						notificationsFragment.setHasOptionsMenu(false);

					Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG);

					if (fragment != null)
						fragment.setHasOptionsMenu(true);
				}
				invalidateOptionsMenu();
			}
		};

		drawer.setDrawerListener(drawerToggle);

		final List<String> labels = KlyphPreferences.getLeftDrawerMenuLabels();
		classes = new ArrayList<String>(KlyphPreferences.getLeftDrawerMenuClasses());
		classes.add("com.abewy.android.apps.klyph.fragment.UserTimeline");
		navAdapter = new DrawerLayoutAdapter(getActionBar().getThemedContext(), R.layout.item_drawer_layout, labels);

		final ListView navList = (ListView) findViewById(R.id.drawer);

		// Setting drawers max width
		int maxWidth = getResources().getDimensionPixelSize(R.dimen.max_drawer_layout_width);

		int w = Math.min(KlyphDevice.getDeviceWidth(), KlyphDevice.getDeviceHeight()) - getResources().getDimensionPixelSize(R.dimen.dip_64);

		int finalWidth = Math.min(maxWidth, w);

		LayoutParams params = ((View) navList.getParent()).getLayoutParams();
		params.width = finalWidth;
		((View) navList.getParent()).setLayoutParams(params);

		final View notificationContainer = findViewById(R.id.notifications_container);
		params = notificationContainer.getLayoutParams();
		params.width = finalWidth;
		notificationContainer.setLayoutParams(params);

		// End max width
		navList.setFadingEdgeLength(0);
		navList.setVerticalFadingEdgeEnabled(false);
		navList.setAdapter(navAdapter);

		navList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int pos, long id)
			{
				updateContent(pos);
				drawer.closeDrawer(Gravity.LEFT);
			}
		});

		// Try to use more data here. ANDROID_ID is a single point of attack.
		// String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

		// Library calls this when it's done.
		// mLicenseCheckerCallback = new MyLicenseCheckerCallback();

		// Construct the LicenseChecker with a policy.
		// mChecker = new LicenseChecker(this, new ServerManagedPolicy(this, new AESObfuscator(SALT, getPackageName(), deviceId)), BASE64_PUBLIC_KEY);

		// mChecker.checkAccess(mLicenseCheckerCallback)

		// Facebook HashKey
		if (KlyphFlags.LOG_FACEBOOK_HASH)
			FacebookUtil.logHash(this);

		// Hierarchy View Connector
		if (KlyphFlags.ENABLE_HIERACHY_VIEW_CONNECTOR)
			HierachyViewUtil.connectHierarchyView(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (notificationsFragment != null && menu.findItem(R.id.menu_notifications) == null)
		{
			final MenuItem notificationItem = menu.add(Menu.NONE, R.id.menu_notifications, 2, R.string.menu_notifications);
			notificationItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			notificationItem.setActionView(R.layout.actionbar_item_notifications);

			final TextView notificationTextView = (TextView) notificationItem.getActionView().findViewById(R.id.textView);

			int count = notificationsFragment.getUnreadCount();

			notificationTextView.setText(String.valueOf(count));

			if (count > 0)
			{
				notificationTextView.setBackgroundResource(AttrUtil.getResourceId(this, R.attr.notificationsItemBackground));
			}
			else
			{
				notificationTextView.setBackgroundResource(R.drawable.notifications_read_background);

			}

			notificationItem.getActionView().setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					onOptionsItemSelected(notificationItem);
				}
			});
		}

		if (!KlyphFlags.IS_PRO_VERSION && menu.findItem(R.id.menu_buy_pro) == null)
		{
			menu.add(Menu.NONE, R.id.menu_buy_pro, 2, R.string.menu_buy_pro).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		}

		if (menu.findItem(R.id.menu_faq) == null)
		{
			menu.add(Menu.NONE, R.id.menu_faq, Menu.NONE, R.string.menu_faq).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onBackPressed()
	{
		if (drawer.isDrawerOpen(Gravity.RIGHT))
		{
			drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
			drawer.closeDrawer(Gravity.RIGHT);
		}
		else
		{
			super.onBackPressed();
		}
	}

	// ___ Facebook login management ___________________________________________

	ViewGroup		adContainer;

	private boolean	sessionInitalized	= false;
	private boolean	loggedIn			= false;

	@Override
	protected void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		Log.d("MainActivity", "onSessionStateChange");
		super.onSessionStateChange(session, state, exception);
		updateView();
	}

	@Override
	public void onUserInfoFetched(User user)
	{
		KlyphSession.setSessionUser(user);

		setTitle(user.getName());

		String birthday = user.getBirthday();
		if (birthday != null && birthday.length() == 10)
		{
			String[] parts = birthday.split("/");

			if (parts.length == 3)
			{
				@SuppressWarnings("deprecation") long date = new Date(Integer.valueOf(parts[2]), Integer.valueOf(parts[0]), Integer.valueOf(parts[1]))
						.getTime();
				BannerAdManager.setTargetingBirthday(date);
			}
		}

		if (!sessionInitalized)
		{
			endInit();
		}

		loggedIn = true;
	}

	private void updateView()
	{
		Session session = Session.getActiveSession();

		if (session.isOpened())
		{
			if (sessionInitalized == false && KlyphSession.getSessionUserId() != null)
			{
				endInit();
			}
		}
	}

	private void endInit()
	{
		if (sessionInitalized == false)
		{
			if (KlyphFlags.LOG_ACCESS_TOKEN)
				Log.d("MainActivity", Session.getActiveSession().getAccessToken());

			// setTitle(KlyphSession.getSessionUserName());

			// If just logged in and notifications enabled, then start the
			// service
			if (loggedIn == false)
			{
				KlyphService.startSelectedServices();
			}

			final TextView tv = (TextView) findViewById(R.id.user_name);
			tv.setText(KlyphSession.getSessionUserName());

			final View timelineView = findViewById(R.id.profile);
			timelineView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					updateContent(classes.size() - 1);
					drawer.closeDrawer(Gravity.LEFT);
				}
			});

			if (KlyphPreferences.areBannerAdsEnabled())
			{
				enableAds(true);
			}

			getActionBar().show();
			loadData();
		}
	}

	private void loadData()
	{
		if (!isFinishing())
		{
			// Let the Application class that the first launch is complete
			// So next time, don't display the fullscreen ad
			KlyphApplication.getInstance().launchComplete();

			// Load other stuff
			notificationsFragment = new Notifications();
			getFragmentManager().beginTransaction().replace(R.id.notifications_container, notificationsFragment, "NotificationsFragment")
					.commitAllowingStateLoss();
			getFragmentManager().invalidateOptionsMenu();

			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

			if (getIntent().getBooleanExtra(KlyphBundleExtras.SHOW_BIRTHDAYS, false) == true)
			{
				// Show birthdays fragment
				updateContent(9);
			}
			else
			{
				// Show newsfeed0 fragment
				updateContent(0);
			}

			drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

			getUser();

			sessionInitalized = true;

			if (getIntent().getBooleanExtra(KlyphBundleExtras.SHOW_NOTIFICATION_MENU, false) == true)
			{
				drawer.openDrawer(Gravity.RIGHT);
				
				notificationsFragment.setHasOptionsMenu(true);
				
				((Fragment) previousFragment).setHasOptionsMenu(false);
				
				invalidateOptionsMenu();
			}
		}
	}

	private void getUser()
	{
		new AsyncRequest(Query.USER, KlyphSession.getSessionUserId(), "", new AsyncRequest.Callback() {

			@Override
			public void onComplete(Response response)
			{
				onGetUserComplete(response);
			}
		}).execute();
	}

	private void onGetUserComplete(final Response response)
	{
		runOnUiThread(new Runnable() {

			@Override
			public void run()
			{
				if (response.getError() == null)
				{
					onRequestSuccess(response.getGraphObjectList());
				}
				else
				{
					onRequestError(response.getError());
				}
			}
		});
	}

	private void onRequestSuccess(List<GraphObject> result)
	{
		if (result.size() > 0)
		{
			User user = (User) result.get(0);

			userCover = user.getPic_cover();
			userPicUrl = user.getPic();

			refreshUserPics();
		}
	}

	private void refreshUserPics()
	{
		ProfileImageView picView = (ProfileImageView) findViewById(R.id.user_profile_picture);
		ImageView coverView = (ImageView) findViewById(R.id.user_cover_picture);

		picView.disableBorder();

		ImageLoader.display(picView, userPicUrl, KlyphUtil.getProfilePlaceHolder(picView.getContext()));

		String userCoverUrl = userCover != null ? userCover.getSource() : null;

		if (userCoverUrl != null && userCoverUrl.length() > 0)
		{
			UserCoverImageView userCoverImageView = (UserCoverImageView) coverView;
			userCoverImageView.setOffset(userCover.getOffset_y());
			ImageLoader.display(coverView, userCoverUrl, R.drawable.picture_place_holder_square_dark);
		}
		else
		{
			coverView.setImageResource(R.drawable.picture_place_holder_square_dark);
		}
	}

	private void onRequestError(RequestError error)
	{
		Log.d("LoginFragment", "onRequestError");
	}

	private int				oldSelection	= -1;
	private IKlyphFragment	previousFragment;

	private void updateContent(int selection)
	{
		if (selection != oldSelection)
		{
			Bundle bundle = new Bundle();
			bundle.putString(KlyphBundleExtras.ELEMENT_ID, KlyphSession.getSessionUserId());

			String className = classes.get(selection);

			if (className.equals("com.abewy.android.apps.klyph.fragment.Chat"))
			{
				PackageManager pm = getPackageManager();

				try
				{
					pm.getPackageInfo(MESSENGER_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
					Intent intent = getPackageManager().getLaunchIntentForPackage(MESSENGER_PACKAGE_NAME);
					startActivity(intent);
				}
				catch (NameNotFoundException e)
				{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MESSENGER_PLAY_STORE_URI));
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
					startActivity(intent);
				}
			}
			else
			{
				if (selection < navAdapter.getCount())
					setTitle(navAdapter.getItem(selection));
				else
					setTitle(KlyphSession.getSessionUserName());
				
				Fragment fragment = Fragment.instantiate(MainActivity.this, className, bundle);

				if (previousFragment != null)
					previousFragment.onSetToBack(this);

				FragmentTransaction tx = getFragmentManager().beginTransaction();

				tx.replace(R.id.main, fragment, FRAGMENT_TAG);
				tx.commitAllowingStateLoss();
				((IKlyphFragment) fragment).onSetToFront(this);

				previousFragment = (IKlyphFragment) fragment;

				navAdapter.setSelectedPosition(selection);
				navAdapter.notifyDataSetChanged();

				oldSelection = selection;

				if (notificationsFragment != null)
					notificationsFragment.setHasOptionsMenu(false);
			}
		}
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_main;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		/*
		 * if (drawerToggle.onOptionsItemSelected(item))
		 * {
		 * return true;
		 * }
		 */

		if (item.getItemId() == android.R.id.home)
		{
			if (drawer.isDrawerOpen(Gravity.LEFT))
			{
				drawer.closeDrawer(Gravity.LEFT);

				if (drawer.isDrawerOpen(Gravity.RIGHT))
					drawer.closeDrawer(Gravity.RIGHT);
			}
			else
			{
				drawer.openDrawer(Gravity.LEFT);

				if (drawer.isDrawerOpen(Gravity.RIGHT))
					drawer.closeDrawer(Gravity.RIGHT);
			}

			return true;
		}
		else if (item.getItemId() == R.id.menu_notifications)
		{
			if (drawer.isDrawerOpen(Gravity.RIGHT))
			{
				drawer.closeDrawer(Gravity.RIGHT);

				if (drawer.isDrawerOpen(Gravity.LEFT))
					drawer.closeDrawer(Gravity.LEFT);
			}
			else
			{
				drawer.openDrawer(Gravity.RIGHT);

				if (drawer.isDrawerOpen(Gravity.LEFT))
					drawer.closeDrawer(Gravity.LEFT);
			}

			return true;
		}
		else if (item.getItemId() == R.id.menu_buy_pro)
		{
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(KLYPH_PRO_PLAY_STORE_URI));
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			startActivity(intent);
		}
		else if (item.getItemId() == R.id.menu_logout)
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
		else if (item.getItemId() == R.id.menu_faq)
		{
			startActivity(new Intent(this, FaqActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}

	private void logout()
	{
		KlyphSession.logout();
		KlyphData.reset();

		if (adContainer != null)
		{
			adContainer.setVisibility(View.GONE);
		}

		getActionBar().hide();
		getFragmentManager().beginTransaction().remove(notificationsFragment);
		notificationsFragment = null;

		if (KlyphFlags.IS_PRO_VERSION == true)
			setTitle(R.string.app_pro_name);
		else
			setTitle(R.string.app_name);

		sessionInitalized = false;
		oldSelection = -1;

		drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

		if (notificationsFragment != null)
		{
			notificationsFragment.onLoggedOut();
			notificationsFragment.reset();
		}

		getFragmentManager().beginTransaction().replace(R.id.main, new LoginFragment(), FRAGMENT_TAG).commit();

		loggedIn = false;
	}

	@Override
	public void onResume()
	{
		Log.d("MainActivity", "onResume");
		super.onResume();

		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed()))
		{
			onSessionStateChange(session, session.getState(), null);
			refreshUserPics();
		}
	}

	private static class DrawerLayoutAdapter extends ArrayAdapter<String>
	{
		/*
		 * private static int[] icons = new int[] {
		 * R.attr.drawerNewsfeedIcon,
		 * R.attr.drawerAlbumsIcon,
		 * R.attr.drawerFriendsIcon,
		 * R.attr.drawerPagesIcon,
		 * R.attr.drawerGroupsIcon,
		 * R.attr.drawerEventsIcon,
		 * R.attr.drawerFollowedIcon,
		 * R.attr.drawerBirthdaysIcon,
		 * R.attr.drawerSearchIcon };
		 */
		private int	selectedPosition	= 0;

		public DrawerLayoutAdapter(Context context, int textViewResourceId, String[] objects)
		{
			super(context, textViewResourceId, objects);
		}

		public DrawerLayoutAdapter(Context context, int textViewResourceId, List<String> objects)
		{
			super(context, textViewResourceId, objects);
		}

		public DrawerLayoutAdapter(Context context, int resource, int textViewResourceId, String[] objects)
		{
			super(context, resource, textViewResourceId, objects);
		}

		public DrawerLayoutAdapter(Context context, int resource, int textViewResourceId, List<String> objects)
		{
			super(context, resource, textViewResourceId, objects);
		}

		public DrawerLayoutAdapter(Context context, int resource, int textViewResourceId)
		{
			super(context, resource, textViewResourceId);
		}

		public DrawerLayoutAdapter(Context context, int textViewResourceId)
		{
			super(context, textViewResourceId);
		}

		public void setSelectedPosition(int position)
		{
			selectedPosition = position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_drawer_layout, parent, false);
			}

			// convertView.setBackgroundColor(position == selectedPosition ? 0x33000000 : 0x00000000);

			// ImageView imageView = (ImageView) convertView.findViewById(R.id.picture);
			// imageView.setImageResource(AttrUtil.getResourceId(getContext(), icons[position]));
			// imageView.setAlpha(position == selectedPosition ? 255 : 226);

			TextView textView = (TextView) convertView.findViewById(R.id.primary_text);
			textView.setText(getItem(position));

			View border = convertView.findViewById(R.id.border);
			border.setVisibility(position == selectedPosition ? View.VISIBLE : View.INVISIBLE);

			return convertView;
		}
	}

	@Override
	public void onNewNotifications()
	{
		invalidateOptionsMenu();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		// mChecker.onDestroy();
		drawer = null;
		drawerToggle = null;
		notificationsFragment = null;
		navAdapter = null;
	}
}