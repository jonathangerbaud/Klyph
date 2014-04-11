package com.abewy.android.apps.klyph.core.fql;

import java.util.List;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Message extends GraphObject
{
	private String		message_id;
	private String		thread_id;
	private String		author_id;
	private String		author_name;
	private String		author_pic;
	private String		body;
	private String		created_time;
	private Attachment	attachment;
	private String		viewer_id;

	public Message()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.MESSAGE;
	}

	public String getMessage_id()
	{
		return message_id;
	}

	public void setMessage_id(String message_id)
	{
		this.message_id = message_id;
	}

	public String getThread_id()
	{
		return thread_id;
	}

	public void setThread_id(String thread_id)
	{
		this.thread_id = thread_id;
	}

	public String getAuthor_id()
	{
		return author_id;
	}

	public void setAuthor_id(String author_id)
	{
		this.author_id = author_id;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getCreated_time()
	{
		return created_time;
	}

	public void setCreated_time(String created_time)
	{
		this.created_time = created_time;
	}

	public Attachment getAttachment()
	{
		return attachment;
	}

	public void setAttachment(Attachment attachment)
	{
		this.attachment = attachment;
	}

	public String getViewer_id()
	{
		return viewer_id;
	}

	public void setViewer_id(String viewer_id)
	{
		this.viewer_id = viewer_id;
	}

	public String getAuthor_name()
	{
		return author_name;
	}

	public void setAuthor_name(String author_name)
	{
		this.author_name = author_name;
	}

	public String getAuthor_pic()
	{
		return author_pic;
	}

	public void setAuthor_pic(String author_pic)
	{
		this.author_pic = author_pic;
	}

	public static class Attachment extends GraphObject
	{
		private List<Media>			media;
		private String				name;
		private String				href;
		private String				fbid;
		private String				caption;
		private String				description;
		private List<Properties>	properties;
		private String				icon;
		private String				fb_object_type;
		private String				fb_object_id;
		private String				fb_checkin;
		private String				tagged_ids;
		private String				sticker_id;
		private String				share_id;
		private String				type;
		private String				title;
		private String				summary;
		private String				image;

		public List<Media> getMedia()
		{
			return media;
		}

		public void setMedia(List<Media> media)
		{
			this.media = media;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getHref()
		{
			return href;
		}

		public void setHref(String href)
		{
			this.href = href;
		}

		public String getFbid()
		{
			return fbid;
		}

		public void setFbid(String fbid)
		{
			this.fbid = fbid;
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

		public List<Properties> getProperties()
		{
			return properties;
		}

		public void setProperties(List<Properties> properties)
		{
			this.properties = properties;
		}

		public String getIcon()
		{
			return icon;
		}

		public void setIcon(String icon)
		{
			this.icon = icon;
		}

		public String getFb_object_type()
		{
			return fb_object_type;
		}

		public void setFb_object_type(String fb_object_type)
		{
			this.fb_object_type = fb_object_type;
		}

		public String getFb_object_id()
		{
			return fb_object_id;
		}

		public void setFb_object_id(String fb_object_id)
		{
			this.fb_object_id = fb_object_id;
		}

		public String getFb_checkin()
		{
			return fb_checkin;
		}

		public void setFb_checkin(String fb_checkin)
		{
			this.fb_checkin = fb_checkin;
		}

		public String getTagged_ids()
		{
			return tagged_ids;
		}

		public void setTagged_ids(String tagged_ids)
		{
			this.tagged_ids = tagged_ids;
		}

		public String getSticker_id()
		{
			return sticker_id;
		}

		public void setSticker_id(String sticker_id)
		{
			this.sticker_id = sticker_id;
		}

		public String getShare_id()
		{
			return share_id;
		}

		public void setShare_id(String share_id)
		{
			this.share_id = share_id;
		}

		public String getType()
		{
			return type;
		}

		public void setType(String type)
		{
			this.type = type;
		}

		public String getTitle()
		{
			return title;
		}

		public void setTitle(String title)
		{
			this.title = title;
		}

		public String getSummary()
		{
			return summary;
		}

		public void setSummary(String summary)
		{
			this.summary = summary;
		}

		public String getImage()
		{
			return image;
		}

		public void setImage(String image)
		{
			this.image = image;
		}
	}

	public static class Media extends GraphObject
	{
		private String		href;
		private String		alt;
		private String		type;
		private String		src;
		private Video		video;
		private String		obj;
		private String		music;
		private String		data;
		private String		photo;
		private String		swf;
		private List<Media>	other_sizes;

		public String getHref()
		{
			return href;
		}

		public void setHref(String href)
		{
			this.href = href;
		}

		public String getAlt()
		{
			return alt;
		}

		public void setAlt(String alt)
		{
			this.alt = alt;
		}

		public String getType()
		{
			return type;
		}

		public void setType(String type)
		{
			this.type = type;
		}

		public String getSrc()
		{
			return src;
		}

		public void setSrc(String src)
		{
			this.src = src;
		}

		public Video getVideo()
		{
			return video;
		}

		public void setVideo(Video video)
		{
			this.video = video;
		}

		public String getObj()
		{
			return obj;
		}

		public void setObj(String obj)
		{
			this.obj = obj;
		}

		public String getMusic()
		{
			return music;
		}

		public void setMusic(String music)
		{
			this.music = music;
		}

		public String getData()
		{
			return data;
		}

		public void setData(String data)
		{
			this.data = data;
		}

		public String getPhoto()
		{
			return photo;
		}

		public void setPhoto(String photo)
		{
			this.photo = photo;
		}

		public String getSwf()
		{
			return swf;
		}

		public void setSwf(String swf)
		{
			this.swf = swf;
		}

		public List<Media> getOther_sizes()
		{
			return other_sizes;
		}

		public void setOther_sizes(List<Media> other_sizes)
		{
			this.other_sizes = other_sizes;
		}
	}

	public static class Music extends GraphObject
	{
		private String	source_url;
		private String	title;
		private String	artist;
		private String	album;

		public String getSource_url()
		{
			return source_url;
		}

		public void setSource_url(String source_url)
		{
			this.source_url = source_url;
		}

		public String getTitle()
		{
			return title;
		}

		public void setTitle(String title)
		{
			this.title = title;
		}

		public String getArtist()
		{
			return artist;
		}

		public void setArtist(String artist)
		{
			this.artist = artist;
		}

		public String getAlbum()
		{
			return album;
		}

		public void setAlbum(String album)
		{
			this.album = album;
		}
	}

	public static class Video extends GraphObject
	{
		private String	display_url;
		private String	source_url;
		private String	owner;
		private String	permalink;
		private String	preview_png;
		private String	source_type;
		private String	created_time;

		public String getDisplay_url()
		{
			return display_url;
		}

		public void setDisplay_url(String display_url)
		{
			this.display_url = display_url;
		}

		public String getSource_url()
		{
			return source_url;
		}

		public void setSource_url(String source_url)
		{
			this.source_url = source_url;
		}

		public String getOwner()
		{
			return owner;
		}

		public void setOwner(String owner)
		{
			this.owner = owner;
		}

		public String getPermalink()
		{
			return permalink;
		}

		public void setPermalink(String permalink)
		{
			this.permalink = permalink;
		}

		public String getPreview_png()
		{
			return preview_png;
		}

		public void setPreview_png(String preview_png)
		{
			this.preview_png = preview_png;
		}

		public String getSource_type()
		{
			return source_type;
		}

		public void setSource_type(String source_type)
		{
			this.source_type = source_type;
		}

		public String getCreated_time()
		{
			return created_time;
		}

		public void setCreated_time(String created_time)
		{
			this.created_time = created_time;
		}
	}

	public static class Properties extends GraphObject
	{
		private String	name;
		private String	text;
		private String	href;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getText()
		{
			return text;
		}

		public void setText(String text)
		{
			this.text = text;
		}

		public String getHref()
		{
			return href;
		}

		public void setHref(String href)
		{
			this.href = href;
		}
	}
}
