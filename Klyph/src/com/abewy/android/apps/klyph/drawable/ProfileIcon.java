package com.abewy.android.apps.klyph.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class ProfileIcon extends Drawable
{
	private final float		mCornerRadius	= 100.0f;
	private final RectF		mRect			= new RectF();
	private final Bitmap	mBitmap;
	private final Paint		mPaint;
	private final int		mSide;
	private final int		mColor;
	private float			mScale;

	public ProfileIcon(Bitmap bitmap, int color)
	{
		mBitmap = bitmap;
		mColor = color;

		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		mSide = (int) Math.min(mBitmap.getWidth(), mBitmap.getHeight());
	}

	@Override
	protected void onBoundsChange(Rect bounds)
	{
		super.onBoundsChange(bounds);

		mScale = (float) bounds.width() / mSide;

		mRect.set(0, 0, bounds.width(), bounds.height());
	}

	@Override
	public void draw(Canvas canvas)
	{
		Matrix matrix = new Matrix();
		matrix.setScale(mScale, mScale);

		int x = (int) (mBitmap.getWidth() - mSide) / 2;
		int y = (int) (mBitmap.getHeight() - mSide) / 2;

		Bitmap b = Bitmap.createBitmap(mBitmap, x, y, mSide, mSide, matrix, true);

		BitmapShader bitmapShader = new BitmapShader(b, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		mPaint.setShader(bitmapShader);

		Paint bPaint = new Paint();
		bPaint.setAntiAlias(true);
		bPaint.setColor(mColor);

		canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, bPaint);

		RectF r = new RectF();
		r.set(2, 2, mRect.right - 2, mRect.bottom - 2);

		canvas.drawRoundRect(r, mCornerRadius, mCornerRadius, mPaint);
	}

	@Override
	public int getOpacity()
	{
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha)
	{
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf)
	{
		mPaint.setColorFilter(cf);
	}
}
