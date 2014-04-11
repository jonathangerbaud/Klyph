package com.abewy.android.apps.klyph.core.fql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Stream extends GraphObject implements Parcelable
{
	private ArrayList<Object>		action_links;		// text, url
	private String					actor_id;
	private String					actor_name;
	private String					actor_type;
	private String					actor_pic;
	private String					app_id;
	private String					app_data;
	private Attachment				attachment;
	private String					attribution;
	private CommentInfo				comment_info;
	private String					created_time;
	private String					description;
	private Map<String, List<Tag>>	description_tags;
	private Event					event;
	private int					is_exportable;
	private boolean 					is_hidden;
	private List<Page>				liked_pages;
	private LikeInfo				like_info;
	private String					message;
	private Map<String, List<Tag>>	message_tags;
	private String					parent_post_id;
	private String					permalink;
	private String					place;
	private String					place_name;
	private Privacy					privacy;
	private String					post_id;
	private int						share_count;
	private String					source_id;
	private List<String>			tagged_ids;
	private List<Tag>				tagged_tags;
	private String					target_id;
	private String					target_name;
	private String					target_type;
	private int						type;
	private String					updated_time;
	private Link					link;
	private Photo					photo;
	private Video					video;
	private Status					status;
	private Application				application;
	private Stream					parent_stream;

	public Stream()
	{

	}

	@Override
	public int getItemViewType()
	{
		return GraphObject.STREAM;
	}

	// ___ Public services

	public boolean  isLink()
	{
		return link.getLink_id().length() > 0;
	}

	public boolean  isPhoto()
	{
		return photo.getObject_id().length() > 0;
	}
	
	public boolean  isAttachedPhoto()
	{
		if (attachment != null && attachment.getFb_object_type() != null && attachment.getFb_object_type().equals("photo"))
		{
			if (attachment.getFb_object_id() != null && attachment.getFb_object_id().length() > 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Return the id (pid or object_id) of the photo in the stream
	 * The photo can be either the liked photo or the atached photo
	 */
	public String getPhotoId()
	{
		if (photo.getObject_id().length() > 0)
		{
			Log.d("Stream", "getPhotoId");
			return photo.getObject_id();
		}
		
		if (isAttachedPhoto())
		{
			Log.d("Stream", "getPhotoId attached");
			return attachment.getMedia().get(0).getPhoto().getFbid();
		}
		
		return "";
	}

	public boolean  isVideo()
	{
		return video.getVid().length() > 0;
	}

	public boolean  isStatus()
	{
		return status.getStatus_id().length() > 0;
	}

	/**
	 * id to use with PostLikeRequest
	 */
	public String getObjectId()
	{
		if (actor_id != null && target_id != null && actor_id.length() > 0 && target_id.length() > 0 && !actor_id.equals(target_id))
		{
			String id = target_id + "_" + post_id.substring(post_id.indexOf("_") + 1);
			Log.d("Stream", "return target_id " + id);
			return target_id + "_" + post_id.substring(post_id.indexOf("_") + 1);
		}
		Log.d("Stream", "return post_id " + post_id);
		return post_id;
	}

	// ___ Privates getter/setter

	@Override
	public boolean  isSelectable(int layout)
	{
		return (comment_info.getCan_comment() || like_info.getCan_like());
	}

	public ArrayList<Object> getAction_links()
	{
		return action_links;
	}

	public void setAction_links(ArrayList<Object> action_links)
	{
		this.action_links = action_links;
	}

	public String getActor_id()
	{
		return actor_id;
	}

	public void setActor_id(String actor_id)
	{
		this.actor_id = actor_id;
	}

	public String getActor_name()
	{
		return actor_name;
	}

	public void setActor_name(String actor_name)
	{
		this.actor_name = actor_name;
	}

	public String getActor_type()
	{
		return actor_type;
	}

	public void setActor_type(String actor_type)
	{
		this.actor_type = actor_type;
	}

	public String getActor_pic()
	{
		return actor_pic;
	}

	public void setActor_pic(String actor_pic)
	{
		this.actor_pic = actor_pic;
	}

	public String getApp_id()
	{
		return app_id;
	}

	public void setApp_id(String app_id)
	{
		this.app_id = app_id;
	}

	public String getApp_data()
	{
		return app_data;
	}

	public void setApp_data(String app_data)
	{
		this.app_data = app_data;
	}

	public Attachment getAttachment()
	{
		return attachment;
	}

	public void setAttachment(Attachment attachment)
	{
		this.attachment = attachment;
	}

	public String getAttribution()
	{
		return attribution;
	}

	public void setAttribution(String attribution)
	{
		this.attribution = attribution;
	}

	public CommentInfo getComment_info()
	{
		return comment_info;
	}

	public void setComment_info(CommentInfo comment_info)
	{
		this.comment_info = comment_info;
	}

	public String getCreated_time()
	{
		return created_time;
	}

	public void setCreated_time(String created_time)
	{
		this.created_time = created_time;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Map<String, List<Tag>> getDescription_tags()
	{
		return description_tags;
	}

	public void setDescription_tags(Map<String, List<Tag>> description_tags)
	{
		this.description_tags = description_tags;
	}

	public Event getEvent()
	{
		return event;
	}

	public void setEvent(Event event)
	{
		this.event = event;
	}

	public int getIs_exportable()
	{
		return is_exportable;
	}

	public void setIs_exportable(int is_exportable)
	{
		this.is_exportable = is_exportable;
	}

	public boolean  getIs_hidden()
	{
		return is_hidden;
	}

	public void setIs_hidden(boolean  is_hidden)
	{
		this.is_hidden = is_hidden;
	}

	public List<Page> getLiked_pages()
	{
		return liked_pages;
	}

	public void setLiked_pages(List<Page> liked_pages)
	{
		this.liked_pages = liked_pages;
	}

	public LikeInfo getLike_info()
	{
		return like_info;
	}

	public void setLike_info(LikeInfo like_info)
	{
		this.like_info = like_info;
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

	public String getParent_post_id()
	{
		return parent_post_id;
	}

	public void setParent_post_id(String parent_post_id)
	{
		this.parent_post_id = parent_post_id;
	}

	public String getPermalink()
	{
		return permalink;
	}

	public void setPermalink(String permalink)
	{
		this.permalink = permalink;
	}

	public String getPlace()
	{
		return place;
	}

	public void setPlace(String place)
	{
		this.place = place;
	}

	public String getPlace_name()
	{
		return place_name;
	}

	public void setPlace_name(String place_name)
	{
		this.place_name = place_name;
	}

	public Privacy getPrivacy()
	{
		return privacy;
	}

	public void setPrivacy(Privacy privacy)
	{
		this.privacy = privacy;
	}

	public String getPost_id()
	{
		return post_id;
	}

	public void setPost_id(String post_id)
	{
		this.post_id = post_id;
	}

	public int getShare_count()
	{
		return share_count;
	}

	public void setShare_count(int share_count)
	{
		this.share_count = share_count;
	}

	public String getSource_id()
	{
		return source_id;
	}

	public void setSource_id(String source_id)
	{
		this.source_id = source_id;
	}

	public List<String> getTagged_ids()
	{
		return tagged_ids;
	}

	public void setTagged_ids(List<String> tagged_ids)
	{
		this.tagged_ids = tagged_ids;
	}

	public List<Tag> getTagged_tags()
	{
		return tagged_tags;
	}

	public void setTagged_tags(List<Tag> tagged_tags)
	{
		this.tagged_tags = tagged_tags;
	}

	public String getTarget_id()
	{
		return target_id;
	}

	public void setTarget_id(String target_id)
	{
		this.target_id = target_id;
	}

	public String getTarget_name()
	{
		return target_name;
	}

	public void setTarget_name(String target_name)
	{
		this.target_name = target_name;
	}

	public String getTarget_type()
	{
		return target_type;
	}

	public void setTarget_type(String target_type)
	{
		this.target_type = target_type;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getUpdated_time()
	{
		return updated_time;
	}

	public void setUpdated_time(String updated_time)
	{
		this.updated_time = updated_time;
	}

	public Link getLink()
	{
		return link;
	}

	public void setLink(Link link)
	{
		this.link = link;
	}

	public Photo getPhoto()
	{
		return photo;
	}

	public void setPhoto(Photo photo)
	{
		this.photo = photo;
	}

	public Video getVideo()
	{
		return video;
	}

	public void setVideo(Video video)
	{
		this.video = video;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public Application getApplication()
	{
		return application;
	}

	public void setApplication(Application application)
	{
		this.application = application;
	}

	public Stream getParent_stream()
	{
		return parent_stream;
	}

	public void setParent_stream(Stream parent_stream)
	{
		this.parent_stream = parent_stream;
	}

	public static class CommentInfo extends GraphObject implements Parcelable
	{
		private String	comment_order;
		private boolean 	can_comment	= false;
		private int		comment_count;

		public CommentInfo()
		{

		}

		public String getComment_order()
		{
			return comment_order;
		}

		public void setComment_order(String comment_order)
		{
			this.comment_order = comment_order;
		}

		public boolean  getCan_comment()
		{
			return can_comment;
		}

		public void setCan_comment(boolean  can_comment)
		{
			this.can_comment = can_comment;
		}

		public int getComment_count()
		{
			return comment_count;
		}

		public void setComment_count(int comment_count)
		{
			this.comment_count = comment_count;
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeByte((byte) (can_comment ? 1 : 0));
			dest.writeString(comment_order);
			dest.writeInt(comment_count);
		}

		public static final Parcelable.Creator<CommentInfo>	CREATOR	= new Parcelable.Creator<CommentInfo>() {
																		public CommentInfo createFromParcel(Parcel in)
																		{
																			return new CommentInfo(in);
																		}

																		public CommentInfo[] newArray(int size)
																		{
																			return new CommentInfo[size];
																		}
																	};

		private CommentInfo(Parcel in)
		{
			can_comment = in.readByte() == 1;
			comment_order = in.readString();
			comment_count = in.readInt();
		}
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(actor_id);
		dest.writeString(actor_name);
		dest.writeString(actor_pic);
		dest.writeString(actor_type);
		dest.writeString(app_data);
		dest.writeString(app_id);
		dest.writeParcelable(attachment, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(attribution);
		dest.writeParcelable(comment_info, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(created_time);
		dest.writeString(description);
		dest.writeInt(description_tags.size());
		for (String key : description_tags.keySet())
		{
			dest.writeString(key);
			dest.writeTypedList(description_tags.get(key));
		}
		dest.writeParcelable(event, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeByte((byte) (is_hidden ? 1 : 0));
		dest.writeTypedList(liked_pages);
		dest.writeParcelable(like_info, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(message);
		dest.writeInt(message_tags.size());
		for (String key : message_tags.keySet())
		{
			dest.writeString(key);
			dest.writeTypedList(message_tags.get(key));
		}
		dest.writeString(parent_post_id);
		dest.writeString(permalink);
		dest.writeString(place);
		dest.writeString(place_name);
		dest.writeParcelable(privacy, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(post_id);
		dest.writeString(source_id);
		dest.writeStringList(tagged_ids);
		dest.writeTypedList(tagged_tags);
		dest.writeString(target_id);
		dest.writeString(target_name);
		dest.writeString(target_type);
		dest.writeInt(type);
		dest.writeString(updated_time);
		dest.writeParcelable(link, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeParcelable(video, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeParcelable(status, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeParcelable(application, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeParcelable(photo, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeParcelable(parent_stream, PARCELABLE_WRITE_RETURN_VALUE);
	}

	public static final Parcelable.Creator<Stream>	CREATOR	= new Parcelable.Creator<Stream>() {
																public Stream createFromParcel(Parcel in)
																{
																	return new Stream(in);
																}

																public Stream[] newArray(int size)
																{
																	return new Stream[size];
																}
															};

	private Stream(Parcel in)
	{
		actor_id = in.readString();
		actor_name = in.readString();
		actor_pic = in.readString();
		actor_type = in.readString();
		app_data = in.readString();
		app_id = in.readString();
		attachment = in.readParcelable(Attachment.class.getClassLoader());
		attribution = in.readString();
		comment_info = in.readParcelable(CommentInfo.class.getClassLoader());
		created_time = in.readString();
		description = in.readString();
		description_tags = new HashMap<String, List<Tag>>();
		int size = in.readInt();
		for (int i = 0; i < size; i++)
		{
			String key = in.readString();
			List<Tag> value = new ArrayList<Tag>();
			in.readTypedList(value, Tag.CREATOR);
			description_tags.put(key, value);
		}
		event = in.readParcelable(Event.class.getClassLoader());
		is_hidden = in.readByte() == 1;
		liked_pages = new ArrayList<Page>();
		in.readTypedList(liked_pages, Page.CREATOR);
		like_info = in.readParcelable(LikeInfo.class.getClassLoader());
		message = in.readString();
		message_tags = new HashMap<String, List<Tag>>();
		size = in.readInt();
		for (int i = 0; i < size; i++)
		{
			String key = in.readString();
			List<Tag> value = new ArrayList<Tag>();
			in.readTypedList(value, Tag.CREATOR);
			message_tags.put(key, value);
		}
		parent_post_id = in.readString();
		permalink = in.readString();
		place = in.readString();
		place_name = in.readString();
		privacy = in.readParcelable(Privacy.class.getClassLoader());
		post_id = in.readString();
		source_id = in.readString();
		tagged_ids = new ArrayList<String>();
		in.readStringList(tagged_ids);
		tagged_tags = new ArrayList<Tag>();
		in.readTypedList(tagged_tags, Tag.CREATOR);
		target_id = in.readString();
		target_name = in.readString();
		target_type = in.readString();
		type = in.readInt();
		updated_time = in.readString();
		link = in.readParcelable(Link.class.getClassLoader());
		video = in.readParcelable(Video.class.getClassLoader());
		status = in.readParcelable(Status.class.getClassLoader());
		application = in.readParcelable(Application.class.getClassLoader());
		photo = in.readParcelable(Photo.class.getClassLoader());
		parent_stream = in.readParcelable(Stream.class.getClassLoader());
	}

}
