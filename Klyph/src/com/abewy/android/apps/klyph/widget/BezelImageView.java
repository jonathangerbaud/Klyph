/*
 * Original code by Google
 * Modified by Jonathan GERBAUD, Abewy
 * 
 * Copyright 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abewy.android.apps.klyph.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import com.abewy.android.apps.klyph.R;

/**
 * An {@link android.widget.ImageView} that draws its contents inside a mask and draws a border
 * drawable on top. This is useful for applying a beveled look to image contents, but is also
 * flexible enough for use with other desired aesthetics.
 */
public class BezelImageView extends KlyphImageView
{
	private Rect		mBounds;

	private Drawable	mBorderDrawable;

	public BezelImageView(Context context)
	{
		this(context, null);
	}

	public BezelImageView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public BezelImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		// Attribute initialization
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BezelImageView, defStyle, 0);

		setBorderDrawable(a.getDrawable(R.styleable.BezelImageView_borderDrawable));

		a.recycle();
	}

	protected void setBorderDrawable(Drawable borderDrawable)
	{
		if (mBorderDrawable != null)
		{
			mBorderDrawable.setCallback(null);
		}

		mBorderDrawable = borderDrawable;

		if (mBorderDrawable != null)
		{
			mBorderDrawable.setCallback(this);
		}
	}

	@Override
	protected boolean  setFrame(int l, int t, int r, int b)
	{
		final boolean  changed = super.setFrame(l, t, r, b);
		mBounds = new Rect(0, 0, r - l, b - t);

		if (mBorderDrawable != null)
		{
			mBorderDrawable.setBounds(mBounds);
		}

		return changed;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		if (mBounds == null)
		{
			return;
		}

		int width = mBounds.width();
		int height = mBounds.height();

		if (width == 0 || height == 0)
		{
			return;
		}

		super.onDraw(canvas);

		if (mBorderDrawable != null)
		{
			mBorderDrawable.draw(canvas);
		}
	}

	@Override
	protected void drawableStateChanged()
	{
		super.drawableStateChanged();
		
		if (mBorderDrawable != null && mBorderDrawable.isStateful())
		{
			mBorderDrawable.setState(getDrawableState());
		}
		if (isDuplicateParentStateEnabled())
		{
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public void invalidateDrawable(Drawable who)
	{
		if (who == mBorderDrawable)
		{
			invalidate();
		}
		else
		{
			super.invalidateDrawable(who);
		}
	}

	@Override
	protected boolean  verifyDrawable(Drawable who)
	{
		return who == mBorderDrawable || super.verifyDrawable(who);
	}
}
