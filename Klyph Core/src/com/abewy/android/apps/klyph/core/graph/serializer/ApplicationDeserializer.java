package com.abewy.android.apps.klyph.core.graph.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.graph.Application;
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
