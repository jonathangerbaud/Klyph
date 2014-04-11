package com.abewy.android.apps.klyph.core.graph;

public class Link extends GraphObject
{
	private String id;
	private UserRef from;
	private String link;
	private String name;
	private String description;
	private String picture;
	private String message;
	private String created_time;
	
	private Likes likes;
	private Comment comments;
	
	public Link()
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

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getPicture()
	{
		return picture;
	}

	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
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

	public Comment getComments()
	{
		return comments;
	}

	public void setComments(Comment comments)
	{
		this.comments = comments;
	}
}
