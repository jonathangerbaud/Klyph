package com.abewy.android.apps.klyph.core.graph;

public class Place extends GraphObject
{
	private String id;
	private String name;
	private Location location;
	
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

	public Location getLocation()
	{
		return location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

	public static class Location extends GraphObject
	{
		private String latitude;
		private String longitude;
		private String country;
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
		public String getCountry()
		{
			return country;
		}
		public void setCountry(String country)
		{
			this.country = country;
		}
	}
}
