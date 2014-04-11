package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ListView;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.KlyphFlags;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.view.ListEmptyView;
import com.abewy.android.apps.klyph.widget.KlyphListView;
import com.abewy.app.BaseListFragment;
import com.abewy.klyph.items.Progress;
import com.abewy.klyph.items.TextButtonItem;
import com.abewy.net.ConnectionState;
import com.haarman.listviewanimations.BaseAdapterDecorator;

public class KlyphFragment extends BaseListFragment implements IKlyphFragment, OnScrollListener, OnRefreshListener
{
	private final String		TAG							= "KlyphFragment " + this.getClass().getSimpleName() + " " + this;

	private final static String	REQUEST_TYPE_SAVED_STATE	= "requestTypeSavedState";
	private final static String	ELEMENT_ID_SAVED_STATE		= "elementIdSavedState";

	// Layout Views
	private ImageButton			backToTopButton;
	private View				loadingView;
	private ListView			listView;

	// Loading resources
	private AsyncRequest		request;
	private int					requestType					= Query.NONE;
	private GraphObject			loadingObject;
	private String				elementId;
	private String				offset;
	private String				initialOffset;

	// Flags
	private boolean				loadWhenViewCreated			= false;
	private boolean				userScroll					= false;
	private boolean				loading						= false;
	private boolean				firstLoad					= true;
	private boolean				isError						= false;
	private boolean				viewDestroyed				= false;
	private boolean				noMoreData					= false;
	private boolean				loadingObjectAsFirstItem	= false;
	private boolean				shouldLoadOnResume			= true;
	private boolean				canAutoLoad					= true;

	// Used if custom layout
	private boolean				listVisible					= false;

	// Empty view text
	private int					emptyText;
	private int					errorText					= R.string.request_error;

	// Pull to refresh
	private PullToRefreshLayout	mPullToRefreshLayout;
	private int					requestNewestType			= Query.NONE;
	private int					errorNewestText				= R.string.request_error;
	private boolean				newestLoading				= false;
	protected int				insertNewestToIndex			= 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Log.d("KlyphFragment", "onCreateView");
		View view;
		if (!hasCustomLayout())
		{
			view = super.onCreateView(inflater, container, savedInstanceState);
		}
		else
		{
			view = inflater.inflate(getCustomLayout(), container, false);
		}

		listView = (ListView) view.findViewById(android.R.id.list);
		View emptyView = getEmptyView();

		if (emptyView != null)
		{
			emptyView.setId(android.R.id.empty);

			((ViewGroup) listView.getParent()).addView(emptyView);

			listView.setEmptyView(emptyView);
		}

		// Now set the ScrollView as the refreshable view, and the refresh listener (this)
		// if (requestNewestType != Query.NONE)
		// attachViewToPullToRefresh(view);

