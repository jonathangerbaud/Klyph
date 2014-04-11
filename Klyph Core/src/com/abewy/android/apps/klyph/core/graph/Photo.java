package com.abewy.android.apps.klyph.core.graph;

import java.util.List;
import java.util.Map;

public class Photo extends GraphObject
{
	private String id;
	private UserRef from;
	private List<Tag> tags;
	private String name;
	private Map<String, List<Tag>> name_tags;
	private String picture;
	private String source;
	private int height;
	private int width;
	private List<Image> images;
	private String link;
	private Place place;
	private String created_time;
	private String updated_time;
	private Comment comments;
	private Likes likes;
	
	public Photo()
	{

	}
	
	@Override
	public int getItemViewType()
	{
		return GraphObject.PHOTO;
	}
	
	
	//___ Getter/Setter
	
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

	public Map<String, List<Tag>> getName_tags()
	{
		return name_tags;
	}

	public void setName_tags(Map<String, List<Tag>> name_tags)
	{
		this.name_tags = name_tags;
	}

	public String getPicture()
	{
		return picture;
	}

	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public List<Image> getImages()
	{
		return images;
	}

	public void setImages(List<Image> images)
	{
		this.images = images;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public Place getPlace()
	{
		return place;
	}

	public void setPlace(Place place)
	{
		this.place = place;
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

	public Comment getComments()
	{
		return comments;
	}

	public void setComments(Comment comments)
	{
		this.comments = comments;
	}

	public Likes getLikes()
	{
		return likes;
	}

	public void setLikes(Likes likes)
	{
		this.likes = likes;
	}
}
