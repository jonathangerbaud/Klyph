package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Attachment;
import com.abewy.android.apps.klyph.core.fql.Checkin;
import com.abewy.android.apps.klyph.core.fql.Media;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class AttachmentDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Attachment attachment = new Attachment();
		
		deserializePrimitives(attachment, data);
		attachment.setMedia(new MediaDeserializer().deserializeArray(getJsonArray(data, "media"), Media.class));
		attachment.setFb_checkin((Checkin) new CheckinDeserializer().deserializeObject(getJsonObject(data, "fb_checkin")));
		
		return attachment;
	}
}
