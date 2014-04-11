package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.TextView;

public class EventDataHolder
{
	private final TextView	date;
	private final TextView	place;
	private final TextView	description;

	public EventDataHolder(TextView date, TextView place, TextView description)
	{
		this.date = date;
		this.place = place;
		this.description = description;
	}

	public TextView getDate()
	{
		return date;
	}

	public TextView getPlace()
	{
		return place;
	}

	public TextView getDescription()
	{
		return description;
	}
}