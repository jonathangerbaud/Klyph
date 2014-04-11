package com.abewy.klyph.items;

import android.view.View.OnClickListener;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class EventResponseItem extends ShadowItem
{
	private Event					event;
	private String					name;
	private OnClickListener			attendButtonListener;
	private OnClickListener			unsureButtonListener;
	private OnClickListener			declineButtonListener;

	public EventResponseItem(Event event, String name)
	{
		this.event = event;
		this.name = name;
	}

	public int getItemViewType()
	{
		return GraphObject.EVENT_RESPONSE;
	}
	
	public String getName()
	{
		return name;
	}

	public Event getEvent()
	{
		return event;
	}

	public OnClickListener getAttendButtonListener()
	{
		return attendButtonListener;
	}

	public void setAttendButtonListener(OnClickListener attendButtonListener)
	{
		this.attendButtonListener = attendButtonListener;
	}

	public OnClickListener getUnsureButtonListener()
	{
		return unsureButtonListener;
	}

	public void setUnsureButtonListener(OnClickListener unsureButtonListener)
	{
		this.unsureButtonListener = unsureButtonListener;
	}

	public OnClickListener getDeclineButtonListener()
	{
		return declineButtonListener;
	}

	public void setDeclineButtonListener(OnClickListener declineButtonListener)
	{
		this.declineButtonListener = declineButtonListener;
	}
}
