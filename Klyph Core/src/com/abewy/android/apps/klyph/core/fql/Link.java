package com.abewy.android.apps.klyph.core.fql;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Stream.CommentInfo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.DailymotionUtil;
import com.abewy.android.apps.klyph.core.util.VimeoUtil;
import com.abewy.android.apps.klyph.core.util.YoutubeUtil;

public class Link extends GraphObject implements Parcelable
{
	private String				backdated_time;
	private boolean				can_backdate;
	private String				caption;
	private CommentInfo			comment_info;
	private String				created_time;
	private List<String>		image_urls;
	private LikeInfo			like_info;
	private String				link_id;
	private String				owner;
	private String				owner_name;
	private String				owner_type;
	private String				owner_pic;
	private String				owner_comment;
	private String				owner_cursor;
	private String				picture;
	private Privacy				privacy;
	private String				summary;
	private String				title;
	private String				url;
	private String				via_id;
	private String				via_name;
	private String				via_type;
	private String				xid;

	public Link()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.LINK;
	}

	// ___ Public services _____________________________________________________
	public boolean isVideoLink()
	{
		return isYoutubeLink() || isDailymotionLink() || isVimeoLink();
	}

	public boolean isYoutubeLink()
	{
		return YoutubeUtil.isYoutubeLink(url);
	}

	public boolean isDailymotionLink()
	{
		return DailymotionUtil.isDailymotionLink(url);
	}

	public boolean isVimeoLink()
	{
		return VimeoUtil.isVimeoLink(url);
	}
	
	public boolean isEventLink()
	{
		return getEventId().length() > 0;
	}
	
	public String getEventId()
	{
		if (url != null && url.length() > 8 && url.substring(0, 8).equals("/events/"))
			return url.substring(8, url.length() - 1);
		
		return "";
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

	public String getCaption()
	{
		return caption;
	}

	public void setCaption(String caption)
	{
		this.caption = caption;
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

	public List<String> getImage_urls()
	{
		return image_urls;
	}

	public void setImage_urls(List<String> image_urls)
	{
		this.image_urls = image_urls;
	}

	public LikeInfo getLike_info()
	{
		return like_info;
	}

	public void setLike_info(LikeInfo like_info)
	{
		this.like_info = like_info;
	}

	public String getLink_id()
	{
		return link_id;
	}

	public void setLink_id(String link_id)
	{
		this.link_id = link_id;
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

	public String getOwner_comment()
	{
		return owner_comment;
	}

	public void setOwner_comment(String owner_comment)
	{
		this.owner_comment = owner_comment;
	}

	public String getOwner_cursor()
	{
		return owner_cursor;
	}

	public void setOwner_cursor(String owner_cursor)
	{
		this.owner_cursor = owner_cursor;
	}

	public String getPicture()
	{
		return picture;
	}

	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	public Privacy getPrivacy()
	{
		return privacy;
	}

	public void setPrivacy(Privacy privacy)
	{
		this.privacy = privacy;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getVia_id()
	{
		return via_id;
	}

	public void setVia_id(String via_id)
	{
		this.via_id = via_id;
	}

	public String getVia_name()
	{
		return via_name;
	}

	public void setVia_name(String via_name)
	{
		this.via_name = via_name;
	}

	public String getVia_type()
	{
		return via_type;
	}

	public void setVia_type(String via_type)
	{
		this.via_type = via_type;
	}

	public String getXid()
	{
		return xid;
	}

	public void setXid(String xid)
	{
		this.xid = xid;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(backdated_time);
		dest.writeByte((byte) (can_backdate ? 1 : 0));
		dest.writeString(caption);
		dest.writeParcelable(comment_info, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(created_time);
		dest.writeStringList(image_urls);
		dest.writeParcelable(like_info, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(link_id);
		dest.writeString(owner);
		dest.writeString(owner_comment);
		dest.writeString(owner_cursor);
		dest.writeString(owner_name);
		dest.writeString(owner_pic);
		dest.writeString(owner_type);
		dest.writeString(picture);
		dest.writeParcelable(privacy, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(summary);
		dest.writeString(title);
		dest.writeString(url);
		dest.writeString(via_id);
		dest.writeString(via_name);
		dest.writeString(via_type);
		dest.writeString(xid);
	}

	public static final Parcelable.Creator<Link>	CREATOR	= new Parcelable.Creator<Link>() {
																public Link createFromParcel(Parcel in)
																{
																	return new Link(in);
																}

																public Link[] newArray(int size)
																{
																	return new Link[size];
																}
															};

	private Link(Parcel in)
	{
		backdated_time = in.readString();
		can_backdate = in.readByte() == 1;
		caption = in.readString();
		comment_info = in.readParcelable(CommentInfo.class.getClassLoader());
		created_time = in.readString();
		image_urls = new ArrayList<String>();
		in.readStringList(image_urls);
		like_info = in.readParcelable(LikeInfo.class.getClassLoader());
		link_id = in.readString();
		owner = in.readString();
		owner_comment = in.readString();
		owner_cursor = in.readString();
		owner_name = in.readString();
		owner_pic = in.readString();
		owner_type = in.readString();
		picture = in.readString();
		privacy = in.readParcelable(Privacy.class.getClassLoader());
		summary = in.readString();
		title = in.readString();
		url = in.readString();
		via_id = in.readString();
		via_name = in.readString();
		via_type = in.readString();
		xid = in.readString();
	}
}
