package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.core.fql.serializer.VideoDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class AlbumVideosAllRequest extends KlyphQuery
{
	@Override
	public String getQuery(String id, String offset)
	{
		String query = "SELECT album_id, created_time, description, embed_html, format, length, link, owner, src, src_hq, thumbnail_link, title, updated_time, vid";
		query += " FROM video WHERE owner = \"" + id + "\"";
		query += " ORDER BY created_time DESC LIMIT 1000";
		
		return query;
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray result)
	{
		VideoDeserializer deserializer = new VideoDeserializer();
		ArrayList<GraphObject> videos = (ArrayList<GraphObject>) deserializer.deserializeArray(result);

		setHasMoreData(videos.size() >= 50);
		
		return videos;
	}
}
