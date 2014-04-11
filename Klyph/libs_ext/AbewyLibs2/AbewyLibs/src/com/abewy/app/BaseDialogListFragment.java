package com.abewy.app;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.abewy.R;

public abstract class BaseDialogListFragment extends DialogFragment
{
	private ListView	listView;
	private View		loadingView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

		View view = inflater.inflate(getLayout(), container);
		
		getDialog().setTitle(getTitle());
        
        listView = (ListView) view.findViewById(android.R.id.list);
		TextView emptyView = (TextView) view.findViewById(android.R.id.empty);
		
		if (emptyView != null)
			listView.setEmptyView(emptyView);

		//setListVisible(false);
		//setEmptyViewVisible(false);
		
        return view;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
	}

	/**
	 * Override this method to define this activity's layout
	 * 
	 * @return the activity's layout. Example : <code>R.layout.main</code>
	 */
	protected abstract int getLayout();
	
	protected String getTitle()
	{
		return "";
	}

	protected ListView getListView()
	{
		return listView;
	}

	protected void setListVisible(boolean visible)
	{
		getListView().setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	protected void setEmptyViewVisible(boolean visible)
	{
		if (getListView().getEmptyView() != null)
			getListView().getEmptyView().setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	protected void setLoadingView(View loadingView)
	{
		this.loadingView = loadingView;
	}

	protected void setLoadingViewVisible(boolean visible)
	{
		if (loadingView != null)
			loadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		listView = null;
		loadingView = null;
	}
}
