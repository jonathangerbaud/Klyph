package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Application;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class ApplicationDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Application application = new Application();
		
		deserializePrimitives(application, data);
		
		return application;
	}
}
