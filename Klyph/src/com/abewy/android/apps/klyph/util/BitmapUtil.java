package com.abewy.android.apps.klyph.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

public class BitmapUtil
{
	public static Bitmap getCirleBitmap(BitmapDrawable drawable, int borderColor)
	{
		return getCirleBitmap(drawable.getBitmap(), borderColor, 4);
	}
	
	public static Bitmap getCirleBitmap(Bitmap bitmap, int borderColor)
	{
		return getCirleBitmap(bitmap, borderColor, 4);
	}
	
	public static Bitmap getCirleBitmap(BitmapDrawable drawable, int borderColor, int border)
	{
		return getCirleBitmap(drawable.getBitmap(), borderColor, border);
	}
	
	public static Bitmap getCirleBitmap(Bitmap bitmap, int borderColor, int border)
	{
		if (bitmap == null)
			return null;
		
		int side = (int) Math.min(bitmap.getWidth(), bitmap.getHeight());
		
		int x = (int) (bitmap.getWidth() - side) / 2;
		int y = (int) (bitmap.getHeight() - side) / 2;

		Bitmap b = Bitmap.createBitmap(bitmap, x, y, side, side, new Matrix(), true);

		BitmapShader bitmapShader = new BitmapShader(b, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(bitmapShader);

		Paint bPaint = new Paint();
		bPaint.setAntiAlias(true);
		bPaint.setColor(borderColor);
		bPaint.setStrokeWidth(border / 2);
		bPaint.setStyle(Style.STROKE);

		
		Bitmap returnBitmap;
		
		try
		{
			returnBitmap = Bitmap.createBitmap(side, side, Config.ARGB_8888);
		}
		catch (OutOfMemoryError e)
		{
			return null;
		}
		
		Canvas canvas = new Canvas(returnBitmap);
		
		float center = (float) side / 2;
		float radius = (float) (side - border/2) / 2;
		
		canvas.drawCircle(center, center, center, paint);
		canvas.drawCircle(center,  center,  radius, bPaint);
		
		return returnBitmap;
	}
}
