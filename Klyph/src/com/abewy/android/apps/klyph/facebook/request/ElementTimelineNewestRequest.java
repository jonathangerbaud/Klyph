package com.abewy.android.apps.klyph.facebook.request;

import com.abewy.android.apps.klyph.facebook.request.base.ElementTimelineRequest;

public class ElementTimelineNewestRequest extends ElementTimelineRequest
{
	@Override
	protected boolean isNewest()
	{
		return true;
	}
}