		return view;
	}

	protected void attachViewToPullToRefresh()
	{
		attachViewToPullToRefresh(getView());
	}

	protected void attachViewToPullToRefresh(View view)
	{
		if (mPullToRefreshLayout == null && view != null && getActivity() != null)
		{
			// This is the View which is created by ListFragment
			ViewGroup viewGroup = (ViewGroup) view;

			// We need to create a PullToRefreshLayout manually
			mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());
			// We can now setup the PullToRefreshLayout
			ActionBarPullToRefresh.from(getActivity())

			// We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
					.insertLayoutInto(viewGroup)

					// We need to mark the ListView and it's Empty View as pullable
					// This is because they are not dirent children of the ViewGroup
					//.theseChildrenArePullable(getListView(), getListView().getEmptyView())
					.theseChildrenArePullable(android.R.id.list, android.R.id.empty)

					// We can now complete the setup as desired
					.listener(this).options(getPullToRefreshOptions()).setup(mPullToRefreshLayout);

			// Log.d("KlyphFragment", "attachViewToPullToRefresh: " + this);
			// view.setVerticalFadingEdgeEnabled(false);
			// view.setFadingEdgeLength(0);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		backToTopButton = (ImageButton) view.findViewById(R.id.back_to_top_button);

		if (backToTopButton != null)
		{
			backToTopButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					setBackToTopButtonVisibility(false);
					getListView().setSelectionFromTop(0, 0);
				}
			});
		}

		getListView().setDrawSelectorOnTop(true);

		loadingObject = getLoadingObject();
		loadingObject.setLoading(true);

		if (hasCustomLayout())
		{
			getListView().setVisibility(View.GONE);
			((View) getListView().getParent()).setVisibility(View.GONE);
			loadingView = view.findViewById(android.R.id.progress);
		}

		getListView().setOnScrollListener(this);

		if (getArguments() != null)
		{
			elementId = getArguments().getString(KlyphBundleExtras.ELEMENT_ID);
		}

		if ((canAutoLoad && elementId != null) || (loadWhenViewCreated == true && elementId != null))
		{
			load();
		}

		// Now set the ScrollView as the refreshable view, and the refresh listener (this)
		if (requestNewestType != Query.NONE)
			attachViewToPullToRefresh(view);
	}

	public void setAutoLoad(boolean canAutoLoad)
	{
		this.canAutoLoad = canAutoLoad;
	}

	@Override
	public ListView getListView()
	{
		return listView;
	}

	/**
	 * Create, add and set the list empty view Override this method if you want
	 * a custom empty view, or if you replaced listview by a gridview for
	 * example
	 */
	protected View getEmptyView()
	{
		return new ListEmptyView(getActivity());
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState)
	{
		super.onViewStateRestored(savedInstanceState);

		if (savedInstanceState != null)
		{
			if (savedInstanceState.getInt(REQUEST_TYPE_SAVED_STATE) != -1)
				requestType = savedInstanceState.getInt(REQUEST_TYPE_SAVED_STATE);

			if (savedInstanceState.getString(ELEMENT_ID_SAVED_STATE) != null)
				elementId = savedInstanceState.getString(ELEMENT_ID_SAVED_STATE);
		}
	}

	private void setOnScrollListener()
	{
		if (getListView() != null)
		{
			getListView().setOnScrollListener(this);
		}
	}

	protected int getRefreshMenuItemPosition()
	{
		return 5;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_refresh)
		{
			onRefreshClicked();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void onRefreshClicked()
	{
		if (loading == false)
		{
			clearAndRefresh();
		}
	}

	protected void clearAndRefresh()
	{
		loading = false;
		isError = false;
		firstLoad = true;
		getAdapter().clear();
		setListVisible(false);
		setEmptyText(emptyText);
		setBackToTopButtonVisibility(false);
		load();
	}

	/**
	 * Override this method to define this activity's layout. Default layout is
	 * the default ListFragment layout : a list, an empty TextView, a
	 * ProgressBar
	 * 
	 * @return the activity's layout. Example : <code>R.layout.main</code>
	 */
	protected int getCustomLayout()
	{
		return R.layout.list;
	}

	private boolean hasCustomLayout()
	{
		return getCustomLayout() != -1;
	}

	@Override
	protected void setListVisible(boolean visible)
	{
		if (!hasCustomLayout())
		{
			super.setListVisible(visible);
		}
		else
		{
			setListVisibility(visible, true);
		}
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
		// getListView().setEmptyView(getView().findViewById(android.R.id.empty));
	}

	@Override
	protected void setEmptyText(int resId)
	{
		if (getListView().getEmptyView() != null)
		{
			((ListEmptyView) getListView().getEmptyView()).setText(resId);
		}
	}

	public void setElementId(String id)
	{
		this.elementId = id;
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

	protected void setIsFirstLoad(boolean isFirstLoad)
	{
		this.firstLoad = isFirstLoad;
	}

	protected boolean hasNoMoreData()
	{
		return noMoreData;
	}

	protected boolean requestHasMoreData()
	{
		return request.hasMoreData();
	}

	protected void setShouldLoadOnResume(boolean shouldLoad)
	{
		shouldLoadOnResume = shouldLoad;
	}

	protected boolean getShouldLoadOnResume()
	{
		return shouldLoadOnResume;
	}

	protected void defineEmptyText(int resId)
	{
		this.emptyText = resId;
		setEmptyText(resId);
	}

	protected void setErrorText(int resId)
	{
		this.errorText = resId;
	}

	public void load()
	{
		if (getView() == null)
		{
			loadWhenViewCreated = true;
		}
		else if ((isFirstLoad() && !isLoading()) || isViewDestroyed())
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

		if (KlyphFlags.LOG_REQUEST_EXEC)
			Log.d(TAG, "request = " + requestType + ", id = " + elementId + ", offset = " + offset);

		request = new AsyncRequest(requestType, elementId, offset, new AsyncRequest.Callback() {

			public void onComplete(Response response)
			{
				// Log.i(TAG, "onCompleted");
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
		if (getView() != null && getListView() != null) // Check if view is
														// created
			populate(result);
	}

	private void onRequestError(RequestError error)
	{
		Log.d(TAG, "error " + error.toString());

		if (getView() == null || getListView() == null)
			return;

		isError = true;
		loading = false;

		int errorText = this.errorText;

		if (!ConnectionState.getInstance(getActivity()).isOnline())
		{
			errorText = R.string.request_connexion_error;
		}

		// Fragment can be destroyed but receive the error
		if (getAdapter() != null)
		{
			if (getAdapter().isEmpty())
			{
				setEmptyText(errorText);
				populate(new ArrayList<GraphObject>());
			}
			else
			{
				TextButtonItem errorItem = new TextButtonItem();
				errorItem.setText(getString(errorText));

				// TODO This is not a good coding practice !
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
	}

	protected MultiObjectAdapter getAdapter()
	{
		if (getListAdapter() != null)
		{
			if (getListAdapter() instanceof BaseAdapterDecorator)
			{
				return (MultiObjectAdapter) ((BaseAdapterDecorator) getListAdapter()).getDecoratedBaseAdapter();
			}
		}
		return (MultiObjectAdapter) getListAdapter();
	}

	protected void populate(List<GraphObject> data)
	{
		if (getView() != null)
		{
			MultiObjectAdapter adapter = getAdapter();

			for (GraphObject graphObject : data)
			{
				adapter.add(graphObject);
			}
			// adapter.addAll(data); is only available in api 11

			endLoading();

			boolean requestHasMoreData = request != null && request.hasMoreData();

			if (data.size() == 0 || !requestHasMoreData)
				noMoreData = true;

			offset = String.valueOf(adapter.getCount());
		}
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

		if (getAdapter().getItemPosition(loadingObject) >= 0)
			getAdapter().remove(loadingObject, false);

		getAdapter().notifyDataSetChanged();

		setListVisible(true);
	}

	private int	previousScrollY	= 0;

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		if (userScroll)
		{
			if (getListView() instanceof KlyphListView)
			{
				int scrollY = ((KlyphListView) getListView()).getScrollBarY();

				// Scroll down
				if (scrollY > previousScrollY)
				{
					setBackToTopButtonVisibility(false);
				}
				// Scroll up
				else if (scrollY < previousScrollY)
				{
					setBackToTopButtonVisibility(firstVisibleItem >= 3);
				}

				previousScrollY = scrollY;
			}

			if (!loading && !firstLoad && !noMoreData && !isError)
			{
				boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount - 5;

				if (loadMore)
				{
					// Log.i(TAG, "onScroll refresh");
					refresh();
				}
			}
		}

		if (scrollListener != null)
			scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}

	private boolean	backToTopButtonVisibilityState	= false;

	private void setBackToTopButtonVisibility(boolean visible)
	{
		if (backToTopButton != null && backToTopButtonVisibilityState != visible)
		{
			backToTopButton.setVisibility(View.VISIBLE);
			backToTopButton.animate().alpha(visible ? 1.0f : 0.0f).setDuration(500).setListener(new Animator.AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation)
				{

				}

				@Override
				public void onAnimationRepeat(Animator animation)
				{

				}

				@Override
				public void onAnimationEnd(Animator animation)
				{
					if (backToTopButton != null)
						backToTopButton.setVisibility(backToTopButton.getAlpha() == 0.0f ? View.GONE : View.VISIBLE);
				}

				@Override
				public void onAnimationCancel(Animator animation)
				{

				}
			}).start();
			backToTopButtonVisibilityState = visible;
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
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onStop()
	{
		super.onStop();
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

		// if (getAdapter() != null)
		// getAdapter().setData(new ArrayList<GraphObject>());

		request = null;
		loadingObject = null;
		requestType = Query.NONE;
		loadingView = null;
		backToTopButton = null;
		mPullToRefreshLayout = null;
		scrollListener = null;
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		// Log.i(TAG, "onDetach");
	}

	@Override
	public void onStart()
	{
		super.onStart();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		setOnScrollListener();

		if (getAdapter() != null)
		{
			getAdapter().notifyDataSetChanged();
		}

		/*
		 * if (getView() != null && requestNewestType != Query.NONE)
		 * {
		 * attachViewToPullToRefresh();
		 * }
		 */
		// Log.d(TAG, "onResume");
		/*
		 * if (!isLoading() && isFirstLoad() && getShouldLoadOnResume()) load();
		 */
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putString(ELEMENT_ID_SAVED_STATE, elementId);

		if (requestType != Query.NONE)
			outState.putInt(REQUEST_TYPE_SAVED_STATE, requestType);
	}

	public void retryRequestAfterError()
	{
		// Log.d(TAG, "retryRequestAfterError ");
		setEmptyText(emptyText);
		refresh();
	}

	public void cancelRequest()
	{
		if (request != null)
		{
			request.cancel();
		}
	}

	@Override
	public void onSetToFront(Activity activity)
	{

	}

	@Override
	public void onSetToBack(Activity activity)
	{

	}

	protected String getAfterCursor()
	{
		return request != null ? request.getAfterCursor() : "";
	}

	protected String getBeforeCursor()
	{
		return request != null ? request.getBeforeCursor() : "";
	}

	protected AsyncRequest getRequest()
	{
		return request;
	}

	// Pull to refresh
	@Override
	public void onRefreshStarted(View arg0)
	{
		if (requestNewestType != Query.NONE)
			loadNewest();
	}

	protected void setNewestRequestType(int requestType)
	{
		this.requestNewestType = requestType;

		if (requestNewestType != Query.NONE)
			attachViewToPullToRefresh();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// Now set the ScrollView as the refreshable view, and the refresh listener (this)
		if (requestNewestType != Query.NONE)
			attachViewToPullToRefresh();
	}

	protected void loadNewest()
	{
		newestLoading = true;
		setPullToRefreshRefreshing(true);

		if (getAdapter().getFirstItem() instanceof TextButtonItem)
		{
			getAdapter().removeAt(0);
			getAdapter().notifyDataSetChanged();
		}

		String offset = null;

		AsyncRequest request = new AsyncRequest(requestNewestType, getElementId(), offset, new AsyncRequest.Callback() {

			public void onComplete(Response response)
			{
				onRequestNewestComplete(response);
			}
		});

		request.execute();
	}

	private void onRequestNewestComplete(final Response response)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onRequestNewestSuccess(response.getGraphObjectList());
					}
					else
					{
						onRequestNewestError(response.getError());
					}
				}
			});
		}
	}

	protected String getNewestOffset(GraphObject graphObject)
	{
		return "";
	}

	private void onRequestNewestSuccess(List<GraphObject> result)
	{
		if (getView() == null || getActivity() == null)
			return;

		populateNewest(result);

		if (mPullToRefreshLayout != null)
			mPullToRefreshLayout.setRefreshComplete();

		// endLoadingNewest();
	}

	private void onRequestNewestError(RequestError error)
	{
		Log.d("StreamListFragment", "error " + error.toString());

		if (getView() == null || getActivity() == null)
			return;

		int errorText = this.errorNewestText;

		if (!ConnectionState.getInstance(getView().getContext()).isOnline())
		{
			errorText = R.string.request_connexion_error;
		}

		TextButtonItem errorItem = new TextButtonItem();
		errorItem.setText(getString(errorText));

		// TODO This is not a good coding practice !
		errorItem.setButtonListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				loadNewest();
			}
		});

		getAdapter().insert(errorItem, 0);
		getAdapter().notifyDataSetChanged();

		if (mPullToRefreshLayout != null)
			mPullToRefreshLayout.setRefreshComplete();

		newestLoading = false;
	}

	protected void populateNewest(List<GraphObject> data)
	{
		if (data.size() > 0)
		{
			// int n = getAdapter().getCount();

			getAdapter().clear();

			/*
			 * for (int i = insertNewestToIndex; i < n; i++)
			 * {
			 * getAdapter().removeAt(i);
			 * }
			 */
			getAdapter().addAll(data);
			getAdapter().notifyDataSetChanged();
		}
		newestLoading = false;

		if (mPullToRefreshLayout != null)
			mPullToRefreshLayout.setRefreshComplete();

		/*
		 * int n = data.size();
		 * 
		 * if (n > 0)
		 * {
		 * int index = getListView().getFirstVisiblePosition();
		 * View v = getListView().getChildAt(index);
		 * int top = v == null ? 0 : v.getTop();
		 * 
		 * int insertIndex = insertNewestToIndex <= getAdapter().getCount() ? insertNewestToIndex : getAdapter().getCount();
		 * if (KlyphPreferences.getNewsfeedGoToTop() == true || insertIndex < 0)
		 * insertIndex = 0;
		 * 
		 * // String postId = null;
		 * 
		 * if (KlyphPreferences.getNewsfeedGoToTop() == true)
		 * {
		 * List<GraphObject> list = new ArrayList<GraphObject>();
		 * 
		 * for (int i = 0; i < insertIndex; i++)
		 * {
		 * list.add(getAdapter().getItem(i));
		 * }
		 * 
		 * /*
		 * if (getAdapter().getCount() > insertIndex)
		 * {
		 * postId = ((Stream) getAdapter().getItem(insertIndex)).getPost_id();
		 * 
		 * int m = getAdapter().getCount();
		 * for (int i = 0; i < m; i++)
		 * {
		 * if (((Stream) data.get(i)).getPost_id().equals(postId))
		 * {
		 * n = i;
		 * break;
		 * }
		 * }
		 * }
		 * 
		 * 
		 * getAdapter().clear();
		 * 
		 * // Collections.reverse(data);
		 * list.addAll(data);
		 * 
		 * populate(list);
		 * setNoMoreData(false);
		 * }
		 * else
		 * {
		 * for (int i = n - 1; i >= 0; i--)
		 * {
		 * getAdapter().insert(data.get(i), insertIndex);
		 * }
		 * }
		 * 
		 * if (getListAdapter() instanceof KlyphAnimationAdapter)
		 * {
		 * int firstVisiblePosition = getListView().getFirstVisiblePosition();
		 * int lastVisiblePosition = getListView().getLastVisiblePosition();
		 * 
		 * if (firstVisiblePosition != lastVisiblePosition)
		 * ((KlyphAnimationAdapter) getListAdapter()).deactivateNext(lastVisiblePosition - firstVisiblePosition);
		 * else
		 * ((KlyphAnimationAdapter) getListAdapter()).deactivateNext(1);
		 * }
		 * 
		 * getAdapter().notifyDataSetChanged();
		 * 
		 * if (KlyphPreferences.getNewsfeedGoToTop() == false)
		 * {
		 * int newPosition = index + n;
		 * 
		 * if (index == 0)
		 * newPosition++;
		 * 
		 * newestLoading = false;
		 * mPullToRefreshAttacher.setRefreshComplete();
		 * 
		 * getListView().setSelectionFromTop(newPosition, top);
		 * }
		 * else
		 * {
		 * newestLoading = false;
		 * mPullToRefreshAttacher.setRefreshComplete();
		 * }
		 * 
		 * /*
		 * if (n > 0)
		 * {
		 * String string = n == 1 ? getString(R.string.newsfeed_new_stream) : getString(R.string.newsfeed_new_streams, n);
		 * Toast toast = Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT);
		 * toast.setGravity(Gravity.CENTER, 0, 0);
		 * toast.show();
		 * }
		 * 
		 * }
		 * else
		 * {
		 * newestLoading = false;
		 * mPullToRefreshAttacher.setRefreshComplete();
		 * }
		 */
	}

	protected void endLoadingNewest()
	{
		// newestLoading = false;
		// getPullToRefreshListView().onRefreshComplete();
	}

	public boolean isNewestLoading()
	{
		return newestLoading;
	}

	public void setNewestLoading(boolean loading)
	{
		newestLoading = loading;
	}

	public void setNewestInsertIndex(int index)
	{
		insertNewestToIndex = index;
	}

	protected void setPullToRefreshRefreshing(boolean refreshing)
	{
		if (mPullToRefreshLayout != null)
			mPullToRefreshLayout.setRefreshing(refreshing);
	}

	private OnScrollListener	scrollListener;

	public void setOnScrollListener(OnScrollListener listener)
	{
		this.scrollListener = listener;
	}

	public void removeOnScrollListener()
	{
		this.scrollListener = null;
	}

	protected Options getPullToRefreshOptions()
	{
		return Options.create().build();
	}
}
