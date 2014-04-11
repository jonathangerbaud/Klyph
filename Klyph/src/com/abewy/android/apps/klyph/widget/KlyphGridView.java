package com.abewy.android.apps.klyph.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class KlyphGridView extends GridView
{
	public KlyphGridView(Context context)
	{
		super(context);
	}
	
	public KlyphGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public KlyphGridView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	public int getScrollBarY()
	{
		return computeVerticalScrollOffset();
	}
	
	public int getScrollBarX()
	{
		return computeHorizontalScrollOffset();
	}
}
