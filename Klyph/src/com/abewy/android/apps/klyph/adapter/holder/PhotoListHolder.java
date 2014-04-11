package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class PhotoListHolder
{
	private ImageView	photo;
	private TextView	name;

	public PhotoListHolder(ImageView photo, TextView name)
	{
		this.photo = photo;
		this.name = name;
	}

	public ImageView getPhoto()
	{
		return photo;
	}
	
	public TextView getName()
	{
		return name;
	}
}
