package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.PicturePrimaryTextHolder;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.core.util.FacebookUtil;
import com.abewy.android.apps.klyph.R;

public class TagAdapter extends KlyphAdapter
{
	public TagAdapter()
	{
		super();
	}
	
	@Override
	protected int getLayout()
	{
		return R.layout.item_picture_primary_text;
	}
	
	@Override
	protected void attachHolder(View view)
	{
		ImageView friendPicture = (ImageView) view.findViewById(R.id.picture);
		TextView friendName = (TextView) view.findViewById(R.id.primary_text);

		setHolder(view, new PicturePrimaryTextHolder(friendPicture, friendName));
	}
	
	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);
		
		PicturePrimaryTextHolder holder = (PicturePrimaryTextHolder) getHolder(view);
		
		//holder.getPicture().setImageDrawable(null);
		
		Tag tag = (Tag) data;

		holder.getPrimaryText().setText(tag.getName());

		String url = FacebookUtil.getImageURLForId(tag.getId());
		loadImage(holder.getPicture(), url, AttrUtil.getResourceId(getContext(view), R.attr.squarePlaceHolderIcon), data);
	}
}
