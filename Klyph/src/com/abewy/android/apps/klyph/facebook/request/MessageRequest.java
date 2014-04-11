package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.Collections;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.MessageDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class MessageRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT message_id, thread_id, author_id, body, created_time, attachment, source, viewer_id FROM message WHERE thread_id = \"" + id + "\"";
		
		if (offset != null && offset.length() > 0)
			query1 += " AND created_time < " + offset;
		
		query1 += " ORDER BY created_time DESC LIMIT 20";
		
		String query2 = "SELECT id, name, type FROM profile WHERE id IN (SELECT author_id FROM #query1)";

		String query3 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT id FROM #query2) AND size = "
				+ Klyph.getStandardImageSizeForRequest() * 2;

		return multiQuery(query1, query2, query3);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray result[])
	{
		JSONArray data = result[0];
		JSONArray profiles = result[1];
		JSONArray urls = result[2];
		
		assocData(data, profiles, "author_id", "id", "author_name", "name");
		assocData(data, urls, "author_id", "id", "author_pic", "url");
		
		MessageDeserializer deserializer = new MessageDeserializer();
		ArrayList<GraphObject> messages = (ArrayList<GraphObject>) deserializer.deserializeArray(data);
		Collections.reverse(messages);

		setHasMoreData(messages.size() >= 20);
		
		return messages;
	}
}
