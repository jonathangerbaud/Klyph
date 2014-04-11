package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendPickerHolder
{
	private ImageView	friendPicture;
	private TextView	friendName;
	private CheckBox	checkbox;

	public FriendPickerHolder(ImageView friendPicture, TextView friendName, CheckBox checkbox)
	{
		this.friendPicture = friendPicture;
		this.friendName = friendName;
		this.checkbox = checkbox;
	}

	public ImageView getFriendPicture()
	{
		return friendPicture;
	}

	public TextView getFriendName()
	{
		return friendName;
	}

	public CheckBox getCheckbox()
	{
		return checkbox;
	}
}
