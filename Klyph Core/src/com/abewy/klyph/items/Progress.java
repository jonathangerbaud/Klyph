package com.abewy.klyph.items;

import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Progress extends GraphObject
{
	public Progress()
	{

	}

	@Override
	public boolean isSelectable(int layout)
	{
		return false;
	}
	
	@Override
	public int getItemViewType()
	{
		return ItemType.PROGRESS;
	}
}
