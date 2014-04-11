package com.abewy.android.apps.klyph.core.graph;

import java.util.List;

public class Likes
{
	private List<UserRef> users;
	private int count;
	
	public Likes()
	{

	}

	public List<UserRef> getUsers()
	{
		return users;
	}

	public void setUsers(List<UserRef> users)
	{
		this.users = users;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	
}
