package com.abewy.android.apps.klyph.facebook.request;



public class UserTimelineFeedRequest extends UserTimelineRequest
{
	@Override
	protected String getWhereCondition(String id)
	{
		return " source_id = " + id + " " +
				"AND is_hidden = 0 " +
				"AND strlen(parent_post_id) = 0 " +
				"AND (strlen(type) > 0 OR actor_id <> " + id + ") " +
				"AND type <> 56 " +
				"ORDER BY created_time " +
				"DESC LIMIT 30";
	}
}
