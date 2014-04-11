package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.FriendRequestDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class FriendsRequestNotificationRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT is_hidden, message, time, uid_from, uid_to, unread FROM friend_request WHERE uid_to = me() AND is_hidden = 0 AND unread = 1 "; 
		
		if (offset != null)
			query1 += "AND time > " + offset;

		String query2 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT uid_from FROM #query1) AND size = " + Klyph.getStandardImageSizeForRequest();
		
		String query3 = "SELECT id, name from profile WHERE id IN (SELECT uid_from FROM #query1)";
		
		return multiQuery(query1, query2, query3);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray data = result[0];
		JSONArray urls = result[1];
		JSONArray names = result[2];
		
		for (int i = 0; i < urls.length(); i++)
		{
			JSONObject jsonObject = urls.optJSONObject(i);
			try
			{
				jsonObject.putOpt("id", String.valueOf(jsonObject.opt("id")));
			}
			catch (JSONException e)
			{
				//e.printStackTrace();
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
				//e.printStackTrace();
			}
		}
		
		assocData(data, urls, "uid_from", "id", "uid_from_pic", "url");
		assocData(data, names, "uid_from", "id", "uid_from_name", "name");
		
		FriendRequestDeserializer deserializer = new FriendRequestDeserializer();
		ArrayList<GraphObject> friendRequests = (ArrayList<GraphObject>) deserializer.deserializeArray(data);
		
		return friendRequests;
	}
}
