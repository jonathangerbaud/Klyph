package com.abewy.android.apps.klyph.request;

import android.os.Bundle;
import com.abewy.android.apps.klyph.core.request.BaseAsyncRequest;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.RequestQuery;
import com.abewy.android.apps.klyph.facebook.request.AlbumPhotosAllRequest;
import com.abewy.android.apps.klyph.facebook.request.AlbumPhotosRequest;
import com.abewy.android.apps.klyph.facebook.request.AlbumRequest;
import com.abewy.android.apps.klyph.facebook.request.AlbumTaggedPhotosRequest;
import com.abewy.android.apps.klyph.facebook.request.AlbumVideosAllRequest;
import com.abewy.android.apps.klyph.facebook.request.AlbumVideosRequest;
import com.abewy.android.apps.klyph.facebook.request.AllFriendsRequest;
import com.abewy.android.apps.klyph.facebook.request.AllPhotosRequest;
import com.abewy.android.apps.klyph.facebook.request.AlternativeNewsFeedRequest;
import com.abewy.android.apps.klyph.facebook.request.BirthdayNotificationRequest;
import com.abewy.android.apps.klyph.facebook.request.BirthdayRequest;
import com.abewy.android.apps.klyph.facebook.request.CommentsRequest;
import com.abewy.android.apps.klyph.facebook.request.DeleteObjectRequest;
import com.abewy.android.apps.klyph.facebook.request.DeleteStatusRequest;
import com.abewy.android.apps.klyph.facebook.request.ElementAlbumRequest;
import com.abewy.android.apps.klyph.facebook.request.ElementEventRequest;
import com.abewy.android.apps.klyph.facebook.request.ElementPageRequest;
import com.abewy.android.apps.klyph.facebook.request.ElementTimelineNewestRequest;
import com.abewy.android.apps.klyph.facebook.request.EventDeclinedRequest;
import com.abewy.android.apps.klyph.facebook.request.EventGoingRequest;
import com.abewy.android.apps.klyph.facebook.request.EventInvitedRequest;
import com.abewy.android.apps.klyph.facebook.request.EventMaybeRequest;
import com.abewy.android.apps.klyph.facebook.request.EventRequest;
import com.abewy.android.apps.klyph.facebook.request.EventTimelineNewestRequest;
import com.abewy.android.apps.klyph.facebook.request.EventTimelineRequest;
import com.abewy.android.apps.klyph.facebook.request.FollowedPeopleRequest;
import com.abewy.android.apps.klyph.facebook.request.FriendListsRequest;
import com.abewy.android.apps.klyph.facebook.request.FriendsRequest;
import com.abewy.android.apps.klyph.facebook.request.FriendsRequestNotificationRequest;
import com.abewy.android.apps.klyph.facebook.request.GroupMembersRequest;
import com.abewy.android.apps.klyph.facebook.request.GroupPhotosRequest;
import com.abewy.android.apps.klyph.facebook.request.GroupProfileRequest;
import com.abewy.android.apps.klyph.facebook.request.GroupTimelineNewestRequest;
import com.abewy.android.apps.klyph.facebook.request.GroupTimelineRequest;
import com.abewy.android.apps.klyph.facebook.request.GroupsRequest;
import com.abewy.android.apps.klyph.facebook.request.LinkRequest;
import com.abewy.android.apps.klyph.facebook.request.MessageRequest;
import com.abewy.android.apps.klyph.facebook.request.NewAlbumRequest;
import com.abewy.android.apps.klyph.facebook.request.NewsFeedFriendListRequest;
import com.abewy.android.apps.klyph.facebook.request.NewsFeedNewestRequest;
import com.abewy.android.apps.klyph.facebook.request.NewsFeedRequest;
import com.abewy.android.apps.klyph.facebook.request.NotificationNewestRequest;
import com.abewy.android.apps.klyph.facebook.request.NotificationRequest;
import com.abewy.android.apps.klyph.facebook.request.PageProfileRequest;
import com.abewy.android.apps.klyph.facebook.request.PageRequest;
import com.abewy.android.apps.klyph.facebook.request.PageTimelineFeedNewestRequest;
import com.abewy.android.apps.klyph.facebook.request.PageTimelineNewestRequest;
import com.abewy.android.apps.klyph.facebook.request.PeriodicNotificationRequest;
import com.abewy.android.apps.klyph.facebook.request.PhotoListRequest;
import com.abewy.android.apps.klyph.facebook.request.PhotoRequest;
import com.abewy.android.apps.klyph.facebook.request.PostAttendEventRequest;
import com.abewy.android.apps.klyph.facebook.request.PostCommentRequest;
import com.abewy.android.apps.klyph.facebook.request.PostDeclineEventRequest;
import com.abewy.android.apps.klyph.facebook.request.PostLikeRequest;
import com.abewy.android.apps.klyph.facebook.request.PostPokeRequest;
import com.abewy.android.apps.klyph.facebook.request.PostReadNotificationRequest;
import com.abewy.android.apps.klyph.facebook.request.PostStatusRequest;
import com.abewy.android.apps.klyph.facebook.request.PostUnlikeRequest;
import com.abewy.android.apps.klyph.facebook.request.PostUnsureEventRequest;
import com.abewy.android.apps.klyph.facebook.request.ProfileUrlRequest;
import com.abewy.android.apps.klyph.facebook.request.SearchEventRequest;
import com.abewy.android.apps.klyph.facebook.request.SearchGroupRequest;
import com.abewy.android.apps.klyph.facebook.request.SearchPageRequest;
import com.abewy.android.apps.klyph.facebook.request.SearchUserRequest;
import com.abewy.android.apps.klyph.facebook.request.StatusRequest;
import com.abewy.android.apps.klyph.facebook.request.StreamGroupRequest;
import com.abewy.android.apps.klyph.facebook.request.StreamRequest;
import com.abewy.android.apps.klyph.facebook.request.ThreadRequest;
import com.abewy.android.apps.klyph.facebook.request.UploadableAlbumRequest;
import com.abewy.android.apps.klyph.facebook.request.UserLikeRequest;
import com.abewy.android.apps.klyph.facebook.request.UserProfilePhotoRequest;
import com.abewy.android.apps.klyph.facebook.request.UserProfileRequest;
import com.abewy.android.apps.klyph.facebook.request.UserRequest;
import com.abewy.android.apps.klyph.facebook.request.UserTimelineFeedNewestRequest;
import com.abewy.android.apps.klyph.facebook.request.UserTimelineFeedRequest;
import com.abewy.android.apps.klyph.facebook.request.UserTimelineNewestRequest;
import com.abewy.android.apps.klyph.facebook.request.UserTimelineRequest;
import com.abewy.android.apps.klyph.facebook.request.VideoRequest;
import com.crashlytics.android.Crashlytics;

