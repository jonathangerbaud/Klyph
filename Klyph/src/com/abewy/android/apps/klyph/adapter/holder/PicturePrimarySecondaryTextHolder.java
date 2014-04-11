package com.abewy.android.apps.klyph.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PicturePrimarySecondaryTextHolder
{
	private ImageView		picture;
	private TextView		primaryText;
	private TextView		secondaryText;
	private RelativeLayout	shadow;
	private View			divider;

	public PicturePrimarySecondaryTextHolder(ImageView picture, TextView primaryText, TextView secondaryText)
	{
		this.picture = picture;
		this.primaryText = primaryText;
		this.secondaryText = secondaryText;
	}

	public PicturePrimarySecondaryTextHolder(ImageView picture, TextView primaryText, TextView secondaryText, RelativeLayout shadow)
	{
		this.picture = picture;
		this.primaryText = primaryText;
		this.secondaryText = secondaryText;
		this.shadow = shadow;
	}
	
	public PicturePrimarySecondaryTextHolder(ImageView picture, TextView primaryText, TextView secondaryText, View divider)
	{
		this.picture = picture;
		this.primaryText = primaryText;
		this.secondaryText = secondaryText;
		this.divider = divider;
	}

	public ImageView getPicture()
	{
		return picture;
	}

	public TextView getPrimaryText()
	{
		return primaryText;
	}

	public TextView getSecondaryText()
	{
		return secondaryText;
	}

	public RelativeLayout getShadow()
	{
		return shadow;
	}
	
	public View getDivider()
	{
		return divider;
	}
}
