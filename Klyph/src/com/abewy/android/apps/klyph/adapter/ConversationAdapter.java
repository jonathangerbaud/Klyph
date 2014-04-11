/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.adapter;

import android.util.Log;
import android.widget.AbsListView;
import com.abewy.android.apps.klyph.core.fql.Message;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.AdItem;

public class ConversationAdapter extends MultiObjectAdapter
{
	private static final int	AD_INTERVAL		= 15;
	private int					lastAdPosition	= -1;

	public ConversationAdapter(AbsListView listView)
	{
		this(listView, 0);
	}

	public ConversationAdapter(AbsListView listView, int layoutType)
	{
		super(listView, layoutType);
	}

	@Override
	public void add(GraphObject object)
	{
		if (object instanceof Message)
		{
			final int size = getCount();
			Log.d("ConversationAdapter", "add: " + size + " " + lastAdPosition);
			if (size - lastAdPosition >= AD_INTERVAL)
			{
				Log.d("ConversationAdapter", "add: add ad");
				lastAdPosition = size;
				super.add(new AdItem());
			}
		}

		super.add(object);
	}

	@Override
	public void insert(GraphObject object, int index)
	{
		if (object instanceof Message)
		{
			final int size = getCount();
			Log.d("ConversationAdapter", "insert: " + size + " " + lastAdPosition);
			if (size - lastAdPosition >= AD_INTERVAL)
			{
				lastAdPosition = size-1;
				super.insert(new AdItem(), 0);
			}
		}
		
		super.insert(object, index);
	}

	@Override
	public void clear()
	{
		super.clear();

		lastAdPosition = -1;
	}
}
