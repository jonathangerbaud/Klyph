package com.abewy.android.apps.klyph.core.graph.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.Link;
import com.abewy.android.apps.klyph.core.graph.UserRef;

public class LinkDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Link link = new Link();

		deserializePrimitives(link, data);

		link.setFrom((UserRef) new UserRefDeserializer().deserializeObject(getJsonObject(data, "from")));
		
		return link;
	}
}
