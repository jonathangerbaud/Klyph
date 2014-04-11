package com.abewy.android.apps.klyph.facebook.request;

import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.util.Log;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.GraphType;
import com.abewy.android.apps.klyph.core.request.RequestQuery;
import com.facebook.HttpMethod;

/**
 * @author Jonathan
 * 
 */
public abstract class KlyphQuery implements RequestQuery
{
	private boolean	hasMoreData	= true;

	public KlyphQuery()
	{

	}

	@Override
	public boolean isFQL()
	{
		return true;
	}

	@Override
	public boolean isMultiQuery()
	{
		return false;
	}

	@Override
	public boolean isBatchQuery()
	{
		return false;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		return "";
	}

	@Override
	public String getQuery(List<GraphObject> previousResults, String id, String offset)
	{
		return "";
	}

	@Override
	public HttpMethod getHttpMethod()
	{
		return HttpMethod.GET;
	}

	@Override
	public Bundle getParams()
	{
		return null;
	}

	@Override
	public boolean returnId()
	{
		return false;
	}

	@Override
	public List<GraphObject> handleResult(JSONObject result)
	{
		return null;
	}

	@Override
	public List<GraphObject> handleResult(JSONArray result)
	{
		return null;
	}

	@Override
	public List<GraphObject> handleResult(JSONArray[] result)
	{
		return null;
	}

	@Override
	public List<GraphObject> handleResult(List<GraphObject> previousResults, JSONArray result)
	{
		return null;
	}

	@Override
	public List<GraphObject> handleResult(List<GraphObject> previousResults, JSONArray[] result)
	{
		return null;
	}

	@Override
	public RequestQuery getNextQuery()
	{
		return null;
	}

	@Override
	public boolean isNextQuery()
	{
		return false;
	}

	protected String getOffset(String offset, String defaultOffset)
	{
		if (offset != null)
			return offset;

		return defaultOffset;
	}

	protected String multiQuery(String... queries)
	{
		String query = "{";

		for (int i = 0; i < queries.length; i++)
		{
			String q = queries[i];
			query += "'query" + (i + 1) + "':'" + q + "'";

			if (i < queries.length - 1)
				query += ",";
		}

		query += "}";

		return query;
	}

	protected void assocData(JSONArray initData, JSONArray addData, String initKey, String addKey, String initValue, String addValue)
	{
		assocData(initData, addData, initKey, addKey, initValue, addValue, null, null);
	}

	protected void assocData(JSONArray initData, JSONArray addData, String initKey, String addKey, String initValue, String addValue,
			String initType, GraphType addType)
	{
		for (int i = 0; i < initData.length(); i++)
		{
			try
			{
				JSONObject initObject = initData.getJSONObject(i);

				for (int j = 0; j < addData.length(); j++)
				{
					JSONObject addObject = addData.getJSONObject(j);

					if (initObject.optJSONArray(initKey) != null)
					{
						JSONArray initArray = initObject.optJSONArray(initKey);

						for (int k = 0; k < initArray.length(); k++)
						{
							Object fromKeyValue = initArray.get(k);
							Object toKeyValue = addObject.get(addKey);

							if (fromKeyValue != null && fromKeyValue.equals(toKeyValue))
							{
								initObject.accumulate(initValue, addObject.get(addValue));

								if (initType != null)
									initObject.accumulate(initType, addType);
							}
						}
					}
					else
					{
						Object fromKeyValue = initObject.get(initKey);
						Object toKeyValue = addObject.get(addKey);

						if (fromKeyValue != null && fromKeyValue.equals(toKeyValue))
						{
							initObject.put(initValue, addObject.get(addValue));

							if (initType != null)
								initObject.put(initType, addType);
						}
					}
				}
			}
			catch (JSONException e)
			{
				Log.d("KlyphQuery", "assoc_data " + e.getMessage());
			}
		}
	}

	protected void assocData2(JSONArray initData, JSONArray addData, String initKey, String addKey, String initValue, String addValue,
			String initType, String addType)
	{
		for (int i = 0; i < initData.length(); i++)
		{
			try
			{
				JSONObject initObject = initData.getJSONObject(i);

				for (int j = 0; j < addData.length(); j++)
				{
					JSONObject addObject = addData.getJSONObject(j);

					if (initObject.optJSONArray(initKey) != null)
					{
						JSONArray initArray = initObject.optJSONArray(initKey);

						for (int k = 0; k < initArray.length(); k++)
						{
							Object fromKeyValue = initArray.get(k);
							Object toKeyValue = addObject.get(addKey);

							if (fromKeyValue != null && fromKeyValue.equals(toKeyValue))
							{
								initObject.accumulate(initValue, addObject.get(addValue));

								if (initType != null)
									initObject.accumulate(initType, addObject.get(addType));
							}
						}
					}
					else
					{
						Object fromKeyValue = initObject.get(initKey);
						Object toKeyValue = addObject.get(addKey);

						if (fromKeyValue != null && fromKeyValue.equals(toKeyValue))
						{
							initObject.put(initValue, addObject.get(addValue));

							if (initType != null)
								initObject.put(initType, addObject.get(addType));
						}
					}
				}
			}
			catch (JSONException e)
			{}
		}
	}

