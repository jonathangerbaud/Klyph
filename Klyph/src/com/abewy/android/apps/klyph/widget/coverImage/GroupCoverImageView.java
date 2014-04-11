package com.abewy.android.apps.klyph.widget.coverImage;

import android.content.Context;
import android.util.AttributeSet;

/**
 * An ImageView which height will be calculated relative 
 * to the 320/851 cover image ratio
 */
public class GroupCoverImageView extends CoverImageView
{
	public static final float RATIO = (float) 250 / 800;
	
	public GroupCoverImageView(Context context)
    {
        super(context);
    }

    public GroupCoverImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public GroupCoverImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

	@Override
	protected float getRatio()
	{
		return RATIO;
	}
}
