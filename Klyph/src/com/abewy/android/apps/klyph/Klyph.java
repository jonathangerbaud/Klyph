package com.abewy.android.apps.klyph;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.abewy.android.apps.klyph.app.AlbumActivity;
import com.abewy.android.apps.klyph.app.EventActivity;
import com.abewy.android.apps.klyph.app.GroupActivity;
import com.abewy.android.apps.klyph.app.PageActivity;
import com.abewy.android.apps.klyph.app.StreamActivity;
import com.abewy.android.apps.klyph.app.UserActivity;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.KlyphFlags;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.fql.FriendRequest;
import com.abewy.android.apps.klyph.core.fql.Group;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.core.fql.User;
import com.abewy.android.apps.klyph.core.fql.User.Relative;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.GraphType;

public class Klyph
{
	private static final int	DEVICE_WIDTH_LIMIT			= 481;
	private static final int	NUM_GRID_COLUMN_BELOW_LIMIT	= 2;
	private static final int	NUM_GRID_COLUMN_ABOVE_LIMIT	= 3;
	private static final int	GRID_COLUMN_DP_SPACE		= 2;

	public static String	FACEBOOK_APP_ID				= "";

	public static void defineFacebookId()
	{
		FACEBOOK_APP_ID = KlyphFlags.IS_PRO_VERSION ? "[PRO_ID]" : "[FREE_ID]";
	}
	
	public static Intent getIntentForGraphObject(Context context, GraphObject object)
	{
		Intent intent = null;

		if (object instanceof User)
		{
			User user = (User) object;

			if (user.getUid().length() == 0)
			{
				return null;
			}

			intent = new Intent();
			intent.setClass(context, UserActivity.class);
			intent.putExtra(KlyphBundleExtras.USER_ID, user.getUid());
			intent.putExtra(KlyphBundleExtras.USER_NAME, user.getName());
		}
		else if (object instanceof Friend)
		{
			Friend friend = (Friend) object;

			if (friend.getUid().length() == 0)
			{
				return null;
			}

			intent = new Intent();
			intent.setClass(context, UserActivity.class);
			intent.putExtra(KlyphBundleExtras.USER_ID, friend.getUid());
			intent.putExtra(KlyphBundleExtras.USER_NAME, friend.getName());
		}
		else if (object instanceof Page)
		{
			Page page = (Page) object;
			intent = new Intent();
			intent.setClass(context, PageActivity.class);
			intent.putExtra(KlyphBundleExtras.PAGE_ID, page.getPage_id());
			intent.putExtra(KlyphBundleExtras.PAGE_NAME, page.getName());
		}
		else if (object instanceof Event)
		{
			Event event = (Event) object;
			intent = new Intent();
			intent.setClass(context, EventActivity.class);
			intent.putExtra(KlyphBundleExtras.EVENT_ID, event.getEid());
			intent.putExtra(KlyphBundleExtras.EVENT_NAME, event.getName());
		}
		else if (object instanceof Stream)
		{
			Stream stream = (Stream) object;
			intent = new Intent();
			intent.setClass(context, StreamActivity.class);
			//intent.putExtra(KlyphBundleExtras.STREAM_ID, stream.getPost_id());
			intent.putExtra(KlyphBundleExtras.STREAM_PARCELABLE, stream);
			// intent.putExtra(CkoobafeBundleExtras.EVENT_NAME,
			// event.getName());
		}
		else if (object instanceof Group)
		{
			Group group = (Group) object;
			intent = new Intent();
			intent.setClass(context, GroupActivity.class);
			intent.putExtra(KlyphBundleExtras.GROUP_ID, group.getGid());
			intent.putExtra(KlyphBundleExtras.GROUP_NAME, group.getName());
		}
		else if (object instanceof Tag)
		{
			Tag tag = (Tag) object;
			
			if (tag.getType().equals("user") || tag.getType().equals(GraphType.FQL_USER.toString()))
			{
				intent = new Intent();
				intent.setClass(context, UserActivity.class);
				intent.putExtra(KlyphBundleExtras.USER_ID, tag.getId());
				intent.putExtra(KlyphBundleExtras.USER_NAME, tag.getName());
			}
			else if (tag.getType().equals("page") || tag.getType().equals(GraphType.FQL_PAGE.toString()))
			{
				intent = new Intent();
				intent.setClass(context, PageActivity.class);
				intent.putExtra(KlyphBundleExtras.PAGE_ID, tag.getId());
				intent.putExtra(KlyphBundleExtras.PAGE_NAME, tag.getName());
			}
		}
		else if (object instanceof Relative)
		{
			Relative user = (Relative) object;

			if (user.getUid().length() == 0)
			{
				return null;
			}

			intent = new Intent();
			intent.setClass(context, UserActivity.class);
			intent.putExtra(KlyphBundleExtras.USER_ID, user.getUid());
			intent.putExtra(KlyphBundleExtras.USER_NAME, user.getName());
		}
		else if (object instanceof FriendRequest)
		{
			FriendRequest fr = (FriendRequest) object;

			if (fr.getUid_from().length() == 0)
			{
				return null;
			}

			intent = new Intent();
			intent.setClass(context, UserActivity.class);
			intent.putExtra(KlyphBundleExtras.USER_ID, fr.getUid_from());
			intent.putExtra(KlyphBundleExtras.USER_NAME, fr.getUid_from_name());
		}

		return intent;
	}

