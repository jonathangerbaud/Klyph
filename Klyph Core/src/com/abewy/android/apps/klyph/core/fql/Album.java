package com.abewy.android.apps.klyph.core.fql;

import java.util.List;
import com.abewy.android.apps.klyph.core.fql.Photo.Image;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Album extends GraphObject
{
	private String		aid;
	private String		backdated_time;
	private boolean		can_backdate;
	private boolean		can_upload;
	private Object		comment_info;
	private String		cover_object_id;
	private String		cover_pid;
	private String		created;
	private List<Image>	cover_images;
	private String		description;
	private String		edit_link;
	private boolean		is_video_album;
	private Object		like_info;
	private String		link;
	private String		location;
	private String		modified;
	private String		modified_major;
	private String		name;
	private String		object_id;
	private String		owner;
	private String		owner_cursor;
	private String		owner_name;
	private int			photo_count;
	private String		place_id;
	private String		type;
	private int			video_count;
	private String		visible;
	private boolean  isTaggedAlbum = false;

	public Album()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.ALBUM;
	}

	public boolean  isTaggedAlbum()
	{
		return isTaggedAlbum;
	}
	
	public void setIsTaggedAlbum(boolean  isTaggedAlbum)
	{
		this.isTaggedAlbum = isTaggedAlbum;
	}

	public String getAid()
	{
		return aid;
	}

	public void setAid(String aid)
	{
		this.aid = aid;
	}

	public String getBackdated_time()
	{
		return backdated_time;
	}

	public void setBackdated_time(String backdated_time)
	{
		this.backdated_time = backdated_time;
	}

	public boolean getCan_backdate()
	{
		return can_backdate;
	}

	public void setCan_backdate(boolean can_backdate)
	{
		this.can_backdate = can_backdate;
	}

	public boolean getCan_upload()
	{
		return can_upload;
	}

	public void setCan_upload(boolean can_upload)
	{
		this.can_upload = can_upload;
	}

	public Object getComment_info()
	{
		return comment_info;
	}

	public void setComment_info(Object comment_info)
	{
		this.comment_info = comment_info;
	}

	public String getCover_object_id()
	{
		return cover_object_id;
	}

	public void setCover_object_id(String cover_object_id)
	{
		this.cover_object_id = cover_object_id;
	}

	public String getCover_pid()
	{
		return cover_pid;
	}

	public void setCover_pid(String cover_pid)
	{
		this.cover_pid = cover_pid;
	}

	public String getCreated()
	{
		return created;
	}

	public void setCreated(String created)
	{
		this.created = created;
	}

	public List<Image> getCover_images()
	{
		return cover_images;
	}

	public void setCover_images(List<Image> cover_images)
	{
		this.cover_images = cover_images;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getEdit_link()
	{
		return edit_link;
	}

	public void setEdit_link(String edit_link)
	{
		this.edit_link = edit_link;
	}

	public boolean getIs_video_album()
	{
		return is_video_album;
	}

	public void setIs_video_album(boolean is_video_album)
	{
		this.is_video_album = is_video_album;
	}

	public Object getLike_info()
	{
		return like_info;
	}

	public void setLike_info(Object like_info)
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

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getModified()
	{
		return modified;
	}

	public void setModified(String modified)
	{
		this.modified = modified;
	}

	public String getModified_major()
	{
		return modified_major;
	}

	public void setModified_major(String modified_major)
	{
		this.modified_major = modified_major;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getObject_id()
	{
		return object_id;
	}

	public void setObject_id(String object_id)
	{
		this.object_id = object_id;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getOwner_cursor()
	{
		return owner_cursor;
	}

	public void setOwner_cursor(String owner_cursor)
	{
		this.owner_cursor = owner_cursor;
	}

	public String getOwner_name()
	{
		return owner_name;
	}

	public void setOwner_name(String owner_name)
	{
		this.owner_name = owner_name;
	}

	public int getPhoto_count()
	{
		return photo_count;
	}

	public void setPhoto_count(int photo_count)
	{
		this.photo_count = photo_count;
	}

	public String getPlace_id()
	{
		return place_id;
	}

	public void setPlace_id(String place_id)
	{
		this.place_id = place_id;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getVideo_count()
	{
		return video_count;
	}

	public void setVideo_count(int video_count)
	{
		this.video_count = video_count;
	}

	public String getVisible()
	{
		return visible;
	}

	public void setVisible(String visible)
	{
		this.visible = visible;
	}
}
