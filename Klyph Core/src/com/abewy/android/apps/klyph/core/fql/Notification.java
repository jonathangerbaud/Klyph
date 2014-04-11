package com.abewy.android.apps.klyph.core.fql;

import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Notification extends GraphObject
{
	public static final String	TYPE_APP_REQUEST	= "app_request";
	public static final String	TYPE_ALBUM			= "album";
	public static final String	TYPE_CANCELED_EVENT	= "canceled_event";
	public static final String	TYPE_COMMENT		= "comment";
	public static final String	TYPE_EVENT			= "event";
	public static final String	TYPE_FRIEND			= "friend";
	public static final String	TYPE_GROUP			= "group";
	public static final String	TYPE_PAGE			= "page";
	public static final String	TYPE_PHOTO			= "photo";
	public static final String	TYPE_POKE			= "poke";
	public static final String	TYPE_STREAM			= "stream";
	public static final String	TYPE_VIDEO			= "video";
	public static final String	TYPE_WEB_APP		= "fb_web_app";
	public static final String	TYPE_USER			= "user";

	private String				notification_id;
	private String				sender_id;
	private String				sender_name;
	private String				sender_pic;
	private String				recipient_id;
	private String				created_time;
	private String				updated_time;
	private String				title_html;
	private String				title_text;
	private String				body_html;
	private String				body_text;
	private String				href;
	private String				app_id;
	private boolean				is_unread;
	private boolean				is_hidden;
	private String				object_id;
	private String				object_name;
	private String				object_type;
	private String				icon_url;
	private Friend				friend;
	private Event				event;
	private Page				page;
	private Group				group;
	private Comment				comment;

	public Notification()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.NOTIFICATION;
	}

	public String getNotification_id()
	{
		return notification_id;
	}

	public void setNotification_id(String notification_id)
	{
		this.notification_id = notification_id;
	}

	public String getSender_id()
	{
		return sender_id;
	}

	public void setSender_id(String sender_id)
	{
		this.sender_id = sender_id;
	}

	public String getSender_name()
	{
		return sender_name;
	}

	public void setSender_name(String sender_name)
	{
		this.sender_name = sender_name;
	}

	public String getSender_pic()
	{
		return sender_pic;
	}

	public void setSender_pic(String sender_pic)
	{
		this.sender_pic = sender_pic;
	}

	public String getRecipient_id()
	{
		return recipient_id;
	}

	public void setRecipient_id(String recipient_id)
	{
		this.recipient_id = recipient_id;
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

	public String getTitle_html()
	{
		return title_html;
	}

	public void setTitle_html(String title_html)
	{
		this.title_html = title_html;
	}

	public String getTitle_text()
	{
		return title_text;
	}

	public void setTitle_text(String title_text)
	{
		this.title_text = title_text;
	}

	public String getBody_html()
	{
		return body_html;
	}

	public void setBody_html(String body_html)
	{
		this.body_html = body_html;
	}

	public String getBody_text()
	{
		return body_text;
	}

	public void setBody_text(String body_text)
	{
		this.body_text = body_text;
	}

	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public String getApp_id()
	{
		return app_id;
	}

	public void setApp_id(String app_id)
	{
		this.app_id = app_id;
	}

	public boolean getIs_unread()
	{
		return is_unread;
	}

	public void setIs_unread(boolean is_unread)
	{
		this.is_unread = is_unread;
	}

	public boolean getIs_hidden()
	{
		return is_hidden;
	}

	public void setIs_hidden(boolean is_hidden)
	{
		this.is_hidden = is_hidden;
	}

	public String getObject_id()
	{
		return object_id;
	}

	public void setObject_id(String object_id)
	{
		this.object_id = object_id;
	}

	public String getObject_name()
	{
		return object_name;
	}

	public void setObject_name(String object_name)
	{
		this.object_name = object_name;
	}

	public String getObject_type()
	{
		return object_type;
	}

	public void setObject_type(String object_type)
	{
		this.object_type = object_type;
	}

	public String getIcon_url()
	{
		return icon_url;
	}

	public void setIcon_url(String icon_url)
	{
		this.icon_url = icon_url;
	}

	public Friend getFriend()
	{
		return friend;
	}

	public void setFriend(Friend friend)
	{
		this.friend = friend;
	}

	public Event getEvent()
	{
		return event;
	}

	public void setEvent(Event event)
	{
		this.event = event;
	}

	public Page getPage()
	{
		return page;
	}

	public void setPage(Page page)
	{
		this.page = page;
	}

	public Group getGroup()
	{
		return group;
	}

	public void setGroup(Group group)
	{
		this.group = group;
	}

	public Comment getComment()
	{
		return comment;
	}

	public void setComment(Comment comment)
	{
		this.comment = comment;
	}
}
