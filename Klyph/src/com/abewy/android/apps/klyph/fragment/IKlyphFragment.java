/**
* @author Jonathan
*/

package com.abewy.android.apps.klyph.fragment;

import android.app.Activity;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public interface IKlyphFragment
{
	public void load();
	public void setAutoLoad(boolean autoLoad);
	public void setElementId(String id);
	public void onSetToFront(Activity activity);
	public void onSetToBack(Activity activity);
	public void setOnScrollListener(OnScrollListener listener);
	public AbsListView getListView();
}
