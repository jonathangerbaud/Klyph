package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class PicturePrimaryTextHolder
{
	private ImageView	picture;
	private TextView	primaryText;

	public PicturePrimaryTextHolder(ImageView picture, TextView primaryText)
	{
		this.picture = picture;
		this.primaryText = primaryText;
	}

	public ImageView getPicture()
	{
		return picture;
	}

	public TextView getPrimaryText()
	{
		return primaryText;
	}
}
