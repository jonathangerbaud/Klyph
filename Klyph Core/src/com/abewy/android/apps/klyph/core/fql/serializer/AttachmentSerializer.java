package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Attachment;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class AttachmentSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);

		Attachment attachment = (Attachment) object;

		CheckinSerializer cs = new CheckinSerializer();
		MediaSerializer ms = new MediaSerializer();

		try
		{
			json.putOpt("fb_checkin", cs.serializeObject(attachment.getFb_checkin()));
			json.putOpt("media", ms.serializeArray(attachment.getMedia()));
		}
		catch (JSONException e)
		{
			Log.d("AttachmentSerializer", "JSONException" + e.getMessage());
		}

		return json;
	}
}
