package com.abewy.android.extended.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class BaseImageView extends ImageView {
    private boolean mBlockLayout;

    public BaseImageView(Context context) {
        super(context);
    }

    public BaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestLayout() {
        if (!mBlockLayout) {
            super.requestLayout();
        }
    }

    @Override
    public void setImageResource(int resId) {
        mBlockLayout = true;
        super.setImageResource(resId);
        mBlockLayout = false;
    }

    @Override
    public void setImageURI(Uri uri) {
        mBlockLayout = true;
        super.setImageURI(uri);
        mBlockLayout = false;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mBlockLayout = true;
        super.setImageDrawable(drawable);
        mBlockLayout = false;
    }

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility)
	{
		//Log.d("KlyphImageview", "onWindowVisibilityChanged " + visibility);
		super.onWindowVisibilityChanged(visibility);
		
		if (visibility == View.GONE)
		{
			setImageDrawable(null);
		}
	}
}