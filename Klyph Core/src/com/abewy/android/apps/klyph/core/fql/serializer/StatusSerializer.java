package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Status;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class StatusSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);

		Status status = (Status) object;

		LikesSerializer ls = new LikesSerializer();
		StreamSerializer.CommentsSerializer cs = new StreamSerializer.CommentsSerializer();

		try
		{
			json.put("like_info", ls.serializeObject(status.getLike_info()));
			json.put("comment_info", cs.serializeObject(status.getComment_info()));
		}
		catch (JSONException e)
		{
			Log.d("StatusSerializer", "JSONException " + e.getMessage());
		}

		return json;
	}
}
