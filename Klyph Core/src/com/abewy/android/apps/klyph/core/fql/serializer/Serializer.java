package com.abewy.android.apps.klyph.core.fql.serializer;

import java.lang.reflect.Field;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public abstract class Serializer implements ISerializer
{
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);

		return json;
	}

	public JSONArray serializeArray(List<? extends GraphObject> data)
	{
		JSONArray array = new JSONArray();

		if (data != null)
		{
			for (int i = 0; i < data.size(); i++)
			{
				JSONObject object = serializeObject(data.get(i));
				array.put(object);
			}
		}

		return array;
	}

	protected void serializePrimitives(GraphObject graphObject, JSONObject json)
	{
		Class<? extends GraphObject> c = graphObject.getClass();

		for (Field field : c.getDeclaredFields())
		{
			field.setAccessible(true);
			String fieldName = field.getName();

			try
			{
				if (field.getType().equals(String.class) || field.getType().equals(int.class)
					|| field.getType().equals(boolean.class) || field.getType().equals(long.class)
					|| field.getType().equals(float.class))
				{
					json.putOpt(fieldName, field.get(graphObject));
				}
			}
			catch (JSONException e)
			{
				Log.e("Deserializer serializePrimitives", "JsonException " + fieldName);
			}
			catch (IllegalAccessException e)
			{
				Log.e("Deserializer serializePrimitives", "IllegalAccessException " + e.getMessage());
			}
		}
	}

	protected void serializeStringList(List<String> list, JSONObject json, String field)
	{
		JSONArray array = new JSONArray(list);

		if (list == null)
		{
			array = new JSONArray();
		}

		try
		{
			json.putOpt(field, array);
		}
		catch (JSONException e)
		{
			Log.e("Deserializer serializeStringList", "JSONException " + e.getMessage());
		}
	}
}
