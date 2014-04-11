/**
* @author Jonathan
*/

package com.abewy.android.apps.klyph.adapter.animation;

import android.widget.BaseAdapter;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.haarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;

public class DeleteAdapter extends AnimateDismissAdapter<GraphObject> 
{

	public DeleteAdapter(BaseAdapter baseAdapter, OnDismissCallback callback)
	{
		super(baseAdapter, callback);
	}
}
