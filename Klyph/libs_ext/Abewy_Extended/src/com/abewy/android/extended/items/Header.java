package com.abewy.android.extended.items;

public class Header extends BaseType
{
	public final String	name;

	public Header(String name)
	{
		this.name = name;
	}

	public int getItemViewType()
	{
		return BaseType.HEADER;
	}

	@Override
	public String getItemPrimaryLabel()
	{
		return name;
	}
}
