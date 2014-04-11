package com.abewy.klyph.items;

import android.view.View.OnClickListener;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class TextButtonItem extends GraphObject
{
	private String					text;
	private OnClickListener			buttonListener;

	
	@Override
	public boolean isSelectable(int layout)
	{
		return false;
	}

	public int getItemViewType()
	{
		return ItemType.TEXT_BUTTON;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public OnClickListener getButtonListener()
	{
		return buttonListener;
	}

	public void setButtonListener(OnClickListener buttonListener)
	{
		this.buttonListener = buttonListener;
	}
}
