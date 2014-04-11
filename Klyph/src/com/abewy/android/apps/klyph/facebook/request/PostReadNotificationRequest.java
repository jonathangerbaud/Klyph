package com.abewy.android.apps.klyph.facebook.request;

import android.os.Bundle;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.facebook.HttpMethod;

public class PostReadNotificationRequest extends KlyphQuery
{
	@Override
	public boolean isFQL()
	{
		return false;
	}
	
	@Override
	public String getQuery(String id, String offset)
	{
		return "/notif_" + KlyphSession.getSessionUserId() + "_" + id;
	}
	
	@Override
	public HttpMethod getHttpMethod()
	{
		return HttpMethod.POST;
	}

	@Override
	public Bundle getParams()
	{
		Bundle bundle = new Bundle();
		bundle.putString("unread", "0");
		return bundle;
	}
	
	
}
