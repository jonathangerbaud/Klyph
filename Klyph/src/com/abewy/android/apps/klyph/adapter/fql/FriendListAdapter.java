package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.PicturePrimaryTextHolder;
import com.abewy.android.apps.klyph.core.fql.FriendList;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class FriendListAdapter extends KlyphAdapter
{
	public FriendListAdapter()
	{
		super();
	}
	
	@Override
	protected int getLayout()
	{
		return android.R.layout.simple_dropdown_item_1line;
	}
	
	@Override
	protected void attachHolder(View view)
	{
		TextView friendListName = (TextView) view.findViewById(android.R.id.text1);

		setHolder(view, new PicturePrimaryTextHolder(null, friendListName));
	}
	
	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);
		
		PicturePrimaryTextHolder holder = (PicturePrimaryTextHolder) getHolder(view);
		
		FriendList friendList = (FriendList) data;

		holder.getPrimaryText().setText(friendList.getName());
	}
}