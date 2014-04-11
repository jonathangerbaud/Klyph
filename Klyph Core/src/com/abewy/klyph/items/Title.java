package com.abewy.klyph.items;


public class Title extends ShadowItem
{
	private String					name;

	public Title()
	{

	}

	@Override
	public boolean isSelectable(int layout)
	{
		return false;
	}

	public int getItemViewType()
	{
		return ItemType.TITLE;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
