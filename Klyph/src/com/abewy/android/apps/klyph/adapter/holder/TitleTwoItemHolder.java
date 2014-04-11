package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleTwoItemHolder
{
	private TextView		title;
	private TextView		firstName;
	private TextView		firstDesc;
	private ImageView		firstIcon;
	private TextView		secondName;
	private TextView		secondDesc;
	private ImageView		secondIcon;
	private RelativeLayout	shadow;

	public TitleTwoItemHolder(TextView title, TextView firstName, TextView firstDesc, ImageView firstIcon,
			TextView secondName, TextView secondDesc, ImageView secondIcon, RelativeLayout shadow)
	{
		this.title = title;
		this.firstName = firstName;
		this.firstDesc = firstDesc;
		this.firstIcon = firstIcon;
		this.secondName = secondName;
		this.secondDesc = secondDesc;
		this.secondIcon = secondIcon;
		this.shadow = shadow;
	}

	public TextView getTitle()
	{
		return title;
	}

	public TextView getFirstName()
	{
		return firstName;
	}

	public TextView getFirstDesc()
	{
		return firstDesc;
	}

	public ImageView getFirstIcon()
	{
		return firstIcon;
	}

	public TextView getSecondName()
	{
		return secondName;
	}

	public TextView getSecondDesc()
	{
		return secondDesc;
	}

	public ImageView getSecondIcon()
	{
		return secondIcon;
	}

	public RelativeLayout getShadow()
	{
		return shadow;
	}
}
