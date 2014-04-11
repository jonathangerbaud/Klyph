package com.abewy.android.apps.klyph.items;

import com.abewy.android.apps.klyph.adapter.AdapterSelector;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class FakeHeaderItem extends GraphObject
{
	private int height;
	
	@Override
	public int getItemViewType()
	{
		return AdapterSelector.FAKE_HEADER;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}
}