package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Location;
import com.abewy.android.apps.klyph.core.fql.User;
import com.abewy.android.apps.klyph.core.fql.User.Cover;
import com.abewy.android.apps.klyph.core.fql.User.Education;
import com.abewy.android.apps.klyph.core.fql.User.IdName;
import com.abewy.android.apps.klyph.core.fql.User.Relative;
import com.abewy.android.apps.klyph.core.fql.User.Work;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UserDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		User user = new User();
		
		deserializePrimitives(user, data);
		user.setEducation(new EducationDeserializer().deserializeArray(getJsonArray(data, "education"), Education.class));
		user.setWork(new WorkDeserializer().deserializeArray(getJsonArray(data, "work"), Work.class));
		user.setPic_cover((Cover) new CoverDeserializer().deserializeObject(getJsonObject(data, "pic_cover")));
		user.setCurrent_address((Location) new LocationDeserializer().deserializeObject(getJsonObject(data, "current_address")));
		user.setCurrent_location((Location) new LocationDeserializer().deserializeObject(getJsonObject(data, "current_location")));
		user.setHometown_location((Location) new LocationDeserializer().deserializeObject(getJsonObject(data, "hometown_location")));
		user.setFamily(new RelativeDeserializer().deserializeArray(getJsonArray(data, "family"), Relative.class));
		
		return user;
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
	
	public static class EducationDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Education education = new Education();
			
			deserializePrimitives(education, data);
			education.setSchool((IdName) new IdNameDeserializer().deserializeObject(getJsonObject(data, "school")));
			education.setYear((IdName) new IdNameDeserializer().deserializeObject(getJsonObject(data, "year")));
			education.setConcentration((IdName) new IdNameDeserializer().deserializeObject(getJsonObject(data, "concentration")));
			
			return education;
		}
	}
	
	public static class WorkDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Work work = new Work();
			
			deserializePrimitives(work, data);
			work.setEmployer((IdName) new IdNameDeserializer().deserializeObject(getJsonObject(data, "employer")));
			work.setLocation((IdName) new IdNameDeserializer().deserializeObject(getJsonObject(data, "location")));
			work.setPosition((IdName) new IdNameDeserializer().deserializeObject(getJsonObject(data, "position")));
			
			return work;
		}
	}
	
	public static class IdNameDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			IdName idName = new IdName();
			
			deserializePrimitives(idName, data);
			
			return idName;
		}
	}
	
	private static class RelativeDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Relative relative = new Relative();
			
			deserializePrimitives(relative, data);
			
			return relative;
		}
	}
}
