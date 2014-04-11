package com.abewy.android.apps.klyph.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class TransalatableTextView extends TextView
{
	private float	mTranslateX	= 0.0F;
	private float	mTranslateY	= 0.0F;

	public TransalatableTextView(Context paramContext)
	{
		this(paramContext, null);
	}

	public TransalatableTextView(Context paramContext, AttributeSet paramAttributeSet)
	{
		this(paramContext, paramAttributeSet, 0);
	}

	public TransalatableTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
	}

	protected void onDraw(Canvas paramCanvas)
	{
		paramCanvas.translate(this.mTranslateX, this.mTranslateY);
		super.onDraw(paramCanvas);
	}

	public void setTranslate(float paramFloat1, float paramFloat2)
	{
		this.mTranslateX = paramFloat1;
		this.mTranslateY = paramFloat2;
		invalidate();
	}
}