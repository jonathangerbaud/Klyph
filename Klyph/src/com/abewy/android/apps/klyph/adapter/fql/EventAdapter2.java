package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.EventHolder2;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.util.TextViewUtil;
import com.abewy.android.apps.klyph.widget.coverImage.EventCoverImageView;

public class EventAdapter2 extends KlyphAdapter
{
	public EventAdapter2()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_event2;
	}
	
	@Override
	public boolean  isEnabled(GraphObject object)
	{
		return false;
	}
	
	@Override
	protected void attachHolder(View view)
	{
		ImageView eventCover = (ImageView) view.findViewById(R.id.event_cover);
		ImageView eventPicture = (ImageView) view.findViewById(R.id.event_picture);
		TextView eventName = (TextView) view.findViewById(R.id.event_name);
		TextView eventHost = (TextView) view.findViewById(R.id.event_host);
		TextView eventPrivacy = (TextView) view.findViewById(R.id.event_privacy);
		view.setTag(new EventHolder2(eventCover, eventPicture, eventName, eventHost, eventPrivacy));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);

		EventHolder2 holder = (EventHolder2) view.getTag();

		Event event = (Event) data;
		
		holder.getEventName().setText(event.getName());

		String by = String.format(getContext(view).getResources().getString(R.string.event_by), event.getHost());
		holder.getEventHost().setText(by);

		TextViewUtil.setElementClickable(getContext(view), holder.getEventHost(), event.getHost(), event.getCreator(), "user", true);

		if (event.getPrivacy() != null)
		{
			if (event.getPrivacy().equals("SECRET"))
				holder.getEventPrivacy().setText(getContext(view).getResources().getString(R.string.event_secret));
			else if (event.getPrivacy().equals("FRIENDS"))
				holder.getEventPrivacy().setText(getContext(view).getResources().getString(R.string.event_friends));
			else if (event.getPrivacy().equals("OPEN"))
				holder.getEventPrivacy().setText(getContext(view).getResources().getString(R.string.event_public));
			else if (event.getPrivacy().equals("CLOSED"))
				holder.getEventPrivacy().setText(getContext(view).getResources().getString(R.string.event_closed));
		}
		
		int placeHolder = AttrUtil.getResourceId(getContext(holder.getEventCover()), R.attr.squarePlaceHolderIcon);

		if (event.getPic_cover() != null && event.getPic_cover().getSource() != null && event.getPic_cover().getSource().length() > 0)
		{
			EventCoverImageView eventCoverImageView = (EventCoverImageView) holder.getEventCover();
			eventCoverImageView.setOffset(event.getPic_cover().getOffset_y());
			
			loadImage(holder.getEventCover(), event.getPic_cover().getSource(), placeHolder, true);
		}
		else
		{
			loadImage(holder.getEventCover(), event.getPic_big(), placeHolder, true);
		}
		
		holder.getEventCover().setVisibility(View.VISIBLE);
		holder.getEventPicture().setVisibility(View.GONE);
	}

	protected boolean isCompatible(View view)
	{
		return view.getTag() instanceof EventHolder2;
	}
}
