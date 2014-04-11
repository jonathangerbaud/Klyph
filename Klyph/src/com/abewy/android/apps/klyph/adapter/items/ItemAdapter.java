package com.abewy.android.apps.klyph.adapter.items;

import android.text.util.Linkify;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.ItemHolder;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.Item;
import com.abewy.android.apps.klyph.R;

public class ItemAdapter extends KlyphAdapter
{
	public ItemAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_item;
	}

	@Override
	protected void attachHolder(View view)
	{
		view.setTag(new ItemHolder((TextView) view.findViewById(R.id.name), (TextView) view.findViewById(R.id.desc), (RelativeLayout) view.findViewById(R.id.item_shadow)));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		ItemHolder holder = (ItemHolder) view.getTag();

		Item item = (Item) data;

		holder.getName().setText(item.getName());
		holder.getDesc().setAutoLinkMask(Linkify.ALL);
		holder.getDesc().setText(item.getDesc());
		holder.getShadow().setVisibility(item.getShadow() == true ? View.VISIBLE : View.GONE);
	}
}
