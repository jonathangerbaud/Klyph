package com.abewy.android.apps.klyph.core.graph.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.UserRef;
import com.abewy.android.apps.klyph.core.graph.UserRef.Picture;

public class UserRefDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		UserRef userRef = new UserRef();

		deserializePrimitives(userRef, data);
		
		JSONObject picture = getJsonObject(data, "picture");
		JSONObject pictureData = picture.optJSONObject("data");
		
		if (pictureData != null)
		{
			userRef.setPicture((Picture) new PictureDeserializer().deserializeObject(pictureData));
		}
		else
		{
			userRef.setPicture((Picture) new PictureDeserializer().deserializeObject(getJsonObject(picture, "data")));
		}

		return userRef;
	}
	
	private static class PictureDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			UserRef.Picture picture = new UserRef.Picture();

			deserializePrimitives(picture, data);

			return picture;
		}
	}
}
