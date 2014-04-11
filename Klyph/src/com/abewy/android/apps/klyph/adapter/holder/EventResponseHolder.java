package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.Button;

public class EventResponseHolder
{
	private final Button	eventGoingButton;
	private final Button	eventUnsureButton;
	private final Button	eventDeclinedButton;

	public EventResponseHolder(Button eventGoingButton, Button eventUnsureButton, Button eventDeclinedButton)
	{
		this.eventGoingButton = eventGoingButton;
		this.eventUnsureButton = eventUnsureButton;
		this.eventDeclinedButton = eventDeclinedButton;
	}

	public Button getEventGoingButton()
	{
		return eventGoingButton;
	}

	public Button getEventUnsureButton()
	{
		return eventUnsureButton;
	}

	public Button getEventDeclinedButton()
	{
		return eventDeclinedButton;
	}
}
