package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.fql.Event.EventResponse;
import com.abewy.android.apps.klyph.core.fql.serializer.EventDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.facebook.request.base.ElementTimelineRequest;

public class EventTimelineRequest extends ElementTimelineRequest
{
	@Override
	protected String getWhereCondition(String id)
	{
		return " source_id = " + id + " AND is_hidden=0 LIMIT 25";
	}
	
	@Override
	public List<String> getAdditionalQueries(String id, String offset)
	{
		if (offset == null || offset.length() == 0)
		{
			String query1 = "SELECT all_members_count, attending_count, can_invite_friends, creator, declined_count, description, eid, end_time, has_profile_pic, hide_guest_list, host, is_date_only, location, name, not_replied_count, pic, pic_big, pic_cover, pic_small, pic_square, privacy, start_time, timezone, unsure_count, update_time, venue, version FROM event WHERE eid = " + id;
			String query2 = "SELECT rsvp_status FROM event_member WHERE eid = " + id + "AND uid = me()";
			
			List<String> queries = new ArrayList<String>();
			queries.add(query1);
			queries.add(query2);
			return queries;
		}

		return new ArrayList<String>();
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		ArrayList<GraphObject> streams = super.handleResult(result);

		if (result.length > 12)
		{
			if (result[12].length() > 0)
			{
				JSONArray eventData = result[12];
				JSONArray rsvp_status = result[13];
				
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
				
				streams.add(0, event);
			}
		}

		setHasMoreData(streams.size() > 0);

		return streams;
	}
}
