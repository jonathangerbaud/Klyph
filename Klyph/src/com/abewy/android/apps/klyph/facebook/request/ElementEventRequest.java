package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.EventDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class ElementEventRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		long now = new Date().getTime() / 1000;
		
		String query1 = "SELECT eid, name, pic_small, pic_big, pic_square, pic, host, description, creator, update_time, venue, location, start_time, end_time, privacy, hide_guest_list, can_invite_friends, all_members_count, attending_count, unsure_count, declined_count, not_replied_count FROM event WHERE eid IN (SELECT eid from event_member where uid = "
						+ id + ") AND start_time > " + now + " ORDER BY start_time ASC";
		query1 += " LIMIT " + getOffset(offset, "0") + ", 25";

		String query2 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT eid FROM #query1) AND size = "
						+ Klyph.getStandardImageSizeForRequest();

		return multiQuery(query1, query2);
	}
	
	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray data = result[0];
		JSONArray urls = result[1];
		
		assocData(data, urls, "eid", "id", "pic", "url");
		
		EventDeserializer deserializer = new EventDeserializer();
		ArrayList<GraphObject> events = (ArrayList<GraphObject>) deserializer.deserializeArray(data);
		
		setHasMoreData(events.size() >= 25);
		
		return events;
	}
}
