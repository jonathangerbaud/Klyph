package com.abewy.android.apps.klyph.facebook.request;

import com.facebook.HttpMethod;

/**
 * Params
 * name (string)
 * location (string)
 * description (string)
 * privacy (json {value:PRIVACY_VALUE})
 */
public class NewAlbumRequest extends KlyphQuery
{
	@Override
	public boolean isFQL()
	{
		return false;
	}
	
	@Override
	public boolean returnId()
	{
		return true;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		return "/me/albums";
	}
	
	public HttpMethod getHttpMethod()
	{
		return HttpMethod.POST;
	}
}