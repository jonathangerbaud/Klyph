package com.abewy.android.apps.klyph.adapter.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.ItemHolder;
import com.abewy.android.apps.klyph.adapter.holder.TitleTwoItemHolder;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.TitleTwoItem;
import com.abewy.android.apps.klyph.R;

public class TitleTwoItemAdapter extends KlyphAdapter
{
	public TitleTwoItemAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_title_two_item;
	}

	@Override
	protected void attachHolder(View view)
	{
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView firstName = (TextView) view.findViewById(R.id.first_item_name);
		TextView firstDesc = (TextView) view.findViewById(R.id.first_item_desc);
		ImageView firstIcon = (ImageView) view.findViewById(R.id.first_item_icon);
		TextView secondName = (TextView) view.findViewById(R.id.second_item_name);
		TextView secondDesc = (TextView) view.findViewById(R.id.second_item_desc);
		ImageView secondIcon = (ImageView) view.findViewById(R.id.second_item_icon);
		RelativeLayout shadow = (RelativeLayout) view.findViewById(R.id.item_shadow);

		view.setTag(new TitleTwoItemHolder(title, firstName, firstDesc, firstIcon, secondName, secondDesc, secondIcon,
				shadow));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		TitleTwoItemHolder holder = (TitleTwoItemHolder) view.getTag();

		TitleTwoItem item = (TitleTwoItem) data;

		holder.getTitle().setText(item.getTitle());
		holder.getFirstName().setText(item.getFirstName());
		holder.getFirstDesc().setText(item.getFirstDesc());
		holder.getSecondName().setText(item.getSecondName());
		holder.getSecondDesc().setText(item.getSecondDesc());

		holder.getFirstIcon().setImageResource(item.getFirstIcon());
		holder.getSecondIcon().setImageResource(item.getSecondIcon());

		holder.getShadow().setVisibility(item.getShadow() == true ? View.VISIBLE : View.GONE);
	}
}
