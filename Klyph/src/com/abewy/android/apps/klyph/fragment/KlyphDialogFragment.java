package com.abewy.android.apps.klyph.fragment;

import java.util.List;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.view.ListEmptyView;
import com.abewy.app.BaseDialogListFragment;
import com.abewy.klyph.items.Progress;
import com.abewy.klyph.items.TextButtonItem;
import com.abewy.net.ConnectionState;

public class KlyphDialogFragment extends BaseDialogListFragment implements OnScrollListener
{
	private final String	TAG							= "KlyphDialogFragment " + this.getClass().getSimpleName();

	private AsyncRequest	request;
	private boolean			userScroll					= false;
	private boolean			loading						= false;
	private boolean			firstLoad					= true;
	private boolean			isError						= false;
	private boolean			viewDestroyed				= false;
	private boolean			noMoreData					= false;
	private boolean			loadingObjectAsFirstItem	= false;
	private GraphObject		loadingObject;
	private int				emptyText;
	private int				errorText = R.string.error;
	private View			loadingView;
	private boolean			listVisible					= false;

	private int				requestType;
	private String			elementId;
	private String			offset;
	private String			initialOffset;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		getListView().setOnScrollListener(this);

		getListView().setVisibility(View.GONE);
		((View) getListView().getParent()).setVisibility(View.GONE);
		loadingView = view.findViewById(android.R.id.progress);
		
		defineEmptyView();

		loadingObject = getLoadingObject();
		loadingObject.setLoading(true);

		// setListVisible(false);
		// setEmptyViewVisible(false);
	}
	
	/**
	 * Create, add and set the list empty view Override this method if you want
	 * a custom empty view, or if you replaced listview by a gridview for
	 * example
	 */
	protected void defineEmptyView()
	{
		ListEmptyView lev = new ListEmptyView(getActivity());
		((ViewGroup) getListView().getParent()).addView(lev);

		getListView().setEmptyView(lev);

		if (this.emptyText > 0)
			setEmptyText(emptyText);
	}

	public void setElementId(String id)
	{
		this.elementId = id;
	}

	public void setListAdapter(ListAdapter adapter)
	{
		getListView().setAdapter(adapter);
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

	protected boolean requestHasMoreData()
	{
		return request.hasMoreData();
	}

	protected void defineEmptyText(int resId)
	{
		this.emptyText = resId;
		setEmptyText(resId);
	}

	protected void setEmptyText(int resId)
	{
		if (getListView().getEmptyView() != null)
		{
			((ListEmptyView) getListView().getEmptyView()).setText(resId);
		}
	}

	protected void setErrorText(int resId)
	{
		this.errorText = resId;
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

	protected void refresh()
	{
		startLoading();
		Log.d(TAG, "request = " + requestType + ", id = " + elementId + ", offset = " + offset);

		request = new AsyncRequest(requestType, elementId, offset, new AsyncRequest.Callback() {

			@Override
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
		if (getView() != null) // Check if view is created
			populate(result);
	}

	private void onRequestError(RequestError error)
	{
		Log.i(TAG, "error " + error.toString());
		if (getView() != null)
		{
			isError = true;
			loading = false;

			int errorText = this.errorText;

			if (!ConnectionState.getInstance(getActivity()).isOnline())
			{
				errorText = R.string.request_connexion_error;
			}

			TextButtonItem errorItem = new TextButtonItem();
			errorItem.setText(getString(errorText));

			// TODO This is not a good copding practice !
			errorItem.setButtonListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					retryRequestAfterError();
				}
			});

			getAdapter().add(errorItem);
			endLoading();
		}
	}

	protected MultiObjectAdapter getAdapter()
	{
		return (MultiObjectAdapter) getListView().getAdapter();
	}

	protected void populate(List<GraphObject> data)
	{
		for (GraphObject graphObject : data)
		{
			getAdapter().add(graphObject);
		}
		// adapter.addAll(data); is only available in api 11

		// getAdapter().notifyDataSetChanged();

		endLoading();

		if (data.size() == 0 || (request != null && !request.hasMoreData()))
			noMoreData = true;
		else
			offset = String.valueOf(getAdapter().getCount());
	}

	protected void startLoading()
	{
		loading = true;

		if (isError == true && getAdapter().getCount() > 0)
		{
			GraphObject lastObject = getAdapter().getLastItem();

			if (lastObject instanceof TextButtonItem)
			{
				getAdapter().remove(lastObject);
			}
		}

		isError = false;

		if (!firstLoad)
		{
			if (!loadingObjectAsFirstItem)
				getAdapter().add(loadingObject);
			else
				getAdapter().insert(loadingObject, 0);

			getAdapter().notifyDataSetChanged();
		}
	}

	protected void endLoading()
	{
		loading = false;
		firstLoad = false;

		getAdapter().remove(loadingObject);
		getAdapter().notifyDataSetChanged();

		setLoadingViewVisible(false);
		setListVisible(true);
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		if (userScroll)
		{
			if (!loading && !firstLoad && !noMoreData && !isError)
			{
				boolean  loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

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

	protected int getLayout()
	{
		return R.layout.list;
	}

	@Override
	protected void setListVisible(boolean visible)
	{
		setListVisibility(visible, true);
	}

	protected void setListVisible(boolean visible, boolean animate)
	{
		setListVisibility(visible, animate);
	}

	private void setListVisibility(boolean visible, boolean animate)
	{
		ensureList();

		if (listVisible == visible)
		{
			return;
		}

		listVisible = visible;

		View parent = (View) getListView().getParent();

		if (visible)
		{
			if (animate)
			{
				loadingView.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
				parent.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
			}
			else
			{
				loadingView.clearAnimation();
				parent.clearAnimation();
			}
			loadingView.setVisibility(View.GONE);
			parent.setVisibility(View.VISIBLE);
		}
		else
		{
			if (animate)
			{
				loadingView.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
				parent.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
			}
			else
			{
				loadingView.clearAnimation();
				parent.clearAnimation();
			}
			loadingView.setVisibility(View.VISIBLE);
			parent.setVisibility(View.GONE);
		}
	}

	protected void ensureList()
	{
		//getListView().setEmptyView(getView().findViewById(android.R.id.empty));
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
	public void onDestroyView()
	{
		super.onDestroyView();
		setViewDestroyed(true);
		// Log.i(TAG, "onDestroyView");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		request = null;
		loadingObject = null;
		loadingView = null;
		requestType = Query.NONE;
		Log.i(TAG, "onDestroy");
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		Log.i(TAG, "onDetach");
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
		
		if (getAdapter() != null)
		{
			getAdapter().notifyDataSetChanged();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
	}

	public void retryRequestAfterError()
	{
		Log.d(TAG, "retryRequestAfterError ");
		setEmptyText(emptyText);
		refresh();
	}
}
