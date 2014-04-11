package com.abewy.android.apps.klyph.widget;

import com.abewy.android.apps.klyph.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable
{
	private boolean 		checked = false;
	private CheckBox	checkbox;

	public CheckableLinearLayout(Context context)
	{
		super(context);
	}

	public CheckableLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public void setChecked(boolean  checked)
	{
		this.checked = checked;
		
		if (checkbox != null)
		{
			checkbox.setChecked(checked);
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
		
		checkbox = (CheckBox) findViewById(R.id.checkbox);
	}

}
