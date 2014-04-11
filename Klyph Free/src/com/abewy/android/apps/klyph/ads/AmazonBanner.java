package com.abewy.android.apps.klyph.ads;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import com.abewy.android.ads.IBannerAd;
import com.abewy.android.ads.IBannerCallback;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdSize;
import com.amazon.device.ads.AdTargetingOptions;

public class AmazonBanner implements IBannerAd
{
	public AmazonBanner()
	{
		
	}

	@Override
	public View createAdView(Activity activity, ViewGroup adContainer, final IBannerCallback callback)
	{
		Log.d("AmazonBanner", "createAdView: ");
		final AdLayout adView = new AdLayout(activity, AdSize.SIZE_300x250);
		adView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		adView.setListener(new com.amazon.device.ads.AdListener() {
			
			@Override
			public void onAdLoaded(AdLayout adView, AdProperties arg1)
			{
				callback.onReceiveAd(adView);
			}
			
			@Override
			public void onAdFailedToLoad(AdLayout adView, AdError error)
			{
				callback.onFailedToReceiveAd(adView, error.getMessage());
			}
			
			@Override
			public void onAdExpanded(AdLayout arg0)
			{
				
			}
			
			@Override
			public void onAdCollapsed(AdLayout arg0)
			{
				
			}
		});
		
		return adView;
	}

	@Override
	public void loadAd(View adView)
	{
		Log.d("AmazonBanner", "loadAd: ");
		//AdRegistration.enableTesting(true);
		AdRegistration.enableLogging(true);
		((AdLayout) adView).loadAd(new AdTargetingOptions());
	}
	
	@Override
	public void destroyAdView(View adView)
	{
		if (adView != null)
			((AdLayout) adView).destroy();
	}
}
