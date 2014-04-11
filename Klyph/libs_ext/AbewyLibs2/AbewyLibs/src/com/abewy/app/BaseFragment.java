package com.abewy.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.abewy.android.ads.BannerAdManager;

public abstract class BaseFragment extends Fragment
{
	private BannerAdManager	adManager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View fragView = inflater.inflate(getLayout(), container, false);

		return fragView;
	}

	/**
	 * Override this method to define this activity's layout
	 * 
	 * @return the activity's layout. Example : <code>R.layout.main</code>
	 */
	protected abstract int getLayout();

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
				adManager = new BannerAdManager(getActivity(), adContainer);
			}
			else
			{
				((ViewGroup) adContainer.getParent()).removeView(adContainer);
			}
		}
	}
}
