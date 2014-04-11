package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Privacy;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class PrivacyDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Privacy privacy = new Privacy();
		
		deserializePrimitives(privacy, data);
		
		return privacy;
	}
}
