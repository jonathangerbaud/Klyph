package com.abewy.android.apps.klyph.core.graph;

import java.util.List;
import java.util.Map;

public class Post extends GraphObject
{
	private String id;
	private UserRef from;
	private List<UserRef> to;
	private String message;
	private Map<String, List<Tag>> message_tags;
	private String picture;
	private String link;
	private String name;
	private String caption;
	private String description;
	private String source;
	private List<Object> properties;
	private List<Action> actions;
	private Privacy privacy;
	private String type;
	private Place place;
	private String story;
	private Map<String, List<Tag>> story_tags;
	private List<UserRef> with_tags;
	private String object_id;
	private Application application;
	private String created_time;
	private String updated_time;
	private int shares;
	private String status_type;
	private Likes likes;
	private Likes comments;
	
	private Photo photoObject;
	private Video videoObject;
	private Link linkObject;
	
	public Post()
	{
		
	}

	@Override
	public int getItemViewType()
	{
		return POST;
	}
	
	//___ Public services
	public boolean  isPhoto()
	{
		return type != null && type.equals("photo");
	}
	
	public boolean  isVideo()
	{
		return type != null && type.equals("video");
	}
	
	public boolean  isLink()
	{
		return type != null && type.equals("link");
	}
	
	public boolean  canLike()
	{
		return actions != null && type.equals("link");
	}
	
	public boolean  canComment()
	{
		return type != null && type.equals("link");
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

	public List<UserRef> getTo()
	{
		return to;
	}

	public void setTo(List<UserRef> to)
	{
		this.to = to;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public Map<String, List<Tag>> getMessage_tags()
	{
		return message_tags;
	}

	public void setMessage_tags(Map<String, List<Tag>> message_tags)
	{
		this.message_tags = message_tags;
	}

	public String getPicture()
	{
		return picture;
	}

	public void setPicture(String picture)
	{
		this.picture = picture;
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

	public String getCaption()
	{
		return caption;
	}

	public void setCaption(String caption)
	{
		this.caption = caption;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public List<Object> getProperties()
	{
		return properties;
	}

	public void setProperties(List<Object> properties)
	{
		this.properties = properties;
	}

	public List<Action> getActions()
	{
		return actions;
	}

	public void setActions(List<Action> actions)
	{
		this.actions = actions;
	}

	public Privacy getPrivacy()
	{
		return privacy;
	}

	public void setPrivacy(Privacy privacy)
	{
		this.privacy = privacy;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Place getPlace()
	{
		return place;
	}

	public void setPlace(Place place)
	{
		this.place = place;
	}

	public String getStory()
	{
		return story;
	}

	public void setStory(String story)
	{
		this.story = story;
	}

	public Map<String, List<Tag>> getStory_tags()
	{
		return story_tags;
	}

	public void setStory_tags(Map<String, List<Tag>> story_tags)
	{
		this.story_tags = story_tags;
	}

	public List<UserRef> getWith_tags()
	{
		return with_tags;
	}

	public void setWith_tags(List<UserRef> with_tags)
	{
		this.with_tags = with_tags;
	}

	public String getObject_id()
	{
		return object_id;
	}

	public void setObject_id(String object_id)
	{
		this.object_id = object_id;
	}

	public Application getApplication()
	{
		return application;
	}

	public void setApplication(Application application)
	{
		this.application = application;
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

	public int getShares()
	{
		return shares;
	}

	public void setShares(int shares)
	{
		this.shares = shares;
	}

	public String getStatus_type()
	{
		return status_type;
	}

	public void setStatus_type(String status_type)
	{
		this.status_type = status_type;
	}

	public Likes getLikes()
	{
		return likes;
	}

	public void setLikes(Likes likes)
	{
		this.likes = likes;
	}

	public Likes getComments()
	{
		return comments;
	}

	public void setComments(Likes comments)
	{
		this.comments = comments;
	}

	public Photo getPhotoObject()
	{
		return photoObject;
	}

	public void setPhotoObject(Photo photoObject)
	{
		this.photoObject = photoObject;
	}

	public Video getVideoObject()
	{
		return videoObject;
	}

	public void setVideoObject(Video videoObject)
	{
		this.videoObject = videoObject;
	}

	public Link getLinkObject()
	{
		return linkObject;
	}

	public void setLinkObject(Link linkObject)
	{
		this.linkObject = linkObject;
	}

	public static class Action extends GraphObject
	{
		private String name;
		private String link;
		
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
	}
	
	public static class Privacy extends GraphObject
	{
		private String value;
		
		public String getValue()
		{
			return value;
		}
		public void setValue(String value)
		{
			this.value = value;
		}
	}
}
