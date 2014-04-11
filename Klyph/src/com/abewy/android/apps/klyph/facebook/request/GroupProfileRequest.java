package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.serializer.GroupDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class GroupProfileRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT creator, description, email, gid, name, office, pic_cover, pic_big, privacy, recent_news, website"
				+ " FROM group WHERE gid = " + id;

		return multiQuery(query1);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray groupData = result[0];

		JSONObject page = groupData.optJSONObject(0);
		ArrayList<GraphObject> data = null;

		if (page != null)
		{
			GroupDeserializer deserializer = new GroupDeserializer();
			data = (ArrayList<GraphObject>) deserializer.deserializeArray(groupData);
		}
		else
		{
			data = new ArrayList<GraphObject>();
		}

		setHasMoreData(false);

		return data;
	}
}
