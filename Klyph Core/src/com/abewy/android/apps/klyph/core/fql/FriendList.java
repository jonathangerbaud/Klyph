package com.abewy.android.apps.klyph.core.fql;

import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class FriendList extends GraphObject
{
	private int		count;
	private String	flid;
	private String	name;
	private String	owner;
	private String	owner_cursor;
	private String	type;

	public FriendList()
	{

	}

	@Override
	public int getItemViewType()
	{
		return GraphObject.FRIEND_LIST;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public String getFlid()
	{
		return flid;
	}

	public void setFlid(String flid)
	{
		this.flid = flid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getOwner_cursor()
	{
		return owner_cursor;
	}

	public void setOwner_cursor(String owner_cursor)
	{
		this.owner_cursor = owner_cursor;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}