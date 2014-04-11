package com.abewy.android.apps.klyph.core.fql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.fql.Stream.CommentInfo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Photo extends GraphObject implements Parcelable
{
	private String				aid;
	private String				aid_cursor;
	private String				album_object_id;
	private String				album_object_id_cursor;
	private String				album_name;
	private String				backdated_time;
	private String				backdated_time_granularity;
	private boolean				can_backdate;
	private boolean				can_delete;
	private boolean				can_tag;
	private String				caption;
	private String				caption_tags;
	private CommentInfo			comment_info;
	private String				created;
	private List<Image>			images;
	private LikeInfo			like_info;
	private String				link;
	private String				modified;
	private String				object_id;
	private String				offline_id;
	private String				owner;
	private String				owner_name;
	private String				owner_type;
	private String				owner_pic;
	private String				owner_cursor;
	private String				page_story_id;
	private String				pid;
	private String				place_id;
	private String				place_name;
	private String				src;
	private String				src_big;
	private int					src_big_height;
	private int					src_big_width;
	private int					src_height;
	private String				src_small;
	private int					src_small_height;
	private int					src_small_width;
	private int					src_width;
	private String				target_id;
	private String				target_name;
	private String				target_type;

	public Photo()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.PHOTO;
	}

	// ___ Public services _____________________________________________________

	public String getLargestImageURL()
	{
		if (images.size() > 0)
		{
			Collections.sort(getImages(), new Comparator<Image>() {

				@Override
				public int compare(Image lhs, Image rhs)
				{
					if (lhs.getWidth() < rhs.getWidth())
						return 1;
					else if (lhs.getWidth() > rhs.getWidth())
						return -1;

					return 0;
				}
			});

			return images.get(0).getSource();
		}

		return null;
	}

	// ___ Public getter/setter

	public String getAid()
	{
		return aid;
	}

	public void setAid(String aid)
	{
		this.aid = aid;
	}

	public String getAid_cursor()
	{
		return aid_cursor;
	}

	public void setAid_cursor(String aid_cursor)
	{
		this.aid_cursor = aid_cursor;
	}

	public String getAlbum_object_id()
	{
		return album_object_id;
	}

	public void setAlbum_object_id(String album_object_id)
	{
		this.album_object_id = album_object_id;
	}

	public String getAlbum_object_id_cursor()
	{
		return album_object_id_cursor;
	}

	public void setAlbum_object_id_cursor(String album_object_id_cursor)
	{
		this.album_object_id_cursor = album_object_id_cursor;
	}

	public String getAlbum_name()
	{
		return album_name;
	}

	public void setAlbum_name(String album_name)
	{
		this.album_name = album_name;
	}

	public String getBackdated_time()
	{
		return backdated_time;
	}

	public void setBackdated_time(String backdated_time)
	{
		this.backdated_time = backdated_time;
	}

	public String getBackdated_time_granularity()
	{
		return backdated_time_granularity;
	}

	public void setBackdated_time_granularity(String backdated_time_granularity)
	{
		this.backdated_time_granularity = backdated_time_granularity;
	}

	public boolean getCan_backdate()
	{
		return can_backdate;
	}

	public void setCan_backdate(boolean can_backdate)
	{
		this.can_backdate = can_backdate;
	}

	public boolean getCan_delete()
	{
		return can_delete;
	}

	public void setCan_delete(boolean can_delete)
	{
		this.can_delete = can_delete;
	}

	public boolean getCan_tag()
	{
		return can_tag;
	}

	public void setCan_tag(boolean can_tag)
	{
		this.can_tag = can_tag;
	}

	public String getCaption()
	{
		return caption;
	}

	public void setCaption(String caption)
	{
		this.caption = caption;
	}

	public String getCaption_tags()
	{
		return caption_tags;
	}

	public void setCaption_tags(String caption_tags)
	{
		this.caption_tags = caption_tags;
	}

	public CommentInfo getComment_info()
	{
		return comment_info;
	}

	public void setComment_info(CommentInfo comment_info)
	{
		this.comment_info = comment_info;
	}

	public String getCreated()
	{
		return created;
	}

	public void setCreated(String created)
	{
		this.created = created;
	}

	public List<Image> getImages()
	{
		return images;
	}

	public void setImages(List<Image> images)
	{
		this.images = images;
	}

	public LikeInfo getLike_info()
	{
		return like_info;
	}

	public void setLike_info(LikeInfo like_info)
	{
		this.like_info = like_info;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getModified()
	{
		return modified;
	}

	public void setModified(String modified)
	{
		this.modified = modified;
	}

	public String getObject_id()
	{
		return object_id;
	}

	public void setObject_id(String object_id)
	{
		this.object_id = object_id;
	}

	public String getOffline_id()
	{
		return offline_id;
	}

	public void setOffline_id(String offline_id)
	{
		this.offline_id = offline_id;
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

	public String getOwner_cursor()
	{
		return owner_cursor;
	}

	public void setOwner_cursor(String owner_cursor)
	{
		this.owner_cursor = owner_cursor;
	}

	public String getPage_story_id()
	{
		return page_story_id;
	}

	public void setPage_story_id(String page_story_id)
	{
		this.page_story_id = page_story_id;
	}

	public String getPid()
	{
		return pid;
	}

	public void setPid(String pid)
	{
		this.pid = pid;
	}

	public String getPlace_id()
	{
		return place_id;
	}

	public void setPlace_id(String place_id)
	{
		this.place_id = place_id;
	}

	public String getPlace_name()
	{
		return place_name;
	}

	public void setPlace_name(String place_name)
	{
		this.place_name = place_name;
	}

	public String getSrc()
	{
		return src;
	}

	public void setSrc(String src)
	{
		this.src = src;
	}

	public String getSrc_big()
	{
		return src_big;
	}

	public void setSrc_big(String src_big)
	{
		this.src_big = src_big;
	}

	public int getSrc_big_height()
	{
		return src_big_height;
	}

	public void setSrc_big_height(int src_big_height)
	{
		this.src_big_height = src_big_height;
	}

	public int getSrc_big_width()
	{
		return src_big_width;
	}

	public void setSrc_big_width(int src_big_width)
	{
		this.src_big_width = src_big_width;
	}

	public int getSrc_height()
	{
		return src_height;
	}

	public void setSrc_height(int src_height)
	{
		this.src_height = src_height;
	}

	public String getSrc_small()
	{
		return src_small;
	}

	public void setSrc_small(String src_small)
	{
		this.src_small = src_small;
	}

	public int getSrc_small_height()
	{
		return src_small_height;
	}

	public void setSrc_small_height(int src_small_height)
	{
		this.src_small_height = src_small_height;
	}

	public int getSrc_small_width()
	{
		return src_small_width;
	}

	public void setSrc_small_width(int src_small_width)
	{
		this.src_small_width = src_small_width;
	}

	public int getSrc_width()
	{
		return src_width;
	}

	public void setSrc_width(int src_width)
	{
		this.src_width = src_width;
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

	public static class Image extends GraphObject implements Parcelable
	{
		private String	source;
		private int		width;
		private int		height;

		public Image()
		{

		}

		public String getSource()
		{
			return source;
		}

		public void setSource(String source)
		{
			this.source = source;
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

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeString(source);
			dest.writeInt(width);
			dest.writeInt(height);
		}

		public static final Parcelable.Creator<Image>	CREATOR	= new Parcelable.Creator<Image>() {
																	public Image createFromParcel(Parcel in)
																	{
																		return new Image(in);
																	}

																	public Image[] newArray(int size)
																	{
																		return new Image[size];
																	}
																};

		private Image(Parcel in)
		{
			source = in.readString();
			width = in.readInt();
			height = in.readInt();
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
		dest.writeString(aid);
		dest.writeString(aid_cursor);
		dest.writeString(album_object_id);
		dest.writeString(album_object_id_cursor);
		dest.writeString(backdated_time);
		dest.writeString(backdated_time_granularity);
		dest.writeByte((byte) (can_backdate ? 0x01 : 0x00));
		dest.writeByte((byte) (can_delete ? 0x01 : 0x00));
		dest.writeByte((byte) (can_tag ? 0x01 : 0x00));
		dest.writeString(caption);
		dest.writeString(caption_tags);
		dest.writeParcelable(comment_info, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(created);
		dest.writeTypedList(images);
		dest.writeParcelable(like_info, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(link);
		dest.writeString(modified);
		dest.writeString(object_id);
		dest.writeString(offline_id);
		dest.writeString(owner);
		dest.writeString(owner_name);
		dest.writeString(owner_type);
		dest.writeString(owner_pic);
		dest.writeString(owner_cursor);
		dest.writeString(page_story_id);
		dest.writeString(pid);
		dest.writeString(place_id);
		dest.writeString(place_name);
		dest.writeString(src);
		dest.writeString(src_big);
		dest.writeInt(src_big_height);
		dest.writeInt(src_big_width);
		dest.writeInt(src_height);
		dest.writeString(src_small);
		dest.writeInt(src_small_height);
		dest.writeInt(src_small_width);
		dest.writeInt(src_width);
		dest.writeString(target_id);
		dest.writeString(target_name);
		dest.writeString(target_type);
	}

	public static final Parcelable.Creator<Photo>	CREATOR	= new Parcelable.Creator<Photo>() {
																public Photo createFromParcel(Parcel in)
																{
																	return new Photo(in);
																}

																public Photo[] newArray(int size)
																{
																	return new Photo[size];
																}
															};

	protected Photo(Parcel in)
	{
		aid = in.readString();
		aid_cursor = in.readString();
		album_object_id = in.readString();
		album_object_id_cursor = in.readString();
		backdated_time = in.readString();
		backdated_time_granularity = in.readString();
		can_backdate = in.readByte() != 0x00;
		can_delete = in.readByte() != 0x00;
		can_tag = in.readByte() != 0x00;
		caption = in.readString();
		caption_tags = in.readString();
		comment_info = in.readParcelable(CommentInfo.class.getClassLoader());
		created = in.readString();
		images = new ArrayList<Image>();
		in.readTypedList(images, Image.CREATOR);
		like_info = in.readParcelable(LikeInfo.class.getClassLoader());
		link = in.readString();
		modified = in.readString();
		object_id = in.readString();
		offline_id = in.readString();
		owner = in.readString();
		owner_name = in.readString();
		owner_type = in.readString();
		owner_pic = in.readString();
		owner_cursor = in.readString();
		page_story_id = in.readString();
		pid = in.readString();
		place_id = in.readString();
		place_name = in.readString();
		src = in.readString();
		src_big = in.readString();
		src_big_height = in.readInt();
		src_big_width = in.readInt();
		src_height = in.readInt();
		src_small = in.readString();
		src_small_height = in.readInt();
		src_small_width = in.readInt();
		src_width = in.readInt();
		target_id = in.readString();
		target_name = in.readString();
		target_type = in.readString();
	}
}
