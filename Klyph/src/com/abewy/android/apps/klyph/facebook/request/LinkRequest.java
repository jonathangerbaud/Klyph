package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.LinkDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class LinkRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		// Get liked links
		String query1 = "SELECT caption, comment_info, created_time, image_urls, like_info, link_id, owner, owner_comment, picture, summary, title, url, via_id "
						+ "FROM link WHERE link_id = " + id;

		// Get source/target users and pages
		String query2 = "SELECT id, name, type from profile " + "WHERE id IN (SELECT owner FROM #query1) " + "OR id IN (SELECT via_id FROM #query1) ";

		// Get profile pics
		String query3 = "SELECT id, url FROM square_profile_pic " + "WHERE id IN (SELECT owner FROM #query1) " + "AND size = "
						+ Klyph.getStandardImageSizeForRequest();

		return multiQuery(query1, query2, query3);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray links = result[0];
		JSONArray profiles = result[1];
		JSONArray pics = result[2];

		assocData2(links, profiles, "owner", "id", "owner_name", "name", "owner_type", "type");
		assocData2(links, profiles, "via_id", "id", "via_name", "name", "via_type", "type");
		assocData(links, pics, "owner", "id", "owner_pic", "url");

		LinkDeserializer sDeserializer = new LinkDeserializer();
		ArrayList<GraphObject> link = (ArrayList<GraphObject>) sDeserializer.deserializeArray(links);

		setHasMoreData(false);

		return link;
	}
}
