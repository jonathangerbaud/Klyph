package com.abewy.android.apps.klyph.adapter;

import android.util.Log;
import com.abewy.android.adapter.TypeAdapter;
import com.abewy.android.apps.klyph.adapter.fql.AlbumAdapter;
import com.abewy.android.apps.klyph.adapter.fql.BirthdayAdapter;
import com.abewy.android.apps.klyph.adapter.fql.CommentAdapter;
import com.abewy.android.apps.klyph.adapter.fql.EducationAdapter;
import com.abewy.android.apps.klyph.adapter.fql.ElementTimelineAdapter;
import com.abewy.android.apps.klyph.adapter.fql.EventAdapter;
import com.abewy.android.apps.klyph.adapter.fql.EventAdapter2;
import com.abewy.android.apps.klyph.adapter.fql.EventAttendeesAdapter;
import com.abewy.android.apps.klyph.adapter.fql.EventDataAdapter;
import com.abewy.android.apps.klyph.adapter.fql.EventResponseAdapter;
import com.abewy.android.apps.klyph.adapter.fql.FriendAdapter;
import com.abewy.android.apps.klyph.adapter.fql.FriendListAdapter;
import com.abewy.android.apps.klyph.adapter.fql.FriendPickerAdapter;
import com.abewy.android.apps.klyph.adapter.fql.FriendPickerSingleAdapter;
import com.abewy.android.apps.klyph.adapter.fql.FriendRequestAdapter;
import com.abewy.android.apps.klyph.adapter.fql.GroupAdapter;
import com.abewy.android.apps.klyph.adapter.fql.GroupTimelineAdapter;
import com.abewy.android.apps.klyph.adapter.fql.MemberAdapter;
import com.abewy.android.apps.klyph.adapter.fql.MessageAdapter;
import com.abewy.android.apps.klyph.adapter.fql.MessageSessionUserAdapter;
import com.abewy.android.apps.klyph.adapter.fql.NotificationAdapter;
import com.abewy.android.apps.klyph.adapter.fql.PageAdapter;
import com.abewy.android.apps.klyph.adapter.fql.PhotoAdapter;
import com.abewy.android.apps.klyph.adapter.fql.PhotoListAdapter;
import com.abewy.android.apps.klyph.adapter.fql.RelativeAdapter;
import com.abewy.android.apps.klyph.adapter.fql.StreamAdapter;
import com.abewy.android.apps.klyph.adapter.fql.StreamAdapter2;
import com.abewy.android.apps.klyph.adapter.fql.StreamLikeAdapter;
import com.abewy.android.apps.klyph.adapter.fql.TagAdapter;
import com.abewy.android.apps.klyph.adapter.fql.ThreadAdapter;
import com.abewy.android.apps.klyph.adapter.fql.VideoAdapter;
import com.abewy.android.apps.klyph.adapter.fql.WorkAdapter;
import com.abewy.android.apps.klyph.adapter.items.FakeHeaderItemAdapter;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.Message;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class AdapterSelector
{
	public static final int	STREAM_LIKE_COUNT		= 1000;
	public static final int	EVENT_DATA				= 1001;
	public static final int	EVENT_RESPONSE			= 1002;
	public static final int	EVENT_ATTENDEES			= 1003;
	public static final int	MESSAGE_USER_SESSION	= 1004;
	public static final int	FAKE_HEADER				= 1005;

	public AdapterSelector()
	{

	}

	static TypeAdapter<GraphObject> getAdapter(GraphObject object, int layoutType, MultiObjectAdapter parentAdapter)
	{
		TypeAdapter<GraphObject> adapter = BaseAdapterSelector.getAdapter(object, layoutType);

		if (adapter != null)
			return adapter;

		switch (getItemViewType(object))
		{
			case GraphObject.STREAM:
			{
				if (layoutType == SpecialLayout.STREAM_DETAIL)
					return new StreamAdapter2(parentAdapter, layoutType);

				return new StreamAdapter(parentAdapter, layoutType);
			}
			case GraphObject.NOTIFICATION:
			{
				return new NotificationAdapter();
			}
			case GraphObject.ALBUM:
			{
				return new AlbumAdapter();
			}
			case GraphObject.GRAPH_COMMENT:
			{
				return new com.abewy.android.apps.klyph.adapter.graph.CommentAdapter();
			}
			case GraphObject.COMMENT:
			{
				return new CommentAdapter();
			}
			case GraphObject.EDUCATION:
			{
				return new EducationAdapter();
			}
			case GraphObject.EVENT:
			{
				if (layoutType == SpecialLayout.EVENT_ABOUT)
					return new EventAdapter2();

				return new EventAdapter();
			}
			case GraphObject.EVENT_RESPONSE:
			{
				return new EventResponseAdapter();
			}
			case GraphObject.FRIEND:
			{
				if (layoutType == SpecialLayout.USER_BIRTHDAY)
					return new BirthdayAdapter();

				if (layoutType == SpecialLayout.FRIEND_PICKER)
					return new FriendPickerAdapter();

				if (layoutType == SpecialLayout.FRIEND_PICKER_SINGLE)
					return new FriendPickerSingleAdapter();

				if (layoutType == SpecialLayout.MEMBER)
					return new MemberAdapter();

				return new FriendAdapter();
			}
			case GraphObject.FRIEND_LIST:
			{
				return new FriendListAdapter();
			}
			case GraphObject.FRIEND_REQUEST:
			{
				return new FriendRequestAdapter();
			}
			case GraphObject.GROUP:
			{
				if (layoutType == SpecialLayout.ELEMENT_TIMELINE)
					return new GroupTimelineAdapter();

				return new GroupAdapter();
			}
			case GraphObject.PAGE:
			{
				if (layoutType == SpecialLayout.ELEMENT_TIMELINE)
					return new ElementTimelineAdapter();

				return new PageAdapter();
			}
			case GraphObject.PHOTO:
			{
				if (layoutType == SpecialLayout.PHOTO)
					return new PhotoAdapter(parentAdapter);

				return new PhotoListAdapter();
			}
			case GraphObject.RELATIVE:
			{
				return new RelativeAdapter();
			}
			case GraphObject.USER:
			{
				return new ElementTimelineAdapter();
			}
			case GraphObject.MESSAGE_THREAD:
			{
				return new ThreadAdapter();
			}
			case GraphObject.MESSAGE:
			{
				return new MessageAdapter();
			}
			case MESSAGE_USER_SESSION:
			{
				return new MessageSessionUserAdapter();
			}
			case GraphObject.TAG:
			{
				return new TagAdapter();
			}
			case GraphObject.VIDEO:
			{
				return new VideoAdapter();
			}
			case GraphObject.WORK:
			{
				return new WorkAdapter();
			}
			case STREAM_LIKE_COUNT:
			{
				return new StreamLikeAdapter();
			}
			case EVENT_DATA:
			{
				return new EventDataAdapter();
			}
			case EVENT_RESPONSE:
			{
				return new EventResponseAdapter();
			}
			case EVENT_ATTENDEES:
			{
				return new EventAttendeesAdapter();
			}
			case FAKE_HEADER:
			{
				return new FakeHeaderItemAdapter();
			}
		}

		Log.e("AdapterSelector", "No adapter defined for type " + object);
		return null;
	}

	static int getItemViewType(GraphObject object)
	{
		if (object.getItemViewType() == GraphObject.MESSAGE)
		{
			if (((Message) object).getAuthor_id().equals(KlyphSession.getSessionUserId()))
			{
				return MESSAGE_USER_SESSION;
			}

			return GraphObject.MESSAGE;
		}

		return object.getItemViewType();
	}
}
