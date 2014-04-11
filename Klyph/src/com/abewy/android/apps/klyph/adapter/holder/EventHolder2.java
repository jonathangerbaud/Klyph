package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class EventHolder2
{
	private ImageView	eventCover;
	private ImageView	eventPicture;
	private TextView	eventName;
	private TextView	eventHost;
	private TextView	eventPrivacy;

	public EventHolder2(ImageView eventCover, ImageView eventPicture, TextView eventName, TextView eventHost, TextView eventPrivacy)
	{
		this.eventCover = eventCover;
		this.eventPicture = eventPicture;
		this.eventName = eventName;
		this.eventHost = eventHost;
		this.eventPrivacy = eventPrivacy;
	}
	
	public ImageView getEventCover()
	{
		return eventCover;
	}

	public ImageView getEventPicture()
	{
		return eventPicture;
	}

	public TextView getEventName()
	{
		return eventName;
	}

	public TextView getEventHost()
	{
		return eventHost;
	}

	public TextView getEventPrivacy()
	{
		return eventPrivacy;
	}
}
