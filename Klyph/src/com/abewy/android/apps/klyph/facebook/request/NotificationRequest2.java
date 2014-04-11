package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.FriendRequestDeserializer;
import com.abewy.android.apps.klyph.core.fql.serializer.NotificationDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class NotificationRequest2 extends KlyphQuery
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
		query1 += " WHERE recipient_id=me() AND is_hidden = 0 ";

		query1 += getOffsetQuery(offset);

		query1 += " ORDER BY updated_time DESC LIMIT 1000";

		String query2 = "SELECT is_hidden, message, time, uid_from, uid_to, unread FROM friend_request WHERE uid_to = me() AND is_hidden = 0 LIMIT 1000";

		String query3 = "SELECT id, url from square_profile_pic WHERE (id IN (SELECT sender_id FROM #query1) OR id IN (SELECT uid_from FROM #query2)) AND size = "
						+ Klyph.getStandardImageSizeForNotification();

		String query4 = "SELECT id, name from profile WHERE id IN (SELECT sender_id FROM #query1) OR id IN (SELECT uid_from FROM #query2)";

		// friend
		String query5 = "SELECT uid, name FROM user" + " WHERE uid IN (SELECT object_id FROM #query1)";

		// event
		String query6 = "SELECT eid, name FROM event" + " WHERE eid IN (SELECT object_id FROM #query1)";

		// page
		String query7 = "SELECT page_id, name FROM page" + " WHERE page_id IN (SELECT object_id FROM #query1)";

		// group
		String query8 = "SELECT gid, name FROM group" + " WHERE gid IN (SELECT object_id FROM #query1)";

		// Stream
		String query9 = "SELECT post_id, object_id FROM comment WHERE object_id IN (SELECT object_id FROM #query1)";

		return multiQuery(query1, query2, query3, query4, query5, query6, query7, query8, query9);
	}

	protected String getOffsetQuery(String offset)
	{
		if (offset != null && offset.length() > 0)
			return " AND updated_time < " + offset;

		return "";
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray notifications = result[0];
		JSONArray friendRequests = result[1];
		JSONArray urls = result[2];
		JSONArray names = result[3];
		JSONArray friends = result[4];
		JSONArray events = result[5];
		JSONArray pages = result[6];
		JSONArray groups = result[7];
		JSONArray comments = result[8];

		assocData(notifications, urls, "sender_id", "id", "sender_pic", "url");
		assocData(notifications, names, "sender_id", "id", "sender_name", "name");
		assocStreamToObjectById(notifications, friends, "object_id", "uid", "friend");
		assocStreamToObjectById(notifications, events, "object_id", "eid", "event");
		assocStreamToObjectById(notifications, pages, "object_id", "page_id", "page");
		assocStreamToObjectById(notifications, groups, "object_id", "gid", "group");
		assocStreamToObjectById(notifications, comments, "object_id", "object_id", "comment");

		for (int i = 0; i < urls.length(); i++)
		{
			JSONObject jsonObject = urls.optJSONObject(i);
			try
			{
				jsonObject.putOpt("id", String.valueOf(jsonObject.opt("id")));
			}
			catch (JSONException e)
			{
				// e.printStackTrace();
			}
		}

		for (int i = 0; i < names.length(); i++)
		{
			JSONObject jsonObject = names.optJSONObject(i);
			try
			{
				jsonObject.putOpt("id", String.valueOf(jsonObject.opt("id")));
			}
			catch (JSONException e)
			{
				// e.printStackTrace();
			}
		}

		assocData(friendRequests, urls, "uid_from", "id", "uid_from_pic", "url");
		assocData(friendRequests, names, "uid_from", "id", "uid_from_name", "name");

		NotificationDeserializer nDeserializer = new NotificationDeserializer();
		ArrayList<GraphObject> n = (ArrayList<GraphObject>) nDeserializer.deserializeArray(notifications);

		FriendRequestDeserializer fDeserializer = new FriendRequestDeserializer();
		ArrayList<GraphObject> f = (ArrayList<GraphObject>) fDeserializer.deserializeArray(friendRequests);

		ArrayList<GraphObject> list = new ArrayList<GraphObject>();
		list.addAll(n);
		list.addAll(f);

		setHasMoreData(false);

		return list;
	}
}
