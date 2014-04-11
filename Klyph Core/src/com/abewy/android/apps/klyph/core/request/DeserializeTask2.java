package com.abewy.android.apps.klyph.core.request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executor;
import org.json.JSONArray;
import android.os.AsyncTask;
import com.abewy.android.apps.klyph.core.KlyphExecutor;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.DeserializeTask.DeserializeCallback;

class DeserializeTask2 extends AsyncTask<JSONArray, Void, List<GraphObject>>
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

	public DeserializeTask2(RequestQuery query, List<GraphObject> previousResults, DeserializeCallback callback)
	{
		this.query = query;
		this.previousResults = previousResults;
		this.callback = callback;
	}

	@Override
	protected List<GraphObject> doInBackground(JSONArray... params)
	{
		JSONArray data = params[0];
		
		if (!query.isNextQuery())
			return query.handleResult(data);
		else
			return query.handleResult(previousResults, data);
	}

	@Override
	protected void onPostExecute(List<GraphObject> result)
	{
		if (callback != null)
			callback.onDeserializeComplete(result);
	}

	DeserializeTask2 executeOnSettingsExecutor()
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