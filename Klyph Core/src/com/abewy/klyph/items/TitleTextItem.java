package com.abewy.klyph.items;


public class TitleTextItem extends ShadowItem
{
	private String					title;
	private String					text;

	public TitleTextItem()
	{

	}
	
	@Override
	public boolean isSelectable(int layout)
	{
		return false;
	}

	public int getItemViewType()
	{
		return ItemType.TITLE_TEXT;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
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
