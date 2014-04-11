package com.abewy.android.apps.klyph.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class KlyphListView extends ListView
{
	public KlyphListView(Context context)
	{
		super(context);
	}
	
	public KlyphListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public KlyphListView(Context context, AttributeSet attrs, int defStyle)
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
