package com.abewy.android.apps.klyph.core.graph;

import java.util.ArrayList;

public class Group
{
	private String id;
	private int version;
	private String icon;
	private Object owner;
	private String name;
	private String description;
	private String link;
	private String privacy;
	private String updated_time;
	
	private ArrayList<Post> feed;
	private ArrayList<Object> members;
	private String picture;
	private ArrayList<Object> docs;
	
	public Group()
	{

	}
}
