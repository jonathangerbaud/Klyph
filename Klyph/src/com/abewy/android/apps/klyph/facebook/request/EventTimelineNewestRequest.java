package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.StreamDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class EventTimelineNewestRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT post_id, source_id, actor_id, target_id, created_time, message, message_tags, ";
		query1 += "attachment, description, description_tags, type, privacy, is_hidden, place, permalink, comment_info, ";
		query1 += "like_info, action_links, tagged_ids, app_data FROM stream WHERE ";
		
		if (offset != null && offset.length() > 0)
			query1 += "created_time > " + offset + " AND ";
		
		query1 += " source_id = " + id + " AND is_hidden=0 LIMIT 25";
		
		String query2 = "SELECT id, name, type from profile WHERE id IN (SELECT actor_id FROM #query1) OR id IN (SELECT target_id FROM #query1)";
		
		String query3 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT actor_id FROM #query1) AND size = "
				+ Klyph.getStandardImageSizeForRequest();

		return multiQuery(query1, query2, query3);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray data = result[0];
		JSONArray data_ids = result[1];
		JSONArray urls = result[2];

		assocData2(data, data_ids, "actor_id", "id", "actor_name", "name", "actor_type", "type");
		assocData(data, urls, "actor_id", "id", "actor_pic", "url");
		assocData2(data, data_ids, "target_id", "id", "target_name", "name", "target_type", "type");

		StreamDeserializer deserializer = new StreamDeserializer();
		ArrayList<GraphObject> streams = (ArrayList<GraphObject>) deserializer.deserializeArray(data);
		
		setHasMoreData(streams.size() >= 0);
		
		return streams;
	}
}
