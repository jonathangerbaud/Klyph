package com.abewy.android.apps.klyph.facebook.request;

import com.facebook.HttpMethod;
import android.os.Bundle;
import android.util.Log;

public class EditCommentRequest extends KlyphQuery
{	
	private String message;
	
	@Override
	public boolean isFQL()
	{
		return false;
	}

	@Override
	public String getQuery(String id, String offset)
	{
		this.message = offset;
		
		return "/" + id + "/";
	}

	@Override
	public Bundle getParams()
	{
		Bundle params = new Bundle();
		Log.d("EditCommentRequest", "getParams: " + message);
		params.putString("message", message);
		
		return params;
	}
	
	@Override
	public HttpMethod getHttpMethod()
	{
		return HttpMethod.POST;
	}
}
