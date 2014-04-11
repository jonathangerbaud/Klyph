package com.abewy.app;

import android.app.ListFragment;

public class BaseListFragment extends ListFragment
{
	protected void setListVisible(boolean visible)
	{
		setListShown(visible);
		
	}

	protected void setEmptyText(int resId)
	{
		if (getActivity() != null)
			setEmptyText(getResources().getString(resId));
	}
}
