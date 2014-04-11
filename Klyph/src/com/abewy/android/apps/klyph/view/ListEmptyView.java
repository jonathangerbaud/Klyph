package com.abewy.android.apps.klyph.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.abewy.android.apps.klyph.R;

public class ListEmptyView extends RelativeLayout
{
	private TextView textView;
	
	public ListEmptyView(Context context)
	{
		super(context);
		init();
	}
	
	public ListEmptyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}
	
	public ListEmptyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	private void init()
	{
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_empty_view, this, true);
		
		textView = (TextView) findViewById(R.id.textView);
	}
	
	public void setText(String text)
	{
		textView.setText(text);
	}
	
	public void setText(int	resId)
	{
		textView.setText(resId);
	}
}
