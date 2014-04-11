package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.LikeInfo;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Photo.Image;
import com.abewy.android.apps.klyph.core.fql.Stream.CommentInfo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class PhotoDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Photo photo = new Photo();
		
		deserializePrimitives(photo, data);
		photo.setComment_info((CommentInfo) new StreamDeserializer.CommentsDeserializer().deserializeObject(getJsonObject(data, "comment_info")));
		photo.setLike_info((LikeInfo) new LikesDeserializer().deserializeObject(getJsonObject(data, "like_info")));
		photo.setImages(new ImageDeserializer().deserializeArray(getJsonArray(data, "images"), Image.class));
		
		return photo;
	}
	
	public static class ImageDeserializer extends Deserializer
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
