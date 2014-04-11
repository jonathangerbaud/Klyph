package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.MessageThreadDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class ThreadRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT thread_id, subject, recipients, updated_time, parent_message_id, parent_thread_id, message_count, snippet, snippet_author, object_id, viewer_id "
						+ "FROM thread " + "WHERE folder_id = 0 ";

		if (getOffset(offset, null) != null)
			query1 += "AND updated_time < " + getOffset(offset, "0");

		query1 += "LIMIT 0, 30";

		String query2 = "SELECT uid, name, first_name FROM user WHERE uid IN (SELECT recipients FROM #query1)";

		String query3 = "SELECT id, name FROM profile WHERE id IN (SELECT recipients FROM #query1)";

		String query4 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT id FROM #query3) AND size = "
						+ Klyph.getStandardImageSizeForRequest() * 2;
		
		return multiQuery(query1, query2, query3, query4);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray data = result[0];
		JSONArray users = result[1];
		JSONArray recipients = result[2];
		JSONArray user_pics = result[3];

		assocData(recipients, users, "id", "uid", "first_name", "first_name");
		assocData(recipients, user_pics, "id", "id", "pic", "url");
		assocData(recipients, recipients, "id", "id", "uid", "id");
		assocData3(data, recipients, "recipients", "id", "recipients_friends");

		MessageThreadDeserializer deserializer = new MessageThreadDeserializer();
		ArrayList<GraphObject> mts = (ArrayList<GraphObject>) deserializer.deserializeArray(data);

		setHasMoreData(mts.size() >= 15);

		return mts;
	}
}
