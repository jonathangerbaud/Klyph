package com.abewy.android.apps.klyph.ads;

import java.util.Date;
import android.app.Activity;
import android.database.sqlite.SQLiteDiskIOException;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewDatabase;
import com.abewy.android.ads.BannerAdManager;
import com.abewy.android.ads.IBannerAd;
import com.abewy.android.ads.IBannerCallback;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AdmobBanner implements IBannerAd
{
	private String	adMobId;

	public AdmobBanner(String adMobId)
	{
		this.adMobId = adMobId;
	}

	@Override
	public View createAdView(Activity activity, ViewGroup adContainer, final IBannerCallback callback)
	{
		// Prevent some crashes in some particular cases
		try
		{
			WebViewDatabase.getInstance(activity).clearFormData();
		}
		catch (SQLiteDiskIOException e)
		{

		}

		final AdView adView = new AdView(activity, AdSize.BANNER, adMobId);
		
		/*float density = KlyphDevice.getDeviceDensity();
		int height = Math.round(AdSize.IAB_MRECT.getHeight() * density);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
		adView.setLayoutParams(params);*/

		adView.setAdListener(new AdListener() {

			@Override
			public void onReceiveAd(Ad arg0)
			{
				callback.onReceiveAd(adView);
			}

			@Override
			public void onPresentScreen(Ad arg0)
			{

			}

			@Override
			public void onLeaveApplication(Ad arg0)
			{

			}

			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode errorCode)
			{
				callback.onFailedToReceiveAd(adView, errorCode.name());
			}

			@Override
			public void onDismissScreen(Ad arg0)
			{

			}
		});

		return adView;
	}

	@Override
	public void loadAd(View adView)
	{
		AdRequest ar = new AdRequest();
		
		if (BannerAdManager.getTargetingBirthday() != BannerAdManager.NONE)
		{
			Date date = new Date();
			date.setTime(BannerAdManager.getTargetingBirthday());
			ar.setBirthday(date);
		}
		((AdView) adView).loadAd(new AdRequest());
	}

	@Override
	public void destroyAdView(View adView)
	{
		if (adView != null)
			((AdView) adView).destroy();
	}
}
