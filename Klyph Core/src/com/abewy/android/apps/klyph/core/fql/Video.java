package com.abewy.android.apps.klyph.core.fql;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Video extends GraphObject implements Parcelable
{
	private String				album_id;
	private String				album_name;
	private String				created_time;
	private String				description;
	private String				embed_html;
	private List<Format>		format;
	private String				length;
	private String				link;
	private String				owner;
	private String				owner_name;
	private String				owner_type;
	private String				owner_pic;
	private String				src;
	private String				src_hq;
	private String				thumbnail_link;
	private String				title;
	private String				updated_time;
	private String				vid;
	private String				source_url;	// media url

	public Video()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.VIDEO;
	}

	public String getVideoFormat()
	{
		String format = src;

		int slash = format.lastIndexOf("/") + 1;
		int questionMark = format.indexOf("?");

		format = format.substring(slash, questionMark);

		int point = format.indexOf(".") + 1;
		format = format.substring(point);

		return format;
	}

	// ___ Getters/Setters _____________________________________________________

	public String getAlbum_id()
	{
		return album_id;
	}

	public void setAlbum_id(String album_id)
	{
		this.album_id = album_id;
	}

	public String getAlbum_name()
	{
		return album_name;
	}

	public void setAlbum_name(String album_name)
	{
		this.album_name = album_name;
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

	public String getEmbed_html()
	{
		return embed_html;
	}

	public void setEmbed_html(String embed_html)
	{
		this.embed_html = embed_html;
	}

	public List<Format> getFormat()
	{
		return format;
	}

	public void setFormat(List<Format> format)
	{
		this.format = format;
	}

	public String getLength()
	{
		return length;
	}

	public void setLength(String length)
	{
		this.length = length;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getOwner_name()
	{
		return owner_name;
	}

	public void setOwner_name(String owner_name)
	{
		this.owner_name = owner_name;
	}

	public String getOwner_type()
	{
		return owner_type;
	}

	public void setOwner_type(String owner_type)
	{
		this.owner_type = owner_type;
	}

	public String getOwner_pic()
	{
		return owner_pic;
	}

	public void setOwner_pic(String owner_pic)
	{
		this.owner_pic = owner_pic;
	}

	public String getSrc()
	{
		return src;
	}

	public void setSrc(String src)
	{
		this.src = src;
	}

	public String getSrc_hq()
	{
		return src_hq;
	}

	public void setSrc_hq(String src_hq)
	{
		this.src_hq = src_hq;
	}

	public String getThumbnail_link()
	{
		return thumbnail_link;
	}

	public void setThumbnail_link(String thumbnail_link)
	{
		this.thumbnail_link = thumbnail_link;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUpdated_time()
	{
		return updated_time;
	}

	public void setUpdated_time(String updated_time)
	{
		this.updated_time = updated_time;
	}

	public String getVid()
	{
		return vid;
	}

	public void setVid(String vid)
	{
		this.vid = vid;
	}

	public String getSource_url()
	{
		return source_url;
	}

	public void setSource_url(String source_url)
	{
		this.source_url = source_url;
	}

	public static class Format extends GraphObject implements Parcelable
	{
		private int		width;
		private int		height;
		private String	filter;
		private String	picture;

		public Format()
		{
			
		}
		
		public int getWidth()
		{
			return width;
		}

		public void setWidth(int width)
		{
			this.width = width;
		}

		public int getHeight()
		{
			return height;
		}

		public void setHeight(int height)
		{
			this.height = height;
		}

		public String getFilter()
		{
			return filter;
		}

		public void setFilter(String filter)
		{
			this.filter = filter;
		}

		public String getPicture()
		{
			return picture;
		}

		public void setPicture(String picture)
		{
			this.picture = picture;
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeInt(width);
			dest.writeInt(height);
			dest.writeString(filter);
			dest.writeString(picture);
		}

		public static final Parcelable.Creator<Format>	CREATOR	= new Parcelable.Creator<Format>() {
																	public Format createFromParcel(Parcel in)
																	{
																		return new Format(in);
																	}

																	public Format[] newArray(int size)
																	{
																		return new Format[size];
																	}
																};

		private Format(Parcel in)
		{
			width = in.readInt();
			height = in.readInt();
			filter = in.readString();
			picture = in.readString();
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
		dest.writeString(album_id);
		dest.writeString(created_time);
		dest.writeString(description);
		dest.writeString(embed_html);
		dest.writeTypedList(format);
		dest.writeString(length);
		dest.writeString(link);
		dest.writeString(owner);
		dest.writeString(owner_name);
		dest.writeString(owner_pic);
		dest.writeString(owner_type);
		dest.writeString(source_url);
		dest.writeString(src);
		dest.writeString(src_hq);
		dest.writeString(thumbnail_link);
		dest.writeString(title);
		dest.writeString(updated_time);
		dest.writeString(vid);
	}

	public static final Parcelable.Creator<Video>	CREATOR	= new Parcelable.Creator<Video>() {
																public Video createFromParcel(Parcel in)
																{
																	return new Video(in);
																}

																public Video[] newArray(int size)
																{
																	return new Video[size];
																}
															};

	private Video(Parcel in)
	{
		album_id = in.readString();
		created_time = in.readString();
		description = in.readString();
		embed_html = in.readString();
		format = new ArrayList<Format>();
		in.readTypedList(format, Format.CREATOR);
		length = in.readString();
		link = in.readString();
		owner = in.readString();
		owner_name = in.readString();
		owner_pic = in.readString();
		owner_type = in.readString();
		source_url = in.readString();
		src = in.readString();
		src_hq = in.readString();
		thumbnail_link = in.readString();
		title = in.readString();
		updated_time = in.readString();
		vid = in.readString();
	}
}
