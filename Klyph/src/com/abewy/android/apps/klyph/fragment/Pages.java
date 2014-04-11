package com.abewy.android.apps.klyph.fragment;

import android.os.Bundle;
import android.view.View;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.android.apps.klyph.R;

public class Pages extends KlyphFakeHeaderGridFragment
{
	public Pages()
	{
		setRequestType(Query.ELEMENT_PAGES);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		defineEmptyText(R.string.empty_list_no_page);
		
		setListVisible(false);
		
		setRequestType(Query.ELEMENT_PAGES);
		
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onGridItemClick(KlyphGridView l, View v, int position, long id)
	{
		Page page = (Page) l.getItemAtPosition(position);

		startActivity(Klyph.getIntentForGraphObject(getActivity(), page));		
	}
}
