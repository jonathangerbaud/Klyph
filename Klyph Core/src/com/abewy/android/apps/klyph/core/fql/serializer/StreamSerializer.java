package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class StreamSerializer extends Serializer
{
	@Override
	public JSONObject serializeObject(GraphObject object)
	{
		JSONObject json = new JSONObject();
		serializePrimitives(object, json);
		
		Stream stream = (Stream) object;

		TagSerializer ts = new TagSerializer();
		ts.serializeMap(stream.getMessage_tags(), json, "message_tags");
		ts.serializeMap(stream.getDescription_tags(), json, "description_tags");
		
		AttachmentSerializer as = new AttachmentSerializer();
		LikesSerializer ls = new LikesSerializer();
		PrivacySerializer ps = new PrivacySerializer();
		CommentsSerializer cs = new CommentsSerializer();
		EventSerializer es = new EventSerializer();
		PageSerializer pas = new PageSerializer();
		LinkSerializer lis = new LinkSerializer();
		PhotoSerializer phs = new PhotoSerializer();
		VideoSerializer vs = new VideoSerializer();
		StatusSerializer ss = new StatusSerializer();
		ApplicationSerializer aps = new ApplicationSerializer();
		StreamSerializer sts = new StreamSerializer();

		try
		{
			json.put("attachment", as.serializeObject(stream.getAttachment()));
			json.put("privacy", ps.serializeObject(stream.getPrivacy()));
			json.put("like_info", ls.serializeObject(stream.getLike_info()));
			json.put("comment_info", cs.serializeObject(stream.getComment_info()));
			json.put("event", es.serializeObject(stream.getEvent()));
			json.put("liked_pages", pas.serializeArray(stream.getLiked_pages()));
			json.put("link", lis.serializeObject(stream.getLink()));
			json.put("photo", phs.serializeObject(stream.getPhoto()));
			json.put("video", vs.serializeObject(stream.getVideo()));
			json.put("status", ss.serializeObject(stream.getStatus()));
			json.put("application", aps.serializeObject(stream.getApplication()));
			
			if (stream.getParent_stream() != null)
				json.put("parent_stream", sts.serializeObject(stream.getParent_stream()));
		}
		catch (JSONException e)
		{
			Log.d("StreamSerializer", "JSONException " + e.getMessage());
		}
		
		return json;
	}
	
	public static class CommentsSerializer extends Serializer
	{
		
	}
}
