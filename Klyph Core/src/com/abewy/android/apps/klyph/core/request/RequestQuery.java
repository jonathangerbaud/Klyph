package com.abewy.android.apps.klyph.core.request;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.facebook.HttpMethod;

public interface RequestQuery
{
	public boolean isFQL();
	
	public boolean isMultiQuery();
	
	public boolean  isBatchQuery();

	public String getQuery(String id, String offset);
	
	public String getQuery(List<GraphObject> previousResults, String id, String offset);
	
	public HttpMethod getHttpMethod();
	
	public Bundle getParams();
	
	public List<GraphObject> handleResult(JSONObject result);

	public List<GraphObject> handleResult(JSONArray result);

	public List<GraphObject> handleResult(JSONArray[] result);
	
	public List<GraphObject> handleResult(List<GraphObject> previousResults, JSONArray result);

	public List<GraphObject> handleResult(List<GraphObject> previousResults, JSONArray[] result);
	
	public boolean hasMoreData();
	
	public RequestQuery getNextQuery();
	
	public boolean isNextQuery();
	
	public boolean returnId();
}
