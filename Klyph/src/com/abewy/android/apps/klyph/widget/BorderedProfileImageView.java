package com.abewy.android.apps.klyph.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.util.BitmapUtil;
import com.abewy.android.apps.klyph.R;
import com.squareup.picasso.PicassoDrawable;

public class BorderedProfileImageView extends KlyphImageView
{
	public BorderedProfileImageView(Context context)
	{
		super(context);
	}

	public BorderedProfileImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public BorderedProfileImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public void setImageDrawable(Drawable drawable)
	{
		
		if (drawable != null && KlyphPreferences.isRoundedPictureEnabled() == true)
		{
			PicassoDrawable pDrawable = (PicassoDrawable) drawable;
			
			super.setImageDrawable(new BitmapDrawable(getContext()
					.getResources(), BitmapUtil.getCirleBitmap(
					pDrawable.getImage().getBitmap(), AttrUtil.getColor(
							getContext(), R.attr.profileIconBackgroundColor))));
		}
		else
		{
			super.setImageDrawable(drawable);
		}
	}
}
