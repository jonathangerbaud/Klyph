package com.abewy.klyph.items;

import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Header extends GraphObject
{
	private String								name;

	public Header()
	{

	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public boolean isSelectable(int layout)
	{
		return false;
	}
	
	public int getItemViewType()
	{
		return ItemType.HEADER;
	}
}
