package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphNotification;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.fql.FriendRequest;
import com.abewy.android.apps.klyph.core.fql.Notification;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.facebook.IFbPermissionCallback;
import com.abewy.android.apps.klyph.facebook.IFbPermissionWorker;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.klyph.items.Header;
import com.facebook.Session;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissListViewTouchListener;

public class Notifications extends KlyphFragment implements IFbPermissionCallback
{
	public static interface NotificationsListener
	{
		public void onNewNotifications();
	}

	private static final List<String>	PERMISSIONS			= Arrays.asList("manage_notifications");
	private static final String			PENDING_PUBLISH_KEY	= "pendingPublishReauthorization";
	private boolean						pendingAnnounce		= false;

	private long						lastUpdateTime		= 0;
	private int							unreadCount;
	private boolean 						loggedIn			= false;

	private MenuItem					refreshItem;

	public Notifications()
	{
		setRequestType(Query.NOTIFICATIONS);
		setNewestRequestType(Query.NOTIFICATIONS);
	}

	@Override
	protected int getCustomLayout()
	{
		return R.layout.fragment_notifications;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setRequestType(Query.NOTIFICATIONS);
		setNewestRequestType(Query.NOTIFICATIONS);

		super.onCreate(savedInstanceState);

		setHasOptionsMenu(false);

		if (savedInstanceState != null)
		{
			pendingAnnounce = savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
		}

		setAutoLoad(false);
	}

	@Override
	protected void attachViewToPullToRefresh()
	{

	}

	@Override
	protected void attachViewToPullToRefresh(View view)
	{

	}

	@Override
	protected int getRefreshMenuItemPosition()
	{
		if (unreadCount > 0)
			return super.getRefreshMenuItemPosition();

		return getResources().getInteger(R.integer.right_menu_notification_icon_position);
	}

	/*
	 * @Override
	 * protected KlyphPullToRefreshListView onCreatePullToRefreshListView(LayoutInflater inflater, Bundle savedInstanceState)
	 * {
	 * return (KlyphPullToRefreshListView) inflater.inflate(R.layout.list_notifications, null);
	 * }
	 */
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		NotificationSwipeDismissAdapter swAdapter = new NotificationSwipeDismissAdapter(new MultiObjectAdapter(getListView()),
				new OnDismissCallback() {

					@Override
					public void onDismiss(AbsListView arg0, int[] positions)
					{
						for (int i : positions)
						{
							if (getAdapter().getItem(i) instanceof Notification)
							{
								Notification notification = (Notification) getAdapter().getItem(i);
								setNotificationRead(notification);
							}
						}

						List<GraphObject> list = new ArrayList<GraphObject>();
						for (GraphObject o : getAdapter().getItems())
						{
							if (!(o instanceof Header))
							{
								list.add(o);
							}
						}

						populate(list);
					}
				});
		swAdapter.setAbsListView(getListView());
		setListAdapter(swAdapter);
		// setListAdapter(new MultiObjectAdapter(getListView()));

		defineEmptyText(R.string.empty_list_no_notification);

		setListVisible(false);

		setRequestType(Query.NOTIFICATIONS);

		loggedIn = true;

