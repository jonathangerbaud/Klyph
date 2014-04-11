package com.abewy.android.extended.items;

import android.view.View.OnClickListener;

public class TextButtonItem extends BaseType
{
	public final String				text;
	public final OnClickListener	buttonListener;

	public TextButtonItem(String text, OnClickListener buttonListener)
	{
		this.text = text;
		this.buttonListener = buttonListener;
	}

	public int getItemViewType()
	{
		return BaseType.TEXT_BUTTON;
	}

	@Override
	public String getItemPrimaryLabel()
	{
		return "";
	}
}
