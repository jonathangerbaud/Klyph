package com.abewy.android.apps.klyph.facebook.request;

import com.abewy.android.apps.klyph.facebook.request.base.ElementTimelineRequest;

public class UserTimelineRequest extends ElementTimelineRequest
{
	@Override
	protected String getWhereCondition(String id)
	{
		return " source_id = " + id + " and actor_id <> " + id + " AND is_hidden = 0 AND strlen(parent_post_id) = 0 ORDER BY created_time DESC LIMIT 30";
	}
	
	/*@Override
	public List<String> getAdditionalQueries(String id, String offset)
	{
		if (offset == null || offset.length() == 0)
		{
			String query1 = "SELECT uid, name, about_me, birthday, birthday_date, "
					+ "can_message, can_post, contact_email, current_address, current_location, education, "
					+ "email, first_name, friend_count, hometown_location, "
					+ "is_app_user, is_blocked, last_name, likes_count, meeting_for, "
					+ "meeting_sex, middle_name, mutual_friend_count, online_presence, pic, "
					+ "pic_big, pic_big_with_logo, pic_cover, pic_small, pic_small_with_logo, pic_square, pic_square_with_logo, "
					+ "pic_with_logo, political, relationship_status, "
					+ "religion, sex, status, "
					+ "subscriber_count, work FROM user WHERE uid = "
					+ id;

			String query2 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT uid FROM #query13) AND size = "
					+ (Klyph.getStandardImageSizeForRequest() * 3);

			List<String> queries = new ArrayList<String>();
			queries.add(query1);
			queries.add(query2);
			return queries;
		}

		return new ArrayList<String>();
	}*/

	/*@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		ArrayList<GraphObject> streams = super.handleResult(result);

		if (result.length > 12)
		{
			if (result[12].length() > 0)
			{
				JSONArray user = result[12];
				JSONArray url = result[13];

				assocData(user, url, "uid", "id", "pic", "url");

				UserDeserializer uDeserializer = new UserDeserializer();
				streams.add(0, (User) uDeserializer.deserializeArray(user).get(0));
			}
		}

		setHasMoreData(streams.size() > 0);

		return streams;
	}*/
}
