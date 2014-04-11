package com.abewy.android.ads;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class BannerAdManager
{
	private static List<IBannerAd>		bannerAds;
	private static Map<View, IBannerAd>	map					= new HashMap<View, IBannerAd>();

	public static int					NONE				= -1;
	public static int					GENDER_MALE			= 0;
	public static int					GENDER_FEMALE		= 1;

	private static int					TARGETING_GENDER	= -1;
	private static long					TARGETING_BIRTHDAY	= -1;

	private static int					index				= -1;

	public static void setBannerAds(List<IBannerAd> bannerAds)
	{
		BannerAdManager.bannerAds = bannerAds;
	}

	private Activity		activity;
	private ViewGroup		adContainer;
	private View			adView;

	private IBannerCallback	callback	= new IBannerCallback() {

											@Override
											public void onReceiveAd(View adView)
											{
												Log.d("BannerAdManager", "onReceiveAd: " + adContainer);
												Log.d("BannerAdManager", "onReceiveAd: " + this);
												if (adView != null && adView.getParent() == null && adContainer != null)
												{
													adContainer.addView(adView);
												}
											}

											@Override
											public void onFailedToReceiveAd(View adView, String error)
											{
												Log.d("BannerAdManager", "onFailedToReceiveAd: " + error);

												if (adView != null && adView.getParent() != null && adContainer != null)
													adContainer.removeView(adView);

												if (adView != null)
												{
													IBannerAd bannerAd = map.get(adView);

													if (bannerAd != null)
														bannerAd.destroyAdView(adView);
												}

												map.remove(adView);
												adView = null;

												final Handler handler = new Handler();
												handler.postDelayed(new Runnable() {
													public void run()
													{
														loadAd();
													}
												}, 5000);
											}
										};

	public BannerAdManager(Activity activity, ViewGroup adContainer)
	{
		this(activity, adContainer, true);
	}
	
	public BannerAdManager(Activity activity, ViewGroup adContainer, boolean loadNow)
	{
		this.activity = activity;
		this.adContainer = adContainer;

		if (loadNow)
			loadAd();
	}

	public void loadAd()
	{
		Log.d("BannerAdManager", "loadAd: " + adContainer);
		Log.d("BannerAdManager", "loadAd: " + this);
		if (activity != null)
		{
			if (bannerAds == null)
			{
				throw new IllegalStateException("You must call setBannerAds() before loadAd()");
			}
			
			if (adView != null && adView.getParent() == adContainer)
			{
				adContainer.removeView(adView);
			}

			setNextBannerAdAsCurrent();

			IBannerAd bannerAd = bannerAds.get(index);

			adView = bannerAd.createAdView(activity, adContainer, callback);

			map.put(adView, bannerAd);

			if (adContainer != null && adView != null)
			{
				adContainer.addView(adView);
			}

			bannerAd.loadAd(adView);
		}
	}

	public void destroy()
	{
		if (adView != null)
		{
			if (adView.getParent() != null)
				((ViewGroup) adView.getParent()).removeView(adView);

			IBannerAd bannerAd = map.get(adView);

			if (bannerAd != null)
				bannerAd.destroyAdView(adView);
		}

		activity = null;
		adContainer = null;
		adView = null;
	}

	private void setNextBannerAdAsCurrent()
	{
		index++;

		if (index >= bannerAds.size())
		{
			index = 0;
		}
	}

	public static void setTargetingGender(int gender)
	{
		TARGETING_GENDER = gender;
	}

	public static int getTargetingGender()
	{
		return TARGETING_GENDER;
	}

	public static void setTargetingBirthday(long birthday)
	{
		TARGETING_BIRTHDAY = birthday;
	}

	public static long getTargetingBirthday()
	{
		return TARGETING_BIRTHDAY;
	}
}
