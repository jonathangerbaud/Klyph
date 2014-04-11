package com.abewy.android.apps.klyph.core.fql;

import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Location extends GraphObject implements Parcelable
{
	private String	street;
	private String	city;
	private String	state;
	private String	country;
	private String	zip;
	private String	latitude;
	private String	longitude;
	private String	id;
	private String	name;
	private String	located_in;

	public Location()
	{
		
	}
	
	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getZip()
	{
		return zip;
	}

	public void setZip(String zip)
	{
		this.zip = zip;
	}

	public String getLatitude()
	{
		return latitude;
	}

	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}

	public String getLongitude()
	{
		return longitude;
	}

	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
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

	public String getLocated_in()
	{
		return located_in;
	}

	public void setLocated_in(String located_in)
	{
		this.located_in = located_in;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(city);
		dest.writeString(country);
		dest.writeString(id);
		dest.writeString(latitude);
		dest.writeString(located_in);
		dest.writeString(longitude);
		dest.writeString(name);
		dest.writeString(state);
		dest.writeString(street);
		dest.writeString(zip);
	}

	public static final Parcelable.Creator<Location>	CREATOR	= new Parcelable.Creator<Location>() {
																	public Location createFromParcel(Parcel in)
																	{
																		return new Location(in);
																	}

																	public Location[] newArray(int size)
																	{
																		return new Location[size];
																	}
																};

	private Location(Parcel in)
	{
		city = in.readString();
		country = in.readString();
		id = in.readString();
		latitude = in.readString();
		located_in = in.readString();
		longitude = in.readString();
		name = in.readString();
		state = in.readString();
		street = in.readString();
		zip = in.readString();
	}
}
