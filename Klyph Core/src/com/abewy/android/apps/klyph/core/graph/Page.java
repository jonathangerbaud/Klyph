package com.abewy.android.apps.klyph.core.graph;


public class Page extends GraphObject
{
	private String id;
	private String name;
	private String link;
	private String category;
	private String description;
	private String about;
	private boolean is_published;
	private boolean can_post;
	private int likes;
	private Object location;
	private String phone;
	private int checkins;
	private String picture;
	private Cover cover;
	private String website;
	private int talking_about_count;
	private Object global_brand_like_count;
	private Object global_brand_talking_about_count;
	private Object global_brand_parent_page;
	
	/*private ArrayList<Post> feed;
	private ArrayList<Object> settings;
	private ArrayList<Object> tagged;
	private ArrayList<Link> links;
	private ArrayList<Photo> photos;
	private ArrayList<Object> groups;
	private ArrayList<Album> albums;
	private ArrayList<StatusMessage> statuses;
	private ArrayList<Video> videos;
	private ArrayList<Note> notes;
	private ArrayList<Post> posts;
	private ArrayList<Post> promotable_posts;
	private ArrayList<Question> questions;
	private ArrayList<Event> events;
	private ArrayList<Object> admins;
	private ArrayList<Object> conversations;
	private ArrayList<Object> milestones;
	private ArrayList<Object> blocked;
	private ArrayList<Object> tabs;
	private ArrayList<Object> global_brand_children;*/
	
	public Page()
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public boolean getIs_published()
	{
		return is_published;
	}

	public void setIs_published(boolean is_published)
	{
		this.is_published = is_published;
	}

	public boolean getCan_post()
	{
		return can_post;
	}

	public void setCan_post(boolean can_post)
	{
		this.can_post = can_post;
	}

	public int getLikes()
	{
		return likes;
	}

	public void setLikes(int likes)
	{
		this.likes = likes;
	}

	public Object getLocation()
	{
		return location;
	}

	public void setLocation(Object location)
	{
		this.location = location;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public int getCheckins()
	{
		return checkins;
	}

	public void setCheckins(int checkins)
	{
		this.checkins = checkins;
	}

	public String getPicture()
	{
		return picture;
	}

	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	public Cover getCover()
	{
		return cover;
	}

	public void setCover(Cover cover)
	{
		this.cover = cover;
	}

	public String getWebsite()
	{
		return website;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}

	public int getTalking_about_count()
	{
		return talking_about_count;
	}

	public void setTalking_about_count(int talking_about_count)
	{
		this.talking_about_count = talking_about_count;
	}

	public Object getGlobal_brand_like_count()
	{
		return global_brand_like_count;
	}

	public void setGlobal_brand_like_count(Object global_brand_like_count)
	{
		this.global_brand_like_count = global_brand_like_count;
	}

	public Object getGlobal_brand_talking_about_count()
	{
		return global_brand_talking_about_count;
	}

	public void setGlobal_brand_talking_about_count(
			Object global_brand_talking_about_count)
	{
		this.global_brand_talking_about_count = global_brand_talking_about_count;
	}

	public Object getGlobal_brand_parent_page()
	{
		return global_brand_parent_page;
	}

	public void setGlobal_brand_parent_page(Object global_brand_parent_page)
	{
		this.global_brand_parent_page = global_brand_parent_page;
	}
	
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getAbout()
	{
		return about;
	}

	public void setAbout(String about)
	{
		this.about = about;
	}

	public static class Cover
	{
		private String id;
		private String source;
		private Number offset_y;
		
		public String getId()
		{
			return id;
		}
		public void setId(String id)
		{
			this.id = id;
		}
		public String getSource()
		{
			return source;
		}
		public void setSource(String source)
		{
			this.source = source;
		}
		public Number getOffset_y()
		{
			return offset_y;
		}
		public void setOffset_y(Number offset_y)
		{
			this.offset_y = offset_y;
		}
	}
}
