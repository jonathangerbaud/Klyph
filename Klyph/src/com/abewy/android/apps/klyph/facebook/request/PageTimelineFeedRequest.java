package com.abewy.android.apps.klyph.facebook.request;

public class PageTimelineFeedRequest extends PageTimelineRequest
{
	protected String getWhereCondition(String id)
	{
		return " source_id = " + id + " AND actor_id = " + id
				+ " AND is_hidden = 0 AND strlen(parent_post_id) = 0 ORDER BY created_time DESC LIMIT 30";
	}
}
