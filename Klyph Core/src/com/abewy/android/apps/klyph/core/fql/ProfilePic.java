package com.abewy.android.apps.klyph.core.fql;

import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class ProfilePic extends GraphObject
{
	private int height;
	private String id;
	private boolean is_silhouette;
	private int real_height;
	private int real_width;
	private String url;
	private int width;

	public ProfilePic()
	{

	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public boolean getIs_silhouette()
	{
		return is_silhouette;
	}

	public void setIs_silhouette(boolean is_silhouette)
	{
		this.is_silhouette = is_silhouette;
	}

	public int getReal_height()
	{
		return real_height;
	}

	public void setReal_height(int real_height)
	{
		this.real_height = real_height;
	}

	public int getReal_width()
	{
		return real_width;
	}

	public void setReal_width(int real_width)
	{
		this.real_width = real_width;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}
}
