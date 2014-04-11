package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.core.fql.serializer.FriendListDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class FriendListsRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return false;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		return "SELECT count, flid, name, owner, owner_cursor, type " +
						"FROM friendlist WHERE owner = me() AND type = \"user_created\" " +
						"ORDER BY name LIMIT 100";
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray result)
	{
		FriendListDeserializer deserializer = new FriendListDeserializer();
		ArrayList<GraphObject> friendLists = (ArrayList<GraphObject>) deserializer.deserializeArray(result);
		
		setHasMoreData(false);
		
		return friendLists;
	}
}
