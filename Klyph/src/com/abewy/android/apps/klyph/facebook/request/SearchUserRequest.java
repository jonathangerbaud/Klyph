package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class SearchUserRequest extends KlyphQuery
{
	private String id;
	private String offset;
	
	@Override
	public boolean isFQL()
	{
		return false;
	}

	@Override
	public boolean isMultiQuery()
	{
		return true;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		this.id = id;
		this.offset = offset;
		return "search";
	}

	@Override
	public Bundle getParams()
	{
		Bundle bundle = new Bundle();
		bundle.putString("q", id);
		bundle.putString("type", getType());
		bundle.putString("fields", "id,name,picture");
		if (offset == null || offset.length() == 0)
			offset = "0";
		
		bundle.putString("limit", "100");
		return bundle;
	}

	protected String getType()
	{
		return "user";
	}

	@Override
	public List<GraphObject> handleResult(JSONArray result)
	{
		Log.d("SearchUser", "handleResult");
		List<GraphObject> list = new ArrayList<GraphObject>();
		
		int n = result.length();
		for (int i = 0; i < n; i++)
		{
			Friend friend = new Friend();
			JSONObject json = result.optJSONObject(i);
			friend.setUid(json.optString("id"));
			friend.setName(json.optString("name"));
			
			JSONObject pic = json.optJSONObject("picture");
			JSONObject data = pic.optJSONObject("data");
			friend.setPic(data.optString("url"));
			list.add(friend);
		}
		
		setHasMoreData(false);
		
		return list;
	}
}
