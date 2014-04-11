package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.UnifiedThread;
import com.abewy.android.apps.klyph.core.fql.UnifiedThread.Sender;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UnifiedThreadDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		UnifiedThread thread = new UnifiedThread();
		
		deserializePrimitives(thread, data);
		thread.setSnippet_sender((Sender) new SenderDeserializer().deserializeObject(getJsonObject(data, "sender")));
		thread.setParticipants(new SenderDeserializer().deserializeArray(getJsonArray(data, "participants"), Sender.class));
		thread.setSenders(new SenderDeserializer().deserializeArray(getJsonArray(data, "senders"), Sender.class));
		thread.setThread_participants(new SenderDeserializer().deserializeArray(getJsonArray(data, "thread_participants"), Sender.class));
		
		return thread;
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
