package com.abewy.android.apps.klyph.core.fql;

import com.abewy.android.apps.klyph.core.graph.GraphObject;


public class Profile extends GraphObject
{
	//private static KlyphAdapter	baseAdapter;
	
	private String id;
	private String name;
	private String type;
	private String pic;
	
	public Profile()
	{

	}
	
	@Override
	public int getItemViewType()
	{
		return GraphObject.PROFILE;
	}

	/*@Override
	public KlyphAdapter getAdapter(int specialLayout)
	{
		if (baseAdapter == null)
			baseAdapter = new TagAdapter(context);

		return baseAdapter;
	}
	
	@Override
	public void resetAdapter()
	{
		baseAdapter = null;
	}*/

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getPic()
	{
		return pic;
	}

	public void setPic(String pic)
	{
		this.pic = pic;
	}
}
