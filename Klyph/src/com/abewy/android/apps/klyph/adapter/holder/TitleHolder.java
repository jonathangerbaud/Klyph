package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleHolder
{
	private TextView		title;
	private RelativeLayout	shadow;

	public TitleHolder(TextView title, RelativeLayout shadow)
	{
		this.title = title;
		this.shadow = shadow;
	}

	public TextView getTitle()
	{
		return title;
	}
	
	public RelativeLayout getShadow()
	{
		return shadow;
	}
}
