package com.abewy.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.abewy.android.ads.BannerAdManager;
import com.viewpagerindicator.PageIndicator;

public abstract class BaseViewPagerActivity extends Activity
{
	protected LinearLayout	progress;
	private View			loadingView;

	private BannerAdManager		adManager;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		if (getCustomTheme() != -1)
		{
			setTheme(getCustomTheme());
		}

		super.onCreate(savedInstanceState);

		setContentView(getLayout());

		Drawable bg = this.getActionBarBackground();
		if (bg != null && getActionBar() != null)
			getActionBar().setBackgroundDrawable(bg);

		getViewPager().setAdapter(getPagerAdapter());

		if (getPageIndicator() != null)
		{
			if (getInitialPosition() >= 0)
				getPageIndicator().setViewPager(getViewPager(), getInitialPosition());
			else
				getPageIndicator().setViewPager(getViewPager());
		}

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

	protected void setLoadingView(View loadingView)
	{
		this.loadingView = loadingView;
	}

	protected void setLoadingViewVisible(boolean visible)
	{
		if (loadingView != null)
			loadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
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
		if (getActionBar() != null)
			getActionBar().setHomeButtonEnabled(enabled);
	}

	/**
	 * Display or hide the back arrow at the left of the action bar
	 * 
	 * @param show true to display, false to hide
	 */
	protected void displayBackArrow(boolean show)
	{
		if (getActionBar() != null)
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

	public void notififyDatasetChanged()
	{
		getPageIndicator().notifyDataSetChanged();
	}

	protected ViewPager getViewPager()
	{
		return null;
	}

	protected PageIndicator getPageIndicator()
	{
		return null;
	}

	protected PagerAdapter getPagerAdapter()
	{
		return null;
	}

	/**
	 * The initial position of the page indicator (and thus the view pager)
	 */
	protected int getInitialPosition()
	{
		return 0;
	}

	protected void showPageIndicator()
	{
		if (getPageIndicator() != null)
		{
			((View) getPageIndicator()).setVisibility(View.VISIBLE);
		}
	}

	protected void showViewPager()
	{
		getViewPager().setVisibility(View.VISIBLE);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		if (adManager != null)
		{
			adManager.destroy();
			adManager = null;
		}
		
		progress = null;
		loadingView = null;
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

	// ___ Ads management ______________________________________________________

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
