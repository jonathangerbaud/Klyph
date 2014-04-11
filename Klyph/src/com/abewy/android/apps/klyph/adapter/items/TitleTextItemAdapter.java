package com.abewy.android.apps.klyph.adapter.items;

import android.text.util.Linkify;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.TitleTextItemHolder;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.TitleTextItem;
import com.abewy.android.apps.klyph.R;

public class TitleTextItemAdapter extends KlyphAdapter
{
	public TitleTextItemAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_title_text_item;
	}

	@Override
	protected void attachHolder(View view)
	{
		view.setTag(new TitleTextItemHolder((TextView) view.findViewById(R.id.title), (TextView) view.findViewById(R.id.text), (RelativeLayout) view.findViewById(R.id.item_shadow)));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		TitleTextItemHolder holder = (TitleTextItemHolder) view.getTag();

		TitleTextItem item = (TitleTextItem) data;

		holder.getTitle().setText(item.getTitle());
		holder.getText().setAutoLinkMask(Linkify.ALL);
		holder.getText().setText(item.getText());
		
		holder.getShadow().setVisibility(item.getShadow() == true ? View.VISIBLE : View.GONE);
	}
}
