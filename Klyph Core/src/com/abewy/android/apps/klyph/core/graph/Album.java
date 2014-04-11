package com.abewy.android.apps.klyph.core.graph;

import java.util.ArrayList;

public class Album
{
	private String id;
	private Object from;
	private String name;
	private String description;
	private String location;
	private String link;
	private String cover_photo;
	private String privacy;
	private String count;
	private String type;
	private String created_time;
	private String updated_time;
	private boolean can_upload;
	
	private ArrayList<Photo> photos;
	private ArrayList<Object> likes;
	private ArrayList<Object> comments;
	private String picture;
	
	public Album()
	{

	}
}
