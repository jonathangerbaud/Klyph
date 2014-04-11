package com.abewy.klyph.items;

import com.abewy.android.apps.klyph.core.graph.GraphObject;


public class AdItem extends GraphObject
{
	
	@Override
	public int getItemViewType()
	{
		return ItemType.ADVERTISING;
	}

	@Override
	public boolean isSelectable(int layout)
	{
		return true;
	}
}
