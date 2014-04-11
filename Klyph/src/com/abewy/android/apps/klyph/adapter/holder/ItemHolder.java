package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemHolder
{
	private TextView		name;
	private TextView		desc;
	private RelativeLayout	shadow;

	public ItemHolder(TextView name, TextView desc, RelativeLayout shadow)
	{
		this.name = name;
		this.desc = desc;
		this.shadow = shadow;
	}

	public TextView getName()
	{
		return name;
	}
	
	public TextView getDesc()
	{
		return desc;
	}

	public RelativeLayout getShadow()
	{
		return shadow;
	}
}
