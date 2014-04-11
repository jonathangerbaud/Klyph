package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.CommentDeserializer;
import com.abewy.android.apps.klyph.core.fql.serializer.StreamDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class StreamRequest2 extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT app_id, attachment, can_comment, can_like, can_remove, comment_count, fromid, id, is_private, likes, object_id, parent_id, post_fbid, post_id, post_id_cursor, text, text_tags, time, user_likes FROM comment " +
						"WHERE parent_id = 0 AND post_id = \"" + id + "\"";
		
		if (getOffset(offset, null) != null)
			query1 += " AND post_id_cursor > \"" + offset + "\"";
						
		query1 += "";

		String query2 = "SELECT app_id, attachment, can_comment, can_like, can_remove, comment_count, fromid, id, is_private, likes, object_id, parent_id, post_fbid, post_id, text, text_tags, time, user_likes FROM comment " +
				"WHERE parent_id IN (SELECT id FROM #query1)"
				+ " ORDER BY time ASC LIMIT 50";

		
		String query3 = "SELECT id, name, type from profile WHERE id IN (SELECT fromid FROM #query1) OR id IN (SELECT fromid FROM #query2)";

		String query4 = "SELECT id, url from square_profile_pic WHERE (id IN (SELECT fromid FROM #query1) or id IN (SELECT fromid FROM #query2)) AND size = "
						+ Klyph.getStandardImageSizeForRequest();
		
		if (offset == null)
		{
			String query5 = "SELECT post_id, parent_post_id, source_id, actor_id, target_id, created_time, message, message_tags, attachment, description, description_tags, type, privacy, is_hidden, place, permalink, comment_info, like_info, action_links, tagged_ids, app_data, app_id"
				+ " FROM stream WHERE post_id = \"" + id + "\"";
			
			//Get sub newsfeed
			String query6 = "SELECT post_id, source_id, actor_id, target_id, app_id, created_time, message, message_tags, attachment, description, ";
			query6 += "description_tags, type, privacy, parent_post_id, place, permalink, comment_info, like_info, action_links, tagged_ids, ";
			query6 += "app_data FROM stream WHERE post_id IN (SELECT parent_post_id FROM #query5 WHERE type = 257 OR type = 245)";
			
			// Get liked links
			String query7 = "SELECT caption, comment_info, created_time, image_urls, like_info, link_id, owner, owner_comment, picture, summary, title, url, via_id "
					+ "FROM link WHERE link_id IN "
					+ "(SELECT substr(post_id, strpos(post_id, \"_\") + 1, strlen(post_id)) FROM #query5)";

			// Get liked photos
			String query8 = "SELECT caption, caption_tags, comment_info, created, images, like_info, modified, object_id, owner, page_story_id, pid, place_id, "
					+ "src, src_big, src_big_height, src_big_width, src_small, src_small_height, src_small_width, src_width, target_id, target_type "
					+ "FROM photo WHERE object_id IN "
					+ "(SELECT substr(post_id, strpos(post_id, \"_\") + 1, strlen(post_id)) from #query5)";

			// Get Liked videos
			String query9 = "SELECT created_time, description, format, length, link, owner, src, src_hq, thumbnail_link, title, updated_time, vid"
					+ " FROM video WHERE vid IN "
					+ "(SELECT substr(post_id, strpos(post_id, \"_\") + 1, strlen(post_id)) from #query5)"
					+ "OR vid IN (SELECT attachment.fb_object_id FROM #query5 "
					+ "WHERE attachment.fb_object_type = \"video\") ";
			
			// Get shared status
			String query10 = "SELECT message, place_id, source, status_id, time, uid"
							+ " FROM status WHERE status_id IN "
							+ "(SELECT substr(attachment.href, strpos(attachment.href, \"posts/\") + 6, strlen(attachment.href)) FROM #query5 WHERE strlen(attachment.href) > 0)";

			// Get events
			String query11 = "SELECT eid, name, description, start_time, pic_big, pic_cover "
					+ "FROM event WHERE eid IN "
					+ "(SELECT substr(post_id, strpos(post_id, \"_\") + 1, strlen(post_id)) FROM #query5)";

			// Get source/target users and pages
			String query12 = "SELECT id, name, type from profile "
					+ "WHERE id IN (SELECT actor_id FROM #query5) "
					+ "OR id IN (SELECT target_id FROM #query5) "
					+ "OR id IN (SELECT tagged_ids FROM #query5) "
					+ "OR id IN (SELECT actor_id FROM #query6) "
					+ "OR id IN (SELECT target_id FROM #query6) "
					+ "OR id IN (SELECT tagged_ids FROM #query6) "
					+ "OR id IN (SELECT owner FROM #query7) "
					+ "OR id IN (SELECT via_id FROM #query7) "
					+ "OR id IN (SELECT owner FROM #query8)"
					+ "OR id IN (SELECT target_id FROM #query8)"
					+ "OR id IN (SELECT owner FROM #query9)"
					+ "OR id IN (SELECT uid FROM #query10)";

			// Get liked pages
			String query13 = "SELECT page_id, name, about, pic_cover FROM page "
					+ "WHERE page_id = 0";//IN (SELECT description_tags.id FROM #query1)";

			// Get profile pics
			String query14 = "SELECT id, url FROM square_profile_pic "
					+ "WHERE (id IN (SELECT actor_id FROM #query5) "
					+ "OR id IN (SELECT actor_id FROM #query6) "
					+ "OR id IN (SELECT owner FROM #query7) "
					+ "OR id IN (SELECT owner FROM #query8) "
					+ "OR id IN (SELECT owner FROM #query9) "
					+ "OR id IN (SELECT uid FROM #query10) "
					+ "OR id IN (SELECT page_id FROM #query13)) " 
					+ "AND size = "
					+ Klyph.getStandardImageSizeForRequest();

			// Get places
			String query15 = "SELECT page_id, name FROM place "
					+ "WHERE page_id IN (SELECT place FROM #query5)"
					+ "OR page_id IN (SELECT place_id FROM #query8)"
					+ "OR page_id IN (SELECT place_id FROM #query10)";
			
			// Get Apps
			String query16 = "SELECT app_id, app_name, app_type, appcenter_icon_url, category, company_name, description, display_name, icon_url, link, logo_url, subcategory" +
					" FROM application" +
					" WHERE app_id IN (SELECT app_id FROM #query5)";

			return multiQuery(query1, query2, query3, query4, query5, query6, query7, query8, query9, query10, query11, query12, query13, query14, query15, query16);
		}
		Log.d("StreamRequest", query1);
		return multiQuery(query1, query2, query3, query4);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray data = result[0];
		JSONArray data2 = result[1];
		JSONArray data_ids = result[2];
		JSONArray urls = result[3];
		
		Log.d("StreamRequest", "Comments size " + data.length());
		Log.d("StreamRequest", "subComments size " + data2.length());
		
		assocData2(data, data_ids, "fromid", "id", "from_name", "name", "from_type", "type");
		assocData(data, urls, "fromid", "id", "from_pic", "url");
		assocData2(data2, data_ids, "fromid", "id", "from_name", "name", "from_type", "type");
		assocData(data2, urls, "fromid", "id", "from_pic", "url");
		
		JSONArray finalData = new JSONArray();
		for (int i = 0; i < data.length(); i++)
		{
			JSONObject topObject = data.optJSONObject(i);
			
			final String topId = topObject.optString("id");
			
			finalData.put(topObject);
			
			for (int j = 0; j < data2.length(); j++)
			{
				JSONObject subObject = data2.optJSONObject(j);
				
				final String parentId = subObject.optString("parent_id");
				
				if (topId.equals(parentId))
				{
					finalData.put(subObject);
				}
			}
		}
		
		CommentDeserializer cDeserializer = new CommentDeserializer();
		ArrayList<GraphObject> comments = (ArrayList<GraphObject>) cDeserializer.deserializeArray(finalData);
		
		if (result.length > 4)
		{
			JSONArray sData = result[4];
			JSONArray sData2 = result[5];
			JSONArray links = result[6];
			JSONArray photos = result[7];
			JSONArray videos = result[8];
			JSONArray status = result[9];
			JSONArray events = result[10];
			JSONArray profiles = result[11];
			JSONArray pages = result[12];
			JSONArray pics = result[13];
			JSONArray places = result[14];
			JSONArray apps = result[15];

			assocData2(links, profiles, "owner", "id", "owner_name", "name",
					"owner_type", "type");
			assocData2(links, profiles, "via_id", "id", "via_name", "name",
					"via_type", "type");
			assocData(links, pics, "owner", "id", "owner_pic", "url");
			
			assocData2(photos, profiles, "owner", "id", "owner_name", "name",
					"owner_type", "type");
			assocData2(photos, profiles, "target", "id", "target_name", "name",
					"target_type", "type");
			assocData(photos, pics, "owner", "id", "owner_pic", "url");
			assocData(photos, places, "place", "page_id", "place_name", "name");
			
			assocData2(videos, profiles, "owner", "id", "owner_name", "name",
					"owner_type", "type");
			assocData(videos, pics, "owner", "id", "owner_pic", "url");
			
			assocData2(status, profiles, "uid", "id", "uid_name", "name",
					"uid_type", "type");
			assocData(status, pics, "uid", "id", "uid_pic", "url");
			assocData(status, places, "place_id", "page_id", "place_name", "name");

			assocData(pages, pics, "page_id", "id", "pic", "url");
			
			assocData2(sData, profiles, "actor_id", "id", "actor_name", "name",
					"actor_type", "type");
			assocData2(sData, profiles, "target_id", "id", "target_name", "name",
					"target_type", "type");
			
			assocData2(sData2, profiles, "actor_id", "id", "actor_name", "name",
					"actor_type", "type");
			assocData2(sData2, profiles, "target_id", "id", "target_name", "name",
					"target_type", "type");
			
			assocData(sData, pics, "actor_id", "id", "actor_pic", "url");
			assocData(sData, places, "place", "page_id", "place_name", "name");
			assocData3(sData, profiles, "tagged_ids", "id", "tagged_tags");
			
			assocData(sData2, pics, "actor_id", "id", "actor_pic", "url");
			assocData(sData2, places, "place", "page_id", "place_name", "name");
			assocData3(sData2, profiles, "tagged_ids", "id", "tagged_tags");
			
			assocStreamToEvent(sData, events);
			assocStreamToLikedPages(sData, pages);
			assocStreamToObjectBySubPostId(sData, links, "link_id", "link");
			assocStreamToObjectBySubPostId(sData, photos, "object_id", "photo");
			assocStreamToObjectBySubPostId(sData, videos, "vid", "video");
			assocStreamToStatus(sData, status);
			assocStreamToObjectById(data, apps, "app_id", "app_id", "application");
			assocStreamToObjectById(data, apps, "parent_post_id", "post_id", "parent_stream");
			
			StreamDeserializer sDeserializer = new StreamDeserializer();
			List<GraphObject> streams = sDeserializer.deserializeArray(sData);
			
			if (streams.size() > 0)
			{
				comments.add(0, streams.get(0));
			}
		}
		
		setHasMoreData(comments.size() >= 0);

		return comments;
	}
}
