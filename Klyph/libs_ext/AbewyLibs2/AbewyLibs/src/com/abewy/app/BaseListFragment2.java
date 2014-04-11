package com.abewy.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public abstract class BaseListFragment2 extends BaseListFragment {

	private View loadingView;
	private boolean listVisible;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
	{
        View fragView = inflater.inflate(getLayout(), container, false);

		return fragView;
	}
	
	@Override
	public void onViewCreated (View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view,  savedInstanceState);
		
		getListView().setEmptyView(view.findViewById(android.R.id.empty));
		loadingView = view.findViewById(android.R.id.progress);
		
		setListVisible(false); 
		//setEmptyViewVisible(false); 
	}

	/** 
	 * Override this method to define this activity's layout
	 * 
	 * @return the activity's layout. Example : <code>R.layout.main</code>
	 */
	protected abstract int getLayout(); 
	
	protected void setListVisible(boolean visible)
	{
		//getListView().setVisibility(visible ? View.VISIBLE : View.GONE);
		setListVisibility(visible, true);
	}
	
	protected void setListVisible(boolean visible, boolean animate)
	{
		//getListView().setVisibility(visible ? View.VISIBLE : View.GONE);
		setListVisibility(visible, animate);
	}
	
	private void setListVisibility(boolean visible, boolean animate)
	{
		if (listVisible == visible) {
            return;
        }
		
		listVisible = visible;
		
		if (visible) {
            if (animate) {
                loadingView.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
                getListView().startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            } else {
            	loadingView.clearAnimation();
            	getListView().clearAnimation();
            }
            loadingView.setVisibility(View.GONE);
            getListView().setVisibility(View.VISIBLE);
        } else {
            if (animate) {
            	loadingView.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            	getListView().startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            } else {
            	loadingView.clearAnimation();
            	getListView().clearAnimation();
            }
            loadingView.setVisibility(View.VISIBLE);
            getListView().setVisibility(View.GONE);
        }
	}
	
	/*protected void setEmptyViewVisible(boolean visible)
	{
		if (getListView().getEmptyView() != null)
			getListView().getEmptyView().setVisibility(visible ? View.VISIBLE : View.GONE);
	}*/
	
	protected void setEmptyText(int resId)
	{
		if (getActivity() != null && getListView().getEmptyView() != null)
		{
			((TextView) getListView().getEmptyView()).setText(resId);
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		loadingView = null;
	}
	
	/*protected void setLoadingView(View loadingView)
	{
		this.loadingView = loadingView;
	}
	
	protected void setLoadingViewVisible(boolean visible)
	{
		if (loadingView != null)
			loadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
	}*/
}
