package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import org.json.JSONArray;
import com.abewy.android.apps.klyph.core.fql.serializer.PageDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class PageRequest extends KlyphQuery
{
	@Override
	public String getQuery(String id, String offset)
	{
		String query = "SELECT name, about, access_token, affiliation, app_id, artists_we_like, attire, awards, band_interests, bio, birthday, booking_agent, built, can_post, categories, checkins, company_overview, culinary_team, current_location, description, description_html, directed_by, fan_count, features, food_styles, founded, general_info, general_manager, genre, global_brand_page_name, global_brand_parent_page_id, has_added_app, hometown, hours, influences, is_community_page, is_published, keywords, location, members, mission, mpg, network, new_like_count, offer_eligible, page_id, page_url, parent_page, parking, personal_info, personal_interests, pharma_safety_info, phone, pic, pic_big, pic_cover, pic_large, pic_small, pic_square, plot_outline, press_contact, price_range, produced_by, products, promotion_eligible, promotion_ineligible_reason, public_transit, record_label, release_date, restaurant_services, restaurant_specialties, schedule, screenplay_by, season, starring, studio, talking_about_count, type, unread_message_count, unseen_message_count, unseen_notif_count, username, website, were_here_count, written_by FROM page WHERE page_id = " + id;
		
		return query;
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONArray result)
	{
		PageDeserializer deserializer = new PageDeserializer();
		ArrayList<GraphObject> page = (ArrayList<GraphObject>) deserializer.deserializeArray(result);

		setHasMoreData(false);
		
		return page;
	}
}
