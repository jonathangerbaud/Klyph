package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.fql.Event.EventResponse;
import com.abewy.android.apps.klyph.core.fql.serializer.EventDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class EventRequest extends KlyphQuery
{
	
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT all_members_count, attending_count, can_invite_friends, creator, declined_count, description, eid, end_time, has_profile_pic, hide_guest_list, host, is_date_only, location, name, not_replied_count, pic, pic_big, pic_cover, pic_small, pic_square, privacy, start_time, timezone, unsure_count, update_time, venue, version FROM event WHERE eid = " + id;
		String query2 = "SELECT rsvp_status FROM event_member WHERE eid = " + id + "AND uid = me()";
		
		return multiQuery(query1, query2);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray eventData = result[0];
		JSONArray rsvp_status = result[1];
		
		ArrayList<GraphObject> data = new ArrayList<GraphObject>();
		
		EventDeserializer deserializer = new EventDeserializer();
		Event event = (Event) deserializer.deserializeArray(eventData).get(0);
		
		try
		{
			JSONObject o = rsvp_status.getJSONObject(0);
			event.setUserResponse(o.getString("rsvp_status"));
		}
		catch (JSONException e)
		{
			event.setUserResponse(EventResponse.NOT_REPLIED.toString());
		}
		
		data.add(event);
		
		setHasMoreData(false);
		
		return data;
	}
}
