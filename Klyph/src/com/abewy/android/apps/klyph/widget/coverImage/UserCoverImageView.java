package com.abewy.android.apps.klyph.widget.coverImage;

import android.content.Context;
import android.util.AttributeSet;

/**
 * An ImageView which height will be calculated relative 
 * to the 320/851 cover image ratio
 */
public class UserCoverImageView extends CoverImageView
{
	public static final float RATIO = (float) 320 / 851;
	
	public UserCoverImageView(Context context)
    {
        super(context);
    }

    public UserCoverImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public UserCoverImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

	@Override
	protected float getRatio()
	{
		return RATIO;
	}
}
