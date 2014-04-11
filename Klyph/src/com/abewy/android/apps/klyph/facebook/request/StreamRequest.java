package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.StreamDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class StreamRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{

		String query1 = "SELECT post_id, parent_post_id, source_id, actor_id, target_id, created_time, message, message_tags, attachment, description, description_tags, type, privacy, is_hidden, place, permalink, comment_info, like_info, action_links, tagged_ids, app_data, app_id"
						+ " FROM stream WHERE post_id = \"" + id + "\"";

		// Get sub newsfeed
		String query2 = "SELECT post_id, source_id, actor_id, target_id, app_id, created_time, message, message_tags, attachment, description, ";
		query2 += "description_tags, type, privacy, parent_post_id, place, permalink, comment_info, like_info, action_links, tagged_ids, ";
		query2 += "app_data FROM stream WHERE post_id IN (SELECT parent_post_id FROM #query1 WHERE type = 257 OR type = 245 AND strlen(parent_post_id) > 0)";

		// Get liked links
		String query3 = "SELECT caption, comment_info, created_time, image_urls, like_info, link_id, owner, owner_comment, picture, summary, title, url, via_id "
						+ "FROM link WHERE link_id IN " + "(SELECT substr(post_id, strpos(post_id, \"_\") + 1, strlen(post_id)) FROM #query1)";

		// Get liked photos
		String query4 = "SELECT caption, caption_tags, comment_info, created, images, like_info, modified, object_id, owner, page_story_id, pid, place_id, "
						+ "src, src_big, src_big_height, src_big_width, src_small, src_small_height, src_small_width, src_width, target_id, target_type "
						+ "FROM photo WHERE object_id IN " + "(SELECT substr(post_id, strpos(post_id, \"_\") + 1, strlen(post_id)) from #query1)";

		// Get Liked videos
		String query5 = "SELECT created_time, description, format, length, link, owner, src, src_hq, thumbnail_link, title, updated_time, vid"
						+ " FROM video WHERE vid IN " + "(SELECT substr(post_id, strpos(post_id, \"_\") + 1, strlen(post_id)) from #query1)"
						+ "OR vid IN (SELECT attachment.fb_object_id FROM #query1 " + "WHERE attachment.fb_object_type = \"video\" AND strlen(attachment.fb_object_id) > 0) ";

		// Get shared status
		String query6 = "SELECT comment_info, like_info, message, place_id, source, status_id, time, uid"
						+ " FROM status WHERE status_id IN (SELECT substr(post_id, strpos(post_id, \"_\") + 1, strlen(post_id)) FROM #query1 WHERE type = 257) OR status_id IN "
						+ "(SELECT substr(attachment.href, strpos(attachment.href, \"posts/\") + 6, strlen(attachment.href)) FROM #query1 WHERE strlen(attachment.href) > 0)";

		// Get events
		String query7 = "SELECT eid, name, description, start_time, pic_big, pic_cover " + "FROM event WHERE eid IN "
						+ "(SELECT substr(post_id, strpos(post_id, \"_\") + 1, strlen(post_id)) FROM #query1)";

		// Get source/target users and pages
		String query8 = "SELECT id, name, type from profile " + "WHERE id IN (SELECT actor_id FROM #query1 WHERE strlen(actor_id) > 0) "
						+ "OR id IN (SELECT target_id FROM #query1 WHERE strlen(target_id) > 0) " + "OR id IN (SELECT tagged_ids FROM #query1) "
						+ "OR id IN (SELECT actor_id FROM #query2 WHERE strlen(actor_id) > 0) " + "OR id IN (SELECT target_id FROM #query2 WHERE strlen(target_id) > 0) "
						+ "OR id IN (SELECT tagged_ids FROM #query2) " + "OR id IN (SELECT owner FROM #query3 WHERE strlen(owner) > 0) "
						+ "OR id IN (SELECT via_id FROM #query3 WHERE strlen(via_id) > 0) " + "OR id IN (SELECT owner FROM #query4 WHERE strlen(owner) > 0)"
						+ "OR id IN (SELECT target_id FROM #query4 WHERE strlen(target_id) > 0)" + "OR id IN (SELECT owner FROM #query5 WHERE strlen(owner) > 0)" + "OR id IN (SELECT uid FROM #query6)";

		// Get liked pages
		String query9 = "SELECT page_id, name, about, pic_cover FROM page " + "WHERE page_id = 0";// IN (SELECT description_tags.id FROM #query1)";

		// Get profile pics
		String query10 = "SELECT id, url FROM square_profile_pic " + "WHERE (id IN (SELECT actor_id FROM #query1 WHERE strlen(actor_id) > 0) "
							+ "OR id IN (SELECT actor_id FROM #query2 WHERE strlen(actor_id) > 0) " + "OR id IN (SELECT owner FROM #query3 WHERE strlen(owner) > 0) "
							+ "OR id IN (SELECT owner FROM #query4 WHERE strlen(owner) > 0) " + "OR id IN (SELECT owner FROM #query5 WHERE strlen(owner) > 0) "
							+ "OR id IN (SELECT uid FROM #query6 WHERE strlen(uid) > 0) " + "OR id IN (SELECT page_id FROM #query9 WHERE strlen(page_id) > 0)) " + "AND size = "
							+ Klyph.getStandardImageSizeForRequest();

		// Get places
		String query11 = "SELECT page_id, name FROM place " + "WHERE page_id IN (SELECT place FROM #query1 WHERE strlen(place) > 0)"
							+ "OR page_id IN (SELECT place_id FROM #query4 WHERE strlen(place_id) > 0)" + "OR page_id IN (SELECT place_id FROM #query6 WHERE strlen(place_id) > 0)";

		// Get Apps
		String query12 = "SELECT app_id, app_name, app_type, appcenter_icon_url, category, company_name, description, display_name, icon_url, link, logo_url, subcategory"
							+ " FROM application" + " WHERE app_id IN (SELECT app_id FROM #query1 WHERE strlen(app_id) > 0)";

		return multiQuery(query1, query2, query3, query4, query5, query6, query7, query8, query9, query10, query11, query12);

	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray sData = result[0];
		JSONArray sData2 = result[1];
		JSONArray links = result[2];
		JSONArray photos = result[3];
		JSONArray videos = result[4];
		JSONArray status = result[5];
		JSONArray events = result[6];
		JSONArray profiles = result[7];
		JSONArray pages = result[8];
		JSONArray pics = result[9];
		JSONArray places = result[10];
		JSONArray apps = result[11];

		assocData2(links, profiles, "owner", "id", "owner_name", "name", "owner_type", "type");
		assocData2(links, profiles, "via_id", "id", "via_name", "name", "via_type", "type");
		assocData(links, pics, "owner", "id", "owner_pic", "url");

		assocData2(photos, profiles, "owner", "id", "owner_name", "name", "owner_type", "type");
		assocData2(photos, profiles, "target", "id", "target_name", "name", "target_type", "type");
		assocData(photos, pics, "owner", "id", "owner_pic", "url");
		assocData(photos, places, "place", "page_id", "place_name", "name");

		assocData2(videos, profiles, "owner", "id", "owner_name", "name", "owner_type", "type");
		assocData(videos, pics, "owner", "id", "owner_pic", "url");

		assocData2(status, profiles, "uid", "id", "uid_name", "name", "uid_type", "type");
		assocData(status, pics, "uid", "id", "uid_pic", "url");
		assocData(status, places, "place_id", "page_id", "place_name", "name");

		assocData(pages, pics, "page_id", "id", "pic", "url");

		assocData2(sData, profiles, "actor_id", "id", "actor_name", "name", "actor_type", "type");
		assocData2(sData, profiles, "target_id", "id", "target_name", "name", "target_type", "type");

		assocData2(sData2, profiles, "actor_id", "id", "actor_name", "name", "actor_type", "type");
		assocData2(sData2, profiles, "target_id", "id", "target_name", "name", "target_type", "type");

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
		assocStreamToObjectById(sData, apps, "app_id", "app_id", "application");
		assocStreamToObjectById(sData, sData2, "parent_post_id", "post_id", "parent_stream");

		StreamDeserializer sDeserializer = new StreamDeserializer();
		List<GraphObject> streams = sDeserializer.deserializeArray(sData);

		ArrayList<GraphObject> list = new ArrayList<GraphObject>();
		
		if (streams.size() > 0)
			list.add(streams.get(0));
		
		setHasMoreData(false);

		return list;
	}
}