public class AsyncRequest extends BaseAsyncRequest
{
	public AsyncRequest(int query, String id, String offset, Callback callBack)
	{
		super(query, id, offset, callBack);
	}

	public AsyncRequest(int query, String id, Bundle params, Callback callBack)
	{
		super(query, id, params, callBack);
	}

	public static final class Query
	{
		public static final int	NONE						= -1;
		public static final int	USER						= 1;
		public static final int	PAGE						= 2;
		public static final int	EVENT						= 3;
		public static final int	NEWSFEED					= 4;
		public static final int	NEWSFEED_NEWEST				= 5;
		public static final int	ELEMENT						= 6;
		public static final int	USER_TIMELINE				= 7;
		public static final int	USER_TIMELINE_NEWEST		= 8;
		public static final int	USER_TIMELINE_FEED			= 9;
		public static final int	USER_TIMELINE_FEED_NEWEST	= 10;
		public static final int	PAGE_TIMELINE				= 11;
		public static final int	PAGE_TIMELINE_NEWEST		= 12;
		public static final int	PAGE_TIMELINE_FEED			= 13;
		public static final int	PAGE_TIMELINE_FEED_NEWEST	= 14;
		public static final int	GROUP_TIMELINE				= 15;
		public static final int	GROUP_TIMELINE_NEWEST		= 16;
		public static final int	ELEMENT_TIMELINE_NEWEST		= 17;
		public static final int	ELEMENT_ALBUMS				= 18;
		public static final int	ALBUM_PHOTOS				= 19;
		public static final int	ALBUM_TAGGED_PHOTOS			= 20;
		public static final int	ALBUM_PHOTOS_ALL			= 21;
		public static final int	ELEMENT_PAGES				= 22;
		public static final int	ELEMENT_EVENTS				= 23;
		public static final int	ALBUM_VIDEOS				= 24;
		public static final int	ALBUM_VIDEOS_ALL			= 25;
		public static final int	BIRTHDAYS					= 28;
		public static final int	NOTIFICATIONS				= 29;
		public static final int	NOTIFICATIONS_NEWEST		= 30;
		public static final int	FRIENDS						= 31;
		public static final int	EVENT_GOING					= 32;
		public static final int	EVENT_MAYBE					= 33;
		public static final int	EVENT_DECLINED				= 34;
		public static final int	EVENT_INVITED				= 35;
		public static final int	STREAM						= 36;
		public static final int	POST_LIKE					= 39;
		public static final int	POST_UNLIKE					= 40;
		public static final int	USER_LIKE					= 41;
		public static final int	PHOTO						= 42;
		public static final int	ALL_FRIENDS					= 43;
		public static final int	POST_STATUS					= 44;
		public static final int	POST_COMMENT				= 45;
		public static final int	POST_EVENT_ATTEND			= 46;
		public static final int	POST_EVENT_UNSURE			= 47;
		public static final int	POST_EVENT_DECLINE			= 48;
		public static final int	EVENT_TIMELINE				= 49;
		public static final int	EVENT_TIMELINE_NEWEST		= 50;
		public static final int	PHOTO_LIST					= 51;
		public static final int	DELETE_POST					= 52;
		public static final int	PROFILE_URL					= 53;
		public static final int	USER_PROFILE_PHOTO			= 54;
		public static final int	DELETE_OBJECT				= 55;
		public static final int	PERIODIC_NOTIFICATIONS		= 56;
		public static final int	BIRTHDAY_NOTIFICATIONS		= 57;
		public static final int	FRIEND_REQUEST_NOTIFICATION	= 58;
		public static final int	GROUPS						= 59;
		public static final int	GROUP_MEMBERS				= 60;
		public static final int	GROUP_PHOTOS				= 61;
		public static final int	UPLOADABLE_ALBUM			= 64;
		public static final int	STATUS						= 65;
		public static final int	LINK						= 66;
		public static final int	NEW_ALBUM					= 68;
		public static final int	FOLLOWED_PEOPLE				= 69;
		public static final int	POST_READ_NOTIFICATION		= 70;
		public static final int	ALL_PHOTOS					= 71;
		public static final int	VIDEO						= 72;
		public static final int	ALBUM						= 73;
		public static final int	SEARCH_USER					= 74;
		public static final int	SEARCH_PAGE					= 75;
		public static final int	SEARCH_GROUP				= 76;
		public static final int	SEARCH_EVENT				= 77;
		public static final int	STREAM_GROUP_REQUEST		= 78;
		public static final int	POKE						= 79;
		public static final int	COMMENTS					= 80;
		public static final int	THREADS						= 81;
		public static final int	MESSAGES					= 82;
		public static final int	USER_PROFILE				= 83;
		public static final int	PAGE_PROFILE				= 84;
		public static final int	GROUP_PROFILE				= 85;
		public static final int	ALTERNATIVE_NEWSFEED		= 86;
		public static final int	FRIEND_LISTS				= 87;
		public static final int	FRIEND_LIST_NEWSFEED		= 88;
		public static final int	EDIT_COMMENT				= 89;
	}

