package com.abewy.android.apps.klyph.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class KlyphImageZoomViewPager extends ViewPager
{

	public KlyphImageZoomViewPager(Context context)
	{
		super(context);
	}

	public KlyphImageZoomViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected boolean  canScroll(View v, boolean  checkV, int dx, int x, int y)
	{
		/*if (v instanceof TouchImageView)
		{
			TouchImageView imageView = (TouchImageView) v;
			return imageView.canScrollHorizontally(dx);
		}*/

		return super.canScroll(v, checkV, dx, x, y);
	}
}
