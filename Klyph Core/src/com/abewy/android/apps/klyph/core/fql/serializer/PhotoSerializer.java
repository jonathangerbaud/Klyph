package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class PhotoSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);
		
		Photo photo = (Photo) object;
		
		StreamSerializer.CommentsSerializer cs = new StreamSerializer.CommentsSerializer();
		LikesSerializer ls = new LikesSerializer();

		try
		{
			json.put("comment_info", cs.serializeObject(photo.getComment_info()));
			json.put("like_info", ls.serializeObject(photo.getLike_info()));
		}
		catch (JSONException e)
		{
			Log.d("StreamSerializer", "JSONException " + e.getMessage());
		}
		
		return json;
	}
}
