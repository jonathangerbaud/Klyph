package com.abewy.android.apps.klyph.widget.coverImage;

import android.content.Context;
import android.util.AttributeSet;

/**
 * An ImageView which height will be calculated relative 
 * to the 320/851 cover image ratio
 */
public class EventCoverImageView extends CoverImageView
{
	public static final float RATIO = (float) 264 / 714;
	
	public EventCoverImageView(Context context)
    {
        super(context);
    }

    public EventCoverImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EventCoverImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

	@Override
	protected float getRatio()
	{
		return RATIO;
	}
}
