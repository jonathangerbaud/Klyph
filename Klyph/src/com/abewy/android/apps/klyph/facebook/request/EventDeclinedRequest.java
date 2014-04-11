package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.FriendDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class EventDeclinedRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT uid, name FROM user WHERE uid IN (SELECT uid FROM event_member WHERE eid = " + id;
		query1 += " AND rsvp_status  = \"declined\"";
		query1 += ") order by name LIMIT " + getOffset(offset, "0") + ", 50";

		String query2 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT uid FROM #query1) AND size = "
						+ Klyph.getStandardImageSizeForRequest();

		return multiQuery(query1, query2);
	}
	
	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray data = result[0];
		JSONArray urls = result[1];
		
		assocData(data, urls, "uid", "id", "pic", "url");
		
		FriendDeserializer deserializer = new FriendDeserializer();
		ArrayList<GraphObject> friends = (ArrayList<GraphObject>) deserializer.deserializeArray(data);
		
		setHasMoreData(friends.size() >= 50);
		
		return friends;
	}
}
