package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class VideoHolder
{
	private ImageView	video_picture;
	private TextView	video_name;

	public VideoHolder(ImageView video_picture, TextView video_name)
	{
		this.video_picture = video_picture;
		this.video_name = video_name;
	}

	public ImageView getPhoto()
	{
		return video_picture;
	}
	
	public TextView getName()
	{
		return video_name;
	}
}
