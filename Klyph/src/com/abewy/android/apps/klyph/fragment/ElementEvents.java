package com.abewy.android.apps.klyph.fragment;

import android.os.Bundle;
import android.view.View;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.android.apps.klyph.R;

public class ElementEvents extends KlyphFakeHeaderGridFragment
{
	public ElementEvents()
	{
		setRequestType(Query.ELEMENT_EVENTS);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		defineEmptyText(R.string.empty_list_no_event);
		
		setListVisible(false);
		
		setRequestType(Query.ELEMENT_EVENTS);
		
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onGridItemClick(KlyphGridView l, View v, int position, long id)
	{
		Event event = (Event) l.getItemAtPosition(position);

		startActivity(Klyph.getIntentForGraphObject(getActivity(), event));
	}
}
