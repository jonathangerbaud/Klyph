package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.core.fql.serializer.ItemDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class ProfileUrlRequest extends KlyphQuery
{
	@Override
	public String getQuery(String id, String offset)
	{
		return "select size, real_size, url from square_profile_pic where id = " + id + " and size = " + offset;
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray result)
	{
		ItemDeserializer deserializer = new ItemDeserializer();
		ArrayList<GraphObject> items = (ArrayList<GraphObject>) deserializer.deserializeArray(result);
		
		return items;
	}
}
