package com.abewy.android.apps.klyph.core.fql;

import java.util.List;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UnifiedThread extends GraphObject
{
	private String				action_id;
	private String				admin_snippet;
	private boolean				archived;
	private List<String>		auto_mute;
	private boolean				can_reply;
	private String				folder;
	private List<String>		former_participants;
	private boolean				has_attachments;
	private boolean				is_group_conversation;
	private boolean				is_named_conversation;
	private boolean				is_suscribed;
	private String				last_visible_add_action_id;
	private String				link;
	private List<String>		mute;
	private String				name;
	private int					num_messages;
	private int					num_unread;
	private List<Object>		object_participants;
	private List<Sender>		participants;
	private String				pic_hash;
	private List<String>		read_receipts;
	private List<Sender>		senders;
	private String				single_recipient;
	private String		snippet;
	private boolean				snippet_message_has_attchment;
	private String				snippet_message_id;
	private Sender				snippet_sender;
	private String				subject;
	private List<String>		tags;
	private List<String>		thread_and_participants_name;
	private String				thread_fbid;
	private String				thread_id;
	private List<Sender>		thread_participants;
	private String				timestamp;
	private String				title;
	private boolean				unread;
	private boolean				unseen;

	public UnifiedThread()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.UNIFIED_THREAD;
	}

	@Override
	public boolean isSelectable(int layout)
	{
		return true;
	}

	public String getAction_id()
	{
		return action_id;
	}

	public void setAction_id(String action_id)
	{
		this.action_id = action_id;
	}

	public String getAdmin_snippet()
	{
		return admin_snippet;
	}

	public void setAdmin_snippet(String admin_snippet)
	{
		this.admin_snippet = admin_snippet;
	}

	public boolean getArchived()
	{
		return archived;
	}

	public void setArchived(boolean archived)
	{
		this.archived = archived;
	}

	public List<String> getAuto_mute()
	{
		return auto_mute;
	}

	public void setAuto_mute(List<String> auto_mute)
	{
		this.auto_mute = auto_mute;
	}

	public boolean getCan_reply()
	{
		return can_reply;
	}

	public void setCan_reply(boolean can_reply)
	{
		this.can_reply = can_reply;
	}

	public String getFolder()
	{
		return folder;
	}

	public void setFolder(String folder)
	{
		this.folder = folder;
	}

	public List<String> getFormer_participants()
	{
		return former_participants;
	}

	public void setFormer_participants(List<String> former_participants)
	{
		this.former_participants = former_participants;
	}

	public boolean getHas_attachments()
	{
		return has_attachments;
	}

	public void setHas_attachments(boolean has_attachments)
	{
		this.has_attachments = has_attachments;
	}

	public boolean getIs_group_conversation()
	{
		return is_group_conversation;
	}

	public void setIs_group_conversation(boolean is_group_conversation)
	{
		this.is_group_conversation = is_group_conversation;
	}

	public boolean getIs_named_conversation()
	{
		return is_named_conversation;
	}

	public void setIs_named_conversation(boolean is_named_conversation)
	{
		this.is_named_conversation = is_named_conversation;
	}

	public boolean getIs_suscribed()
	{
		return is_suscribed;
	}

	public void setIs_suscribed(boolean is_suscribed)
	{
		this.is_suscribed = is_suscribed;
	}

	public String getLast_visible_add_action_id()
	{
		return last_visible_add_action_id;
	}

	public void setLast_visible_add_action_id(String last_visible_add_action_id)
	{
		this.last_visible_add_action_id = last_visible_add_action_id;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public List<String> getMute()
	{
		return mute;
	}

	public void setMute(List<String> mute)
	{
		this.mute = mute;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getNum_messages()
	{
		return num_messages;
	}

	public void setNum_messages(int num_messages)
	{
		this.num_messages = num_messages;
	}

	public int getNum_unread()
	{
		return num_unread;
	}

	public void setNum_unread(int num_unread)
	{
		this.num_unread = num_unread;
	}

	public List<Object> getObject_participants()
	{
		return object_participants;
	}

	public void setObject_participants(List<Object> object_participants)
	{
		this.object_participants = object_participants;
	}

	public List<Sender> getParticipants()
	{
		return participants;
	}

	public void setParticipants(List<Sender> participants)
	{
		this.participants = participants;
	}

	public String getPic_hash()
	{
		return pic_hash;
	}

	public void setPic_hash(String pic_hash)
	{
		this.pic_hash = pic_hash;
	}

	public List<String> getRead_receipts()
	{
		return read_receipts;
	}

	public void setRead_receipts(List<String> read_receipts)
	{
		this.read_receipts = read_receipts;
	}

	public List<Sender> getSenders()
	{
		return senders;
	}

	public void setSenders(List<Sender> senders)
	{
		this.senders = senders;
	}

	public String getSingle_recipient()
	{
		return single_recipient;
	}

	public void setSingle_recipient(String single_recipient)
	{
		this.single_recipient = single_recipient;
	}

	public String getSnippet()
	{
		return snippet;
	}

	public void setSnippet(String snippet)
	{
		this.snippet = snippet;
	}

	public boolean getSnippet_message_has_attchment()
	{
		return snippet_message_has_attchment;
	}

	public void setSnippet_message_has_attchment(boolean snippet_message_has_attchment)
	{
		this.snippet_message_has_attchment = snippet_message_has_attchment;
	}

	public String getSnippet_message_id()
	{
		return snippet_message_id;
	}

	public void setSnippet_message_id(String snippet_message_id)
	{
		this.snippet_message_id = snippet_message_id;
	}

	public Sender getSnippet_sender()
	{
		return snippet_sender;
	}

	public void setSnippet_sender(Sender snippet_sender)
	{
		this.snippet_sender = snippet_sender;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public List<String> getTags()
	{
		return tags;
	}

	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}

	public List<String> getThread_and_participants_name()
	{
		return thread_and_participants_name;
	}

	public void setThread_and_participants_name(List<String> thread_and_participants_name)
	{
		this.thread_and_participants_name = thread_and_participants_name;
	}

	public String getThread_fbid()
	{
		return thread_fbid;
	}

	public void setThread_fbid(String thread_fbid)
	{
		this.thread_fbid = thread_fbid;
	}

	public String getThread_id()
	{
		return thread_id;
	}

	public void setThread_id(String thread_id)
	{
		this.thread_id = thread_id;
	}

	public List<Sender> getThread_participants()
	{
		return thread_participants;
	}

	public void setThread_participants(List<Sender> thread_participants)
	{
		this.thread_participants = thread_participants;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public boolean getUnread()
	{
		return unread;
	}

	public void setUnread(boolean unread)
	{
		this.unread = unread;
	}

	public boolean getUnseen()
	{
		return unseen;
	}

	public void setUnseen(boolean unseen)
	{
		this.unseen = unseen;
	}

	public static class Sender extends GraphObject
	{
		private String	name;
		private String	email;
		private String	user_id;

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
