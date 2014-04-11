package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.RequestQuery;

public class StreamGroupRequest extends KlyphQuery
{
	@Override
	public boolean isFQL()
	{
		return false;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		return "/" + id;
	}

	@Override
	public ArrayList<GraphObject> handleResult(JSONObject result)
	{
		Stream s = new Stream();
		s.setPost_id(result.optString("id"));
		ArrayList<GraphObject> list = new ArrayList<GraphObject>();
		list.add(s);
		
		return list;
	}
	
	@Override
	public RequestQuery getNextQuery()
	{
		return new NextQuery();
	}
	
	private static class NextQuery extends StreamRequest
	{
		@Override
		public boolean isNextQuery()
		{
			return true;
		}
		
		@Override
		public String getQuery(List<GraphObject> previousResults, String id, String offset)
		{
			Stream s = (Stream) previousResults.get(0);
			id = s.getPost_id();
			return getQuery(id, offset);
		}
		
		@Override
		public List<GraphObject> handleResult(List<GraphObject> previousResults, JSONArray[] result)
		{
			return handleResult(result);
		}
	}
}
