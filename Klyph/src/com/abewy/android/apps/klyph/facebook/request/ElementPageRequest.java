package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.core.fql.serializer.PageDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class ElementPageRequest extends KlyphQuery
{
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		String query1 = "SELECT page_id,name,username,description,page_url,categories,is_community_page,pic_small,pic_big,pic_square,pic,pic_large,pic_cover,unread_notif_count,new_like_count,fan_count,global_brand_parent_page_id,type,website,has_added_app,general_info,can_post,checkins,is_published";
		query1 += " FROM page WHERE page_id IN (";
		query1 += "SELECT page_id from page_fan where uid = " + id + " LIMIT " + getOffset(offset, "0") + ", 25)";

		String query2 = "SELECT id, url from square_profile_pic WHERE id IN (SELECT page_id FROM #query1) AND size = " + Klyph.getStandardImageSizeForRequest();
		
		return multiQuery(query1, query2);
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray[] result)
	{
		JSONArray data = result[0];
		JSONArray urls = result[1];
		
		assocData(data, urls, "page_id", "id", "pic", "url");
		
		PageDeserializer deserializer = new PageDeserializer();
		ArrayList<GraphObject> pages = (ArrayList<GraphObject>) deserializer.deserializeArray(data);
		
		setHasMoreData(pages.size() >= 25);
		
		return pages;
	}
}
