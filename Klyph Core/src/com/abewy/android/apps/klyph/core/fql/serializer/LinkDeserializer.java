package com.abewy.android.apps.klyph.core.fql.serializer;

import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.LikeInfo;
import com.abewy.android.apps.klyph.core.fql.Link;
import com.abewy.android.apps.klyph.core.fql.Privacy;
import com.abewy.android.apps.klyph.core.fql.Stream.CommentInfo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class LinkDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Link link = new Link();

		deserializePrimitives(link, data);

		link.setComment_info((CommentInfo) new StreamDeserializer.CommentsDeserializer().deserializeObject(getJsonObject(data, "comment_info")));
		link.setLike_info((LikeInfo) new LikesDeserializer().deserializeObject(getJsonObject(data, "like_info")));
		link.setPrivacy((Privacy) new PrivacyDeserializer().deserializeObject(getJsonObject(data, "privacy")));
		
		
		return link;
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
