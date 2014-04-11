package com.abewy.android.apps.klyph.facebook.request;

public class NotificationNewestRequest extends NotificationRequest
{
	@Override
	protected String getOffsetQuery(String offset)
	{
		if (offset != null)
			return " AND updated_time > " + offset;
		
		return "";
	}
}
