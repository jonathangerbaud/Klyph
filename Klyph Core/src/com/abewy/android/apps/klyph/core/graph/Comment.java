package com.abewy.android.apps.klyph.core.graph;

import java.util.List;
import com.abewy.android.apps.klyph.core.fql.Tag;


public class Comment extends GraphObject
{
	private String id;
	private UserRef from;
	private String message;
	private Attachment attachment;
	private String created_time;
	private int like_count;
	private boolean user_likes;
	private String parent;
	private boolean can_comment;
	private int comment_count;
	
	public Comment()
	{

	}
	
	@Override
	public int getItemViewType()
	{
		return GraphObject.GRAPH_COMMENT;
	}

	
	//___ Public services
	
	public boolean  hasParentComment()
	{
		return parent != null && parent.length() > 0;
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

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public Attachment getAttachment()
	{
		return attachment;
	}

	public void setAttachment(Attachment attachment)
	{
		this.attachment = attachment;
	}

	public String getCreated_time()
	{
		return created_time;
	}

	public void setCreated_time(String created_time)
	{
		this.created_time = created_time;
	}

	public int getLike_count()
	{
		return like_count;
	}

	public void setLike_count(int like_count)
	{
		this.like_count = like_count;
	}

	public boolean getUser_likes()
	{
		return user_likes;
	}

	public void setUser_likes(boolean user_likes)
	{
		this.user_likes = user_likes;
	}

	public String getParent()
	{
		return parent;
	}

	public void setParent(String parent)
	{
		this.parent = parent;
	}

	public boolean getCan_comment()
	{
		return can_comment;
	}

	public void setCan_comment(boolean can_comment)
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
		
		public boolean  isVideoShare()
		{
			return type != null && type.equals("video_share");
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
}
