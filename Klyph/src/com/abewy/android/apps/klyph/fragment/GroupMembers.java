package com.abewy.android.apps.klyph.fragment;

import android.os.Bundle;
import android.view.View;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.android.apps.klyph.R;

public class GroupMembers extends KlyphFakeHeaderGridFragment
{
	public GroupMembers()
	{
		setRequestType(Query.GROUP_MEMBERS);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		defineEmptyText(R.string.empty_list_no_member);
		
		setListVisible(false);

		setRequestType(Query.GROUP_MEMBERS);
		
		super.onViewCreated(view, savedInstanceState);
		setListAdapter(new MultiObjectAdapter(getListView(), SpecialLayout.MEMBER));
	}
	
	@Override
	public void onGridItemClick(KlyphGridView l, View v, int position, long id)
	{
		Friend friend = (Friend) l.getItemAtPosition(position);

		startActivity(Klyph.getIntentForGraphObject(getActivity(), friend));
	}
}
