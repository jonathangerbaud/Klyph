package com.abewy.android.apps.klyph.facebook.request;

public class UserTimelineFeedNewestRequest extends UserTimelineFeedRequest
{
	@Override
	protected boolean isNewest()
	{
		return true;
	}
}
