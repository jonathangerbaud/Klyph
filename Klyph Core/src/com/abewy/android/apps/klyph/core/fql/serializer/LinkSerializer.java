package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Link;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class LinkSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);
		
		Link link = (Link) object;

		LikesSerializer ls = new LikesSerializer();
		StreamSerializer.CommentsSerializer cs = new StreamSerializer.CommentsSerializer();
		PrivacySerializer ps = new PrivacySerializer();

		try
		{
			json.put("comment_info", cs.serializeObject(link.getComment_info()));
			json.put("privacy", ps.serializeObject(link.getPrivacy()));
			json.put("like_info", ls.serializeObject(link.getLike_info()));
		}
		catch (JSONException e)
		{
			Log.d("LinkSerializer", "JSONException " + e.getMessage());
		}
		
		return json;
	}
}
