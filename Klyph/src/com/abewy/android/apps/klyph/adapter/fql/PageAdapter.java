package com.abewy.android.apps.klyph.adapter.fql;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.PicturePrimarySecondaryTextHolder;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.fql.Page.Hours;
import com.abewy.android.apps.klyph.core.fql.Page.Parking;
import com.abewy.android.apps.klyph.core.fql.Page.PaymentOptions;
import com.abewy.android.apps.klyph.core.fql.Page.RestaurantServices;
import com.abewy.android.apps.klyph.core.fql.Page.RestaurantSpecialties;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.R;

public class PageAdapter extends KlyphAdapter
{
	private int placeHolder = -1;
	
	public PageAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		//return R.layout.item_picture_primary_secondary_text;
		return R.layout.item_grid_picture_primary_secondary_text;
	}

	@Override
	protected void attachHolder(View view)
	{
		ImageView pagePicture = (ImageView) view.findViewById(R.id.picture);
		TextView pageName = (TextView) view.findViewById(R.id.primary_text);
		TextView pageCategory = (TextView) view.findViewById(R.id.secondary_text);

		setHolder(view, new PicturePrimarySecondaryTextHolder(pagePicture, pageName, pageCategory));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);
		
		PicturePrimarySecondaryTextHolder holder = (PicturePrimarySecondaryTextHolder) getHolder(view);
		
		//holder.getPicture().setImageDrawable(null);

		Page page = (Page) data;

		holder.getPrimaryText().setText(page.getName());
		holder.getSecondaryText().setText(page.getType().toUpperCase());
		
		if (placeHolder == -1)
			placeHolder = AttrUtil.getResourceId(getContext(holder.getPicture()), R.attr.squarePlaceHolderIcon);

		loadImage(holder.getPicture(), page.getPic(), placeHolder, data);
	}
}
