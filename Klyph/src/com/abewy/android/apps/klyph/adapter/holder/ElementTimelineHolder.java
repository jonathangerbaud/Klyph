package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class ElementTimelineHolder
{
	private ImageView	elementCoverImage;
	private ImageView	elementProfileImage;
	private TextView	elementDetail1;
	private TextView	elementDetail2;
	private TextView	elementDetail3;
	private TextView	elementDetail4;
	private TextView	likes;
	private TextView	talkAbout;

	public ElementTimelineHolder(ImageView elementCoverImage, ImageView elementProfileImage, TextView elementDetail1,
			TextView elementDetail2, TextView elementDetail3, TextView elementDetail4, TextView likes,
			TextView talkAbout)
	{
		this.elementCoverImage = elementCoverImage;
		this.elementProfileImage = elementProfileImage;
		this.elementDetail1 = elementDetail1;
		this.elementDetail2 = elementDetail2;
		this.elementDetail3 = elementDetail3;
		this.elementDetail4 = elementDetail4;
		this.likes = likes;
		this.talkAbout = talkAbout;
	}

	public ImageView getElementCoverImage()
	{
		return elementCoverImage;
	}

	public ImageView getElementProfileImage()
	{
		return elementProfileImage;
	}

	public TextView getElementDetail1()
	{
		return elementDetail1;
	}

	public TextView getElementDetail2()
	{
		return elementDetail2;
	}

	public TextView getElementDetail3()
	{
		return elementDetail3;
	}

	public TextView getElementDetail4()
	{
		return elementDetail4;
	}

	public TextView getLikes()
	{
		return likes;
	}

	public TextView getTalkAbout()
	{
		return talkAbout;
	}
}
