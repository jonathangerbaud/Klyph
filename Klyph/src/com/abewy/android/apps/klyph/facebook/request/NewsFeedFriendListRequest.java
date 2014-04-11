package com.abewy.android.apps.klyph.facebook.request;


public class NewsFeedFriendListRequest extends NewsFeedRequest
{
	@Override
	protected String getNewsfeedFilter(String id)
	{
		return  " source_id IN (SELECT uid FROM friendlist_member WHERE flid = " + id + ") AND filter_key=\"nf\" AND ";
	}
}
