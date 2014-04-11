package com.abewy.android.apps.klyph.facebook.request;

import com.facebook.HttpMethod;

public class PostUnlikeRequest extends PostLikeRequest
{
	@Override
	public HttpMethod getHttpMethod()
	{
		return HttpMethod.DELETE;
	}
}
