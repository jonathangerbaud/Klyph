package com.abewy.android.apps.klyph.ads;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.abewy.android.ads.IBannerAd;
import com.abewy.android.ads.IBannerCallback;

public class TapContextBanner implements IBannerAd
{
	public TapContextBanner()
	{
		
	}

	@Override
	public View createAdView(Activity activity, ViewGroup adContainer, final IBannerCallback callback)
	{		 
		/*final AdView adView = new AdView(activity, "");
		
		adView.setAdCallback(new AdCallback() {
			
			@Override
			public void onAdShown()
			{
				Log.d("TapContextBanner", "onAdShown: ");
				callback.onReceiveAd(adView);
			}
			
			@Override
			public void onAdNotShown()
			{
				Log.d("TapContextBanner", "onAdNotShown: ");
				callback.onFailedToReceiveAd(adView, "");
				adView.setAdCallback(null);
			}
			
			@Override
			public void onAdClose()
			{
				Log.d("TapContextBanner", "onAdClose: ");
			}
			
			@Override
			public void onAdClicked()
			{
				
			}
		});
		
		return adView;*/
		return null;
	}

	@Override
	public void loadAd(View adView)
	{
		//((AdView) adView).loadNextAd();
	}
	
	@Override
	public void destroyAdView(View adView)
	{
		//if (adView != null)
			//((AdView) adView).release();
	}
}
