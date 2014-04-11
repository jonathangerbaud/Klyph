package com.abewy.android.apps.klyph.core.fql;

import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Privacy extends GraphObject implements Parcelable
{
	private String	description;
	private String	value;
	private String	friends;
	private String	networks;
	private String	allow;
	private String	deny;

	public Privacy()
	{

	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getFriends()
	{
		return friends;
	}

	public void setFriends(String friends)
	{
		this.friends = friends;
	}

	public String getNetworks()
	{
		return networks;
	}

	public void setNetworks(String networks)
	{
		this.networks = networks;
	}

	public String getAllow()
	{
		return allow;
	}

	public void setAllow(String allow)
	{
		this.allow = allow;
	}

	public String getDeny()
	{
		return deny;
	}

	public void setDeny(String deny)
	{
		this.deny = deny;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(allow);
		dest.writeString(deny);
		dest.writeString(description);
		dest.writeString(friends);
		dest.writeString(networks);
		dest.writeString(value);
	}

	public static final Parcelable.Creator<Privacy>	CREATOR	= new Parcelable.Creator<Privacy>() {
																public Privacy createFromParcel(Parcel in)
																{
																	return new Privacy(in);
																}

																public Privacy[] newArray(int size)
																{
																	return new Privacy[size];
																}
															};

	private Privacy(Parcel in)
	{
		allow = in.readString();
		deny = in.readString();
		description= in.readString();
		friends = in.readString();
		networks = in.readString();
		value = in.readString();
	}
}
