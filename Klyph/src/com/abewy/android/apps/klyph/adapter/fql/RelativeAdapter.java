package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.PicturePrimarySecondaryTextHolder;
import com.abewy.android.apps.klyph.core.fql.User.Relative;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.core.util.FacebookUtil;
import com.abewy.android.apps.klyph.R;

public class RelativeAdapter extends KlyphAdapter
{
	public RelativeAdapter()
	{
		super();

	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_picture_primary_secondary_text;
	}

	@Override
	protected void attachHolder(View view)
	{
		ImageView userPicture = (ImageView) view.findViewById(R.id.picture);
		TextView userName = (TextView) view.findViewById(R.id.primary_text);
		TextView userBirthday = (TextView) view.findViewById(R.id.secondary_text);
		RelativeLayout shadow = (RelativeLayout) view.findViewById(R.id.item_shadow);
		
		setHolder(view, new PicturePrimarySecondaryTextHolder(userPicture, userName, userBirthday, shadow));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);
		
		PicturePrimarySecondaryTextHolder holder = (PicturePrimarySecondaryTextHolder) getHolder(view);
		
		//holder.getPicture().setImageDrawable(null);
		
		Relative user = (Relative) data;

		holder.getPrimaryText().setText(user.getName());
		holder.getSecondaryText().setText(user.getRelationship());
		
		String url = FacebookUtil.getImageURLForId(user.getUid(), FacebookUtil.NORMAL);

		loadImage(holder.getPicture(), url, AttrUtil.getResourceId(getContext(view), R.attr.squarePlaceHolderIcon), data);
		
		holder.getShadow().setVisibility(user.getShadow() == true ? View.VISIBLE : View.GONE);
	}

	@Override
	public boolean  isEnabled(GraphObject object)
	{
		return true;
	}
}
