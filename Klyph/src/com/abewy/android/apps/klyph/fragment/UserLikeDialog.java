package com.abewy.android.apps.klyph.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.R;

public class UserLikeDialog extends KlyphDialogFragment implements OnItemClickListener
{
	public UserLikeDialog()
	{
		super();
		setRequestType(Query.USER_LIKE);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		setListAdapter(new MultiObjectAdapter(getListView()));
		
		defineEmptyText(R.string.empty_list_no_user);
		 
		getListView().setOnItemClickListener(this);
		setListVisible(false);
		
		setRequestType(Query.USER_LIKE);
		
		load();
	}
	
	@Override
	protected String getTitle()
	{
		return getResources().getString(R.string.user_like);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Friend friend = (Friend) arg0.getItemAtPosition(arg2);
		
		startActivity(Klyph.getIntentForGraphObject(getActivity(), friend));		
	}
}
