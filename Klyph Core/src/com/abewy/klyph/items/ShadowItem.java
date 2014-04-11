package com.abewy.klyph.items;

import com.abewy.android.apps.klyph.core.graph.GraphObject;

public abstract class ShadowItem extends GraphObject
{
	private boolean					shadow	= false;

	public ShadowItem()
	{

	}

	public boolean getShadow()
	{
		return shadow;
	}

	public void setShadow(boolean shadow)
	{
		this.shadow = shadow;
	}
}
