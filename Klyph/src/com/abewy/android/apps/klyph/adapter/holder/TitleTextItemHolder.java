package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleTextItemHolder
{
	private TextView		title;
	private TextView		text;
	private RelativeLayout	shadow;

	public TitleTextItemHolder(TextView title, TextView text, RelativeLayout shadow)
	{
		this.title = title;
		this.text = text;
		this.shadow = shadow;
	}
	
	public TextView getTitle()
	{
		return title;
	}

	public TextView getText()
	{
		return text;
	}

	public RelativeLayout getShadow()
	{
		return shadow;
	}
}
