package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.Button;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.EventResponseHolder;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.fragment.EventFragment.EventResponseItem;

public class EventResponseAdapter extends KlyphAdapter
{
	public EventResponseAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_event_response;
	}

	@Override
	protected void attachHolder(View view)
	{
		view.setTag(new EventResponseHolder((Button) view
				.findViewById(R.id.event_going_button), (Button) view.findViewById(R.id.event_unsure_button),
				(Button) view.findViewById(R.id.event_declined_button)));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		final EventResponseHolder holder = (EventResponseHolder) view.getTag();

		EventResponseItem eventResponseItem = (EventResponseItem) data;
		final Event event = eventResponseItem.getEvent();

		setButtonDrawable(holder, event);
		
		if (eventResponseItem.getAttendButtonListener() != null)
		{
			holder.getEventGoingButton().setOnClickListener(eventResponseItem.getAttendButtonListener());
		}
		
		if (eventResponseItem.getUnsureButtonListener() != null)
		{
			holder.getEventUnsureButton().setOnClickListener(eventResponseItem.getUnsureButtonListener());
		}
		
		if (eventResponseItem.getDeclineButtonListener() != null)
		{
			holder.getEventDeclinedButton().setOnClickListener(eventResponseItem.getDeclineButtonListener());
		}
	}

	private void setButtonDrawable(EventResponseHolder holder, Event event)
	{
		boolean isGoing = event.isUserAttendingEvent();
		boolean isUnsure = event.isUserUnsureEvent();
		boolean isDeclined = event.isUserDeclinedEvent();

		setButtonDrawable(holder.getEventGoingButton(), isGoing ? R.attr.eventGoingSelectedIcon : R.attr.eventGoingIcon);
		setButtonDrawable(holder.getEventUnsureButton(), isUnsure ? R.attr.eventUnsureSelectedIcon
				: R.attr.eventUnsureIcon);
		setButtonDrawable(holder.getEventDeclinedButton(), isDeclined ? R.attr.eventDeclinedSelectedIcon
				: R.attr.eventDeclinedIcon);
	}

	private void setButtonDrawable(Button button, int drawableAttr)
	{
		button.setCompoundDrawablesWithIntrinsicBounds(AttrUtil.getDrawable(getContext(button), drawableAttr), null, null,
				null);
	}
}
