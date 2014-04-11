package com.abewy.android.apps.klyph.core.fql.serializer;

import java.util.ArrayList;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Message;
import com.abewy.android.apps.klyph.core.fql.Message.Attachment;
import com.abewy.android.apps.klyph.core.fql.Message.Media;
import com.abewy.android.apps.klyph.core.fql.Message.Music;
import com.abewy.android.apps.klyph.core.fql.Message.Properties;
import com.abewy.android.apps.klyph.core.fql.Message.Video;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class MessageDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Message message = new Message();
		
		deserializePrimitives(message, data);
		
		message.setAttachment((Attachment) new AttachmentDeserializer().deserializeObject(getJsonObject(data, "attachment")));
		
		return message;
	}
	
	private static class AttachmentDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Attachment attachment = new Attachment();
			
			deserializePrimitives(attachment, data);
			
			attachment.setProperties(new PropertiesDeserializer().deserializeArray(getJsonArray(data, "properties"), Properties.class));
			attachment.setMedia(new MediaDeserializer().deserializeArray(getJsonArray(data, "media"), Media.class));
			
			return attachment;
		}
	}
	
	private static class MediaDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Media media = new Media();
			
			deserializePrimitives(media, data);
			
			media.setVideo((Video) new VideoDeserializer().deserializeObject(getJsonObject(data, "video")));
			
			if (getJsonArray(data, "other_sizes") != null)
				media.setOther_sizes(new MediaDeserializer().deserializeArray(getJsonArray(data, "other_sizes"), Media.class));
			else
				media.setOther_sizes(new ArrayList<Media>());
			
			return media;
		}
	}
	
	private static class VideoDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Video video = new Video();
			
			deserializePrimitives(video, data);
			
			return video;
		}
	}
	
	private static class MusicDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Music music = new Music();
			
			deserializePrimitives(music, data);
			
			return music;
		}
	}
	
	private static class PropertiesDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Properties properties = new Properties();
			
			deserializePrimitives(properties, data);
			
			return properties;
		}
	}
}
