package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Media;
import com.abewy.android.apps.klyph.core.fql.Media.Photo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class MediaSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);
		
		Media media = (Media) object;

		PhotoSerializer ps = new PhotoSerializer();
		VideoSerializer vs = new VideoSerializer();
		SwfSerializer swfs = new SwfSerializer();
		
		try
		{
			json.put("photo", ps.serializeObject(media.getPhoto()));
			json.put("video", vs.serializeObject(media.getVideo()));
			json.put("swf", swfs.serializeObject(media.getSwf()));
		}
		catch (JSONException e)
		{
			Log.d("MediaSerializer", "JsonException " + e);
		}
		
		return json;
	}
	
	private static class PhotoSerializer extends Serializer
	{
		@Override
		public JSONObject serializeObject(GraphObject object)
		{
			JSONObject json = new JSONObject();
			serializePrimitives(object, json);
			
			Photo photo = (Photo) object;

			ImageSerializer is = new ImageSerializer();
			try
			{
				json.put("images", is.serializeArray(photo.getImages()));
			}
			catch (JSONException e)
			{
				Log.d("PhotoSerializer", "JsonException " + e);
			}
			
			return json;
		}
		
		private static class ImageSerializer extends Serializer
		{
			
		}
	}
	
	private static class SwfSerializer extends Serializer
	{
		
	}
}
