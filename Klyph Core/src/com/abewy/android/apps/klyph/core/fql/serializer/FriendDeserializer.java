package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.fql.User.Education;
import com.abewy.android.apps.klyph.core.fql.User.Work;
import com.abewy.android.apps.klyph.core.fql.serializer.UserDeserializer.EducationDeserializer;
import com.abewy.android.apps.klyph.core.fql.serializer.UserDeserializer.WorkDeserializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class FriendDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Friend friend = new Friend();
		
		deserializePrimitives(friend, data);
		
		friend.setEducation(new EducationDeserializer().deserializeArray(getJsonArray(data, "education"), Education.class));
		friend.setWork(new WorkDeserializer().deserializeArray(getJsonArray(data, "work"), Work.class));
		
		return friend;
	}
}
