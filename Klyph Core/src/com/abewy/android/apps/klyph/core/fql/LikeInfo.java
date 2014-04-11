package com.abewy.android.apps.klyph.core.fql;

import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class LikeInfo extends GraphObject implements Parcelable
{
	private int					like_count;
	private boolean				user_likes;
	private boolean				can_like;

	public LikeInfo()
	{

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

	public boolean getCan_like()
	{
		return can_like;
	}

	public void setCan_like(boolean can_like)
	{
		this.can_like = can_like;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeByte((byte) (can_like ? 1 : 0));
		dest.writeInt(like_count);
		dest.writeByte((byte) (user_likes ? 1 : 0));
	}

	public static final Parcelable.Creator<LikeInfo>	CREATOR	= new Parcelable.Creator<LikeInfo>() {
																public LikeInfo createFromParcel(Parcel in)
																{
																	return new LikeInfo(in);
																}

																public LikeInfo[] newArray(int size)
																{
																	return new LikeInfo[size];
																}
															};

	private LikeInfo(Parcel in)
	{
		can_like = in.readByte() == 1;
		like_count = in.readInt();
		user_likes = in.readByte() == 1;
		
	}
}