		new Handler().postDelayed(new Runnable() {
			public void run()
			{
				loadNotifications();
			}
		}, 2000);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{		
		refreshItem = menu.add(Menu.NONE, R.id.menu_refresh, 1, R.string.refresh);
		refreshItem.setIcon(AttrUtil.getResourceId(getActivity(), R.attr.refreshIcon)).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		setActionBarRefreshItemLoading(isLoading());

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		
		if (unreadCount > 0 && menu.findItem(R.id.menu_dismiss) == null)
		{
			MenuItem item = menu.add(Menu.NONE, R.id.menu_dismiss, 1, "Dismiss");
			item.setIcon(AttrUtil.getResourceId(getActivity(), R.attr.dismissNotificationsIcon));
			item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
	}

	protected void setActionBarRefreshItemLoading(boolean loading)
	{
		if (refreshItem != null)
		{
			if (loading == true)
			{
				refreshItem.setActionView(R.layout.actionbar_item_refresh);
			}
			else
			{
				refreshItem.setActionView(null);
			}
		}
	}

	@Override
	protected void startLoading()
	{
		setActionBarRefreshItemLoading(true);
		super.startLoading();
	}

	@Override
	protected void endLoading()
	{
		setActionBarRefreshItemLoading(true);
		super.endLoading();
	}

	@Override
	protected void endLoadingNewest()
	{
		setActionBarRefreshItemLoading(false);
		super.endLoadingNewest();
	}

	@Override
	public boolean  onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_dismiss)
		{
			List<GraphObject> list = new ArrayList<GraphObject>();
			List<List<AsyncRequest>> batchs = new ArrayList<List<AsyncRequest>>();
			List<AsyncRequest> requests = new ArrayList<AsyncRequest>();

			batchs.add(requests);

			for (GraphObject o : getAdapter().getItems())
			{
				if (o instanceof Notification || o instanceof FriendRequest)
				{
					if (o instanceof Notification)
					{
						Notification n = (Notification) o;

						if (requests.size() == 40)
						{
							requests = new ArrayList<AsyncRequest>();
							batchs.add(requests);
						}

						requests.add(new AsyncRequest(Query.POST_READ_NOTIFICATION, n.getNotification_id(), "", null));
						n.setIs_unread(false);
					}
					list.add(o);
				}
			}

			populate(list);
			unreadCount = 0;
			getActivity().invalidateOptionsMenu();

			for (List<AsyncRequest> batch : batchs)
			{
				if (batch.size() > 0)
					AsyncRequest.executeBatch(batch);
			}

			((NotificationsListener) getActivity()).onNewNotifications();

			return true;
		}
		else if (item.getItemId() == R.id.menu_refresh)
		{
			onRefreshClicked();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onOpenPane()
	{
		KlyphPreferences.setLastCheckedNotificationTime(new Date().getTime() / 1000);

		// If last update greater than 1 min, do an update
		if ((new Date().getTime() - lastUpdateTime > 1 * 60 * 1000) && hasPermissions())
		{
			loadNotifications();
		}
	}

	public int getNewNotificationsCount()
	{
		List<Notification> list = new ArrayList<Notification>();
		for (GraphObject o : getAdapter().getItems())
		{
			if (o instanceof Notification)
			{
				if (((Notification) o).getIs_unread() == true)
				{
					list.add((Notification) o);
				}
			}
		}

		long lastCheckedTime = KlyphPreferences.getLastCheckedNotificationTime();

		int count = 0;
		for (Notification notification : list)
		{
			long nTime = 0;

			try
			{
				nTime = Long.parseLong(notification.getUpdated_time());
			}
			catch (NumberFormatException e)
			{

			}

			if (nTime > lastCheckedTime)
				count++;
		}

		return count;
	}

	public int getUnreadCount()
	{
		int count = 0;

		if (getAdapter() != null)
		{
			for (GraphObject o : getAdapter().getItems())
			{
				if (o instanceof Notification)
				{
					Notification n = (Notification) o;

					if (n.getIs_unread() == true)
						count++;
				}
			}
		}

		return count;
	}

	private boolean  hasPermissions()
	{
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		return permissions.containsAll(PERMISSIONS);
	}

	@Override
	public void load()
	{
		loadNotifications();
	}

	public void reset()
	{
		if (getAdapter() != null)
			getAdapter().clear();

		setIsFirstLoad(true);
	}

	@Override
	public void onCancelPermissions()
	{
		defineEmptyText(R.string.notifications_permissions_denied);
		populate(new ArrayList<GraphObject>());
		pendingAnnounce = false;
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		getAdapter().clear(false);

		List<GraphObject> readNotifications = new ArrayList<GraphObject>();
		List<GraphObject> unreadNotifications = new ArrayList<GraphObject>();
		List<GraphObject> friendRequests = new ArrayList<GraphObject>();

		for (GraphObject graphObject : data)
		{
			if (graphObject instanceof Notification)
			{
				if (((Notification) graphObject).getIs_unread() == true)
				{
					unreadNotifications.add(graphObject);
				}
				else
				{
					readNotifications.add(graphObject);
				}
			}
			else
			{
				friendRequests.add(graphObject);
			}
		}

		List<GraphObject> list = new ArrayList<GraphObject>();

		unreadCount = unreadNotifications.size();

		if (unreadNotifications.size() > 0)
		{
			unreadNotifications.get(unreadNotifications.size() - 1).setShowDivider(false);

			Header nTitle = new Header();
			nTitle.setName(getString(R.string.notifications_unread_header_title, unreadNotifications.size()));
			list.add(nTitle);
			list.addAll(unreadNotifications);
		}

		if (readNotifications.size() > 0)
		{
			readNotifications.get(readNotifications.size() - 1).setShowDivider(false);

			Header nTitle = new Header();
			nTitle.setName(getString(R.string.notifications_read_header_title));
			list.add(nTitle);
			list.addAll(readNotifications);
		}

		if (friendRequests.size() > 0)
		{
			friendRequests.get(friendRequests.size() - 1).setShowDivider(false);

			Header fTitle = new Header();
			fTitle.setName(getString(R.string.notifications_friend_request_header_title));
			list.add(fTitle);
			list.addAll(friendRequests);
		}

		super.populate(list);
		lastUpdateTime = new Date().getTime();

		setActionBarRefreshItemLoading(false);

		((NotificationsListener) getActivity()).onNewNotifications();

		// getActivity().supportInvalidateOptionsMenu();
		/*
		 * if (data.size() > 0)
		 * setOffset(((Notification) data.get(data.size() - 1)).getCreated_time());
		 */
	}

	@Override
	protected void populateNewest(List<GraphObject> data)
	{
		populate(data);
	}

	@Override
	public void onPermissionsChange()
	{
		if (pendingAnnounce)
		{
			loadNotifications();
		}
	}

	private void loadNotifications()
	{
		pendingAnnounce = false;
		if (!hasPermissions())
		{
			pendingAnnounce = true;
			requestExtendedPermissions(Session.getActiveSession());
			return;
		}

		defineEmptyText(R.string.empty_list_no_notification);
		setIsFirstLoad(true);
		super.load();
	}

	private void requestExtendedPermissions(Session session)
	{
		if (getActivity() != null)
			((IFbPermissionWorker) getActivity()).requestPublishPermissions(this, PERMISSIONS);
	}

	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		bundle.putBoolean(PENDING_PUBLISH_KEY, pendingAnnounce);
	}

