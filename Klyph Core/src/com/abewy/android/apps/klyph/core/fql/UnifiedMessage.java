package com.abewy.android.apps.klyph.core.fql;

import java.util.List;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UnifiedMessage extends GraphObject
{
	private String					action_id;
	private List<Object>			attachment_map;
	private List<String>			attachments;
	private String					body;
	private String					containing_message_id;
	private Coordinates				coordinates;
	private String					forwaded_message_id;
	private List<UnifiedMessage>	forwaded_messages;
	private String					html_body;
	private boolean					is_forwarded;
	private boolean					is_user_generated;
	private Object					log_message;
	private String					message_id;
	private Object					object_sender;
	private String					offline_threading_id;
	private List<String>			recipients;
	private Sender					sender;
	private List<Object>			share_map;
	private List<String>			shares;
	private String					subject;
	private List<Object>			tags;
	private String					thread_id;
	private String					timestamp;
	private boolean					unread;

	public UnifiedMessage()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.UNIFIED_MESSAGE;
	}

	@Override
	public boolean isSelectable(int layout)
	{
		return false;
	}

	public String getAction_id()
	{
		return action_id;
	}

	public void setAction_id(String action_id)
	{
		this.action_id = action_id;
	}

	public List<Object> getAttachment_map()
	{
		return attachment_map;
	}

	public void setAttachment_map(List<Object> attachment_map)
	{
		this.attachment_map = attachment_map;
	}

	public List<String> getAttachments()
	{
		return attachments;
	}

	public void setAttachments(List<String> attachments)
	{
		this.attachments = attachments;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getContaining_message_id()
	{
		return containing_message_id;
	}

	public void setContaining_message_id(String containing_message_id)
	{
		this.containing_message_id = containing_message_id;
	}

	public Coordinates getCoordinates()
	{
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates)
	{
		this.coordinates = coordinates;
	}

	public String getForwaded_message_id()
	{
		return forwaded_message_id;
	}

	public void setForwaded_message_id(String forwaded_message_id)
	{
		this.forwaded_message_id = forwaded_message_id;
	}

	public List<UnifiedMessage> getForwaded_messages()
	{
		return forwaded_messages;
	}

	public void setForwaded_messages(List<UnifiedMessage> forwaded_messages)
	{
		this.forwaded_messages = forwaded_messages;
	}

	public String getHtml_body()
	{
		return html_body;
	}

	public void setHtml_body(String html_body)
	{
		this.html_body = html_body;
	}

	public boolean getIs_forwarded()
	{
		return is_forwarded;
	}

	public void setIs_forwarded(boolean is_forwarded)
	{
		this.is_forwarded = is_forwarded;
	}

	public boolean getIs_user_generated()
	{
		return is_user_generated;
	}

	public void setIs_user_generated(boolean is_user_generated)
	{
		this.is_user_generated = is_user_generated;
	}

	public Object getLog_message()
	{
		return log_message;
	}

	public void setLog_message(Object log_message)
	{
		this.log_message = log_message;
	}

	public String getMessage_id()
	{
		return message_id;
	}

	public void setMessage_id(String message_id)
	{
		this.message_id = message_id;
	}

	public Object getObject_sender()
	{
		return object_sender;
	}

	public void setObject_sender(Object object_sender)
	{
		this.object_sender = object_sender;
	}

	public String getOffline_threading_id()
	{
		return offline_threading_id;
	}

	public void setOffline_threading_id(String offline_threading_id)
	{
		this.offline_threading_id = offline_threading_id;
	}

	public List<String> getRecipients()
	{
		return recipients;
	}

	public void setRecipients(List<String> recipients)
	{
		this.recipients = recipients;
	}

	public Sender getSender()
	{
		return sender;
	}

	public void setSender(Sender sender)
	{
		this.sender = sender;
	}

	public List<Object> getShare_map()
	{
		return share_map;
	}

	public void setShare_map(List<Object> share_map)
	{
		this.share_map = share_map;
	}

	public List<String> getShares()
	{
		return shares;
	}

	public void setShares(List<String> shares)
	{
		this.shares = shares;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public List<Object> getTags()
	{
		return tags;
	}

	public void setTags(List<Object> tags)
	{
		this.tags = tags;
	}

	public String getThread_id()
	{
		return thread_id;
	}

	public void setThread_id(String thread_id)
	{
		this.thread_id = thread_id;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public boolean getUnread()
	{
		return unread;
	}

	public void setUnread(boolean unread)
	{
		this.unread = unread;
	}



	public static class Coordinates
	{
		private String	latitude;
		private String	longitude;
		private String	altitude;
		private String	accuracy;
		private String	altitudeAccuracy;
		private String	heading;
		private String	speed;
		private String	timestamp;

		public String getLatitude()
		{
			return latitude;
		}

		public void setLatitude(String latitude)
		{
			this.latitude = latitude;
		}

		public String getLongitude()
		{
			return longitude;
		}

		public void setLongitude(String longitude)
		{
			this.longitude = longitude;
		}

		public String getAltitude()
		{
			return altitude;
		}

		public void setAltitude(String altitude)
		{
			this.altitude = altitude;
		}

		public String getAccuracy()
		{
			return accuracy;
		}

		public void setAccuracy(String accuracy)
		{
			this.accuracy = accuracy;
		}

		public String getAltitudeAccuracy()
		{
			return altitudeAccuracy;
		}

		public void setAltitudeAccuracy(String altitudeAccuracy)
		{
			this.altitudeAccuracy = altitudeAccuracy;
		}

		public String getHeading()
		{
			return heading;
		}

		public void setHeading(String heading)
		{
			this.heading = heading;
		}

		public String getSpeed()
		{
			return speed;
		}

		public void setSpeed(String speed)
		{
			this.speed = speed;
		}

		public String getTimestamp()
		{
			return timestamp;
		}

		public void setTimestamp(String timestamp)
		{
			this.timestamp = timestamp;
		}
	}
	
	public static class Sender extends GraphObject
	{
		private String name;
		private String email;
		private String user_id;
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public String getEmail()
		{
			return email;
		}
		public void setEmail(String email)
		{
			this.email = email;
		}
		public String getUser_id()
		{
			return user_id;
		}
		public void setUser_id(String user_id)
		{
			this.user_id = user_id;
		}
	}
}
