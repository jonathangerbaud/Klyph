package com.abewy.android.apps.klyph.core.graph;

import java.util.List;

public class Video extends GraphObject
{
	private String id;
	private UserRef from;
	private List<Tag> tags;
	private String name;
	private String description;
	private String picture;
	private String embed_html;
	private String source;
	private String created_time;
	private String updated_time;
	
	private Likes likes;
	private Comment comments;
	
	public Video()
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

	public List<Tag> getTags()
	{
		return tags;
	}

	public void setTags(List<Tag> tags)
	{
		this.tags = tags;
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

	public String getEmbed_html()
	{
		return embed_html;
	}

	public void setEmbed_html(String embed_html)
	{
		this.embed_html = embed_html;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getCreated_time()
	{
		return created_time;
	}

	public void setCreated_time(String created_time)
	{
		this.created_time = created_time;
	}

	public String getUpdated_time()
	{
		return updated_time;
	}

	public void setUpdated_time(String updated_time)
	{
		this.updated_time = updated_time;
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
