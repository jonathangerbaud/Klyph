package com.abewy.android.apps.klyph.facebook.request;

public class UserTimelineNewestRequest extends UserTimelineRequest
{
	@Override
	protected boolean isNewest()
	{
		return true;
	}
}
