package com.abewy.android.apps.klyph.core.fql;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Comment extends GraphObject implements Parcelable
{
	private String				app_id;
	private Attachment			attachment;
	private boolean				can_comment;
	private boolean				can_like;
	private boolean				can_remove;
	private boolean				comment_count;
	private String				fromid;
	private String				from_name;
	private String				from_type;
	private String				from_pic;
	private String				id;
	private boolean				is_private;
	private int					likes;
	private String				object_id;
	private String				object_id_cursor;
	private String				parent_id;
	private String				post_fbid;
	private String				post_id;
	private String				post_id_cursor;
	private String				text;
	private List<Tag>			text_tags;
	private String				time;
	private boolean				user_likes;

	public Comment()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.COMMENT;
	}
	
	public String getApp_id()
	{
		return app_id;
	}

	public void setApp_id(String app_id)
	{
		this.app_id = app_id;
	}

	public boolean getCan_like()
	{
		return can_like;
	}

	public void setCan_like(boolean can_like)
	{
		this.can_like = can_like;
	}

	public boolean getCan_remove()
	{
		return can_remove;
	}

	public void setCan_remove(boolean can_remove)
	{
		this.can_remove = can_remove;
	}

	public String getFromid()
	{
		return fromid;
	}

	public void setFromid(String fromid)
	{
		this.fromid = fromid;
	}

	public String getFrom_name()
	{
		return from_name;
	}

	public void setFrom_name(String from_name)
	{
		this.from_name = from_name;
	}

	public String getFrom_type()
	{
		return from_type;
	}

	public void setFrom_type(String from_type)
	{
		this.from_type = from_type;
	}

	public String getFrom_pic()
	{
		return from_pic;
	}

	public void setFrom_pic(String from_pic)
	{
		this.from_pic = from_pic;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public boolean getIs_private()
	{
		return is_private;
	}

	public void setIs_private(boolean is_private)
	{
		this.is_private = is_private;
	}

	public int getLikes()
	{
		return likes;
	}

	public void setLikes(int likes)
	{
		this.likes = likes;
	}

	public String getObject_id()
	{
		return object_id;
	}

	public void setObject_id(String object_id)
	{
		this.object_id = object_id;
	}

	public String getParent_id()
	{
		return parent_id;
	}

	public void setParent_id(String parent_id)
	{
		this.parent_id = parent_id;
	}

	public String getPost_fbid()
	{
		return post_fbid;
	}

	public void setPost_fbid(String post_fbid)
	{
		this.post_fbid = post_fbid;
	}

	public String getPost_id()
	{
		return post_id;
	}

	public void setPost_id(String post_id)
	{
		this.post_id = post_id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public List<Tag> getText_tags()
	{
		return text_tags;
	}

	public void setText_tags(List<Tag> text_tags)
	{
		this.text_tags = text_tags;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public boolean getUser_likes()
	{
		return user_likes;
	}

	public void setUser_likes(boolean user_likes)
	{
		this.user_likes = user_likes;
	}

	public Attachment getAttachment()
	{
		return attachment;
	}

	public void setAttachment(Attachment attachment)
	{
		this.attachment = attachment;
	}

	public boolean getCan_comment()
	{
		return can_comment;
	}

	public void setCan_comment(boolean can_comment)
	{
		this.can_comment = can_comment;
	}

	public boolean getComment_count()
	{
		return comment_count;
	}

	public void setComment_count(boolean comment_count)
	{
		this.comment_count = comment_count;
	}

	public String getObject_id_cursor()
	{
		return object_id_cursor;
	}

	public void setObject_id_cursor(String object_id_cursor)
	{
		this.object_id_cursor = object_id_cursor;
	}

	public String getPost_id_cursor()
	{
		return post_id_cursor;
	}

	public void setPost_id_cursor(String post_id_cursor)
	{
		this.post_id_cursor = post_id_cursor;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}
	
	//___ Public services ___
	public boolean  hasParentComment()
	{
		return parent_id != null && parent_id.length() > 0 && !parent_id.equals("0");
	}

	public static class Attachment extends GraphObject
	{
		private String			description;
		private List<Tag>		description_tags;
		private Media			media;
		private Target			target;
		private String			title;
		private String			type;
		private String			url;
		private List<String>	subattachments;

		public String getDescription()
		{
			return description;
		}

		public void setDescription(String description)
		{
			this.description = description;
		}

		public List<Tag> getDescription_tags()
		{
			return description_tags;
		}

		public void setDescription_tags(List<Tag> description_tags)
		{
			this.description_tags = description_tags;
		}

		public Media getMedia()
		{
			return media;
		}

		public void setMedia(Media media)
		{
			this.media = media;
		}

		public Target getTarget()
		{
			return target;
		}

		public void setTarget(Target target)
		{
			this.target = target;
		}

		public String getTitle()
		{
			return title;
		}

		public void setTitle(String title)
		{
			this.title = title;
		}

		public String getType()
		{
			return type;
		}

		public void setType(String type)
		{
			this.type = type;
		}

		public String getUrl()
		{
			return url;
		}

		public void setUrl(String url)
		{
			this.url = url;
		}

		public List<String> getSubattachments()
		{
			return subattachments;
		}

		public void setSubattachments(List<String> subattachments)
		{
			this.subattachments = subattachments;
		}
		
		public boolean  isPhoto()
		{
			return type != null && type.equals("photo");
		}
		
		public boolean  isShare()
		{
			return type != null && type.equals("share");
		}

		public static class Media extends GraphObject
		{
			private Image	image;

			public Image getImage()
			{
				return image;
			}

			public void setImage(Image image)
			{
				this.image = image;
			}

			public static class Image extends GraphObject
			{
				private int		height;
				private String	src;
				private int		width;

				public int getHeight()
				{
					return height;
				}

				public void setHeight(int height)
				{
					this.height = height;
				}

				public String getSrc()
				{
					return src;
				}

				public void setSrc(String src)
				{
					this.src = src;
				}

				public int getWidth()
				{
					return width;
				}

				public void setWidth(int width)
				{
					this.width = width;
				}
			}
		}

		public static class Target extends GraphObject
		{
			private String	id;
			private String	url;

			public String getId()
			{
				return id;
			}

			public void setId(String id)
			{
				this.id = id;
			}

			public String getUrl()
			{
				return url;
			}

			public void setUrl(String url)
			{
				this.url = url;
			}
		}
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(app_id);
		dest.writeByte((byte) (can_like ? 1 : 0));
		dest.writeByte((byte) (can_remove ? 1 : 0));
		dest.writeString(from_name);
		dest.writeString(from_pic);
		dest.writeString(from_type);
		dest.writeString(fromid);
		dest.writeString(id);
		dest.writeByte((byte) (is_private ? 1 : 0));
		dest.writeInt(likes);
		dest.writeString(object_id);
		dest.writeString(parent_id);
		dest.writeString(post_fbid);
		dest.writeString(post_id);
		dest.writeString(text);
		dest.writeTypedList(text_tags);
		dest.writeString(time);
		dest.writeByte((byte) (user_likes ? 1 : 0));
	}

	public static final Parcelable.Creator<Comment>	CREATOR	= new Parcelable.Creator<Comment>() {
																public Comment createFromParcel(Parcel in)
																{
																	return new Comment(in);
																}

																public Comment[] newArray(int size)
																{
																	return new Comment[size];
																}
															};

	private Comment(Parcel in)
	{
		app_id = in.readString();
		can_like = in.readByte() == 1;
		can_remove = in.readByte() == 1;
		from_name = in.readString();
		from_pic = in.readString();
		from_type = in.readString();
		fromid = in.readString();
		id = in.readString();
		likes = in.readInt();
		object_id = in.readString();
		parent_id = in.readString();
		post_fbid = in.readString();
		post_id = in.readString();
		text = in.readString();
		text_tags = new ArrayList<Tag>();
		in.readTypedList(text_tags, Tag.CREATOR);
		time = in.readString();
		user_likes = in.readByte() == 1;
	}
}