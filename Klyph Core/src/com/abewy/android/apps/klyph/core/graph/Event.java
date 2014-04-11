package com.abewy.android.apps.klyph.core.graph;

import java.util.ArrayList;

public class Event
{
	private String id;
	private Object owner;
	private String name;
	private String description;
	private String start_time;
	private String end_time;
	private String location;
	private Object venue;
	private String privacy;
	private String updated_time;
	private String picture;
	
	private ArrayList<Post> feed;
	private ArrayList<Object> noreply;
	private ArrayList<Object> invited;
	private ArrayList<Object> attending;
	private ArrayList<Object> maybe;
	private ArrayList<Object> declined;
	private ArrayList<Video> videos;
	
	public Event()
	{

	}
}
