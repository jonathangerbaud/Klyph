package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.animation.Animator;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
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
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.app.BaseFragment;
import com.abewy.klyph.items.Progress;
import com.abewy.klyph.items.TextButtonItem;
import com.abewy.net.ConnectionState;
import com.crashlytics.android.Crashlytics;
import com.haarman.listviewanimations.BaseAdapterDecorator;

public class KlyphFragment2 extends BaseFragment implements IKlyphFragment, OnScrollListener, OnRefreshListener
{
	private final String		TAG							= "KlyphFragment2 " + this.getClass().getSimpleName() + " " + this;

	private final static String	REQUEST_TYPE_SAVED_STATE	= "requestTypeSavedState";
	private final static String	ELEMENT_ID_SAVED_STATE		= "elementIdSavedState";

	// Layout Views
	private KlyphGridView		gridView;
	private ImageButton			backToTopButton;
	private View				loadingView;

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
	private boolean				gridVisible					= false;

	// Empty view text
	private int					emptyText;
	private int					errorText					= R.string.request_error;

	// Pull to refresh
	private PullToRefreshLayout	mPullToRefreshLayout;
	private int					requestNewestType			= Query.NONE;
	private int					errorNewestText				= R.string.request_error;
	private boolean				newestLoading				= false;
	private int					insertNewestToIndex			= 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Log.d("KlyphFragment2", "onCreateView");
		View view = inflater.inflate(getCustomLayout(), container, false);

		gridView = (KlyphGridView) view.findViewById(R.id.grid);
		
		View emptyView = getEmptyView();
		
		if (emptyView != null)
		{
			emptyView.setId(android.R.id.empty);
			
			((ViewGroup) gridView.getParent()).addView(emptyView);
			
			gridView.setEmptyView(emptyView);
		}
		
		// Now set the ScrollView as the refreshable view, and the refresh listener (this)
		if (requestNewestType != Query.NONE)
			attachViewToPullToRefresh();

