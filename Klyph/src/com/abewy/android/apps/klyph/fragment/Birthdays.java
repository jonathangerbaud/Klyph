package com.abewy.android.apps.klyph.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.android.apps.klyph.R;

public class Birthdays extends KlyphFragment2
{
	public Birthdays()
	{
		setRequestType(Query.BIRTHDAYS);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		Calendar calendar = new GregorianCalendar();
		Date trialTime = new Date();
		calendar.setTime(trialTime);

		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

		if (month.length() == 1)
			month = "0" + month;

		if (day.length() == 1)
			day = "0" + day;

		setInitialOffset(month + "/" + day);
		
		setListAdapter(new MultiObjectAdapter(getListView(), SpecialLayout.USER_BIRTHDAY));
		
		defineEmptyText(R.string.empty_list_no_birthday);
		
		setRequestType(Query.BIRTHDAYS);
		setListVisible(false);

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		super.populate(data);
		
		if (data.size() > 0)
			setOffset(((Friend) data.get(data.size() - 1)).getBirthday_date());
	}
	
	@Override
	public void onGridItemClick(KlyphGridView l, View v, int position, long id)
	{
		Friend friend = (Friend) l.getItemAtPosition(position);
		startActivity(Klyph.getIntentForGraphObject(getActivity(), friend));
	}
}
