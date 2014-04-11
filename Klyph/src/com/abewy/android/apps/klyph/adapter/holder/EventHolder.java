package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class EventHolder
{
	private ImageView	eventCover;
	private TextView	eventName;
	private TextView	eventDate;
	private TextView	eventPlace;

	public EventHolder(ImageView eventCover, TextView eventName, TextView eventDate, TextView eventPlace)
	{
		this.eventCover = eventCover;
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.eventPlace = eventPlace;
	}
	
	public ImageView getEventCover()
	{
		return eventCover;
	}

	public TextView getEventName()
	{
		return eventName;
	}

	public TextView getEventDate()
	{
		return eventDate;
	}

	public TextView getEventPlace()
	{
		return eventPlace;
	}
}
