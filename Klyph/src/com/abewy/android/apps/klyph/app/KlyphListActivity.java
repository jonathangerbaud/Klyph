package com.abewy.android.apps.klyph.app;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.app.BaseListActivity;
import com.abewy.klyph.items.Progress;
import com.abewy.android.apps.klyph.R;

public class KlyphListActivity extends BaseListActivity implements OnScrollListener
{
	private final String	TAG							= "CkoobafeListActivity " + this.getClass().getSimpleName();

	private boolean			userScroll					= false;
	private boolean			loading						= false;
	private boolean			firstLoad					= true;
	private boolean			viewDestroyed				= false;
	private boolean			noMoreData					= false;
	private boolean			loadingObjectAsFirstItem	= false;
	private GraphObject		loadingObject;

	private int				requestType;
	private String			elementId;
	private String			offset;
	private String			initialOffset;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setLoadingView(findViewById(android.R.id.progress));

		getListView().setEmptyView(findViewById(android.R.id.empty));

		getListView().setOnScrollListener(this);

		loadingObject = getLoadingObject();
		loadingObject.setLoading(true);

		setListVisible(false);
		setEmptyViewVisible(false);
	}

	public void setElementId(String id)
	{
		this.elementId = id;
	}

	public void load()
	{
		// Log.i(TAG, "load");

		if ((isFirstLoad() && !isLoading()) || isViewDestroyed())
		{
			setViewDestroyed(false);
			setOffset(initialOffset);
			setNoMoreData(false);
			firstLoad = true;

			refresh();
		}
	}

	protected String getElementId()
	{
		return elementId;
	}

	protected void setOffset(String offset)
	{
		this.offset = offset;
	}

	protected void setInitialOffset(String offset)
	{
		this.initialOffset = offset;
	}

	protected void setRequestType(int requestType)
	{
		this.requestType = requestType;
	}

	protected void setNoMoreData(boolean noMoreData)
	{
		this.noMoreData = noMoreData;
	}

	protected void setLoadingObjectAsFirstItem(boolean loadingObjectAsFirstItem)
	{
		this.loadingObjectAsFirstItem = loadingObjectAsFirstItem;
	}

	private void setViewDestroyed(boolean viewDestroyed)
	{
		this.viewDestroyed = viewDestroyed;
	}

	protected boolean isViewDestroyed()
	{
		return viewDestroyed;
	}

	protected boolean isLoading()
	{
		return loading;
	}

	protected boolean isFirstLoad()
	{
		return firstLoad;
	}

	protected boolean hasNoMoreData()
	{
		return noMoreData;
	}

	protected void refresh()
	{
		startLoading();
		Log.i(TAG, "request = " + requestType + ", id = " + elementId + ", offset = " + offset);
		AsyncRequest request = new AsyncRequest(requestType, elementId, offset, new AsyncRequest.Callback() {

			public void onComplete(Response response)
			{
				Log.i(TAG, "onCompleted");
				onRequestComplete(response);
			}
		});

		request.execute();
	}

	private void onRequestComplete(final Response response)
	{
		runOnUiThread(new Runnable() {

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

	private void onRequestSuccess(List<GraphObject> result)
	{
		populate(result);
	}

	private void onRequestError(RequestError error)
	{
		Log.i(TAG, "error " + error.toString());
	}

	protected MultiObjectAdapter getAdapter()
	{
		return (MultiObjectAdapter) getListAdapter();
	}

	protected void populate(List<GraphObject> data)
	{
		for (GraphObject graphObject : data)
		{
			getAdapter().add(graphObject);
		}
		// adapter.addAll(data); is only available in api 11

		endLoading();

		if (data.size() == 0)
			noMoreData = true;
		else
			offset = String.valueOf(getAdapter().getCount());
	}

	protected void startLoading()
	{
		loading = true;

		if (!firstLoad)
		{
			if (!loadingObjectAsFirstItem)
				getAdapter().add(loadingObject);
			else
				getAdapter().insert(loadingObject, 0);
		}
	}

	protected void endLoading()
	{
		loading = false;
		firstLoad = false;

		getAdapter().remove(loadingObject);

		setLoadingViewVisible(false);
		setListVisible(true);
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		if (userScroll)
		{
			if (!loading && !firstLoad && !noMoreData)
			{
				boolean  loadMore = firstVisibleItem + visibleItemCount >= totalItemCount - 5;

				if (loadMore)
				{
					// Log.i(TAG, "onScroll refresh");
					refresh();
				}
			}
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		userScroll = scrollState != OnScrollListener.SCROLL_STATE_IDLE;
	}

	protected GraphObject getLoadingObject()
	{
		return new Progress();
	}

	@Override
	protected Class<? extends Activity> getHomeClass()
	{
		return MainActivity.class;
	}

	@Override
	protected int getLayout()
	{
		return R.layout.list;
	}

	@Override
	protected int getCustomTheme()
	{
		return KlyphPreferences.getTheme();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		// Log.i(TAG, "onPause");
	}

	@Override
	public void onStop()
	{
		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		setViewDestroyed(true);
		// Log.i(TAG, "onDestroyView");
	}

	@Override
	public void onStart()
	{
		super.onStart();
		// Log.i(TAG, "onStart");
	}

	@Override
	public void onResume()
	{
		super.onResume();
		// Log.i(TAG, "onResume");
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
	}
}
