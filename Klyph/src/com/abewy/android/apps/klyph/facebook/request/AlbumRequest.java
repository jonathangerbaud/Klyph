package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.core.fql.serializer.AlbumDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class AlbumRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT aid, backdated_time, can_backdate, can_upload, comment_info, cover_object_id, cover_pid, created, description, edit_link, like_info, link, location, modified, modified_major, name, object_id, owner, owner_cursor, photo_count, place_id, type, video_count, visible";
		query1 += " FROM album WHERE object_id = \"" + id + "\" OR aid = \"" + id + "\"";
		
		String query2 = "SELECT id, name FROM profile WHERE id IN (SELECT owner FROM #query1)";
		
		String query3 = "SELECT pid, images from photo where pid in (select cover_pid from #query1)";
		
		return multiQuery(query1, query2, query3);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray album = result[0];
		JSONArray owner = result[1];
		JSONArray images = result[2];
		
		assocData(album, owner, "owner", "id", "owner_name", "name");
		assocData(album, images, "cover_pid", "pid", "cover_images", "images");
		
		AlbumDeserializer deserializer = new AlbumDeserializer();
		ArrayList<GraphObject> list = (ArrayList<GraphObject>) deserializer.deserializeArray(album);

		setHasMoreData(false);
		
		return list;
	}
}
