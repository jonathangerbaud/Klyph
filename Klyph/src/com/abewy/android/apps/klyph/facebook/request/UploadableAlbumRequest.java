package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.core.fql.serializer.AlbumDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UploadableAlbumRequest extends KlyphQuery
{
	@Override
	public String getQuery(String id, String offset)
	{
		String query = "SELECT object_id, name ";
		query += " FROM album WHERE can_upload AND owner = " + id;
		query += " ORDER BY name DESC LIMIT 9999";

		return query;
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray result)
	{
		AlbumDeserializer deserializer = new AlbumDeserializer();

		ArrayList<GraphObject> albums = (ArrayList<GraphObject>) deserializer.deserializeArray(result);
		
		return albums;
	}
}
