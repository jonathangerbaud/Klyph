/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.adapter;

import java.util.ArrayList;
import java.util.List;
import android.widget.AbsListView;
import com.abewy.android.adapter.MultiTypeAdapter;
import com.abewy.android.adapter.TypeAdapter;
import com.abewy.android.apps.klyph.adapter.animation.DeleteAdapter;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.crashlytics.android.Crashlytics;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;

public class MultiObjectAdapter extends MultiTypeAdapter<GraphObject>
{
	private DeleteAdapter	deleteAdapter;

	public MultiObjectAdapter(AbsListView listView)
	{
		this(listView, 0);
	}

	public MultiObjectAdapter(AbsListView listView, int layoutType)
	{
		super(layoutType);

		if (listView != null)
		{
			deleteAdapter = new DeleteAdapter(this, new OnDismissCallback() {

				@Override
				public void onDismiss(AbsListView listView, int[] reverseSortedPositions)
				{
					for (int position : reverseSortedPositions)
					{
						removeAt(position);
					}
				}
			});
			deleteAdapter.setAbsListView(listView);
		}
	}

	@Override
	public void remove(GraphObject object)
	{
		remove(object, false);
	}

	public void remove(GraphObject object, boolean animated)
	{
		if (animated == false || deleteAdapter == null)
		{
			super.remove(object);
			notifyDataSetChanged();
		}
		else
		{
			List<Integer> list = new ArrayList<Integer>();
			list.add(getItemPosition(object));
			deleteAdapter.animateDismiss(list);
		}
	}

	@Override
	public void removeAt(int index)
	{
		removeAt(index, false);
	}

	public void removeAt(int index, boolean animated)
	{
		if (index >= 0 && index < getCount())
		{
			if (animated == false || deleteAdapter == null)
			{
				super.removeAt(index);
				notifyDataSetChanged();
			}
			else
			{
				List<Integer> list = new ArrayList<Integer>();
				list.add(index);
				deleteAdapter.animateDismiss(list);
			}
		}
	}
	
	@Override
	public void removeFirst()
	{
		removeFirst(false);
	}
	
	public void removeFirst(boolean animated)
	{
		if (animated == false || deleteAdapter == null)
		{
			super.removeFirst();
		}
		else
		{
			List<Integer> list = new ArrayList<Integer>();
			list.add(0);
			deleteAdapter.animateDismiss(list);
		}
	}
	
	@Override
	public void removeLast()
	{
		removeLast(false);
	}
	
	public void removeLast(boolean animated)
	{
		if (animated == false || deleteAdapter == null)
		{
			super.removeLast();
		}
		else
		{
			List<Integer> list = new ArrayList<Integer>();
			list.add(getCount() - 1);
			deleteAdapter.animateDismiss(list);
		}
	}

	@Override
	protected TypeAdapter<GraphObject> getAdapter(GraphObject object, int layoutType)
	{
		TypeAdapter<GraphObject> adapter = AdapterSelector.getAdapter(object, layoutType, this);

		if (adapter == null)
		{
			Crashlytics.setString("MultiObjectAdapter_object", object != null ? object.toString() : "object is null");
			Crashlytics.setString("MultiObjectAdapter_layout", String.valueOf(layoutType));
		}

		return adapter;
	}

	private List<Integer>	types	= new ArrayList<Integer>();

	@Override
	protected int getItemViewType(GraphObject object)
	{
		int type = AdapterSelector.getItemViewType(object);
		int index = types.indexOf(type);

		if (index == -1)
		{
			index = types.size();
			types.add(type);
		}

		return index;
	}

}
