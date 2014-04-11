package com.abewy.android.apps.klyph.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.abewy.android.apps.klyph.R;

public class CheckableGalleryLayout extends RelativeLayout implements Checkable
{
	private boolean 		checked;
	private ImageView	checkmark;

	public CheckableGalleryLayout(Context context)
	{
		super(context);
	}

	public CheckableGalleryLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public CheckableGalleryLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public void setChecked(boolean  checked)
	{
		this.checked = checked;
		
		if (checkmark != null)
		{
			checkmark.setVisibility((checked == true) ? View.VISIBLE : View.GONE);
		}
	}

	@Override
	public boolean  isChecked()
	{
		return checked;
	}

	@Override
	public void toggle()
	{
		setChecked(!checked);
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		checkmark = (ImageView) findViewById(R.id.checkmark);
		
		setChecked(checked);
	}

}
