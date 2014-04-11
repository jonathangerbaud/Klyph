package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextItemHolder
{
	private TextView		text;
	private RelativeLayout	shadow;

	public TextItemHolder(TextView text, RelativeLayout shadow)
	{
		this.text = text;
		this.shadow = shadow;
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
