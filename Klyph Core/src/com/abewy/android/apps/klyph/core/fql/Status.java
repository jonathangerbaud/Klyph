package com.abewy.android.apps.klyph.core.fql;

import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.fql.Stream.CommentInfo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Status extends GraphObject implements Parcelable
{
	private String	message;
	private String	place_id;
	private String	place_name;
	private String	source;
	private String	status_id;
	private String	time;
	private String	uid;
	private String	uid_name;
	private String	uid_type;
	private String	uid_pic;
	private LikeInfo like_info;
	private CommentInfo comment_info;

	public Status()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.STATUS;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
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

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getStatus_id()
	{
		return status_id;
	}

	public void setStatus_id(String status_id)
	{
		this.status_id = status_id;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getUid_name()
	{
		return uid_name;
	}

	public void setUid_name(String uid_name)
	{
		this.uid_name = uid_name;
	}

	public String getUid_type()
	{
		return uid_type;
	}

	public void setUid_type(String uid_type)
	{
		this.uid_type = uid_type;
	}

	public String getUid_pic()
	{
		return uid_pic;
	}

	public void setUid_pic(String uid_pic)
	{
		this.uid_pic = uid_pic;
	}

	public LikeInfo getLike_info()
	{
		return like_info;
	}

	public void setLike_info(LikeInfo like_info)
	{
		this.like_info = like_info;
	}

	public CommentInfo getComment_info()
	{
		return comment_info;
	}

	public void setComment_info(CommentInfo comment_info)
	{
		this.comment_info = comment_info;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(message);
		dest.writeString(place_id);
		dest.writeString(place_name);
		dest.writeString(source);
		dest.writeString(status_id);
		dest.writeString(time);
		dest.writeString(uid);
		dest.writeString(uid_name);
		dest.writeString(uid_pic);
		dest.writeString(uid_type);
		dest.writeParcelable(like_info, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeParcelable(comment_info, PARCELABLE_WRITE_RETURN_VALUE);
	}

	public static final Parcelable.Creator<Status>	CREATOR	= new Parcelable.Creator<Status>() {
																public Status createFromParcel(Parcel in)
																{
																	return new Status(in);
																}

																public Status[] newArray(int size)
																{
																	return new Status[size];
																}
															};

	private Status(Parcel in)
	{
		message = in.readString();
		place_id = in.readString();
		place_name = in.readString();
		source = in.readString();
		status_id = in.readString();
		time = in.readString();
		uid = in.readString();
		uid_name = in.readString();
		uid_pic = in.readString();
		uid_type = in.readString();
		like_info = in.readParcelable(LikeInfo.class.getClassLoader());
		comment_info = in.readParcelable(CommentInfo.class.getClassLoader());
	}
}
