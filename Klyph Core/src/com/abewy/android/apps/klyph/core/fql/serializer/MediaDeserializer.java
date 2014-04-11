package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Media;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.core.fql.Media.Image;
import com.abewy.android.apps.klyph.core.fql.Media.Photo;
import com.abewy.android.apps.klyph.core.fql.Media.Swf;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class MediaDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Media media = new Media();

		deserializePrimitives(media, data);
		media.setPhoto((Photo) new PhotoDeserializer().deserializeObject(getJsonObject(data, "photo")));
		media.setVideo((Video) new VideoDeserializer().deserializeObject(getJsonObject(data, "video")));
		media.setSwf((Swf) new SwfDeserializer().deserializeObject(getJsonObject(data, "swf")));

		return media;
	}

	private static class PhotoDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Photo photo = new Photo();

			if (data != null)
			{
				deserializePrimitives(photo, data);

				photo.setImages(new ImageDeserializer().deserializeArray(getJsonArray(data, "images"), Image.class));

			}

			return photo;
		}

		private static class ImageDeserializer extends Deserializer
		{
			@Override
			public GraphObject deserializeObject(JSONObject data)
			{
				Image image = new Image();

				if (data != null)
				{
					deserializePrimitives(image, data);
				}

				return image;
			}
		}
	}

	private static class SwfDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Swf swf = new Swf();

			if (data != null)
			{
				deserializePrimitives(swf, data);
			}

			return swf;
		}
	}
}
