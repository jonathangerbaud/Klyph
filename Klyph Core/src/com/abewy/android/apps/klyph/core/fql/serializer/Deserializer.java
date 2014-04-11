package com.abewy.android.apps.klyph.core.fql.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public abstract class Deserializer implements IDeserializer
{
	public GraphObject deserializeObject(JSONObject data)
	{
		return null;
	}

	public List<GraphObject> deserializeArray(JSONArray data)
	{
		List<GraphObject> list = new ArrayList<GraphObject>();

		if (data != null)
		{
			int n = data.length();
			for (int i = 0; i < n; i++)
			{
				try
				{
					list.add(deserializeObject(data.getJSONObject(i)));
				}
				catch (JSONException e)
				{}

			}
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> deserializeArray(JSONArray data, Class<T> type)
	{
		List<T> list = new ArrayList<T>();

		if (data != null)
		{
			int n = data.length();
			for (int i = 0; i < n; i++)
			{
				try
				{
					list.add((T) deserializeObject(data.getJSONObject(i)));
				}
				catch (JSONException e)
				{}

			}
		}

		return list;
	}

	protected void deserializePrimitives(GraphObject graphObject, JSONObject json)
	{
		if (json == null)
			return;

		Class<? extends GraphObject> c = graphObject.getClass();

		for (Field field : c.getDeclaredFields())
		{
			// If field is static and/or final, then we ignore it
			if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))
				continue;
			
			String fieldName = field.getName();
			String methodName = new StringBuilder().append("set").append(Character.toUpperCase(fieldName.charAt(0))).append(fieldName.substring(1))
					.toString();

			Class<?> type = field.getType();
			try
			{
				if (type == String.class)
				{
					c.getMethod(methodName, String.class).invoke(graphObject, getString(json.optString(fieldName)));
				}
				else if (type == int.class)
				{
					c.getMethod(methodName, int.class).invoke(graphObject, json.optInt(fieldName));
				}
				else if (type == boolean .class)
				{
					c.getMethod(methodName, boolean.class).invoke(graphObject, getbooleanValue(json, fieldName));
				}
				else if (type == long.class)
				{
					c.getMethod(methodName, long.class).invoke(graphObject, json.optLong(fieldName));
				}
				else if (type == float.class)
				{
					c.getMethod(methodName, float.class).invoke(graphObject, json.optDouble(fieldName));
				}
			}
			catch (NoSuchMethodException e)
			{
				Log.e("Deserializer deserializePrimitives", "No such method named " + methodName);
			}
			catch (InvocationTargetException e)
			{
				Log.e("Deserializer deserializePrimitives", "InvocationTargetException " + e.getMessage());
			}
			catch (IllegalAccessException e)
			{
				Log.e("Deserializer deserializePrimitives", "IllegalAccessException " + e.getMessage());
			}
		}
	}
	
	private boolean getbooleanValue(JSONObject object, String field)
	{
		int intValue = object.optInt(field, -1);
		
		if (intValue > -1)
		{
			return (int) intValue > 0;
		}
		
		String stringValue = object.optString(field, null);
		
		if (stringValue != null)
		{
			if ("true".equalsIgnoreCase(stringValue))
			{
				return true;
			}
			else if ("false".equalsIgnoreCase(stringValue))
			{
				return false;
			}
		}
		
		return object.optBoolean(field, false);
	}

	protected List<String> deserializeStringList(JSONArray array)
	{
		List<String> list = new ArrayList<String>();

		if (array != null)
		{
			int n = array.length();
			for (int i = 0; i < n; i++)
			{
				list.add(array.optString(i));
			}
		}

		return list;
	}

	protected String getString(String string)
	{
		if (string != null && !string.equals("null"))
		{
			return string;
		}

		return "";
	}

	protected JSONObject getJsonObject(JSONObject object, String key)
	{
		JSONObject o = object.optJSONObject(key);

		if (o != null)
			return o;
		else
			return new JSONObject();
	}

	protected JSONArray getJsonArray(JSONObject object, String key)
	{
		return object.optJSONArray(key);
	}
}
