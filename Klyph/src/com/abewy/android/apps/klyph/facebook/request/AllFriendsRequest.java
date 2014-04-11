package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.FriendDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class AllFriendsRequest extends KlyphQuery
{	
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT uid, name FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 = me()) order by name LIMIT 0, 10000";

		String query2 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT uid FROM #query1) AND size = " + Klyph.getStandardImageSizeForRequest();
		
		return multiQuery(query1, query2);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray result[])
	{
		JSONArray friends = result[0];
		JSONArray pics = result[1];
		
		assocData(friends, pics, "uid", "id", "pic", "url");
		
		FriendDeserializer deserializer = new FriendDeserializer();
		ArrayList<GraphObject> list = (ArrayList<GraphObject>) deserializer.deserializeArray(friends);
		
		setHasMoreData(false);
		
		return list;
	}
}
