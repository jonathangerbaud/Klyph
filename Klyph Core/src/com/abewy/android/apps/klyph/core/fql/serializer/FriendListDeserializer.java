package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.FriendList;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class FriendListDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		FriendList friendList = new FriendList();
		
		deserializePrimitives(friendList, data);
		
		return friendList;
	}
}
