package com.abewy.klyph.items;


public class TextItem extends ShadowItem
{
	private String					text;

	public TextItem()
	{

	}
	
	@Override
	public boolean isSelectable(int layout)
	{
		return false;
	}

	public int getItemViewType()
	{
		return ItemType.TEXT;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
}
