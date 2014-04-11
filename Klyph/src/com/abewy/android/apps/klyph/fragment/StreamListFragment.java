package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphData;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.animation.GoogleCardStyleAdapter;
import com.abewy.android.apps.klyph.adapter.animation.KlyphAnimationAdapter;
import com.abewy.android.apps.klyph.app.IActionbarSpinner;
import com.abewy.android.apps.klyph.app.PostActivity;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.FriendList;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.serializer.StreamDeserializer;
import com.abewy.android.apps.klyph.core.fql.serializer.StreamSerializer;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.BaseAsyncRequest.Callback;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class StreamListFragment extends KlyphFragment implements OnNavigationListener
{
	private static final int	POST_CODE		= 101;
	private static final int	STREAM_CODE		= 102;

	private static String		stream_position_id;

	private boolean				isFirstLoad		= true;
	private boolean				isStoredData	= false;

	private ReadDataTask		readTask;
	private int					spinnerPosition	= 0;

	private List<GraphObject>	friendLists;
	private boolean				canSaveStream	= true;

	public StreamListFragment()
	{

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		int query = KlyphPreferences.isAlternativeNewsfeed() ? Query.ALTERNATIVE_NEWSFEED : Query.NEWSFEED;
		setRequestType(query);
		setNewestRequestType(query);
		setShouldLoadOnResume(false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		MultiObjectAdapter adapter = /* KlyphPreferences.areBannerAdsEnabled() ? new NewsfeedAdapter(getListView()) : */new MultiObjectAdapter(
				getListView());

		if (KlyphPreferences.animateCards())
		{
			GoogleCardStyleAdapter gcsAdapter = new GoogleCardStyleAdapter(adapter);
			gcsAdapter.setAbsListView(getListView());
			setListAdapter(gcsAdapter);
		}
		else
		{
			setListAdapter(adapter);
		}

		defineEmptyText(R.string.empty_list_no_stream);
		// getListView().setPadding(0, 8, 0, 0);

		// getListView().setDivider(getResources().getDrawable(R.drawable.hdivider_transparent));
		// getGridView().setVerticalSpacing(16);
		// getListView().setItemsCanFocus(false);
		getListView().setDrawSelectorOnTop(true);

		getListView().setSelector(AttrUtil.getResourceId(getActivity(), R.attr.streamSelector));

		setListVisible(false);

		if (KlyphData.getFriendLists() == null)
		{
			new AsyncRequest(Query.FRIEND_LISTS, "", "", new Callback() {

				@Override
				public void onComplete(Response response)
				{
					onRequestComplete(response);
				}
			}).execute();
		}

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK && requestCode == POST_CODE)
		{
			if (isNewestLoading() == false)
			{
				setPullToRefreshRefreshing(true);
				loadNewest();
			}
		}
		else if (resultCode == Activity.RESULT_OK && requestCode == STREAM_CODE)
		{
			if (data != null)
			{
				boolean deleted = data.getBooleanExtra(KlyphBundleExtras.DELETED, false);

				if (deleted == true)
				{
					String id = data.getStringExtra(KlyphBundleExtras.STREAM_ID);

					for (GraphObject stream : getAdapter().getItems())
					{
						if (((Stream) stream).getPost_id().equals(id))
						{
							// delete(stream);
							stream.setToDelete(true);
							getAdapter().remove(stream, true);
							storeSessionStreams();
							break;
						}
					}
				}
			}
		}
	}

	@Override
	protected int getCustomLayout()
	{
		return R.layout.list_timeline;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
	}

	@Override
	public void load()
	{
		if (isFirstLoad)
		{
			if (KlyphData.getLastStreams() != null)
			{
				populate(KlyphData.getLastStreams());

				if (stream_position_id != null)
				{
					for (GraphObject graphObject : getAdapter().getItems())
					{
						if (graphObject instanceof Stream && stream_position_id.equals(((Stream) graphObject).getPost_id()))
						{
							getListView().setSelection(getAdapter().getItemPosition(graphObject));
						}
					}
				}

				setNoMoreData(false);
			}
			else
			{
				readTask = new ReadDataTask();
				readTask.execute();
			}
		}
		else
		{
			super.load();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.add(Menu.NONE, R.id.menu_post, Menu.NONE, "Post").setIcon(AttrUtil.getResourceId(getActivity(), R.attr.editIcon))
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_post)
		{
			if (getActivity() != null)
				startActivityForResult(new Intent(getActivity(), PostActivity.class), POST_CODE);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		Log.d("StreamListFragment", "populate: ");
		int previousSize = getAdapter().getCount();

		super.populate(data);

		if (data.size() > 0)
		{
			setOffset(((Stream) data.get(data.size() - 1)).getCreated_time());

			if (canSaveStream)
			{
				storeSessionStreams();

				if (previousSize < 15 && !isStoredData)
					storeData();
			}
		}

		// getListView().setItemsCanFocus(false);

		isFirstLoad = false;
	}

	@Override
	protected void populateNewest(List<GraphObject> data)
	{
		int n = data.size();

		if (n > 0)
		{
			int index = getListView().getFirstVisiblePosition();
			View v = getListView().getChildAt(index);
			int top = v == null ? 0 : v.getTop();

			int lastIndex = -1;
			if (KlyphPreferences.getNewsfeedGoToTop() == false)
			{
				List<GraphObject> list = new ArrayList<GraphObject>();
				list.addAll(getAdapter().getItems());

				// Looking for a matching post
				outer: for (int i = 0; i < n; i++)
				{
					Stream stream = (Stream) data.get(i);

					for (int j = 0, m = list.size(); j < m; j++)
					{
						if (list.get(j) instanceof Stream)
						{
							Stream s = (Stream) list.get(j);
							Log.d("StreamListFragment", "populateNewest: " + stream.getPost_id() + " " + s.getPost_id());
							if (stream.getPost_id().equals(s.getPost_id()))
							{
								lastIndex = i;
								// list.remove(s);
								// list.add(stream);
								break outer;
							}
						}
					}
				}

				Log.d("StreamListFragment", "populateNewest: " + lastIndex);

				// If no match, then insert all the data
				if (lastIndex == -1)
					lastIndex = data.size();

				// Insert filtered data
				for (int i = lastIndex - 1; i >= 0; i--)
				{
					list.add(0, data.get(i));
				}

				getAdapter().clear(false);
				getAdapter().addAll(list);
			}
			else
			{
				getAdapter().clear(false);
				getAdapter().addAll(data);
				setOffset(((Stream) data.get(data.size() - 1)).getCreated_time());
			}

			if (getListAdapter() instanceof KlyphAnimationAdapter)
			{
				int firstVisiblePosition = getListView().getFirstVisiblePosition();
				int lastVisiblePosition = getListView().getLastVisiblePosition();

				if (firstVisiblePosition != lastVisiblePosition)
					((KlyphAnimationAdapter) getListAdapter()).deactivateNext(lastVisiblePosition - firstVisiblePosition);
				else
					((KlyphAnimationAdapter) getListAdapter()).deactivateNext(1);
			}

			getAdapter().notifyDataSetChanged();

			if (KlyphPreferences.getNewsfeedGoToTop() == false)
			{
				getListView().setSelectionFromTop(lastIndex, top);
			}
		}

		setNewestLoading(false);
		setPullToRefreshRefreshing(false);

		if (data.size() > 0)
		{
			storeSessionStreams();
			storeData();
		}
	}

	private void storeSessionStreams()
	{
		if (getAdapter() != null && getAdapter().getCount() > 0)
		{
			// Make a copy of the list
			List<GraphObject> streams = new ArrayList<GraphObject>();

			// Add only the streams (e.g. no progress or error items)
			for (GraphObject graphObject : getAdapter().getItems())
			{
				if (graphObject instanceof Stream)
					streams.add(graphObject);
			}

			KlyphData.setLastStreams(streams);
		}
	}

	@Override
	protected String getNewestOffset(GraphObject graphObject)
	{
		Stream stream = (Stream) graphObject;
		return stream.getCreated_time();
	}

	private void storeData()
	{
		new StoreDataTask().execute();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		GraphObject object = (GraphObject) l.getItemAtPosition(position);

		if (object instanceof Stream)
		{
			Stream stream = (Stream) l.getItemAtPosition(position);

			if (stream.isSelectable(0) == true)
			{
				startActivityForResult(Klyph.getIntentForGraphObject(getActivity(), stream), STREAM_CODE);
			}
		}
	}

	private class ReadDataTask extends AsyncTask<Void, Void, List<GraphObject>>
	{

		@Override
		protected List<GraphObject> doInBackground(Void... params)
		{
			// long time = new Date().getTime();
			String json = KlyphPreferences.getLastStories();
			// Log.d("StreamListFragment", "read last stories from prefs time " + (new Date().getTime() - time));
			JSONArray data;
			try
			{
				data = new JSONArray(json);
			}
			catch (JSONException e)
			{
				return null;
			}
			// Log.d("StreamListFragment", "read last stories jsonarray time " + (new Date().getTime() - time));
			StreamDeserializer ss = new StreamDeserializer();
			List<GraphObject> list = ss.deserializeArray(data);
			// Log.d("StreamListFragment", "read last stories time " + (new Date().getTime() - time));
			return list;
		}

		@Override
		protected void onPostExecute(List<GraphObject> result)
		{
			onStoredDataLoaded(result);
		}
	}

	private void onStoredDataLoaded(final List<GraphObject> data)
	{
		if (data != null && getView() != null)
		{
			if (getActivity() != null)
			{
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run()
					{
						isStoredData = true;
						populate(data);
						isStoredData = false;
						setNoMoreData(false);

						setPullToRefreshRefreshing(true);
						loadNewest();
					}
				});
			}
		}
		else
		{
			if (getActivity() != null)
			{
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run()
					{
						StreamListFragment.super.load();
					}
				});
			}
		}
	}

	private class StoreDataTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{
			long time = new Date().getTime();

			List<GraphObject> streams = KlyphData.getLastStreams().subList(0, Math.min(15, KlyphData.getLastStreams().size()));

			StreamSerializer ss = new StreamSerializer();
			JSONArray json = ss.serializeArray(streams);
			String jsonString = json.toString();
			KlyphPreferences.setLastStories(jsonString);

			Log.d("StreamListFragment", "save last stories time " + (new Date().getTime() - time));
			return null;
		}

		@Override
		protected void onPostExecute(Void params)
		{

		}
	}

	@Override
	public void onPause()
	{
		if (getListView() != null && getAdapter() != null)
		{
			int pos = getListView().getFirstVisiblePosition();
			GraphObject o = getAdapter().getItem(pos);

			if (pos < getAdapter().getCount() && o instanceof Stream)
			{
				stream_position_id = ((Stream) o).getPost_id();
			}
			else if (pos > 0)
			{
				pos--;

				if (pos < getAdapter().getCount() && o instanceof Stream)
					stream_position_id = ((Stream) o).getPost_id();
			}
		}

		setPullToRefreshRefreshing(false);

		super.onPause();
	}

	@Override
	public void onDestroyView()
	{
		Log.d("StreamListFragment", "onDestroyView");
		if (readTask != null)
		{
			readTask.cancel(true);
			readTask = null;
		}
		super.onDestroyView();
	}

	@Override
	public void onDestroy()
	{
		Log.d("StreamListFragment", "onDestroy");
		if (readTask != null)
		{
			readTask.cancel(true);
			readTask = null;
		}
		super.onDestroy();
	}

	// ___ Action bar spinner & friend lists

	private void onRequestComplete(final Response response)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						onRequestError(response.getError());
					}
				}
			});
		}
	}

	private void onRequestSuccess(List<GraphObject> result)
	{
		KlyphData.setFriendLists(result);
		if (getView() != null && getListView() != null)
		{
			onSetToFront(getActivity());
		}
	}

	private void onRequestError(RequestError error)
	{

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId)
	{
		if (spinnerPosition != itemPosition)
		{
			spinnerPosition = itemPosition;

			cancelRequest();

			if (spinnerPosition == 0)
			{
				int query = KlyphPreferences.isAlternativeNewsfeed() ? Query.ALTERNATIVE_NEWSFEED : Query.NEWSFEED;
				setRequestType(query);
				setNewestRequestType(query);
				setElementId(KlyphSession.getSessionUserId());
				canSaveStream = true;
			}
			else
			{
				int query = Query.FRIEND_LIST_NEWSFEED;
				setRequestType(query);
				setNewestRequestType(query);
				FriendList fl = (FriendList) friendLists.get(itemPosition);
				setElementId(fl.getFlid());
				canSaveStream = false;
			}
			setOffset(null);

			clearAndRefresh();

			return true;
		}

		return false;
	}

	@Override
	public void onSetToFront(Activity activity)
	{
		friendLists = KlyphData.getFriendLists();

		if (activity != null && friendLists != null && friendLists.size() > 0)
		{
			FriendList fl = new FriendList();
			fl.setOwner(KlyphSession.getSessionUserId());
			fl.setName(activity.getString(R.string.fragment_header_newsfeed));
			friendLists.add(0, fl);

			((IActionbarSpinner) activity).displaySpinnerInActionBar(friendLists, spinnerPosition, this);
			activity.setTitle("");
		}
	}

	@Override
	public void onSetToBack(Activity activity)
	{
		if (activity != null)
			((IActionbarSpinner) activity).removeSpinnerInActionBar();
	}
}
