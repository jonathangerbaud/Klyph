package com.abewy.android.apps.klyph.core.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import com.abewy.android.apps.klyph.core.BaseApplication;
import com.abewy.android.apps.klyph.core.KlyphFlags;
import com.abewy.android.apps.klyph.core.KlyphLocale;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.DeserializeTask.DeserializeCallback;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Session;

public abstract class BaseAsyncRequest implements DeserializeCallback
{
	private com.facebook.Request	request;

	public interface Callback
	{
		void onComplete(Response response);
	}

	private int					query;
	private String				id;
	private String				offset;
	private Bundle				params;
	private Callback			callBack;
	private RequestQuery		subQuery;
	private List<GraphObject>	previousResults;
	private String				afterCursor;
	private String				beforeCursor;
	private String				pagingBefore;
	private String				pagingNext;

	public BaseAsyncRequest(int query, String id, String offset, Callback callBack)
	{
		this.query = query;
		this.id = id;
		this.offset = offset;
		this.callBack = callBack;

		subQuery = getSubQuery(query);
	}

	public BaseAsyncRequest(int query, String id, Bundle params, Callback callBack)
	{
		this.query = query;
		this.id = id;
		this.params = params;
		this.callBack = callBack;

		subQuery = getSubQuery(query);
	}

	protected int getQuery()
	{
		return query;
	}

	protected String getId()
	{
		return id;
	}

	protected String getOffset()
	{
		return offset;
	}

	public void cancel()
	{
		if (request != null)
		{
			request.setCallback(null);
			request = null;
		}
	}

	public void execute()
	{
		execute(subQuery);
	}

	private void execute(RequestQuery query)
	{
		if (KlyphFlags.LOG_REQUEST_EXEC)
		{
			Log.d("AsyncRequest " + query.toString(), "id = " + id + ", offset = " + offset);
		}

		if (KlyphSession.getSessionUser() == null)
			return;

		com.facebook.Request r = getRequest();

		startTime = new Date().getTime();
		com.facebook.Request.executeBatchAsync(r);
	}

	public static void executeBatch(List<? extends BaseAsyncRequest> requests)
	{
		List<com.facebook.Request> fbRequests = new ArrayList<com.facebook.Request>();
		for (BaseAsyncRequest asyncRequest : requests)
		{
			fbRequests.add(asyncRequest.getRequest());
		}

		com.facebook.Request.executeBatchAsync(fbRequests);
	}

	protected com.facebook.Request getRequest()
	{
		String path = (subQuery.isFQL() == true) ? "/fql" : subQuery.getQuery(id, offset);

		Bundle params = new Bundle();
		params.putString("locale", KlyphLocale.getFbLocale());
		params.putString("date_format", "U"); // Get dates in unix timestamp format

		if (subQuery.getHttpMethod() == HttpMethod.GET)
		{
			if (subQuery.isFQL() == true)
			{
				String q = subQuery.isNextQuery() == false ? subQuery.getQuery(id, offset) : subQuery.getQuery(previousResults, id, offset);
				params.putString("q", q);
			}
		}
		else
		{
			if (this.params != null)
			{
				for (String key : this.params.keySet())
				{
					params.putString(key, this.params.getString(key));
				}
			}
		}

		Bundle queryParams = subQuery.getParams();
		if (queryParams != null)
		{
			for (String key : queryParams.keySet())
			{
				params.putString(key, queryParams.getString(key));
			}
		}

		if (Session.getActiveSession() == null)
			Session.openActiveSessionFromCache(BaseApplication.getInstance());

		request = new com.facebook.Request(Session.getActiveSession(), path, params, subQuery.getHttpMethod(),

		new com.facebook.Request.Callback() {
			public void onCompleted(com.facebook.Response response)
			{
				handleResponse(response);
			}
		});

		return request;
	}

	private long	startTime;

