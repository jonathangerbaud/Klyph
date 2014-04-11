package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class EventSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);
		
		Event event  = (Event) object;
		
		PrimitiveSerializer ps = new PrimitiveSerializer();
		
		try
		{
			json.put("pic_cover", ps.serializeObject(event.getPic_cover()));
			json.put("venue", ps.serializeObject(event.getVenue()));
		}
		catch (JSONException e)
		{
			Log.d("EventSerializer", "JSONException " + e.getMessage());
		}
		
		return json;
	}
}
