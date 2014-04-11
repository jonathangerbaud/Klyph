package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Checkin;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class CheckinDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Checkin checkin = new Checkin();
		
		deserializePrimitives(checkin, data);
		
		return checkin;
	}
}
