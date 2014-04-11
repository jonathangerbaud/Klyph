package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class SearchEventRequest extends SearchUserRequest
{
	protected String getType()
	{
		return "event";
	}
	
	@Override
	public Bundle getParams()
	{
		Bundle bundle = super.getParams();
		bundle.putString("fields", "id,name,picture,start_time,location");
		return bundle;
	}
	
	@Override
	public List<GraphObject> handleResult(JSONArray result)
	{
		List<GraphObject> list = new ArrayList<GraphObject>();
		
		int n = result.length();
		for (int i = 0; i < n; i++)
		{
			Event event = new Event();
			JSONObject json = result.optJSONObject(i);
			event.setEid(json.optString("id"));
			event.setName(json.optString("name"));
			event.setStart_time(json.optString("start_time"));
			event.setLocation(json.optString("location"));
			
			JSONObject pic = json.optJSONObject("picture");
			JSONObject data = pic.optJSONObject("data");
			event.setPic(data.optString("url"));
			list.add(event);
		}
		
		setHasMoreData(false);
		
		return list;
	}
}
