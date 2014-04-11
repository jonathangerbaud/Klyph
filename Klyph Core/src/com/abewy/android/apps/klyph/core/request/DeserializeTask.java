/**
* @author Jonathan
*/

package com.abewy.android.apps.klyph.core.request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import com.abewy.android.apps.klyph.core.KlyphExecutor;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

class DeserializeTask extends AsyncTask<JSONArray, Void, List<GraphObject>>
{
	public interface DeserializeCallback
	{
		public void onDeserializeComplete(List<GraphObject> result);
		public void onDeserializeComplete(RequestError error);
	}
	
	private RequestQuery query;
	private List<GraphObject> previousResults;
	private DeserializeCallback callback;
	
	private static Method executeOnExecutorMethod;
	
	static {
        for (Method method : AsyncTask.class.getMethods()) {
            if ("executeOnExecutor".equals(method.getName())) {
                Class<?>[] parameters = method.getParameterTypes();
                if ((parameters.length == 2) && (parameters[0] == Executor.class) && parameters[1].isArray()) {
                    executeOnExecutorMethod = method;
                    break;
                }
            }
        }
    }
	
	public DeserializeTask(RequestQuery query, List<GraphObject> previousResults, DeserializeCallback callback)
	{
		this.query = query;
		this.previousResults = previousResults;
		this.callback = callback;
	}
	
	@Override
	protected List<GraphObject> doInBackground(JSONArray... params)
	{
		JSONArray data = params[0];
		JSONArray[] jsonArray = new JSONArray[data.length()];

		try
		{
			for (int i = 0; i < data.length(); i++)
			{
				JSONObject resultSet = data.getJSONObject(i);
				String n = resultSet.getString("name");

				int index = Integer.parseInt(n.substring(5));
				jsonArray[(index - 1)] = resultSet.getJSONArray("fql_result_set");
			}

			if (!query.isNextQuery())
				return query.handleResult(jsonArray);
			else
				return query.handleResult(previousResults, jsonArray);
		}
		catch (JSONException e)
		{
			return null;
		}
	}

	@Override
	protected void onPostExecute(List<GraphObject> result)
	{
		if (result != null)
		{
			if (callback != null)
				callback.onDeserializeComplete(result);
		}
		else
		{
			RequestError error = new RequestError(999, "JSONException", "Error parsing returned data");
			
			if (callback != null)
				callback.onDeserializeComplete(error);
		}
	}
	
	DeserializeTask executeOnSettingsExecutor() {
        try {
            if (executeOnExecutorMethod != null) {
                executeOnExecutorMethod.invoke(this, KlyphExecutor.getExecutor(), null);
                return this;
            }
        } catch (InvocationTargetException e) {
            // fall-through
        } catch (IllegalAccessException e) {
            // fall-through
        }

        this.execute();
        return this;
    }
}