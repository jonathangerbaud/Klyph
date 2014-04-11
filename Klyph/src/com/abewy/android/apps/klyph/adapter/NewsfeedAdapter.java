/**
* @author Jonathan
*/

package com.abewy.android.apps.klyph.adapter;

import android.widget.AbsListView;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.AdItem;

public class NewsfeedAdapter extends MultiObjectAdapter
{
	private static final int AD_INTERVAL = 15;
	private int lastAdPosition = -1;
	
	public NewsfeedAdapter(AbsListView listView)
	{
		this(listView, 0);
	}
	
	public NewsfeedAdapter(AbsListView listView, int layoutType)
	{
		super(listView, layoutType);
	}

	@Override
	public void add(GraphObject object)
	{
		if (object instanceof Stream)
		{
			final int size = getCount();
			if (size - lastAdPosition >= AD_INTERVAL)
			{
				lastAdPosition = size; 
				super.add(new AdItem());
			}
		}
		
		super.add(object);
	}

	@Override
	public void clear()
	{
		super.clear();
		
		lastAdPosition = -1;
	}
}
