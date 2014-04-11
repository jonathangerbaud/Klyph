package com.abewy.android.apps.klyph.facebook.request;

import com.facebook.HttpMethod;

public class PostLikeRequest extends KlyphQuery
{
	@Override
	public boolean isFQL()
	{
		return false;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		return "/" + id + "/likes";
	}
	
	@Override
	public HttpMethod getHttpMethod()
	{
		return HttpMethod.POST;
	}
}
