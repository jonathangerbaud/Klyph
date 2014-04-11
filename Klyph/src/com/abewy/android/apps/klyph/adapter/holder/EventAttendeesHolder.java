package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.TextView;

public class EventAttendeesHolder
{
	private final TextView	numAttendees;
	private final TextView	numGoing;
	private final TextView	numUnsure;
	private final TextView	numDeclined;

	public EventAttendeesHolder(TextView numAttendees, TextView numGoing, TextView numUnsure, TextView numDeclined)
	{
		this.numAttendees = numAttendees;
		this.numGoing = numGoing;
		this.numUnsure = numUnsure;
		this.numDeclined = numDeclined;
	}

	public TextView getNumAttendees()
	{
		return numAttendees;
	}
	
	public TextView getNumGoing()
	{
		return numGoing;
	}

	public TextView getNumUnsure()
	{
		return numUnsure;
	}

	public TextView getNumDeclined()
	{
		return numDeclined;
	}
}