	public static Intent getIntentForParams(Context context, String id, String name, String type)
	{
		Intent intent = null;

		if (type.equals("fql.user") || type.equals("graph.user") || type.equals("user") || type.equals("friend"))
		{
			intent = new Intent(context, UserActivity.class);
			intent.putExtra(KlyphBundleExtras.USER_ID, id);
			intent.putExtra(KlyphBundleExtras.USER_NAME, name);
		}
		else if (type.equals("fql.page") || type.equals("graph.page") || type.equals("page"))
		{
			intent = new Intent(context, PageActivity.class);
			intent.putExtra(KlyphBundleExtras.PAGE_ID, id);
			intent.putExtra(KlyphBundleExtras.PAGE_NAME, name);
		}
		else if (type.equals("fql.album"))
		{
			intent = new Intent(context, AlbumActivity.class);
			intent.putExtra(KlyphBundleExtras.ALBUM_ID, id);
			intent.putExtra(KlyphBundleExtras.ALBUM_NAME, name);
		}
		else if (type.equals("group"))
		{
			intent = new Intent(context, GroupActivity.class);
			intent.putExtra(KlyphBundleExtras.GROUP_ID, id);
			intent.putExtra(KlyphBundleExtras.GROUP_NAME, name);
		}
		else if (type.equals("fql.event") || type.equals("event"))
		{
			intent = new Intent(context, EventActivity.class);
			intent.putExtra(KlyphBundleExtras.EVENT_ID, id);
			intent.putExtra(KlyphBundleExtras.EVENT_NAME, name);
		}
		else
		{
			Log.e("Klyph", "getIntentForParams : Click on an unlisted type : " + type);
		}

		return intent;
	}

	public static int getNumGridColumn()
	{
		return KlyphDevice.getDeviceWidth() > DEVICE_WIDTH_LIMIT ? NUM_GRID_COLUMN_ABOVE_LIMIT : NUM_GRID_COLUMN_BELOW_LIMIT;
	}

	public static int getGridColumnWidth()
	{
		int numColumn = getNumGridColumn();
		float w = (float) (KlyphDevice.getDeviceWidth() - GRID_COLUMN_DP_SPACE * KlyphDevice.getDeviceDensity()) / numColumn;
		return (int) w;
	}

	public static int getStandardImageSizeForRequest()
	{
		int imageSize = 50;
		
		return (int) ((int) imageSize * KlyphDevice.getDeviceDensity());
	}
	
	public static int getStandardImageSizeForNotification()
	{
		int imageSize = 64;
		
		return (int) ((int) imageSize * KlyphDevice.getDeviceDensity());
	}
}
