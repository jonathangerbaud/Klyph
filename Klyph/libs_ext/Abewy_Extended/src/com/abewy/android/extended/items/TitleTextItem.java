package com.abewy.android.extended.items;


public class TitleTextItem extends BaseType
{
	public final String					title;
	public final String					text;

	public TitleTextItem(String title, String text)
	{
		this.title = title;
		this.text = text;
	}
	
	@Override
	public String getItemPrimaryLabel()
	{
		return title;
	}
	
	public int getItemViewType()
	{
		return BaseType.TITLE_TEXT;
	}
}
