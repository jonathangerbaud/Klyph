package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.fql.serializer.FriendRequestDeserializer;
import com.abewy.android.apps.klyph.core.fql.serializer.UserDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UserProfileRequest extends KlyphQuery
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
		String query2 = "SELECT uid, relationship, name, birthday FROM family WHERE profile_id IN (SELECT uid FROM #query1)";
		String query3 = "SELECT id, url from square_profile_pic WHERE id = " + id + " AND size = "
						+ (int) (KlyphDevice.getDeviceDensity() * 96);
		String query4 = "SELECT uid1, uid2 FROM friend WHERE uid1 = me() AND uid2 = " + id;
		String query5 = "SELECT is_hidden, message, time, uid_from, uid_to, unread FROM friend_request WHERE (uid_to = me() and uid_from = " + id + ") OR (uid_to = " + id + " and uid_from = me())";
		
		return multiQuery(query1, query2, query3, query4, query5);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray userData = result[0];
		JSONArray familyData = result[1];
		JSONArray urls = result[2];
		JSONArray isFriend = result[3];
		JSONArray friendRequest = result[4];
		
		assocData(userData, urls, "uid", "id", "pic", "url");
		
		JSONObject user = userData.optJSONObject(0);
		ArrayList<GraphObject> data = null;
		
		if (user != null)
		{
			try
			{
				user.put("family", familyData);
			}
			catch (JSONException e)
			{
				
			}
			
			if (isFriend != null && isFriend.length() == 1)
			{
				try
				{
					user.putOpt("isFriend", true);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			
			UserDeserializer deserializer = new UserDeserializer();
			data = (ArrayList<GraphObject>) deserializer.deserializeArray(userData);
			
			FriendRequestDeserializer frDeserializer = new FriendRequestDeserializer();
			List<GraphObject> fr = frDeserializer.deserializeArray(friendRequest);
			if (fr.size() > 0)
			{
				data.add(fr.get(0));
			}
		}
		else
		{
			data = new ArrayList<GraphObject>();
		}

		setHasMoreData(false);
		
		return data;
	}
}
