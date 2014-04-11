package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.fql.Event.Cover;
import com.abewy.android.apps.klyph.core.fql.Event.Venue;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class EventDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Event event = new Event();
		
		deserializePrimitives(event, data);
		
		event.setPic_cover((Cover) new CoverDeserializer().deserializeObject(getJsonObject(data, "pic_cover")));
		event.setVenue((Venue) new VenueDeserializer().deserializeObject(getJsonObject(data, "venue")));
		
		return event;
	}
	

	private static class CoverDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Cover cover = new Cover();
			
			deserializePrimitives(cover, data);
			
			return cover;
		}
	}
	
	private static class VenueDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Venue venue = new Venue();
			
			deserializePrimitives(venue, data);
			
			return venue;
		}
	}
}
