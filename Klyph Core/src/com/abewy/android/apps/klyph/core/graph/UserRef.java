package com.abewy.android.apps.klyph.core.graph;

public class UserRef extends GraphObject
{
	private String	id;
	private String	name;
	private Picture	picture;

	public UserRef()
	{

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

	public Picture getPicture()
	{
		return picture;
	}

	public void setPicture(Picture picture)
	{
		this.picture = picture;
	}
	
	public static class Picture extends GraphObject
	{
		private String url;
		private boolean  is_silhouette;
		
		public String getUrl()
		{
			return url;
		}
		public void setUrl(String url)
		{
			this.url = url;
		}
		public boolean  getIs_silhouette()
		{
			return is_silhouette;
		}
		public void setIs_silhouette(boolean  is_silouette)
		{
			this.is_silhouette = is_silouette;
		}
	}
}