	@Override
	protected void doCallBack(RequestError error)
	{
		// Crashlytics report on request error
		if (getQuery() == Query.NEWSFEED || getQuery() == Query.NEWSFEED_NEWEST)
		{
			Crashlytics.setString("Query " + getQuery(), error.getMessage());

			try
			{
				throw new Exception("Class : " + this.getClass().getName() + "\n, Request " + getQuery() + ", Id " + getId() + ", Offset "
									+ getOffset() + "\n, Error " + error.getMessage());
			}
			catch (Exception e)
			{
				Crashlytics.logException(e);
			}
		}

		super.doCallBack(error, null, null);
	}

	@Override
	protected RequestQuery getSubQuery(int query)
	{
		switch (query)
		{
			case Query.USER:
			{
				return new UserRequest();
			}
			case Query.PAGE:
			{
				return new PageRequest();
			}
			case Query.EVENT:
			{
				return new EventRequest();
			}
			case Query.NEWSFEED:
			{
				return new NewsFeedRequest();
			}
			case Query.NEWSFEED_NEWEST:
			{
				return new NewsFeedNewestRequest();
			}
			// ToDo
			case Query.ELEMENT:
			{
				return new PostCommentRequest();
			}
			case Query.USER_TIMELINE:
			{
				return new UserTimelineRequest();
			}
			case Query.USER_TIMELINE_NEWEST:
			{
				return new UserTimelineNewestRequest();
			}
			case Query.USER_TIMELINE_FEED:
			{
				return new UserTimelineFeedRequest();
			}
			case Query.USER_TIMELINE_FEED_NEWEST:
			{
				return new UserTimelineFeedNewestRequest();
			}
			case Query.PAGE_TIMELINE:
			{
				return new UserTimelineFeedRequest();
			}
			case Query.PAGE_TIMELINE_NEWEST:
			{
				return new PageTimelineNewestRequest();
			}
			case Query.PAGE_TIMELINE_FEED:
			{
				return new UserTimelineFeedRequest();
			}
			case Query.PAGE_TIMELINE_FEED_NEWEST:
			{
				return new PageTimelineFeedNewestRequest();
			}
			case Query.GROUP_TIMELINE:
			{
				return new GroupTimelineRequest();
			}
			case Query.GROUP_TIMELINE_NEWEST:
			{
				return new GroupTimelineNewestRequest();
			}
			case Query.ELEMENT_TIMELINE_NEWEST:
			{
				return new ElementTimelineNewestRequest();
			}
			case Query.ELEMENT_ALBUMS:
			{
				return new ElementAlbumRequest();
			}
			case Query.ALBUM_PHOTOS:
			{
				return new AlbumPhotosRequest();
			}
			case Query.ALBUM_TAGGED_PHOTOS:
			{
				return new AlbumTaggedPhotosRequest();
			}
			case Query.ALBUM_PHOTOS_ALL:
			{
				return new AlbumPhotosAllRequest();
			}
			case Query.ELEMENT_PAGES:
			{
				return new ElementPageRequest();
			}
			case Query.ELEMENT_EVENTS:
			{
				return new ElementEventRequest();
			}
			case Query.ALBUM_VIDEOS:
			{
				return new AlbumVideosRequest();
			}
			case Query.ALBUM_VIDEOS_ALL:
			{
				return new AlbumVideosAllRequest();
			}
			case Query.BIRTHDAYS:
			{
				return new BirthdayRequest();
			}
			case Query.NOTIFICATIONS:
			{
				return new NotificationRequest();
			}
			case Query.NOTIFICATIONS_NEWEST:
			{
				return new NotificationNewestRequest();
			}
			case Query.FRIENDS:
			{
				return new FriendsRequest();
			}
			case Query.EVENT_GOING:
			{
				return new EventGoingRequest();
			}
			case Query.EVENT_MAYBE:
			{
				return new EventMaybeRequest();
			}
			case Query.EVENT_DECLINED:
			{
				return new EventDeclinedRequest();
			}
			case Query.EVENT_INVITED:
			{
				return new EventInvitedRequest();
			}
			case Query.STREAM:
			{
				return new StreamRequest();
			}
			case Query.POST_LIKE:
			{
				return new PostLikeRequest();
			}
			case Query.POST_UNLIKE:
			{
				return new PostUnlikeRequest();
			}
			case Query.USER_LIKE:
			{
				return new UserLikeRequest();
			}
			case Query.PHOTO:
			{
				return new PhotoRequest();
			}
			case Query.ALL_FRIENDS:
			{
				return new AllFriendsRequest();
			}
			case Query.POST_STATUS:
			{
				return new PostStatusRequest();
			}
			case Query.POST_COMMENT:
			{
				return new PostCommentRequest();
			}
			case Query.POST_EVENT_ATTEND:
			{
				return new PostAttendEventRequest();
			}
			case Query.POST_EVENT_UNSURE:
			{
				return new PostUnsureEventRequest();
			}
			case Query.POST_EVENT_DECLINE:
			{
				return new PostDeclineEventRequest();
			}
			case Query.EVENT_TIMELINE:
			{
				return new EventTimelineRequest();
			}
			case Query.EVENT_TIMELINE_NEWEST:
			{
				return new EventTimelineNewestRequest();
			}
			case Query.PHOTO_LIST:
			{
				return new PhotoListRequest();
			}
			case Query.DELETE_POST:
			{
				return new DeleteStatusRequest();
			}
			case Query.PROFILE_URL:
			{
				return new ProfileUrlRequest();
			}
			case Query.USER_PROFILE_PHOTO:
			{
				return new UserProfilePhotoRequest();
			}
			case Query.DELETE_OBJECT:
			{
				return new DeleteObjectRequest();
			}
			case Query.PERIODIC_NOTIFICATIONS:
			{
				return new PeriodicNotificationRequest();
			}
			case Query.BIRTHDAY_NOTIFICATIONS:
			{
				return new BirthdayNotificationRequest();
			}
			case Query.FRIEND_REQUEST_NOTIFICATION:
			{
				return new FriendsRequestNotificationRequest();
			}
			case Query.GROUPS:
			{
				return new GroupsRequest();
			}
			case Query.GROUP_MEMBERS:
			{
				return new GroupMembersRequest();
			}
			case Query.GROUP_PHOTOS:
			{
				return new GroupPhotosRequest();
			}
			case Query.UPLOADABLE_ALBUM:
			{
				return new UploadableAlbumRequest();
			}
			case Query.STATUS:
			{
				return new StatusRequest();
			}
			case Query.LINK:
			{
				return new LinkRequest();
			}
			case Query.NEW_ALBUM:
			{
				return new NewAlbumRequest();
			}
			case Query.FOLLOWED_PEOPLE:
			{
				return new FollowedPeopleRequest();
			}
			case Query.POST_READ_NOTIFICATION:
			{
				return new PostReadNotificationRequest();
			}
			case Query.ALL_PHOTOS:
			{
				return new AllPhotosRequest();
			}
			case Query.VIDEO:
			{
				return new VideoRequest();
			}
			case Query.ALBUM:
			{
				return new AlbumRequest();
			}
			case Query.SEARCH_USER:
			{
				return new SearchUserRequest();
			}
			case Query.SEARCH_PAGE:
			{
				return new SearchPageRequest();
			}
			case Query.SEARCH_GROUP:
			{
				return new SearchGroupRequest();
			}
			case Query.SEARCH_EVENT:
			{
				return new SearchEventRequest();
			}
			case Query.STREAM_GROUP_REQUEST:
			{
				return new StreamGroupRequest();
			}
			case Query.POKE:
			{
				return new PostPokeRequest();
			}
			case Query.COMMENTS:
			{
				return new CommentsRequest();
			}
			case Query.THREADS:
			{
				return new ThreadRequest();
			}
			case Query.MESSAGES:
			{
				return new MessageRequest();
			}
			case Query.USER_PROFILE:
			{
				return new UserProfileRequest();
			}
			case Query.PAGE_PROFILE:
			{
				return new PageProfileRequest();
			}
			case Query.GROUP_PROFILE:
			{
				return new GroupProfileRequest();
			}
			case Query.ALTERNATIVE_NEWSFEED:
			{
				return new AlternativeNewsFeedRequest();
			}
			case Query.FRIEND_LISTS:
			{
				return new FriendListsRequest();
			}
			case Query.FRIEND_LIST_NEWSFEED:
			{
				return new NewsFeedFriendListRequest();
			}
		}

		return null;
	}
}