package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.Item;

public class ItemDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Item item = new Item();
		
		deserializePrimitives(item, data);
		
		return item;
	}
}
