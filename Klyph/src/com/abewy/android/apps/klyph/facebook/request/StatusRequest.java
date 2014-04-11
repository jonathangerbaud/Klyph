package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.StatusDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class StatusRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		// Get shared status
		String query1 = "SELECT message, place_id, source, status_id, time, uid" + " FROM status WHERE status_id = " + id;

		// Get source/target users and pages
		String query2 = "SELECT id, name, type from profile " + "WHERE id IN (SELECT uid FROM #query1)";

		// Get profile pics
		String query3 = "SELECT id, url FROM square_profile_pic " + "WHERE id IN (SELECT uid FROM #query1) " + "AND size = "
						+ Klyph.getStandardImageSizeForRequest();

		// Get places
		String query4 = "SELECT page_id, name FROM place " + "WHERE page_id IN (SELECT place_id FROM #query1)";

		return multiQuery(query1, query2, query3, query4);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray status = result[0];
		JSONArray profiles = result[1];
		JSONArray pics = result[2];
		JSONArray places = result[3];

		assocData2(status, profiles, "uid", "id", "uid_name", "name", "uid_type", "type");
		assocData(status, pics, "uid", "id", "uid_pic", "url");
		assocData(status, places, "place_id", "page_id", "place_name", "name");

		StatusDeserializer sDeserializer = new StatusDeserializer();
		ArrayList<GraphObject> statut = (ArrayList<GraphObject>) sDeserializer.deserializeArray(status);

		setHasMoreData(false);

		return statut;
	}
}