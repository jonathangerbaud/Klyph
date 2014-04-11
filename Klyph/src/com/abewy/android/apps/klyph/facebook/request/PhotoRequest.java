package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.PhotoDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class PhotoRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT object_id, aid, pid, owner, src_small, src_small_width, src_small_height, src_big, src_big_width, src_big_height, src, src_width, src_height, link, caption, caption_tags, created, modified, album_object_id, place_id, images, like_info, comment_info, can_delete, target_id, target_type";
		query1 += " FROM photo WHERE pid = \"" + id + "\" OR object_id = \"" + id + "\"";

		// Get source/target users and pages
		String query2 = "SELECT id, name, type from profile " 
				+ "WHERE id IN (SELECT owner FROM #query1)"
				+ "OR id IN (SELECT target_id FROM #query1)";

		// Album
		String query3 = "SELECT aid, name FROM album " 
				+ "WHERE object_id IN (SELECT album_object_id FROM #query1)";
		
		// Get profile pics
		String query4 = "SELECT id, url FROM square_profile_pic "
				+ "WHERE id IN (SELECT owner FROM #query1) "
				+ "AND size = "
				+ Klyph.getStandardImageSizeForRequest();

		// Get places
		String query5 = "SELECT page_id, name FROM place "
				+ "WHERE page_id IN (SELECT place_id FROM #query1)";

		return multiQuery(query1, query2, query3, query4, query5);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray photo = result[0];
		JSONArray profiles = result[1];
		JSONArray album = result[2];
		JSONArray pics = result[3];
		JSONArray places = result[4];

		assocData2(photo, profiles, "owner", "id", "owner_name", "name", "owner_type", "type");
		assocData2(photo, profiles, "owner", "id", "target_name", "name", "target_type", "type");
		assocData(photo, album, "album_object_id", "object_id", "album_name", "name");
		assocData(photo, pics, "owner", "id", "owner_pic", "url");
		assocData(photo, places, "place", "page_id", "place_name", "name");

		PhotoDeserializer deserializer = new PhotoDeserializer();
		ArrayList<GraphObject> list = (ArrayList<GraphObject>) deserializer.deserializeArray(photo);

		setHasMoreData(false);

		return list;
	}
}
