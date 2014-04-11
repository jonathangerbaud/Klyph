package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.core.fql.Group;
import com.abewy.android.apps.klyph.core.fql.serializer.GroupDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class GroupTimelineRequest extends
		com.abewy.android.apps.klyph.facebook.request.base.ElementTimelineRequest
{
	@Override
	protected String getWhereCondition(String id)
	{
		return " source_id = "
				+ id
				+ " AND is_hidden = 0 AND strlen(parent_post_id) = 0 ORDER BY created_time DESC LIMIT 30";
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		ArrayList<GraphObject> streams = super.handleResult(result);

		setHasMoreData(streams.size() > 0);

		return streams;
	}
}
