package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.UserDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UserRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT uid, name, about_me, activities, affiliations, allowed_restrictions, birthday, birthday_date, books, can_message, can_post, contact_email, currency, current_address, current_location, devices, education, email, email_hashes, first_name, friend_count, friend_request_count, hometown_location, inspirational_people, install_type, interests, is_app_user, is_blocked, languages, last_name, likes_count, locale, meeting_for, meeting_sex, middle_name, movies, music, mutual_friend_count, name_format, notes_count, online_presence, pic, pic_big, pic_big_with_logo, pic_cover, pic_small, pic_small_with_logo, pic_square, pic_square_with_logo, pic_with_logo, political, profile_blurb, profile_update_time, profile_url, proxied_email, quotes, relationship_status, religion, search_tokens, security_settings, sex, significant_other_id, sort_first_name, sort_last_name, sports, status, subscriber_count, third_party_id, timezone, tv, verified, video_upload_limits, wall_count, website, work FROM user WHERE uid = " + id;
		String query2 = "SELECT id, url from square_profile_pic WHERE id = " + id + " AND size = "
						+ Klyph.getStandardImageSizeForRequest();
		
		return multiQuery(query1, query2);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray userData = result[0];
		JSONArray urls = result[1];
		
		assocData(userData, urls, "uid", "id", "pic", "url");
		
		JSONObject user = userData.optJSONObject(0);
		ArrayList<GraphObject> data = null;
		
		if (user != null)
		{
			UserDeserializer deserializer = new UserDeserializer();
			data = (ArrayList<GraphObject>) deserializer.deserializeArray(userData);
		}
		else
		{
			data = new ArrayList<GraphObject>();
		}

		setHasMoreData(false);
		
		return data;
	}
}
