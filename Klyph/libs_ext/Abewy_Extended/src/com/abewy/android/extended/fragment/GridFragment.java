/**
 * @author Jonathan
 */

package com.abewy.android.extended.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import com.abewy.android.extended.R;

public abstract class GridFragment extends Fragment
{
	public static interface IEmptyView
	{
		public void setText(String text);

		public void setText(int text);
	}

	private GridView	gridView;
	private View		loadingView;
	private boolean		gridVisible;
	private int			errorText	= -1;
	private int			emptyText	= -1;

	public GridFragment()
	{}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getCustomLayout(), container, false);

		gridView = (GridView) view.findViewById(R.id.grid);

		View emptyView = getEmptyView();

		if (emptyView != null)
		{
			emptyView.setId(android.R.id.empty);

			((ViewGroup) gridView.getParent()).addView(emptyView);

			gridView.setEmptyView(emptyView);
		}

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		if (gridView == null)
			throw new IllegalStateException("GridFragment : There is no GridView with id \"grid\" defined in the layout");

		gridView.setDrawSelectorOnTop(true);
		gridView.setNumColumns(getColumnCount());

		((View) gridView.getParent()).setVisibility(View.GONE);
		loadingView = view.findViewById(android.R.id.progress);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> gridView, View view, int position, long id)
			{
				onGridItemClick((GridView) gridView, view, position, id);
			}
		});
	}

	protected abstract void onGridItemClick(GridView gridView, View view, int position, long id);

	protected int getColumnCount()
	{
		return 2;
	}

	protected boolean updateNumColumnOnOrientationChange()
	{
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration myConfig)
	{
		super.onConfigurationChanged(myConfig);

		if (getGridView() != null)
		{
			int pos = getGridView().getFirstVisiblePosition();
			getGridView().setNumColumns(getColumnCount());
			getGridView().setSelection(pos);
		}
	}

	protected GridView getGridView()
	{
		if (gridView == null && getView() != null)
			gridView = (GridView) getView().findViewById(R.id.grid);

		return gridView;
	}

	protected ListAdapter getAdapter()
	{
		if (getGridView() != null && getGridView().getAdapter() != null)
		{
			return getGridView().getAdapter();
		}

		return null;
	}

	protected void setAdapter(ListAdapter adapter)
	{
		if (getGridView() != null)
			getGridView().setAdapter(adapter);
	}

	/**
	 * Create, add and set the list empty view Override this method if you want
	 * a custom empty view, or if you replaced listview by a gridview for
	 * example
	 */
	protected abstract View getEmptyView();

	/**
	 * Override this method to define this activity's layout. Default layout is
	 * the default ListFragment layout : a list, an empty TextView, a
	 * ProgressBar
	 * 
	 * @return the activity's layout. Example : <code>R.layout.main</code>
	 */
	protected abstract int getCustomLayout();

	protected void setGridVisible(boolean visible)
	{
		setGridVisibility(visible, true);
	}

	private void setGridVisibility(boolean visible, boolean animate)
	{
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

	protected void setEmptyText(int resId)
	{
		emptyText = resId;
		setText(emptyText);
	}

	protected void setErrorText(int resId)
	{
		errorText = resId;
	}

	private void setText(int resId)
	{
		if (resId != -1)
		{
			final View emptyView = getGridView().getEmptyView();
			if (emptyView != null && emptyView instanceof IEmptyView)
			{
				((IEmptyView) emptyView).setText(resId);
			}
		}
	}

	protected void onLoad()
	{
		setText(emptyText);
		setGridVisible(false);
	}

	protected void onLoaded()
	{
		setGridVisible(true);
	}

	protected void onError()
	{
		setText(errorText);
	}
}
