package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class UserListDialog extends KlyphDialogFragment implements OnItemClickListener
{
	private List<GraphObject> list;
	private int customTitle = -1;
	private String customStringTitle;
	
	public UserListDialog()
	{
		super();
	}
	
	public UserListDialog(boolean hideTitleBar)
	{
		super();
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		defineEmptyText(R.string.empty_list_no_user);
		setEmptyViewVisible(false);
		
		setListAdapter(new MultiObjectAdapter(getListView()));
		
		getListView().setOnItemClickListener(this);
		
		setListVisible(false);
		
		if (list != null)
		{
			populate(list);
			
			setNoMoreData(true);
		}
	}
	
	public void loadList(List<Tag> list)
	{
		this.list = new ArrayList<GraphObject>();
		this.list.addAll(list);
	}
	
	public void setCustomTitle(int title)
	{
		this.customTitle = title;
	}
	
	public void setCustomTitle(String title)
	{
		this.customStringTitle = title;
	}
	
	@Override
	protected String getTitle()
	{
		if (customTitle != -1)
			return getString(customTitle);
		
		if (customStringTitle != null)
			return customStringTitle;
		
		return getResources().getString(R.string.user_like);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		GraphObject object = (GraphObject) arg0.getItemAtPosition(position);
		
		if (object instanceof Tag)
		{
			Tag tag = (Tag) object;
			startActivity(Klyph.getIntentForGraphObject(getActivity(), tag));		
		}
		else if (object instanceof Friend)
		{
			Friend friend = (Friend) object;
			startActivity(Klyph.getIntentForGraphObject(getActivity(), friend));		
		}
		
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		list = null;
	}

}
