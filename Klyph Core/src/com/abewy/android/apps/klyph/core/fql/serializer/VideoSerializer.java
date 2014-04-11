package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class VideoSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);
		
		Video video = (Video) object;
		
		FormatSerializer fs = new FormatSerializer();

		try
		{
			json.put("format", fs.serializeArray(video.getFormat()));
		}
		catch (JSONException e)
		{
			Log.d("StreamSerializer", "JSONException " + e.getMessage());
		}
		
		return json;
	}
	
	private static class FormatSerializer extends Serializer
	{
		
	}
}
