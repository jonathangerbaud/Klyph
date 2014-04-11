package com.abewy.android.apps.klyph.core.fql;

import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class FriendRequest extends GraphObject
{
	private boolean	is_hidden;
	private String	message;
	private String	time;
	private String	uid_from;
	private String	uid_from_name;
	private String	uid_from_pic;
	private String	uid_to;
	private boolean	unread;

	public FriendRequest()
	{

	}
	
	public int getItemViewType()
	{
		return GraphObject.FRIEND_REQUEST;
	}

	public boolean getIs_hidden()
	{
		return is_hidden;
	}

	public void setIs_hidden(boolean is_hidden)
	{
		this.is_hidden = is_hidden;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getUid_from()
	{
		return uid_from;
	}

	public void setUid_from(String uid_from)
	{
		this.uid_from = uid_from;
	}

	public String getUid_from_name()
	{
		return uid_from_name;
	}

	public void setUid_from_name(String uid_from_name)
	{
		this.uid_from_name = uid_from_name;
	}

	public String getUid_from_pic()
	{
		return uid_from_pic;
	}

	public void setUid_from_pic(String uid_from_pic)
	{
		this.uid_from_pic = uid_from_pic;
	}

	public String getUid_to()
	{
		return uid_to;
	}

	public void setUid_to(String uid_to)
	{
		this.uid_to = uid_to;
	}

	public boolean getUnread()
	{
		return unread;
	}

	public void setUnread(boolean unread)
	{
		this.unread = unread;
	}
}
