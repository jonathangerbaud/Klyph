package com.abewy.android.apps.klyph.facebook.request;


public class NewsFeedNewestRequest extends NewsFeedRequest
{
	@Override
	protected String getFilter(String offset)
	{
		return "created_time > " + offset + " AND ";
	}
	
	protected String getOrderBy()
	{
		
		return "DESC";
	}
}
