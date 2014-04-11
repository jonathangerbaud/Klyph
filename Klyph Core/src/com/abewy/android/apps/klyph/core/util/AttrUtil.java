package com.abewy.android.apps.klyph.core.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;


public class AttrUtil
{
	public static String getString(Context context, int attr)
	{
		TypedArray ta = context.obtainStyledAttributes(new int[] { attr });
		String string = ta.getString(0);
		ta.recycle();
		
		return string;
	}
	
	public static Drawable getDrawable(Context context, int attr)
	{
		TypedArray ta = context.obtainStyledAttributes(new int[] { attr });
		Drawable drawable = ta.getDrawable(0);
		ta.recycle();
		
		return drawable;
	}
	
	public static Drawable[] getDrawable(Context context, int[] attrs)
	{
		TypedArray ta = context.obtainStyledAttributes(attrs);
		Drawable[] drawables = new Drawable[attrs.length];
		
		for (int i = 0; i < attrs.length; i++)
		{
			drawables[i] = ta.getDrawable(i);
		}
		
		ta.recycle();
		
		return drawables;
	}
	
	public static boolean getboolean(Context context, int attr, boolean defaultValue)
	{
		TypedArray ta = context.obtainStyledAttributes(new int[] { attr });
		boolean bool = ta.getBoolean(0, defaultValue);
		ta.recycle();
		
		return bool;
	}
	
	public static int getColor(Context context, int attr)
	{
		TypedArray ta = context.obtainStyledAttributes(new int[] { attr });
		int color = ta.getColor(0, 0);
		ta.recycle();
		
		return color;
	}
	
	public static int getPixelDimension(Context context, int attr)
	{
		TypedArray ta = context.obtainStyledAttributes(new int[] { attr });
		int dimension = ta.getDimensionPixelSize(0, 0);
		ta.recycle();
		
		return dimension;
	}
	
	public static TypedValue getValue(Context context, int attr)
	{
		TypedArray ta = context.obtainStyledAttributes(new int[] { attr });
		TypedValue tv = new TypedValue();
		boolean bool = ta.getValue(0, tv);
		ta.recycle();
		if (bool == true)
			return tv;
		
		return null;
	}
	
	public static int getResourceId(Context context, int attr)
	{
		return getResourceId(context, attr, 0);
	}
	
	public static int getResourceId(Context context, int attr, int defaultValue)
	{
		TypedArray ta = context.obtainStyledAttributes(new int[] { attr });
		int resourceId = ta.getResourceId(0, defaultValue);
		ta.recycle();
		
		return resourceId;
	}
}
