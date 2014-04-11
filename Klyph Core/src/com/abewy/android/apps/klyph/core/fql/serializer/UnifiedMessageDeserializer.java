package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.UnifiedMessage;
import com.abewy.android.apps.klyph.core.fql.UnifiedMessage.Sender;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UnifiedMessageDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		UnifiedMessage message = new UnifiedMessage();
		
		deserializePrimitives(message, data);
		message.setSender((Sender) new SenderDeserializer().deserializeObject(getJsonObject(data, "sender")));
		
		return message;
	}
	
	private static class SenderDeserializer extends Deserializer
	{

		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Sender sender = new Sender();
			
			deserializePrimitives(sender, data);
			
			return sender;
		}
		
	}
}
