package com.abewy.android.extended.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class TypeAdapter<T>
{
	public TypeAdapter()
	{
		
	}
	
	public View createView(ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(getLayoutRes(), parent, false);
		
		attachViewHolder(view);
		
		return view;
	}
	
	public abstract void setLayoutParams(View view);
	
	public abstract void bindData(View view, T data, int position);
	
	public abstract boolean  isEnabled(T object);

	protected abstract  void attachViewHolder(View view);

	protected abstract int getLayoutRes();

}
