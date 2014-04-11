package com.abewy.android.apps.klyph.facebook.request;

import com.facebook.HttpMethod;

public class PostUnsureEventRequest extends KlyphQuery
{
	@Override
	public boolean isFQL()
	{
		return false;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		return "/" + id + "/maybe";
	}
	
	public HttpMethod getHttpMethod()
	{
		return HttpMethod.POST;
	}
}
