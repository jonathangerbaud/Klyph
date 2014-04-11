package com.abewy.android.apps.klyph.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.abewy.android.adapter.TypeAdapter;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;

public abstract class KlyphAdapter extends TypeAdapter<GraphObject>
{

	public KlyphAdapter()
	{
		
	}
	
	@Override
	protected int getLayoutRes()
	{
		return getLayout();
	}
	
	protected int getLayout()
	{
		return 0;
	}
	
	@Override
	public void bindData(View view, GraphObject object)
	{
		mergeViewWithData(view, object);
	}
	
	protected void mergeViewWithData(View view, GraphObject object)
	{
		setData(view, object);
	}
	
	@Override
	public boolean  isEnabled(GraphObject object)
	{
		return true;
	}
	
	@Override
	protected void attachViewHolder(View view)
	{
		attachHolder(view);
	}
	
	protected void attachHolder(View view)
	{
		
	}
	
	@Override
	public void setLayoutParams(View view)
	{
		
	}

	protected void loadImage(ImageView imageView, String url)
	{
		ImageLoader.display(imageView, url);
	}

	protected void loadImage(ImageView imageView, String url, int placeHolder)
	{
		ImageLoader.display(imageView, url, placeHolder);
	}

	protected void loadImage(ImageView imageView, String url, boolean fadeIn)
	{
		ImageLoader.display(imageView, url, fadeIn);
	}
	
	protected void loadImage(ImageView imageView, String url, GraphObject graphObject)
	{
		ImageLoader.display(imageView, url, !graphObject.getDisplayedOnce());
	}

	protected void loadImage(ImageView imageView, String url, int placeHolder, boolean fadeIn)
	{
		ImageLoader.display(imageView, url, fadeIn, placeHolder);
	}
	
	protected void loadImage(ImageView imageView, String url, int placeHolder, GraphObject graphObject)
	{
		ImageLoader.display(imageView, url, !graphObject.getDisplayedOnce(), placeHolder);
	}

	protected Context getContext(View view)
	{
		return view.getContext();
	}
	
	protected void setHolder(View view, Object holder)
	{
		view.setTag(R.id.view_holder, holder);
	}

	protected Object getHolder(View view)
	{
		return view.getTag(R.id.view_holder);
	}

	protected void setData(View view, GraphObject data)
	{
		view.setTag(R.id.view_data, data);
	}

	protected GraphObject getData(View view)
	{
		return (GraphObject) view.getTag(R.id.view_data);
	}
}
