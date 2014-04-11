package com.abewy.android.apps.klyph.core.fql;

import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Tag extends GraphObject implements Parcelable
{
	private String				id;
	private String				name;
	private int					offset;
	private int					length;
	private String				type;

	public Tag()
	{

	}

	@Override
	public int getItemViewType()
	{
		return GraphObject.TAG;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getOffset()
	{
		return offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(id);
		dest.writeInt(length);
		dest.writeString(name);
		dest.writeInt(offset);
		dest.writeString(type);
	}

	public static final Parcelable.Creator<Tag>	CREATOR	= new Parcelable.Creator<Tag>() {
																public Tag createFromParcel(Parcel in)
																{
																	return new Tag(in);
																}

																public Tag[] newArray(int size)
																{
																	return new Tag[size];
																}
															};

	private Tag(Parcel in)
	{
		id = in.readString();
		length = in.readInt();
		name = in.readString();
		offset = in.readInt();
		type = in.readString();
	}
}
