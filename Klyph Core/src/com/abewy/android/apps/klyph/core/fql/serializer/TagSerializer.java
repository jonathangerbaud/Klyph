package com.abewy.android.apps.klyph.core.fql.serializer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Tag;

public class TagSerializer extends Serializer
{	
	public void serializeMap(Map<String, List<Tag>> tags, JSONObject json, String field)
	{
		JSONObject tagObject = new JSONObject();
		
		for (Iterator<String> iterator = tags.keySet().iterator(); iterator.hasNext();)
		{
			String key = (String) iterator.next();
			
			List<Tag> tag = tags.get(key);
			
			try
			{
				tagObject.put(key, super.serializeArray(tag));
			}
			catch (JSONException e)
			{
				Log.d("TagSerializer", "JsonException " + e.getMessage());
			}
		}
		
		try
		{
			json.put(field, tagObject);
		}
		catch (JSONException e)
		{
			Log.d("TagSerializer", "JsonException " + e.getMessage());
		}
	}
}
