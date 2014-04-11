package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Group;
import com.abewy.android.apps.klyph.core.fql.Location;
import com.abewy.android.apps.klyph.core.fql.Group.Cover;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class GroupDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Group group = new Group();
		
		deserializePrimitives(group, data);
		group.setPic_cover((Cover) new CoverDeserializer().deserializeObject(getJsonObject(data, "pic_cover")));
		
		group.setVenue((Location) new LocationDeserializer().deserializeObject(getJsonObject(data, "venue")));
		
		return group;
	}
	
	private static class CoverDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Cover cover = new Cover();
			
			deserializePrimitives(cover, data);
			
			return cover;
		}
	}
}
