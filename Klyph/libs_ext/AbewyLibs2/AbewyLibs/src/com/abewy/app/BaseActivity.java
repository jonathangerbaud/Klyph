package com.abewy.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.abewy.android.ads.BannerAdManager;

public abstract class BaseActivity extends Activity
{
	private BannerAdManager	adManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		if (getCustomTheme() != -1)
		{
			setTheme(getCustomTheme());
		}

		super.onCreate(savedInstanceState);

		setContentView(getLayout());

		setAppIconBackToHomeEnabled(true);

		Drawable bg = this.getActionBarBackground();
		if (bg != null && getActionBar() != null)
			getActionBar().setBackgroundDrawable(bg);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			Intent intent = new Intent(this, getHomeClass());
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}

		return false;
	}
	
	@Override
	public void onDestroy()
	{
		if (adManager != null)
		{
			adManager.destroy();
			adManager = null;
		}

		super.onDestroy();
	}

	/**
	 * Set the title of the action bar
	 * 
	 * @param title The title
	 */
	protected void setTitle(String title)
	{
		if (getActionBar() != null)
			getActionBar().setTitle(title);
	}

	@Override
	public void setTitle(int titleId)
	{
		if (getActionBar() != null)
			getActionBar().setTitle(titleId);
	}

	/**
	 * Set the subtitle of the action bar
	 * 
	 * @param subtitle The subtitle
	 */
	protected void setSubtitle(String subtitle)
	{
		if (getActionBar() != null)
			getActionBar().setSubtitle(subtitle);
	}

	/**
	 * Set the subtitle of the action bar
	 * 
	 * @param titleId The subtitle resource id
	 */
	protected void setSubtitle(int titleId)
	{
		if (getActionBar() != null)
			getActionBar().setSubtitle(titleId);
	}

	/**
	 * Override this method to define this activity's layout
	 * 
	 * @return the activity's layout. Example : <code>R.layout.main</code>
	 */
	protected abstract int getLayout();

	/**
	 * Override this method to define this activity's theme
	 * 
	 * @return the activity's layout. Example :
	 *         <code>R.style.MyCustomTheme</code>
	 */
	protected int getCustomTheme()
	{
		return -1;
	}

	/**
	 * Enable or disable the click on the app icon on the left of the action bar
	 * goes back to the main/home activity. If disabled, click on the icon does
	 * nothing. If enabled, make sure to set the main/home activity by
	 * overriding the <i>getHomeClass</i> method.
	 * 
	 * @param enabled
	 */
	protected void setAppIconBackToHomeEnabled(boolean enabled)
	{
		getActionBar().setHomeButtonEnabled(enabled);
	}

	/**
	 * Display or hide the back arrow at the left of the action bar
	 * 
	 * @param show true to display, false to hide
	 */
	protected void displayBackArrow(boolean show)
	{
		getActionBar().setDisplayHomeAsUpEnabled(show);
	}

	/**
	 * You must override this method to display a background in the action bar
	 * Don't forget to set TileMode options on the drawable if needed
	 * 
	 * Example :
	 * <code>BitmapDrawable bg = (BitmapDrawable) getResources().getDrawable(
				R.drawable.action_bar_bg);
		bg.setTileModeX(TileMode.REPEAT);
		return bg;</code>
	 * 
	 * @return the drawable background for the action bar
	 */
	protected Drawable getActionBarBackground()
	{
		return null;
	}

	/**
	 * The Home activity to display when the user click on the app icon on the
	 * left of the action bar
	 * 
	 * @return The class of the home activity
	 */
	protected Class<? extends Activity> getHomeClass()
	{
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public final <E extends View> E findView(int id)
	{
		try
		{
			return (E) findViewById(id);
		}
		catch (ClassCastException ex)
		{
			Log.d("BaseActivity2", "getView: " + ex);
			throw ex;
		}
	}

	/**
	 * Manage the ads visibility in the layout
	 * 
	 * @param ad The view container where ads will be displayed
	 * @param enabled true to enable ads, false to remove from layout.
	 */
	protected void manageAdView(ViewGroup adContainer, boolean enabled)
	{
		if (adContainer != null)
		{
			if (adContainer != null)
			{
				if (adManager != null)
				{
					adManager.destroy();
				}
				
				if (enabled)
				{
					adManager = new BannerAdManager(this, adContainer);
				}
				else
				{
					((ViewGroup) adContainer.getParent()).removeView(adContainer);
				}
			}
		}
	}
}
