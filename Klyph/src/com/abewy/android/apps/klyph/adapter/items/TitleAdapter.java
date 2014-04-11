package com.abewy.android.apps.klyph.adapter.items;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.TitleHolder;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.Title;
import com.abewy.android.apps.klyph.R;

public class TitleAdapter extends KlyphAdapter
{
	public TitleAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_title;
	}

	@Override
	protected void attachHolder(View view)
	{
		view.setTag(new TitleHolder((TextView) view.findViewById(R.id.title), (RelativeLayout) view.findViewById(R.id.item_shadow)));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		TitleHolder holder = (TitleHolder) view.getTag();

		Title title = (Title) data;

		holder.getTitle().setText(title.getName());
		
		holder.getShadow().setVisibility(title.getShadow() == true ? View.VISIBLE : View.GONE);
	}
}
