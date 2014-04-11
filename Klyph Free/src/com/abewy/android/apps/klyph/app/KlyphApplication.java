package com.abewy.android.apps.klyph.app;

import java.util.ArrayList;
import java.util.List;
import com.abewy.android.ads.BannerAdManager;
import com.abewy.android.ads.IBannerAd;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.ads.AdmobBanner;

public class KlyphApplication extends com.abewy.android.apps.klyph.KlyphApplication
{
	@Override
	protected void initAds()
	{
		//AdRegistration.setAppKey("038faf04326f4ccbb7ab1c107e3dbd25");
		//AdRegistration.enableTesting(true);
		
		List<IBannerAd> bannerAds = new ArrayList<IBannerAd>();
		//bannerAds.add(new AmazonBanner());
		
		bannerAds.add(new AdmobBanner(getString(R.string.admob_id)));
		BannerAdManager.setBannerAds(bannerAds);
	}
}
