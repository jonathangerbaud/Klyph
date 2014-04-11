package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageButton;
import android.widget.TextView;

public class TextButtonItemHolder
{
	private TextView		text;
	private ImageButton		button;

	public TextButtonItemHolder(TextView text, ImageButton button)
	{
		this.text = text;
		this.button = button;
	}

	public TextView getText()
	{
		return text;
	}

	public void setText(TextView text)
	{
		this.text = text;
	}

	public ImageButton getButton()
	{
		return button;
	}

	public void setButton(ImageButton button)
	{
		this.button = button;
	}
}
