package com.abewy.android.apps.klyph.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.abewy.android.apps.klyph.KlyphData;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.KlyphLocale;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.app.BaseActivity2;
import com.facebook.Session;

public class TitledActivity extends BaseActivity2
{
	private boolean	userHasDonated	= false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(getCustomTheme());
		KlyphLocale.defineLocale(getBaseContext());

		super.onCreate(savedInstanceState);

		userHasDonated = KlyphPreferences.hasUserDonated();

		setContentView(getLayout());

		KlyphDevice.initDeviceValues(this);

		setAppIconBackToHomeEnabled(true);
		displayBackArrow(true);

		// manageAdView((AdView) findViewById(R.id.ad), LFPA.FREE_VERSION);

		if (!KlyphSession.isLogged())
		{
			logout();
		}
		else if (Session.getActiveSession() == null)
		{
			Session.openActiveSessionFromCache(this);
		}
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_main;
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
			startActivity(intent);

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

	@Override
	protected Drawable getActionBarBackground()
	{
		/*
		 * BitmapDrawable bg = (BitmapDrawable) getResources().getDrawable(
		 * R.drawable.action_bar_bg); bg.setTileModeX(TileMode.REPEAT); return
		 * bg;
		 */
		return null;
	}

	@Override
	protected Class<? extends Activity> getHomeClass()
	{
		return MainActivity.class;
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (userHasDonated != KlyphPreferences.hasUserDonated())
		{
			userHasDonated = KlyphPreferences.hasUserDonated();
			invalidateOptionsMenu();
		}
	}

}
