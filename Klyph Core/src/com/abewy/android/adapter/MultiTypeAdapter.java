/**
 * @author Jonathan
 */

package com.abewy.android.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public abstract class MultiTypeAdapter<T> extends BaseAdapter implements Filterable
{
	private List<T>	items;
	private int		layoutType;
	private Filter	filter;
	private boolean isLinkedlist;

	public MultiTypeAdapter()
	{
		this(0);
	}

	public MultiTypeAdapter(int layoutType)
	{
		super();

		this.layoutType = layoutType;

		items = new LinkedList<T>();
		isLinkedlist = true;
	}

	public int getCount()
	{
		return items != null ? items.size() : 0;
	}

	public List<T> getItems()
	{
		return items;
	}

	public T getItem(int index)
	{
		if (index >= 0 && index < items.size())
			return items.get(index);

		return null;
	}

	public T getFirstItem()
	{
		if (isLinkedlist)
			return ((LinkedList<T>) items).getFirst();
		
		return getItem(0);
	}

	public T getLastItem()
	{
		if (isLinkedlist)
			return ((LinkedList<T>) items).getFirst();
		
		return getItem(items.size() - 1);
	}

	@Override
	public boolean  hasStableIds()
	{
		return true;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public int getItemPosition(T object)
	{
		return items.indexOf(object);
	}

	public void add(T object)
	{
		items.add(object);
	}
	
	public void addAll(Collection<T> objects)
	{
		for (T object : objects)
		{
			add(object);
		}
	}

	public void insert(T object, int index)
	{
		items.add(index, object);
	}

	public void remove(T object)
	{
		items.remove(object);
		notifyDataSetChanged();
	}

	public void removeAt(int index)
	{
		items.remove(index);
		notifyDataSetChanged();
	}
	
	public void removeFirst()
	{
		if (isLinkedlist)
			((LinkedList<T>) items).removeFirst();
		else
			items.remove(0);
		
		notifyDataSetChanged();
	}
	
	public void removeLast()
	{
		if (isLinkedlist)
			((LinkedList<T>) items).removeLast();
		else
			items.remove(items.size() - 1);
		
		notifyDataSetChanged();
	}

	public void clear()
	{
		clear(true);
	}

	public void clear(boolean  notify)
	{
		items.clear();

		if (notify)
			notifyDataSetChanged();
	}

	public void refill()
	{
		List<T> list = new LinkedList<T>();
		isLinkedlist = true;
		
		list.addAll(items);

		items.clear();
		items.addAll(list);
		notifyDataSetChanged();
	}

	public void setData(List<T> data)
	{
		items = data;
		isLinkedlist = items instanceof LinkedList;
		notifyDataSetChanged();
	}

	@Override
	public int getViewTypeCount()
	{
		return 10;
	}

	@Override
	public int getItemViewType(int position)
	{
		if (position < 0 || position >= getCount())
			return  IGNORE_ITEM_VIEW_TYPE;
		//Log.d("MultiTypeAdapter", "getItemViewType: " + position + " " + getCount());
		return getItemViewType(getItem(position));
	}

	@Override
	public boolean  areAllItemsEnabled()
	{
		return false;
	}

	@Override
	public boolean  isEnabled(int position)
	{
		T object = getItem(position);
		return getAdapter(object, layoutType).isEnabled(object);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		T object = getItem(position);
		
		TypeAdapter<T> adapter = getAdapter(object, layoutType);
		
		if (convertView == null)
			convertView = adapter.createView(parent);
		
		adapter.setLayoutParams(convertView);
		
		adapter.bindData(convertView, object);

		return convertView;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		return getView(position, convertView, parent);
	}
	
	protected abstract TypeAdapter<T> getAdapter(T object, int layoutType);
	
	protected abstract int getItemViewType(T object);

	@Override
	public Filter getFilter()
	{
		return filter;
	}

	public void setFilter(Filter filter)
	{
		this.filter = filter;
	}
}
