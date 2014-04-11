package com.abewy.android.extended.items;

public class Title extends BaseType
{
	public final String	name;

	public Title(String name)
	{
		this.name = name;
	}

	public int getItemViewType()
	{
		return BaseType.TITLE;
	}

	@Override
	public String getItemPrimaryLabel()
	{
		return name;
	}
}
