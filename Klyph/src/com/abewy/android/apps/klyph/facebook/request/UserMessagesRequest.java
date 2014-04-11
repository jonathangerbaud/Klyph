package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.Collections;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.UnifiedMessageDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UserMessagesRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT action_id, attachment_map, attachments, body, containing_message_id, coordinates, "
				+ "forwarded_message_id, forwarded_messages, html_body, is_forwarded, is_user_generated, log_message, " 
				+ "message_id, object_sender, offline_threading_id, recipients, sender, share_map, shares, subject, tags," 
				+ " thread_id, timestamp, unread"
				+ " FROM unified_message"
				+ " WHERE thread_id IN (SELECT thread_id FROM unified_thread "
										+ "WHERE single_recipient = " + id
										+ " AND folder = \"inbox\")";
		
		if (offset != null && offset.length() > 0)
			query1 += " AND timestamp < " + offset;
		
		query1 += " ORDER BY timestamp DESC LIMIT 20";

		String query2 = "SELECT id, name, type FROM profile WHERE id IN (SELECT recipients FROM #query1)";

		String query3 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT id FROM #query2) AND size = "
				+ Klyph.getStandardImageSizeForRequest() * 2;

		return multiQuery(query1, query2, query3);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray data = result[0];
		JSONArray recipients = result[1];
		JSONArray user_pics = result[2];

		assocData(recipients, user_pics, "id", "id", "pic", "url");
		assocData3(data, recipients, "recipients", "id", "recipients_friends");

		UnifiedMessageDeserializer deserializer = new UnifiedMessageDeserializer();
		ArrayList<GraphObject> mts = (ArrayList<GraphObject>) deserializer.deserializeArray(data);
		Collections.reverse(mts);

		setHasMoreData(mts.size() >= 15);

		return mts;
	}
}
