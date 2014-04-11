package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.MessageThread;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class MessageThreadSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);
		
		MessageThread messageThread = (MessageThread) object;

		FriendSerializer fs = new FriendSerializer();
		
		try
		{
			json.put("recipients_friends", fs.serializeArray(messageThread.getRecipients_friends()));
		}
		catch (JSONException e)
		{
			Log.d("MessageThreadSerializer", "JSONException " + e.getMessage());
		}
		
		return json;
	}
}
