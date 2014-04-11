package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.core.fql.serializer.GroupDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class GroupsRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT administrator, bookmark_order, gid, positions, uid, unread " +
				"FROM group_member " +
				"WHERE uid = me() ORDER BY bookmark_order " +
				"LIMIT " + getOffset(offset, "0") + ", 50";
		
		String query2 = "SELECT gid, name, description, pic_cover, pic_big " +
				"FROM group " +
				"WHERE gid IN (SELECT gid FROM #query1)";

		return multiQuery(query1, query2);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray group_member = result[0];
		JSONArray groups_data = result[1];
		
		assocData(groups_data, group_member, "gid", "gid", "unread", "unread");
		assocData(groups_data, group_member, "gid", "gid", "administrator", "administrator");
		assocData(groups_data, group_member, "gid", "gid", "positions", "positions");
		
		GroupDeserializer deserializer = new GroupDeserializer();
		ArrayList<GraphObject> groups = (ArrayList<GraphObject>) deserializer.deserializeArray(groups_data);
		
		setHasMoreData(groups.size() >= 50);
		
		return groups;
	}
}
