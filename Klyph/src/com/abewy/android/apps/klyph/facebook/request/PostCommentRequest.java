package com.abewy.android.apps.klyph.facebook.request;

import com.facebook.HttpMethod;

public class PostCommentRequest extends KlyphQuery
{
	@Override
	public boolean isFQL()
	{
		return false;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		return "/" + id + "/comments";
	}
	
	public HttpMethod getHttpMethod()
	{
		return HttpMethod.POST;
	}
}
