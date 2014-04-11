package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.EventAttendeesHolder;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.fragment.EventFragment.EventAttendees;

public class EventAttendeesAdapter extends KlyphAdapter
{
	public EventAttendeesAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_event_attendees;
	}

	@Override
	protected void attachHolder(View view)
	{
		view.setTag(new EventAttendeesHolder((TextView) view.findViewById(R.id.event_num_attendees), (TextView) view
				.findViewById(R.id.event_num_going), (TextView) view.findViewById(R.id.event_num_unsure), (TextView) view.findViewById(R.id.event_num_declined)));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		final EventAttendeesHolder holder = (EventAttendeesHolder) view.getTag();

		final EventAttendees eventAttendess = (EventAttendees) data;
		final Event event = eventAttendess.getEvent();
		
		holder.getNumAttendees().setText(String.valueOf(event.getAll_members_count()));
		holder.getNumGoing().setText(String.valueOf(event.getAttending_count()));
		holder.getNumUnsure().setText(String.valueOf(event.getUnsure_count()));
		holder.getNumDeclined().setText(String.valueOf(event.getDeclined_count()));

		
		if (eventAttendess.getInvitedListener() != null)
		{
			((ViewGroup) holder.getNumAttendees().getParent()).setOnClickListener(eventAttendess.getInvitedListener());
		}
		if (eventAttendess.getGoingListener() != null)
		{
			((ViewGroup) holder.getNumGoing().getParent()).setOnClickListener(eventAttendess.getGoingListener());
		}
		if (eventAttendess.getUnsureListener() != null)
		{
			((ViewGroup) holder.getNumUnsure().getParent()).setOnClickListener(eventAttendess.getUnsureListener());
		}
		if (eventAttendess.getDeclinedListener() != null)
		{
			((ViewGroup) holder.getNumDeclined().getParent()).setOnClickListener(eventAttendess.getDeclinedListener());
		}

	}
}
