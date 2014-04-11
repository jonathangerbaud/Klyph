package com.abewy.android.apps.klyph.core.fql.serializer;

import java.util.ArrayList;
import org.json.JSONObject;
import com.abewy.android.apps.klyph.core.fql.Application;
import com.abewy.android.apps.klyph.core.fql.Attachment;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.fql.LikeInfo;
import com.abewy.android.apps.klyph.core.fql.Link;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Privacy;
import com.abewy.android.apps.klyph.core.fql.Status;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.core.fql.Stream.CommentInfo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class StreamDeserializer extends Deserializer
{
	@Override
	public GraphObject deserializeObject(JSONObject data)
	{
		Stream stream = new Stream();

		deserializePrimitives(stream, data);

		stream.setAttachment((Attachment) new AttachmentDeserializer().deserializeObject(getJsonObject(data, "attachment")));
		stream.setLike_info((LikeInfo) new LikesDeserializer().deserializeObject(getJsonObject(data, "like_info")));
		stream.setPrivacy((Privacy) new PrivacyDeserializer().deserializeObject(getJsonObject(data, "privacy")));
		stream.setComment_info((CommentInfo) new CommentsDeserializer().deserializeObject(getJsonObject(data, "comment_info")));
		stream.setLiked_pages(new PageDeserializer().deserializeArray(getJsonArray(data, "liked_pages"), Page.class));
		stream.setEvent((Event) new EventDeserializer().deserializeObject(getJsonObject(data, "event")));
		stream.setLink((Link) new LinkDeserializer().deserializeObject(getJsonObject(data, "link")));
		stream.setPhoto((Photo) new PhotoDeserializer().deserializeObject(getJsonObject(data, "photo")));
		stream.setVideo((Video) new VideoDeserializer().deserializeObject(getJsonObject(data, "video")));
		stream.setStatus((Status) new StatusDeserializer().deserializeObject(getJsonObject(data, "status")));
		stream.setApplication((Application) new ApplicationDeserializer().deserializeObject(getJsonObject(data, "application")));
		
		if (data.optJSONObject("parent_stream") != null)
			stream.setParent_stream((Stream) new StreamDeserializer().deserializeObject(getJsonObject(data, "parent_stream")));
		
		if (data.optJSONArray("message_tags") != null)
			stream.setMessage_tags(new TagDeserializer().deserializeMap(getJsonArray(data, "message_tags")));
		else
			stream.setMessage_tags(new TagDeserializer().deserializeMap(getJsonObject(data, "message_tags")));
		
		if (data.optJSONArray("description_tags") != null)
			stream.setDescription_tags(new TagDeserializer().deserializeMap(getJsonArray(data, "description_tags")));
		else
			stream.setDescription_tags(new TagDeserializer().deserializeMap(getJsonObject(data, "description_tags")));
		
		if (data.optJSONArray("tagged_tags") != null)
		{
			
			stream.setTagged_tags(new TagDeserializer().deserializeArray(getJsonArray(data, "tagged_tags"), Tag.class));
		}
		else
			stream.setTagged_tags(new ArrayList<Tag>());
		
		return stream;
	}

	public static class CommentsDeserializer extends Deserializer
	{
		@Override
		public GraphObject deserializeObject(JSONObject data)
		{
			CommentInfo comments = new CommentInfo();

			deserializePrimitives(comments, data);

			return comments;
		}
	}
}
