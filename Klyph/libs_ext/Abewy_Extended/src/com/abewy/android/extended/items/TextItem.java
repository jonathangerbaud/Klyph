package com.abewy.android.extended.items;

public class TextItem extends BaseType
{
	public final String	text;

	public TextItem(String text)
	{
		this.text = text;
	}

	public int getItemViewType()
	{
		return BaseType.TEXT;
	}

	@Override
	public String getItemPrimaryLabel()
	{
		return "";
	}
}
