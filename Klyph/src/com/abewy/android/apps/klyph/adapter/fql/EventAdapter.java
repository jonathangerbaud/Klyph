package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.EventHolder;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.util.DateUtil;
import com.abewy.android.apps.klyph.widget.coverImage.EventCoverImageView;

public class EventAdapter extends KlyphAdapter
{
	public EventAdapter()
	{
		super();
	}

	protected int getLayout()
	{
		// return R.layout.item_picture_primary_secondary_text;
		//return R.layout.item_grid_picture_primary_secondary_text;
		return R.layout.item_event;
	}

	protected void attachHolder(View view)
	{
		ImageView eventCover = (ImageView) view.findViewById(R.id.event_cover);
		TextView eventName = (TextView) view.findViewById(R.id.event_name);
		TextView eventDate = (TextView) view.findViewById(R.id.event_date);
		TextView eventPlace = (TextView) view.findViewById(R.id.event_place);

		setHolder(view, new EventHolder(eventCover, eventName, eventDate,  eventPlace));
	}

	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);

		EventHolder holder = (EventHolder) getHolder(view);

		// holder.getPicture().setImageDrawable(null);

		Event event = (Event) data;

		holder.getEventName().setText(event.getName());

		holder.getEventDate().setText(DateUtil.getFormattedDateTimeWithYear(DateUtil.getUnixTimeFromDate(event.getStart_time())));

		String place = "";
		if (event.getVenue() != null)
		{
			place = event.getVenue().getName();

			if (place != null && place.length() > 0)
			{
				
			}
		}
		else if (event.getLocation() != null && event.getLocation().length() > 0)
		{
			place = event.getLocation();
		}
		
		if (place != null && place.length() > 0)
		{
			holder.getEventPlace().setText(place);
			holder.getEventPlace().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getEventPlace().setVisibility(View.GONE);
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
	}
}
