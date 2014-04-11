package com.abewy.android.apps.klyph.core.fql.serializer;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class TagDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Tag tag = new Tag();

		deserializePrimitives(tag, data);

		return tag;
	}

	public Map<String, List<Tag>> deserializeMap(JSONObject data)
	{
		Map<String, List<Tag>> map = new Hashtable<String, List<Tag>>();

		if (data != null)
		{
			for (Iterator iterator = data.keys(); iterator.hasNext();)
			{
				String key = (String) iterator.next();

				JSONArray tags = data.optJSONArray(key);
				map.put(key, deserializeArray(tags, Tag.class));
			}
		}

		return map;
	}

	public Map<String, List<Tag>> deserializeMap(JSONArray data)
	{
		Map<String, List<Tag>> map = new Hashtable<String, List<Tag>>();

		if (data != null)
		{
			int n = data.length();
			for (int i = 0; i < n; i++)
			{
				JSONArray tags = data.optJSONArray(i);
				map.put(String.valueOf(i), deserializeArray(tags, Tag.class));

			}
		}

		return map;
	}
}
