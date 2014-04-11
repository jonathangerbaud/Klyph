package com.abewy.android.apps.klyph.core.graph;

import java.util.ArrayList;

public class StatusMessage extends GraphObject
{
	private String id;
	private UserRef from;
	private Object place;
	private String message;
	private int rating;
	private String created_time;
	private String story;
	
	private Likes likes;
	
	public StatusMessage()
	{

	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public UserRef getFrom()
	{
		return from;
	}

	public void setFrom(UserRef from)
	{
		this.from = from;
	}

	public Object getPlace()
	{
		return place;
	}

	public void setPlace(Object place)
	{
		this.place = place;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public int getRating()
	{
		return rating;
	}

	public void setRating(int rating)
	{
		this.rating = rating;
	}

	public String getCreated_time()
	{
		return created_time;
	}

	public void setCreated_time(String created_time)
	{
		this.created_time = created_time;
	}

	public Likes getLikes()
	{
		return likes;
	}

	public void setLikes(Likes likes)
	{
		this.likes = likes;
	}
	
	public String getStory()
	{
		return story;
	}

	public void setStory(String story)
	{
		this.story = story;
	}

	@Override
	public String toString()
	{
		return id + " " + message + " " + rating + " " + created_time;
	}
}
