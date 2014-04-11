package com.abewy.android.apps.klyph.widget.coverImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

public abstract class CoverImageView extends ImageView
{
	private int		yOffset			= 0;

	public CoverImageView(Context context)
	{
		super(context);
		setScaleType(ScaleType.MATRIX);
	}

	public CoverImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setScaleType(ScaleType.MATRIX);
	}

	public CoverImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setScaleType(ScaleType.MATRIX);
	}

	abstract protected float getRatio();

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() * getRatio()));
	}

	public void setOffset(int yOffset)
	{
		if (yOffset < 100)
			this.yOffset = (int) (yOffset / 2);
		else
			this.yOffset = (int) (yOffset / 4);
			
	}

	@Override
	protected boolean  setFrame(int l, int t, int r, int b)
	{
		boolean  changed = super.setFrame(l, t, r, b);

		if (getDrawable() == null)
			return changed;

		int dwidth = getDrawable().getIntrinsicWidth();
		int dheight = getDrawable().getIntrinsicHeight();

		int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
		// int vheight = getHeight() - getPaddingTop() - getPaddingBottom();

		Matrix matrix = new Matrix();
		float scale;
		float dy = 0;

		scale = (float) vwidth / dwidth;
		dy = (float) ((dheight * scale) * (yOffset / 100f));

		matrix.setScale(scale, scale);
		matrix.postTranslate(0, -dy);

		setImageMatrix(matrix);

		return changed;
	}
}
