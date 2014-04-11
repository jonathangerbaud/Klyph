package com.abewy.android.apps.klyph.fragment;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.app.MessageActivity;
import com.abewy.android.apps.klyph.core.fql.MessageThread;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class Threads extends KlyphFragment
{
	public Threads()
	{
		setRequestType(Query.THREADS);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		setListAdapter(new MultiObjectAdapter(getListView()));

		defineEmptyText(R.string.empty_list_no_message);

		getListView().setDrawSelectorOnTop(false);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		setListVisible(false);

		setRequestType(Query.THREADS);

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		GraphObject object = (GraphObject) l.getItemAtPosition(position);
		
		if (object instanceof MessageThread)
		{
			MessageThread thread = (MessageThread) l.getItemAtPosition(position);
			Intent intent = new Intent(getActivity(), MessageActivity.class);
			intent.putExtra(KlyphBundleExtras.THREAD_ID, thread.getThread_id());
			//intent.putExtra(KlyphBundleExtras.THREAD_NAME, thread.getName());
			startActivity(intent);
		}
	}
	
	@Override
	protected void populate(List<GraphObject> data)
	{
		super.populate(data);
		
		if (data.size() > 0)
			setOffset(((MessageThread) data.get(data.size() - 1)).getUpdated_time());
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

	}
}