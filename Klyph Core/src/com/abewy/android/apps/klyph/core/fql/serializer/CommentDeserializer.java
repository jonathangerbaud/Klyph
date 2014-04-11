package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Comment;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.core.fql.Comment.Attachment;
import com.abewy.android.apps.klyph.core.fql.Comment.Attachment.Media;
import com.abewy.android.apps.klyph.core.fql.Comment.Attachment.Target;
import com.abewy.android.apps.klyph.core.fql.Comment.Attachment.Media.Image;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class CommentDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Comment comment = new Comment();
		
		deserializePrimitives(comment, data);
		
		comment.setAttachment((Attachment) new AttachmentDeserializer().deserializeObject(getJsonObject(data, "attachment")));
		comment.setText_tags(new TagDeserializer().deserializeArray(getJsonArray(data, "text_tags"), Tag.class));
		
		
		return comment;
	}
	
	private static class AttachmentDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Attachment attachment = new Attachment();
			
			deserializePrimitives(attachment, data);
			
			attachment.setDescription_tags(new TagDeserializer().deserializeArray(getJsonArray(data, "description_tags"), Tag.class));
			attachment.setMedia((Media) new MediaDeserializer().deserializeObject(getJsonObject(data, "media")));
			attachment.setTarget((Target) new TargetDeserializer().deserializeObject(getJsonObject(data, "target")));
			
			return attachment;
		}
		
		private static class MediaDeserializer extends Deserializer
		{
			@Override
			public GraphObject deserializeObject(JSONObject data)
			{
				Media media = new Media();
				
				media.setImage((Image) new ImageDeserializer().deserializeObject(getJsonObject(data, "image")));
				
				return media;
			}
			
			private static class ImageDeserializer extends Deserializer
			{
				@Override
				public GraphObject deserializeObject(JSONObject data)
				{
					Image image = new Image();
					
					deserializePrimitives(image, data);
					
					return image;
				}
			}
		}
		
		private static class TargetDeserializer extends Deserializer
		{
			@Override
			public GraphObject deserializeObject(JSONObject data)
			{
				Target target = new Target();
				
				deserializePrimitives(target, data);
				
				return target;
			}
		}
	}
	
	
}
