package com.abewy.android.apps.klyph.facebook.request;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.util.Log;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.serializer.CommentDeserializer;

public class CommentsRequest extends KlyphQuery
{
	private String cursor;
	@Override
	public boolean isMultiQuery()
	{
		return true;
	}
	
	@Override
	public boolean isFQL()
	{
		return false;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		cursor = offset;
		return "/" + id + "/comments";
	}

	@Override
	public Bundle getParams()
	{
		Bundle params = new Bundle();
		
		// Get only top levels comments, replies are in the comments connection field
		//params.putString("filter", "toplevel");
		//params.putString("fields", "id,from.fields(id,name,picture.type(large)),message,attachment,created_time,like_count,user_likes,parent,can_comment,comment_count");
		
		// Get all comments in chronlogical order
		// So we must remove replies afterwards
		params.putString("filter", "stream");
		params.putString("limit", "50");
		params.putString("fields", "id,from.fields(id,name,picture.type(large)),message,attachment,created_time,like_count,user_likes,parent,can_comment,comment_count,comments.limit(15).fields(id,from.fields(id,name,picture.type(large)),message,attachment,created_time,like_count,user_likes,parent,can_comment,comment_count)");
		
		if (getOffset(cursor, "").length() > 0)
			params.putString("after", cursor);
		
		return params;
	}
	
	@Override
	public ArrayList<GraphObject> handleResult(JSONArray result)
	{
		JSONArray jsonArray = new JSONArray();
		
		int n = result.length();
		for (int i = 0; i < n; i++)
		{
			JSONObject comment = result.optJSONObject(i);
			
			if (comment != null)
			{
				JSONObject parent = comment.optJSONObject("parent");
				
				// If this is a reply, then we skip it
				if (parent != null)
				{
					continue;
				}
				
				jsonArray.put(comment);
				
				JSONObject subComments = comment.optJSONObject("comments");
				
				if (subComments != null)
				{
					JSONArray data = subComments.optJSONArray("data");
					
					if (data != null)
					{
						int m = data.length();
						for (int j = 0; j < m; j++)
						{
							JSONObject subComment = data.optJSONObject(j);
							
							if (subComment != null)
							{
								jsonArray.put(subComment);
							}
						}
					}
				}
			}
		}
		
		CommentDeserializer deserializer = new CommentDeserializer();
		ArrayList<GraphObject> comments = (ArrayList<GraphObject>) deserializer.deserializeArray(jsonArray);
		
		setHasMoreData(comments.size() > 0);

		return comments;
	}
}
