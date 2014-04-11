package com.abewy.android.widget;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public abstract class MultiTypeAdapter extends BaseAdapter implements Filterable
{
	private List<Object>	objects;
	private int					specialLayout;
	private Filter				filter;

	public MultiTypeAdapter()
	{
		this(0);
	}

	public MultiTypeAdapter(int specialLayout)
	{
		super();
		this.specialLayout = specialLayout;
		objects = new ArrayList<Object>();
	}

	public int getCount()
	{
		if (objects != null)
			return objects.size();
		
		return 0;
	}
	
	public List<Object> getItems()
	{
		return new ArrayList<Object>();//objects//objects;
	}

	public Object getItem(int index)
	{
		if (index < objects.size())
			return objects.get(index);

		return null;
	}
	
	public Object getFirstItem()
	{
		return getItem(0);
	}
	
	public Object getLastItem()
	{
		return getItem(objects.size() - 1);
	}

	// @Override
	public long getItemId(int position)
	{
		return position;
	}

	public int getItemPosition(Object object)
	{
		return objects.indexOf(object);
	}

	public void add(Object object)
	{
		objects.add(object);
		// notifyDataSetChanged();
	}

	public void insert(Object object, int index)
	{
		objects.add(index, object);
		// notifyDataSetChanged();
	}

	public void remove(Object object)
	{
		objects.remove(object);
		notifyDataSetChanged();
	}

	public void removeAt(int index)
	{
		removeAt(index, false);
	}

	public void removeAt(int index, boolean animated)
	{
		if (index >= 0 && index < objects.size())
		{
			objects.remove(index);
			notifyDataSetChanged();
		}
	}

	public void clear()
	{
		clear(true);
	}
	
	public void clear(boolean notify)
	{
		objects.clear();
		
		if (notify)
			notifyDataSetChanged();
	}
	
	public void refill()
	{
		List<Object> list = new ArrayList<Object>();
		list.addAll(objects);
		
		objects.clear();
		objects.addAll(list);
		notifyDataSetChanged();
	}

	public void setData(List<Object> data)
	{
		objects = data;
		notifyDataSetChanged();
	}

	@Override
	public int getViewTypeCount()
	{
		/*ArrayList<Integer> types = new ArrayList<Integer>();

		for (int i = 0; i < getCount(); i++)
		{
			Object object = getItem(i);

			if (!types.contains(object.getItemViewType()))
				types.add(object.getItemViewType());
		}*/

		return 1;//types.size() + 1;
	}

	@Override
	public int getItemViewType(int position)
	{
		return 0;//getItem(position).getItemViewType();
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return false;
	}

	@Override
	public boolean isEnabled(int position)
	{
		return isSelectable(position, specialLayout);
	}
	
	protected abstract boolean isSelectable(int position, int specialLayout);
	
	protected abstract View initViewForType(Object object, View convertView, ViewGroup parent, int specialLayout);
	
	protected abstract View updateView(Object object, View convertView, int specialLayout);

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Object object = getItem(position);
		
		convertView = initViewForType(object, convertView, parent, specialLayout);
		
		updateView(object, convertView, specialLayout);
		
		return convertView;
	}

	@Override
	public Filter getFilter()
	{
		return filter;
	}

	public void setFilter(Filter filter)
	{
		this.filter = filter;;
	}
}
