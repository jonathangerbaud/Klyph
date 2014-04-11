package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.VideoDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class VideoRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT album_id, created_time, description, format, length, link, owner, src, src_hq, thumbnail_link, title, updated_time, vid";
		query1 += " FROM video WHERE vid = \"" + id + "\"";
		
		String query2 = "SELECT id, name FROM profile WHERE id IN (SELECT owner FROM #query1)";
		
		String query3 = "SELECT aid, name FROM album WHERE object_id IN (SELECT album_id FROM #query1 )";
		
		// Get profile pics
		String query4 = "SELECT id, url FROM square_profile_pic "
				+ "WHERE id IN (SELECT owner FROM #query1) "
				+ "AND size = "
				+ Klyph.getStandardImageSizeForRequest();
					
		return multiQuery(query1, query2, query3, query4);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray video = result[0];
		JSONArray owner = result[1];
		JSONArray album = result[2];
		JSONArray pics = result[3];
		
		assocData(video, owner, "owner", "id", "owner_name", "name");
		assocData(video, album, "album_object_id", "object_id", "album_name", "name");
		assocData(video, pics, "owner", "id", "owner_pic", "url");
		
		VideoDeserializer deserializer = new VideoDeserializer();
		ArrayList<GraphObject> list = (ArrayList<GraphObject>) deserializer.deserializeArray(video);

		setHasMoreData(false);
		
		return list;
	}
}
