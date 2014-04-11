package com.abewy.android.apps.klyph.adapter.items;

import android.view.View;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.HeaderHolder;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.Header;
import com.abewy.android.apps.klyph.R;

public class HeaderAdapter extends KlyphAdapter
{
	public HeaderAdapter()
	{
		super();
	}
	
	@Override
	protected int getLayout()
	{
		return R.layout.item_header;
	}
	
	@Override
	protected void attachHolder(View view)
	{
		view.setTag(new HeaderHolder((TextView) view.findViewById(R.id.header_title)));
	}
	
	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		Header header = (Header) data;
		
		HeaderHolder holder = (HeaderHolder) view.getTag();
		holder.getHeaderTitle().setText(header.getName());
	}
}
