package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Location;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class LocationDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Location location = new Location();
		
		deserializePrimitives(location, data);
		
		return location;
	}
}
