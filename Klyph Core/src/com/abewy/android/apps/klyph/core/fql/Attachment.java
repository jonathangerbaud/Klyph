package com.abewy.android.apps.klyph.core.fql;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Attachment extends GraphObject implements Parcelable
{
	private List<Media>	media;
	private String		name;
	private String		caption;
	private String		description;
	private String		properties;
	private String		icon;
	private String		href;
	private String		fb_object_type;	// album, photo
	private String		fb_object_id;
	private Checkin		fb_checkin;

	public Attachment()
	{

	}

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

	public String getProperties()
	{
		return properties;
	}

	public void setProperties(String properties)
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

	public Checkin getFb_checkin()
	{
		return fb_checkin;
	}

	public void setFb_checkin(Checkin fb_checkin)
	{
		this.fb_checkin = fb_checkin;
	}

	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	// ___ Public Services _____________________________________________________

	public boolean isPhoto()
	{
		return fb_object_type != null && fb_object_type.equals(AttachmentType.PHOTO);
	}

	public boolean isAlbum()
	{
		return fb_object_type != null && fb_object_type.equals(AttachmentType.ALBUM);
	}

	public boolean isVideo()
	{
		return fb_object_type != null && fb_object_type.equals(AttachmentType.VIDEO)
				|| fb_object_type.equals(AttachmentType.SWF);
	}

	public boolean isLink()
	{
		return fb_object_type != null && fb_object_type.equals(AttachmentType.LINK);
	}

	public boolean isCheckin()
	{
		return fb_checkin.exist();
	}

	public boolean isFbVideo()
	{
		return isVideo() && fb_object_type != null && fb_object_type.equals(AttachmentType.VIDEO);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(caption);
		dest.writeString(description);
		dest.writeParcelable(fb_checkin, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(fb_object_id);
		dest.writeString(fb_object_type);
		dest.writeString(href);
		dest.writeString(icon);
		dest.writeTypedList(media);
		dest.writeString(name);
		dest.writeString(properties);
	}

	public static final Parcelable.Creator<Attachment>	CREATOR	= new Parcelable.Creator<Attachment>() {
																public Attachment createFromParcel(Parcel in)
																{
																	return new Attachment(in);
																}

																public Attachment[] newArray(int size)
																{
																	return new Attachment[size];
																}
															};

	private Attachment(Parcel in)
	{
		caption = in.readString();
		description= in.readString();
		fb_checkin= in.readParcelable(Checkin.class.getClassLoader());
		fb_object_id= in.readString();
		fb_object_type= in.readString();
		href= in.readString();
		icon= in.readString();
		media = new ArrayList<Media>();
		in.readTypedList(media, Media.CREATOR);
		name= in.readString();
		properties= in.readString();
	}
}
