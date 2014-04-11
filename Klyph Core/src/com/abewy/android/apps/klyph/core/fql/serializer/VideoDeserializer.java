package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.core.fql.Video.Format;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class VideoDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Video video = new Video();
		
		deserializePrimitives(video, data);
		video.setFormat(new FormatDeserializer().deserializeArray(getJsonArray(data, "format"), Format.class));
		
		return video;
	}
	
	private static class FormatDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Format format = new Format();
			
			deserializePrimitives(format, data);
			
			return format;
		}
	}
}
