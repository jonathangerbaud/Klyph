package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class SearchPageRequest extends SearchUserRequest
{
	protected String getType()
	{
		return "page";
	}
	
	@Override
	public Bundle getParams()
	{
		Bundle bundle = super.getParams();
		bundle.putString("fields", "id,name,picture,category");
		return bundle;
	}

	@Override
	public List<GraphObject> handleResult(JSONArray result)
	{
		List<GraphObject> list = new ArrayList<GraphObject>();
		
		int n = result.length();
		for (int i = 0; i < n; i++)
		{
			Page page = new Page();
			JSONObject json = result.optJSONObject(i);
			page.setPage_id(json.optString("id"));
			page.setName(json.optString("name"));
			page.setType(json.optString("category"));
			
			JSONObject pic = json.optJSONObject("picture");
			JSONObject data = pic.optJSONObject("data");
			page.setPic(data.optString("url"));
			list.add(page);
		}
		
		setHasMoreData(false);
		
		return list;
	}
}