		return view;
	}

	private void attachViewToPullToRefresh()
	{
		attachViewToPullToRefresh(getView());
	}

	private void attachViewToPullToRefresh(View view)
	{
		if (mPullToRefreshLayout == null && view != null)
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
                .theseChildrenArePullable(R.id.grid, android.R.id.empty)
                
                // We can now complete the setup as desired
                .listener(this)
                //.options(...)
                .setup(mPullToRefreshLayout);
		
		
		
		//view.setVerticalFadingEdgeEnabled(false);
		//view.setFadingEdgeLength(0);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		if (gridView == null)
			throw new IllegalStateException("KlyphFragment2 : There is no KlyphGridView with id \"grid\" defined in the layout");

		backToTopButton = (ImageButton) view.findViewById(R.id.back_to_top_button);

		if (backToTopButton != null)
		{
			backToTopButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					setBackToTopButtonVisibility(false);
					gridView.setSelection(0);
				}
			});
		}

		gridView.setDrawSelectorOnTop(true);
		gridView.setNumColumns(getNumColumn());
		gridView.setFadingEdgeLength(0);
		gridView.setVerticalFadingEdgeEnabled(false);

		loadingObject = new Progress();
		loadingObject.setLoading(true);

		gridView.setVisibility(View.GONE);
		((View) gridView.getParent()).setVisibility(View.GONE);
		loadingView = view.findViewById(android.R.id.progress);

		gridView.setOnScrollListener(this);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> gridView, View view, int position, long id)
			{
				onGridItemClick((KlyphGridView) gridView, view, position, id);
			}
		});

		if (getArguments() != null)
		{
			elementId = getArguments().getString(KlyphBundleExtras.ELEMENT_ID);
		}

		if ((canAutoLoad && elementId != null) || (loadWhenViewCreated == true && elementId != null))
		{
			load();
		}
	}

	@Override
	protected int getLayout()
	{
		return R.layout.grid;
	}

	public void setAutoLoad(boolean canAutoLoad)
	{
		this.canAutoLoad = canAutoLoad;
	}

	protected boolean updateNumColumnOnOrientationChange()
	{
		return true;
	}

	protected int getNumColumn()
	{
		return getResources().getInteger(R.integer.klyph_grid_num_column);
	}

	@Override
	public void onConfigurationChanged(Configuration myConfig)
	{
		super.onConfigurationChanged(myConfig);

		if (getGridView() != null)
		{
			int pos = getGridView().getFirstVisiblePosition();
			getGridView().setNumColumns(getNumColumn());
			getGridView().setSelection(pos);

			if (getAdapter() != null)
				getAdapter().notifyDataSetChanged();
		}
	}

	/**
	 * Alias for getGridView()
	 * 
	 * @see getGridView()
	 */
	public KlyphGridView getListView()
	{
		return getGridView();
	}

	protected KlyphGridView getGridView()
	{
		if (gridView == null && getView() != null)
			gridView = (KlyphGridView) getView().findViewById(R.id.grid);

		return gridView;
	}

	protected ListAdapter getListAdadapter()
	{
		return getGridAdapter();
	}

	protected ListAdapter getGridAdapter()
	{
		if (getGridView() != null)
			return getGridView().getAdapter();

		return null;
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
			if (savedInstanceState.getInt(REQUEST_TYPE_SAVED_STATE) != Query.NONE)
				requestType = savedInstanceState.getInt(REQUEST_TYPE_SAVED_STATE);

			elementId = savedInstanceState.getString(ELEMENT_ID_SAVED_STATE);
			Log.i(TAG, "onViewStateRestored, savedInstanceState not null, requestType = " + requestType);
			Log.i(TAG, "onViewStateRestored, savedInstanceState not null, elementId = " + elementId);
		}
	}

	private void setOnScrollListener()
	{
		if (getGridView() != null)
		{
			getGridView().setOnScrollListener(this);
		}
	}

	protected int getRefreshMenuItemPosition()
	{
		return 5;
	}

	@Override
	public boolean  onOptionsItemSelected(MenuItem item)
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
		return R.layout.grid;
	}

	protected void setGridVisible(boolean visible)
	{
		setGridVisibility(visible, true);
	}

	/**
	 * Alias for setGridVisible()
	 * 
	 * @see setGridVisible()
	 */
	protected void setListVisible(boolean visible)
	{
		setGridVisible(visible);
	}

	private void setGridVisibility(boolean visible, boolean animate)
	{
		ensureList();

		if (gridVisible == visible)
		{
			return;
		}

		gridVisible = visible;

		View parent = (View) getGridView().getParent();

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

			if (parent != null)
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

			if (parent != null)
				parent.setVisibility(View.GONE);
		}
	}

	protected void ensureList()
	{
		// getGridView().setEmptyView(getView().findViewById(android.R.id.empty));
	}

	protected void setEmptyText(int resId)
	{
		if (getGridView().getEmptyView() != null)
		{
			((ListEmptyView) getGridView().getEmptyView()).setText(resId);
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
		if (getView() != null && getGridView() != null) // Check if view is
														// created
			populate(result);
	}

	private void onRequestError(RequestError error)
	{
		Log.d(TAG, "error " + error.toString());

		// Crashlytics report on request error
		try
		{
			throw new Exception("Class : " + this.getClass().getName() + "\n, Request " + requestType + ", Id " + elementId + ", Offset " + offset
								+ "\n, Error " + error.getMessage());
		}
		catch (Exception e)
		{
			Crashlytics.logException(e);
		}

		if (getView() == null || getGridView() == null)
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
		if (getGridView() != null && getGridView().getAdapter() != null)
		{
			if (getGridView().getAdapter() instanceof BaseAdapterDecorator)
			{
				return (MultiObjectAdapter) ((BaseAdapterDecorator) getGridView().getAdapter()).getDecoratedBaseAdapter();
			}
			else
			{
				return (MultiObjectAdapter) getGridView().getAdapter();
			}
		}

		return null;
	}

	protected void setGridAdapter(ListAdapter adapter)
	{
		if (getGridView() != null)
			getGridView().setAdapter(adapter);
	}

	protected void setListAdapter(ListAdapter adapter)
	{
		setGridAdapter(adapter);
	}

	protected void onGridItemClick(KlyphGridView gridView, View view, int position, long id)
	{

	}

	protected void populate(List<GraphObject> data)
	{
		if (getView() != null && getAdapter() != null)
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
			else
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
			if (getGridView() instanceof KlyphGridView)
			{
				int scrollY = ((KlyphGridView) getGridView()).getScrollBarY();

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
				boolean  loadMore = firstVisibleItem + visibleItemCount >= totalItemCount - 5;

				if (loadMore)
				{
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
					if 	(backToTopButton != null)
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
		return loadingObject;
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
		gridView = null;
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// Now set the ScrollView as the refreshable view, and the refresh listener (this)
		if (requestNewestType != Query.NONE)
			attachViewToPullToRefresh();
	}

	protected void setNewestRequestType(int requestType)
	{
		this.requestNewestType = requestType;

		if (requestNewestType != Query.NONE && getView() != null)
			attachViewToPullToRefresh();
	}

	protected void loadNewest()
	{
		newestLoading = true;

		if (getAdapter().getFirstItem() instanceof TextButtonItem)
		{
			getAdapter().removeFirst();
			getAdapter().notifyDataSetChanged();
		}

		String offset = null;

		/*
		 * if (KlyphPreferences.getNewsfeedGoToTop() == false && getAdapter().getCount() > 0 && getAdapter().getCount() > insertNewestToIndex)
		 * {
		 * GraphObject first = (GraphObject) getAdapter().getItem(insertNewestToIndex);
		 * offset = getNewestOffset(first);
		 * }
		 */

		AsyncRequest request = new AsyncRequest(requestNewestType, getElementId(), offset, new AsyncRequest.Callback() {

			public void onComplete(Response response)
			{
				Log.i("StreamListFragment", "onCompleted");
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
		Log.d("StreamListFragment", "onRequestNewestSuccess");

		if (getView() == null || getActivity() == null)
			return;

		populateNewest(result);

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

		mPullToRefreshLayout.setRefreshComplete();

		newestLoading = false;
	}

	protected void populateNewest(List<GraphObject> data)
	{
		if (data.size() > 0)
		{
			int n = getAdapter().getCount();
			getAdapter().clear();
			/*for (int i = insertNewestToIndex; i < n; i++)
			{
				getAdapter().removeAt(i);
			}*/
			getAdapter().addAll(data);
			getAdapter().notifyDataSetChanged();
		}
		newestLoading = false;
		mPullToRefreshLayout.setRefreshComplete();

		/*
		 * int n = data.size();
		 * 
		 * if (n > 0)
		 * {
		 * int index = getListView().getFirstVisiblePosition();
		 * // View v = getListView().getChildAt(index);
		 * // int top = v == null ? 0 : v.getTop();
		 * 
		 * int insertIndex = insertNewestToIndex <= getAdapter().getCount() ? insertNewestToIndex : getAdapter().getCount();
		 * if (insertIndex < 0)
		 * insertIndex = 0;
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
		 * getAdapter().clear();
		 * 
		 * for (GraphObject graphObject : list)
		 * {
		 * getAdapter().add(graphObject);
		 * }
		 * }
		 * 
		 * for (int i = n - 1; i >= 0; i--)
		 * {
		 * getAdapter().insert(data.get(i), insertIndex);
		 * }
		 * 
		 * if (getGridAdapter() instanceof KlyphAnimationAdapter)
		 * {
		 * int firstVisiblePosition = getListView().getFirstVisiblePosition();
		 * int lastVisiblePosition = getListView().getLastVisiblePosition();
		 * 
		 * if (firstVisiblePosition != lastVisiblePosition)
		 * ((KlyphAnimationAdapter) getGridAdapter()).deactivateNext(lastVisiblePosition - firstVisiblePosition);
		 * else
		 * ((KlyphAnimationAdapter) getGridAdapter()).deactivateNext(1);
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
		 * getGridView().smoothScrollToPosition(newPosition);
		 * // getListView().setSelectionFromTop(newPosition, top);
		 * }
		 * else
		 * {
		 * newestLoading = false;
		 * mPullToRefreshAttacher.setRefreshComplete();
		 * }
		 * 
		 * Toast toast = Toast.makeText(getActivity(), getString(R.string.newsfeed_new_streams, n), Toast.LENGTH_SHORT);
		 * toast.setGravity(Gravity.CENTER, 0, 0);
		 * toast.show();
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

	public void setNewestInsertIndex(int index)
	{
		insertNewestToIndex = index;
	}

	protected void setPullToRefreshRefreshing(boolean  refreshing)
	{
		mPullToRefreshLayout.setRefreshing(refreshing);
	}
	
	
	private OnScrollListener scrollListener;
	public void setOnScrollListener(OnScrollListener listener)
	{
		this.scrollListener = listener;
	}
	
	public void removeOnScrollListener()
	{
		this.scrollListener = null;
	}

}