	@Override
	public void onListItemClick(ListView gridView, View view, int position, long id)
	{
		GraphObject graphObject = getAdapter().getItem(position);

		if (graphObject instanceof Notification)
		{
			Notification notification = (Notification) graphObject;

			if (notification.getIs_unread() == true)
			{
				setNotificationRead(notification);

				((NotificationsListener) getActivity()).onNewNotifications();
			}

			Intent intent = KlyphNotification.getIntentForNotification(getActivity(), notification);

			if (intent != null)
			{
				startActivity(intent);
			}
		}
		else if (graphObject instanceof FriendRequest)
		{

			/*
			 * new AsyncRequest(Query.POST_READ_NOTIFICATION, ((FriendRequest) graphObject).getUid_from(), "", new AsyncRequest.Callback() {
			 * 
			 * @Override
			 * public void onComplete(Response response)
			 * {
			 * Log.d("NotificationsNotifications", "result " + response.getError());
			 * }
			 * }).execute();
			 */
			startActivity(Klyph.getIntentForGraphObject(getActivity(), graphObject));
		}
	}

	private void setNotificationRead(Notification notification)
	{
		notification.setIs_unread(false);

		new AsyncRequest(Query.POST_READ_NOTIFICATION, notification.getNotification_id(), "", new AsyncRequest.Callback() {

			@Override
			public void onComplete(Response response)
			{
				Log.d("NotificationsNotifications", "result " + response.getError());
			}
		}).execute();
	}

