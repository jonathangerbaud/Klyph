package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.core.fql.serializer.PhotoDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class AlbumPhotosAllRequest extends KlyphQuery
{
	@Override
	public String getQuery(String id, String offset)
	{
		String query = "SELECT aid, aid_cursor, album_object_id, album_object_id_cursor, backdated_time, backdated_time_granularity, can_backdate, can_delete, can_tag, caption, caption_tags, comment_info, created, images, like_info, link, modified, object_id, offline_id, owner, owner_cursor, page_story_id, pid, place_id, src, src_big, src_big_height, src_big_width, src_height, src_small, src_small_height, src_small_width, src_width, target_id, target_type";
		query += " FROM photo WHERE album_object_id = \"" + id + "\"";
		query += " LIMIT 1000";

		return query;
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray result)
	{
		PhotoDeserializer deserializer = new PhotoDeserializer();
		ArrayList<GraphObject> photos = (ArrayList<GraphObject>) deserializer.deserializeArray(result);
		
		setHasMoreData(false);

		return photos;
	}
}