	protected void handleResponse(com.facebook.Response response)
	{
		if (KlyphFlags.LOG_REQUEST_PERFORMANCE)
		{
			Log.d("TIME " + query, "requestTime = " + (new Date().getTime() - startTime));
			startTime = new Date().getTime();
		}

		/*
		 * Log.d("BaseAsyncRequest", "response " + response);
		 * Log.d("BaseAsyncRequest", "response error" + response.getError());
		 * Log.d("BaseAsyncRequest", "response graph" + response.getGraphObject());
		 */

		if ((response.getError() == null || response.getError().getErrorCode() == -1)
			&& (response.getGraphObject() != null || response.getGraphObjectList() != null))
		{
			// if (KlyphFlags.LOG_REQUEST_RESULT == true)
			// longInfo(jsonResponse.toString());

			try
			{
				if (subQuery.isFQL())
				{
					JSONObject jsonResponse = response.getGraphObject().getInnerJSONObject();
					if (subQuery.returnId())
					{
						String id = (String) response.getGraphObject().getProperty("id");
						doCallBack(id);
					}
					else if (!subQuery.getHttpMethod().equals(HttpMethod.GET))
					{
						doCallBack(new ArrayList<GraphObject>());
					}
					else if (subQuery.isMultiQuery())
					{
						JSONArray data = (JSONArray) jsonResponse.getJSONArray("data");

						new DeserializeTask(subQuery, previousResults, this).execute(data);
					}
					else
					{
						JSONArray data = (JSONArray) jsonResponse.getJSONArray("data");

						new DeserializeTask2(subQuery, previousResults, this).execute(data);
					}
				}
				else
				{
					if (subQuery.returnId())
					{
						String id = (String) response.getGraphObject().getProperty("id");
						doCallBack(id);
					}
					else if (!subQuery.isMultiQuery())
					{
						JSONObject jsonResponse = response.getGraphObject().getInnerJSONObject();
						doCallBack(subQuery.handleResult(jsonResponse));
					}
					/*
					 * else
					 * {
					 * JSONArray data = response.getGraphObjectList().getInnerJSONArray();
					 * new DeserializeTask3(subQuery, previousResults, this).execute(data);
					 * }
					 */
					else
					{
						JSONObject jsonResponse = response.getGraphObject().getInnerJSONObject();

						JSONObject paging = jsonResponse.optJSONObject("paging");

						if (paging != null)
						{
							JSONObject cursors = paging.optJSONObject("cursors");

							if (cursors != null)
							{
								afterCursor = cursors.optString("after");
								beforeCursor = cursors.optString("before");
							}
							
							pagingBefore = paging.optString("before");
							pagingNext = paging.optString("next");
						}
						JSONArray data = (JSONArray) jsonResponse.getJSONArray("data");

						new DeserializeTask2(subQuery, previousResults, this).execute(data);
					}
				}

			}
			catch (JSONException e)
			{
				RequestError error = new RequestError(999, "JSONException", e.getMessage());
				doCallBack(error);
			}
		}
		else
		{
			longInfo("Error " + response.getError());

			FacebookRequestError fbError = response.getError();

			if (KlyphFlags.ENABLE_BUG_REPORT)
			{
				/*
				 * if (query == Query.POST_LIKE || query == Query.POST_UNLIKE || query == Query.POST_COMMENT
				 * || query == Query.NOTIFICATIONS)
				 * {
				 * ACRA.getErrorReporter().handleSilentException(fbError.getException());
				 * }
				 */
			}

			RequestError error = new RequestError(fbError.getErrorCode(), fbError.getErrorType(), fbError.getErrorMessage());
			doCallBack(error);
		}
	}

	@Override
	public void onDeserializeComplete(List<GraphObject> result)
	{
		doCallBack(result);
	}

	@Override
	public void onDeserializeComplete(RequestError error)
	{
		doCallBack(error);
	}

	protected void doCallBack(RequestError error)
	{
		doCallBack(error, null, null);
	}

	protected void doCallBack(List<GraphObject> objects)
	{
		if (subQuery.getNextQuery() == null)
		{
			doCallBack(null, null, objects);
		}
		else
		{
			previousResults = objects;
			subQuery = subQuery.getNextQuery();
			execute(subQuery);
		}
	}

	protected void doCallBack(String id)
	{
		if (KlyphFlags.LOG_REQUEST_PERFORMANCE)
			Log.d("TIME " + query, "deserializeTime = " + (new Date().getTime() - startTime));

		if (callBack != null)
		{
			final Response response = new Response(id);
			callBack.onComplete(response);

			/*
			 * Runnable runnable = new Runnable() {
			 * public void run()
			 * {
			 * callBack.onComplete(response);
			 * }
			 * };
			 * 
			 * runnable.run();
			 */
		}
	}

	protected void doCallBack(RequestError error, GraphObject object, List<GraphObject> objects)
	{
		if (KlyphFlags.LOG_REQUEST_PERFORMANCE)
			Log.d("TIME " + query, "deserializeTime = " + (new Date().getTime() - startTime));

		if (callBack != null)
		{
			final Response response = new Response(error, object, objects);
			callBack.onComplete(response);

			/*
			 * Runnable runnable = new Runnable() {
			 * public void run()
			 * {
			 * callBack.onComplete(response);
			 * }
			 * };
			 * 
			 * runnable.run();
			 */
		}
	}

	protected abstract RequestQuery getSubQuery(int query);

	protected void longInfo(String str)
	{
		if (str.length() > 4000)
		{
			Log.d("AsyncRequest", str.substring(0, 4000));
			longInfo(str.substring(4000));
		}
		else
			Log.d("AsyncRequest", str);
	}

	public boolean hasMoreData()
	{
		return subQuery.hasMoreData();
	}

	public String getAfterCursor()
	{
		return afterCursor;
	}

	public String getBeforeCursor()
	{
		return beforeCursor;
	}
	
	public String getPagingBefore()
	{
		return pagingBefore;
	}

	public String getPagingNext()
	{
		return pagingNext;
	}
}
