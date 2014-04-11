package com.abewy.android.apps.klyph.core.fql;

import java.util.List;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class MessageThread extends GraphObject
{
	private String				thread_id;
	private String				folder_id;
	private String				subject;
	private List<String>		recipients;
	private List<Friend>		recipients_friends;
	private String				updated_time;
	private String				parent_message_id;
	private String				parent_thread_id;
	private int					message_count;
	private String				snippet;
	private String				snippet_author;
	private String				object_id;
	private int					unread;
	private String				viewer_id;
	private boolean  			friend_is_online;

	public MessageThread()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.MESSAGE_THREAD;
	}

	public String getThread_id()
	{
		return thread_id;
	}

	public void setThread_id(String thread_id)
	{
		this.thread_id = thread_id;
	}

	public String getFolder_id()
	{
		return folder_id;
	}

	public void setFolder_id(String folder_id)
	{
		this.folder_id = folder_id;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public List<String> getRecipients()
	{
		return recipients;
	}

	public void setRecipients(List<String> recipients)
	{
		this.recipients = recipients;
	}

	public String getUpdated_time()
	{
		return updated_time;
	}

	public void setUpdated_time(String updated_time)
	{
		this.updated_time = updated_time;
	}

	public String getParent_message_id()
	{
		return parent_message_id;
	}

	public void setParent_message_id(String parent_message_id)
	{
		this.parent_message_id = parent_message_id;
	}

	public String getParent_thread_id()
	{
		return parent_thread_id;
	}

	public void setParent_thread_id(String parent_thread_id)
	{
		this.parent_thread_id = parent_thread_id;
	}

	public int getMessage_count()
	{
		return message_count;
	}

	public void setMessage_count(int message_count)
	{
		this.message_count = message_count;
	}

	public String getSnippet()
	{
		return snippet;
	}

	public void setSnippet(String snippet)
	{
		this.snippet = snippet;
	}

	public String getSnippet_author()
	{
		return snippet_author;
	}

	public void setSnippet_author(String snippet_author)
	{
		this.snippet_author = snippet_author;
	}

	public String getObject_id()
	{
		return object_id;
	}

	public void setObject_id(String object_id)
	{
		this.object_id = object_id;
	}

	public int getUnread()
	{
		return unread;
	}

	public void setUnread(int unread)
	{
		this.unread = unread;
	}

	public String getViewer_id()
	{
		return viewer_id;
	}

	public void setViewer_id(String viewer_id)
	{
		this.viewer_id = viewer_id;
	}

	public List<Friend> getRecipients_friends()
	{
		return recipients_friends;
	}

	public void setRecipients_friends(List<Friend> recipients_friends)
	{
		this.recipients_friends = recipients_friends;
	}
	
	public boolean  getFriend_is_online()
	{
		return friend_is_online;
	}

	public void setFriend_is_online(boolean  friend_is_online)
	{
		this.friend_is_online = friend_is_online;
	}
	
	
	//___ public services

	public boolean  isSingleUserConversation()
	{
		return recipients != null && recipients.size() == 2;
	}
	
	public boolean  isMultiUserConversation()
	{
		return recipients != null && recipients.size() > 2;
	}
}