	@Override
	protected void onRefreshClicked()
	{
		if (!isLoading())
		{
			setActionBarRefreshItemLoading(true);
			loadNewest();
		}
	}

	@Override
	public void onPause()
	{
		try
		{
			getActivity().unregisterReceiver(mMessageReceiver);
			getActivity().unregisterReceiver(mNotificationStatusChangeReceiver);
		}
		catch (IllegalArgumentException e)
		{

		}
		super.onPause();
	}

	public void onLoggedOut()
	{
		loggedIn = false;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		refreshItem = null;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		getActivity().registerReceiver(mMessageReceiver, new IntentFilter(KlyphBundleExtras.NOTIFICATION_EVENT));
		getActivity().registerReceiver(mMessageReceiver, new IntentFilter("com.abewy.android.apps.klyph.action.NOTIFICATION_STATUS_CHANGE"));

		// Update list in case some items have been read
		if (getAdapter() != null)
		{
			List<GraphObject> list = new ArrayList<GraphObject>();
			for (GraphObject o : getAdapter().getItems())
			{
				if (o instanceof Notification || o instanceof FriendRequest)
					list.add(o);
			}

			populate(list);
		}

		// If last update greater than 1 min, do an update
		if ((new Date().getTime() - lastUpdateTime > 1 * 60 * 1000) || KlyphPreferences.hasNotificationReadStatusChanged())
		{
			KlyphPreferences.setNotificationReadStatusChanged(false);

			if (hasPermissions() && loggedIn == true)
			{
				loadNotifications();
			}
		}
	}

	private BroadcastReceiver	mMessageReceiver					= new BroadcastReceiver() {
																		@Override
																		public void onReceive(Context context, Intent intent)
																		{
																			if (hasPermissions() && !isLoading())
																				loadNotifications();
																		}
																	};

	private BroadcastReceiver	mNotificationStatusChangeReceiver	= new BroadcastReceiver() {
																		@Override
																		public void onReceive(Context context, Intent intent)
																		{
																			if (hasPermissions() && !isLoading())
																				loadNotifications();
																		}
																	};

	private static class NotificationSwipeDismissAdapter extends SwipeDismissAdapter
	{
		private OnDismissCallback	mCallback;

		public NotificationSwipeDismissAdapter(BaseAdapter baseAdapter, OnDismissCallback callback)
		{
			super(baseAdapter, callback);
			mCallback = callback;
		}

		@Override
		public void setAbsListView(AbsListView listView)
		{
			super.setAbsListView(listView);
			listView.setOnTouchListener(new NotificationSwipeDismissListViewTouchListener(listView, mCallback));
		}
	}

	private static class NotificationSwipeDismissListViewTouchListener extends SwipeDismissListViewTouchListener
	{
		private AbsListView	mListView;

		public NotificationSwipeDismissListViewTouchListener(AbsListView listView, OnDismissCallback callback)
		{
			super(listView, callback);
			mListView = listView;
		}

		@Override
		public boolean  onTouch(View view, MotionEvent motionEvent)
		{
			int childCount = mListView.getChildCount();
			View child, downView = null;
			Rect rect = new Rect();
			int[] listViewCoords = new int[2];
			mListView.getLocationOnScreen(listViewCoords);
			int x = (int) motionEvent.getRawX() - listViewCoords[0];
			int y = (int) motionEvent.getRawY() - listViewCoords[1];

			for (int i = 0; i < childCount; i++)
			{
				child = mListView.getChildAt(i);
				child.getHitRect(rect);
				if (rect.contains(x, y))
				{
					downView = child;
					break;
				}
			}

			if (downView != null)
			{
				int position = mListView.getPositionForView(downView);

				Object o = mListView.getAdapter().getItem(position);

				if (o instanceof Notification)
				{
					Notification n = (Notification) o;

					if (n.getIs_unread() == false)
						return false;
					else
						mListView.requestDisallowInterceptTouchEvent(true);
				}
				else
				{
					return false;
				}
			}
			return super.onTouch(view, motionEvent);
		}
	}
}
