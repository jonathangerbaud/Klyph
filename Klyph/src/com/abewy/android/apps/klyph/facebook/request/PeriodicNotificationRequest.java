package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import android.util.Log;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.NotificationDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class PeriodicNotificationRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT notification_id, sender_id, recipient_id, created_time, updated_time, title_html, title_text, body_html, body_text, href, app_id, is_unread, is_hidden, object_id, object_type, icon_url FROM notification";
		query1 += " WHERE recipient_id=me() AND is_hidden = 0 AND is_unread = 1";

		if (offset != null && offset.length() > 0)
			query1 += " AND updated_time > " + offset;

		query1 += " ORDER BY created_time DESC";
		
		String query2 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT sender_id FROM #query1 WHERE strlen(sender_id) > 0) AND size = "
						+ Klyph.getStandardImageSizeForNotification();

		String query3 = "SELECT id, name from profile WHERE id IN (SELECT sender_id FROM #query1 WHERE strlen(sender_id) > 0)";

		// friend
		String query4 = "SELECT uid, name FROM user" + " WHERE uid IN (SELECT object_id FROM #query1 WHERE strlen(object_id) > 0)";

		// event
		String query5 = "SELECT eid, name FROM event" + " WHERE eid IN (SELECT object_id FROM #query1 WHERE strlen(object_id) > 0)";

		// page
		String query6 = "SELECT page_id, name FROM page" + " WHERE page_id IN (SELECT object_id FROM #query1 WHERE strlen(object_id) > 0)";

		// group
		String query7 = "SELECT gid, name FROM group" + " WHERE gid IN (SELECT object_id FROM #query1 WHERE strlen(object_id) > 0)";

		// Stream
		String query8 = "SELECT post_id, object_id FROM comment WHERE object_id IN (SELECT object_id FROM #query1 WHERE strlen(object_id) > 0)";

		return multiQuery(query1, query2, query3, query4, query5, query6, query7, query8);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray data = result[0];
		JSONArray urls = result[1];
		JSONArray names = result[2];
		JSONArray friends = result[3];
		JSONArray events = result[4];
		JSONArray pages = result[5];
		JSONArray groups = result[6];
		JSONArray comments = result[7];
		Log.d("NotificationRequest", "size " + data.length());
		assocData(data, urls, "sender_id", "id", "sender_pic", "url");
		assocData(data, names, "sender_id", "id", "sender_name", "name");
		assocStreamToObjectById(data, friends, "object_id", "uid", "friend");
		assocStreamToObjectById(data, events, "object_id", "eid", "event");
		assocStreamToObjectById(data, pages, "object_id", "page_id", "page");
		assocStreamToObjectById(data, groups, "object_id", "gid", "group");
		assocStreamToObjectById(data, comments, "object_id", "object_id", "comment");

		NotificationDeserializer deserializer = new NotificationDeserializer();
		ArrayList<GraphObject> notifications = (ArrayList<GraphObject>) deserializer.deserializeArray(data);

		return notifications;
	}
}
