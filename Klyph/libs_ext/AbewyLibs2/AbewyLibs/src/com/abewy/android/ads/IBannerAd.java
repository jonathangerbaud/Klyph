package com.abewy.android.ads;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public interface IBannerAd
{
	public View createAdView(Activity activity, ViewGroup adContainer, final IBannerCallback callback);
	public void loadAd(View adView);
	public void destroyAdView(View adView);
}
