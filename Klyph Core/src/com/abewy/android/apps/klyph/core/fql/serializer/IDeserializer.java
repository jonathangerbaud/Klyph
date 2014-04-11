package com.abewy.android.apps.klyph.core.fql.serializer;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public interface IDeserializer
{
	public GraphObject deserializeObject(JSONObject data);
	public List<GraphObject> deserializeArray(JSONArray data);
	public <T> List<T> deserializeArray(JSONArray data, Class<T> type);

}
