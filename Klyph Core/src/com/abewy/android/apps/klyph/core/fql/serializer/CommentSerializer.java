package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Comment;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class CommentSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);

		Comment comment = (Comment) object;

		TagSerializer ts = new TagSerializer();

		try
		{
			json.putOpt("text_tags", ts.serializeArray(comment.getText_tags()));
		}
		catch (JSONException e)
		{
			Log.d("CommentSerializer", "JSONException" + e.getMessage());
		}

		return json;
	}
}