	/**
	 * Assoc a string array with an object array
	 */
	protected void assocData3(JSONArray initData, JSONArray addData, String initKey, String addKey, String putKey)
	{
		for (int i = 0; i < initData.length(); i++)
		{
			JSONObject initObject = initData.optJSONObject(i);

			if (initObject.optJSONArray(initKey) != null)
			{
				JSONArray initArray = initObject.optJSONArray(initKey);

				for (int j = 0; j < initArray.length(); j++)
				{
					String value = initArray.optString(j);

					for (int k = 0; k < addData.length(); k++)
					{
						JSONObject addObject = addData.optJSONObject(k);

						if (addObject != null)
						{
							String addValue = addObject.optString(addKey);

							if (value.equals(addValue))
							{
								try
								{
									if (initObject.optJSONArray(putKey) == null)
										initObject.putOpt(putKey, new JSONArray());

									initObject.accumulate(putKey, addObject);
								}
								catch (JSONException e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	protected void assocStreamToEvent(JSONArray streams, JSONArray events)
	{
		for (int i = 0; i < streams.length(); i++)
		{
			JSONObject initObject = streams.optJSONObject(i);
			String postId = initObject.optString("post_id");
			postId = postId.substring(postId.indexOf("_") + 1);

			for (int j = 0; j < events.length(); j++)
			{
				JSONObject event = events.optJSONObject(j);
				String eid = event.optString("eid");

				if (postId.equals(eid))
				{
					try
					{
						initObject.putOpt("event", event);
					}
					catch (JSONException e)
					{
						// e.printStackTrace();
					}
				}
			}
		}
	}

	protected void assocStreamToLikedPages(JSONArray streams, JSONArray pages)
	{
		for (int i = 0; i < streams.length(); i++)
		{
			JSONObject initObject = streams.optJSONObject(i);
			int type = initObject.optInt("type");

			if (type != 161 && type != 0)
				continue;

			JSONArray arrayTags = initObject.optJSONArray("description_tags");
			JSONObject objectTags = initObject.optJSONObject("description_tags");

			if (arrayTags != null && arrayTags.length() > 0)
			{
				JSONArray subArray = arrayTags.optJSONArray(0);

				for (int j = 0; j < subArray.length(); j++)
				{
					JSONObject o = subArray.optJSONObject(j);
					String id = o.optString("id");

					for (int k = 0; k < pages.length(); k++)
					{
						JSONObject page = pages.optJSONObject(k);
						String pageId = page.optString("page_id");

						if (id.equals(pageId))
						{
							try
							{
								initObject.accumulate("liked_pages", page);
							}
							catch (JSONException e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
			else if (objectTags != null)
			{
				for (Iterator iterator = objectTags.keys(); iterator.hasNext();)
				{
					String key = (String) iterator.next();

					JSONArray subArray = objectTags.optJSONArray(key);

					for (int j = 0; j < subArray.length(); j++)
					{
						JSONObject o = subArray.optJSONObject(j);
						String id = o.optString("id");

						for (int k = 0; k < pages.length(); k++)
						{
							JSONObject page = pages.optJSONObject(k);
							String pageId = page.optString("page_id");

							if (id.equals(pageId))
							{
								try
								{
									if (initObject.optJSONArray("liked_pages") == null)
										initObject.putOpt("liked_pages", new JSONArray());

									initObject.accumulate("liked_pages", page);
								}
								catch (JSONException e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	protected void assocStreamToObjectBySubPostId(JSONArray left, JSONArray right, String rightId, String leftParam)
	{
		int n = left.length();
		int m = right.length();
		for (int i = 0; i < n; i++)
		{
			JSONObject leftObject = left.optJSONObject(i);
			String leftIdValue = leftObject.optString("post_id");

			int index = leftIdValue.indexOf("_");
			leftIdValue = leftIdValue.substring(index + 1);

			for (int j = 0; j < m; j++)
			{
				JSONObject rightObject = right.optJSONObject(j);
				String rightIdValue = rightObject.optString(rightId);

				if (leftIdValue.equals(rightIdValue))
				{
					try
					{
						leftObject.putOpt(leftParam, rightObject);
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected void assocStreamToObjectById(JSONArray left, JSONArray right, String leftId, String rightId, String leftParam)
	{
		int n = left.length();
		int m = right.length();
		for (int i = 0; i < n; i++)
		{
			JSONObject leftObject = left.optJSONObject(i);
			String leftIdValue = leftObject.optString(leftId);

			for (int j = 0; j < m; j++)
			{
				JSONObject rightObject = right.optJSONObject(j);
				String rightIdValue = rightObject.optString(rightId);

				if (leftIdValue.equals(rightIdValue))
				{
					try
					{
						leftObject.putOpt(leftParam, rightObject);
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected void assocStreamToStatus(JSONArray streams, JSONArray status)
	{
		for (int i = 0; i < streams.length(); i++)
		{
			boolean found = false;
			
			JSONObject initObject = streams.optJSONObject(i);
			String pId = initObject.optString("post_id");

			int startIndex = pId.indexOf("_");

			if (startIndex > -1)
			{
				String subId = pId.substring(startIndex + 1);
				
				for (int j = 0, n = status.length(); j < n; j++)
				{
					JSONObject s = status.optJSONObject(j);
					String sId = s.optString("status_id");
					
					if (sId != null && subId.equals(sId))
					{
						try
						{
							initObject.putOpt("status", s);
						}
						catch (JSONException e)
						{
							Log.d("KlyphQuery", "assocStreamToStatus error " + e.getMessage());
						}
						
						found = true;
						break;
					}
				}
			}
			
			if (found == true)
				continue;

			JSONObject attachment = initObject.optJSONObject("attachment");

			if (attachment != null)
			{
				String id = attachment.optString("href");

				int index = id.indexOf("posts/");
				if (index == -1)
					continue;

				id = id.substring(index + 6);

				for (int j = 0; j < status.length(); j++)
				{
					JSONObject s = status.optJSONObject(j);
					String sId = s.optString("status_id");

					if (id.equals(sId))
					{
						try
						{
							initObject.putOpt("status", s);
						}
						catch (JSONException e)
						{
							Log.d("KlyphQuery", "assocStreamToStatus error " + e.getMessage());
						}

						break;
					}
				}
			}
		}
	}

	protected void assocPostToPhoto(JSONArray posts, JSONArray photos)
	{
		assocPostToObjectById("photo", posts, photos, "object_id", "id", "photoObject");
	}

	protected void assocPostToVideo(JSONArray posts, JSONArray videos)
	{
		assocPostToObjectById("video", posts, videos, "object_id", "id", "videoObject");
	}

	protected void assocPostToLink(JSONArray posts, JSONArray links)
	{
		assocPostToObjectById("link", posts, links, "object_id", "id", "linkObject");
	}

	private void assocPostToObjectById(String type, JSONArray left, JSONArray right, String leftId, String rightId, String leftParam)
	{
		int n = left.length();
		int m = right.length();

		for (int i = 0; i < n; i++)
		{
			JSONObject leftObject = left.optJSONObject(i);

			String lType = leftObject.optString("type");
			if (lType == null || !lType.equals(type))
				continue;

			String leftIdValue = leftObject.optString(leftId);

			if (leftIdValue == null)
				continue;

			for (int j = 0; j < m; j++)
			{
				JSONObject rightObject = right.optJSONObject(j);
				String rightIdValue = rightObject.optString(rightId);

				if (leftIdValue.equals(rightIdValue))
				{
					try
					{
						leftObject.putOpt(leftParam, rightObject);
					}
					catch (JSONException e)
					{
						Log.d("KlyphQuery", "Exception " + e.getMessage());
						e.printStackTrace();
					}

					break;
				}
			}
		}
	}

	@Override
	public boolean hasMoreData()
	{
		return hasMoreData;
	}

	/**
	 * Take care ! Tables checkins, feed, home, links, notes, photos, posts,
	 * statuses, tagged, videos may return subset results, meaning that result
	 * count may be different than the offset limit parameter
	 * 
	 * @param hasMoreData
	 *            true if hasMoreData, false otherwise.
	 */
	protected void setHasMoreData(boolean hasMoreData)
	{
		this.hasMoreData = hasMoreData;
	}

	protected boolean isOffsetDefined(String offset)
	{
		return offset != null && offset.length() > 0;
	}
}
