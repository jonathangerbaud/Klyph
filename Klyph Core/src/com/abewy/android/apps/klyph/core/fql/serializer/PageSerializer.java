package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class PageSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);
		
		Page page  = (Page) object;
		
		PrimitiveSerializer ps = new PrimitiveSerializer();
		
		try
		{
			json.put("pic_cover", ps.serializeObject(page.getPic_cover()));
			json.put("location", ps.serializeObject(page.getLocation()));
			json.put("hours", ps.serializeObject(page.getLocation()));
			json.put("parking", ps.serializeObject(page.getParking()));
			json.put("payment_options", ps.serializeObject(page.getPayment_options()));
			json.put("restaurant_services", ps.serializeObject(page.getRestaurant_services()));
			json.put("restaurant_specialties", ps.serializeObject(page.getRestaurant_specialties()));
			json.put("categories", new JSONArray(page.getCategories()));
			json.put("food_styles", new JSONArray(page.getFood_styles()));
		}
		catch (JSONException e)
		{
			Log.d("EventSerializer", "JSONException " + e.getMessage());
		}
		
		return json;
	}
}
