package com.abewy.android.apps.klyph.core.graph.serializer;

import java.util.ArrayList;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.Photo;
import com.abewy.android.apps.klyph.core.graph.Place;
import com.abewy.android.apps.klyph.core.graph.Tag;
import com.abewy.android.apps.klyph.core.graph.UserRef;

public class PhotoDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Photo photo = new Photo();

		deserializePrimitives(photo, data);

		photo.setFrom((UserRef) new UserRefDeserializer().deserializeObject(getJsonObject(data, "from")));
		photo.setPlace((Place) new PlaceDeserializer().deserializeObject(getJsonObject(data, "place")));
		
		if (data.optJSONArray("tags") != null)
			photo.setTags(new TagDeserializer().deserializeArray(getJsonArray(data, "tags"), Tag.class));
		else
			photo.setTags(new ArrayList<Tag>());
		
		if (data.optJSONArray("name_tags") != null)
			photo.setName_tags(new TagDeserializer().deserializeMap(getJsonArray(data, "name_tags")));
		else
			photo.setName_tags(new TagDeserializer().deserializeMap(getJsonObject(data, "name_tags")));
		
		
		
		return photo;
	}
}
