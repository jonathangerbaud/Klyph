package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.facebook.request.base.ElementTimelineRequest;

public class PageTimelineRequest extends ElementTimelineRequest
{
	protected String getWhereCondition(String id)
	{
		return " source_id = " + id + " AND actor_id <> " + id
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
