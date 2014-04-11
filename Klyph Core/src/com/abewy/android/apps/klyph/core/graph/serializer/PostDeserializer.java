package com.abewy.android.apps.klyph.core.graph.serializer;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.graph.Application;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.Link;
import com.abewy.android.apps.klyph.core.graph.Photo;
import com.abewy.android.apps.klyph.core.graph.Post;
import com.abewy.android.apps.klyph.core.graph.Post.Action;
import com.abewy.android.apps.klyph.core.graph.Post.Privacy;
import com.abewy.android.apps.klyph.core.graph.UserRef;
import com.abewy.android.apps.klyph.core.graph.Video;

public class PostDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Post post = new Post();

		deserializePrimitives(post, data);

		post.setFrom((UserRef) new UserRefDeserializer().deserializeObject(getJsonObject(data, "from")));
		post.setApplication((Application) new ApplicationDeserializer().deserializeObject(getJsonObject(data, "application")));
		post.setPrivacy((Privacy) new PrivacyDeserializer().deserializeObject(getJsonObject(data, "privacy")));
		
		JSONObject to = data.optJSONObject("to"); 
		if (to != null)
		{
			JSONArray toArray = to.optJSONArray("data");
			
			if (toArray != null && toArray.length() > 0)
			{
				post.setTo(new UserRefDeserializer().deserializeArray(toArray, UserRef.class));
			}
			else
			{
				post.setTo(new ArrayList<UserRef>());
			}
		}
		else
		{
			post.setTo(new ArrayList<UserRef>());
		}
		
		if (data.optJSONArray("with_tags") != null)
			post.setWith_tags(new UserRefDeserializer().deserializeArray(getJsonArray(data, "with_tags"), UserRef.class));
		else
			post.setWith_tags(new ArrayList<UserRef>());
		
		if (data.optJSONArray("actions") != null)
			post.setActions(new ActionDeserializer().deserializeArray(getJsonArray(data, "actions"), Action.class));
		else
			post.setActions(new ArrayList<Action>());
		
		if (data.optJSONArray("message_tags") != null)
			post.setMessage_tags(new TagDeserializer().deserializeMap(getJsonArray(data, "message_tags")));
		else
			post.setMessage_tags(new TagDeserializer().deserializeMap(getJsonObject(data, "message_tags")));
		
		if (data.optJSONArray("story_tags") != null)
			post.setStory_tags(new TagDeserializer().deserializeMap(getJsonArray(data, "story_tags")));
		else
			post.setStory_tags(new TagDeserializer().deserializeMap(getJsonObject(data, "story_tags")));
		
		if (data.optJSONObject("photoObject") != null)
			post.setPhotoObject((Photo) new PhotoDeserializer().deserializeObject(getJsonObject(data, "photoObject")));
		
		if (data.optJSONObject("videoObject") != null)
			post.setVideoObject((Video) new VideoDeserializer().deserializeObject(getJsonObject(data, "videoObject")));
		
		if (data.optJSONObject("linkObject") != null)
			post.setLinkObject((Link) new LinkDeserializer().deserializeObject(getJsonObject(data, "linkObject")));
		
		return post;
	}

	private static class ActionDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Action action = new Action();

			deserializePrimitives(action, data);

			return action;
		}
	}
	
	private static class PrivacyDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			Privacy privacy = new Privacy();

			deserializePrimitives(privacy, data);

			return privacy;
		}
	}
}
