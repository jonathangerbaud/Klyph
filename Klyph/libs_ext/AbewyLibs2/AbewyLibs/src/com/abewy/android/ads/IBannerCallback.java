package com.abewy.android.ads;

import android.view.View;

public interface IBannerCallback
{
	public void onReceiveAd(View adView);  
	public void onFailedToReceiveAd(View adView, String error);  
}
