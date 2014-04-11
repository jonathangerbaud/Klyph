package com.abewy.android.apps.klyph.core.graph.serializer;

import java.util.ArrayList;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.Tag;
import com.abewy.android.apps.klyph.core.graph.UserRef;
import com.abewy.android.apps.klyph.core.graph.Video;

public class VideoDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Video video = new Video();

		deserializePrimitives(video, data);

		video.setFrom((UserRef) new UserRefDeserializer().deserializeObject(getJsonObject(data, "from")));
		
		if (data.optJSONArray("tags") != null)
			video.setTags(new TagDeserializer().deserializeArray(getJsonArray(data, "tags"), Tag.class));
		else
			video.setTags(new ArrayList<Tag>());
		
		return video;
	}
}
