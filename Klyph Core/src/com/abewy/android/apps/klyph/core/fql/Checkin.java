package com.abewy.android.apps.klyph.core.fql;

import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Checkin extends GraphObject implements Parcelable
{
	private String	checkin_id;
	private String	author_uid;
	private String	page_id;

	public Checkin()
	{

	}

	public String getCheckin_id()
	{
		return checkin_id;
	}

	public void setCheckin_id(String checkin_id)
	{
		this.checkin_id = checkin_id;
	}

	public String getAuthor_uid()
	{
		return author_uid;
	}

	public void setAuthor_uid(String author_uid)
	{
		this.author_uid = author_uid;
	}

	public String getPage_id()
	{
		return page_id;
	}

	public void setPage_id(String page_id)
	{
		this.page_id = page_id;
	}

	public boolean exist()
	{
		return checkin_id != null && checkin_id.length() > 0;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(author_uid);
		dest.writeString(checkin_id);
		dest.writeString(page_id);
	}

	public static final Parcelable.Creator<Checkin>	CREATOR	= new Parcelable.Creator<Checkin>() {
																public Checkin createFromParcel(Parcel in)
																{
																	return new Checkin(in);
																}

																public Checkin[] newArray(int size)
																{
																	return new Checkin[size];
																}
															};

	private Checkin(Parcel in)
	{
		author_uid = in.readString();
		checkin_id = in.readString();
		page_id = in.readString();
	}
}
