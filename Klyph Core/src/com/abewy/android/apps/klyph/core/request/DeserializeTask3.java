package com.abewy.android.apps.klyph.core.request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.util.Log;
import com.abewy.android.apps.klyph.core.KlyphExecutor;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.DeserializeTask.DeserializeCallback;

class DeserializeTask3 extends AsyncTask<JSONArray, Void, List<GraphObject>>
{
	private RequestQuery		query;
	private List<GraphObject>	previousResults;
	private DeserializeCallback	callback;

	private static Method		executeOnExecutorMethod;

	static
	{
		for (Method method : AsyncTask.class.getMethods())
		{
			if ("executeOnExecutor".equals(method.getName()))
			{
				Class<?>[] parameters = method.getParameterTypes();
				if ((parameters.length == 2) && (parameters[0] == Executor.class) && parameters[1].isArray())
				{
					executeOnExecutorMethod = method;
					break;
				}
			}
		}
	}

	public DeserializeTask3(RequestQuery query, List<GraphObject> previousResults, DeserializeCallback callback)
	{
		this.query = query;
		this.previousResults = previousResults;
		this.callback = callback;
	}

	@Override
	protected List<GraphObject> doInBackground(JSONArray... params)
	{
		JSONArray data = params[0];
		
		int n = data.length();
		JSONArray[] array = new JSONArray[n];
		
		for (int i = 0; i < n; i++)
		{
			JSONObject jsonObject = data.optJSONObject(i);
			
			String jsonString = jsonObject.optString("body");
			
			JSONObject bodyObject = null;
			
			try
			{
				bodyObject = new JSONObject(jsonString);
			}
			catch (JSONException e)
			{
				
			}
			
			if (bodyObject == null)
			{
				array[i] = new JSONArray();
				continue;
			}
			
			JSONArray dataArray = bodyObject.optJSONArray("data"); 
			if (dataArray != null)
			{
				array[i] = dataArray;
			}
			else
			{
				JSONArray jsonArray = new JSONArray();
				
				for (@SuppressWarnings("unchecked")
				Iterator<String> iterator = (Iterator<String>) bodyObject.keys(); iterator.hasNext();)
				{
					String key = (String) iterator.next();
					jsonArray.put(bodyObject.opt(key));
				}
				
				array[i] = jsonArray;
			}
		}
		
		return query.handleResult(array);
		
		/*if (!query.isNextQuery())
			return query.handleResult(data);
		else
			return query.handleResult(previousResults, data);*/
	}

	@Override
	protected void onPostExecute(List<GraphObject> result)
	{
		if (callback != null)
			callback.onDeserializeComplete(result);
	}

	DeserializeTask3 executeOnSettingsExecutor()
	{
		try
		{
			if (executeOnExecutorMethod != null)
			{
				executeOnExecutorMethod.invoke(this, KlyphExecutor.getExecutor(), null);
				return this;
			}
		}
		catch (InvocationTargetException e)
		{
			// fall-through
		}
		catch (IllegalAccessException e)
		{
			// fall-through
		}

		this.execute();
		return this;
	}
}