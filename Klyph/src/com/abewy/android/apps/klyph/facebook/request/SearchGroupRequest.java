package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import com.abewy.android.apps.klyph.core.fql.Group;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class SearchGroupRequest extends SearchUserRequest
{
	protected String getType()
	{
		return "group";
	}
	
	@Override
	public Bundle getParams()
	{
		Bundle bundle = super.getParams();
		bundle.putString("fields", "id,name,description");
		return bundle;
	}

	@Override
	public List<GraphObject> handleResult(JSONArray result)
	{
		List<GraphObject> list = new ArrayList<GraphObject>();
		
		int n = result.length();
		for (int i = 0; i < n; i++)
		{
			Group group = new Group();
			JSONObject json = result.optJSONObject(i);
			group.setGid(json.optString("id"));
			group.setName(json.optString("name"));
			group.setDescription(json.optString("description"));
			
			list.add(group);
		}
		
		setHasMoreData(false);
		
		return list;
	}
}
