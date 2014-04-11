package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.SearchView;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.fql.Group;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;

public class Groups extends KlyphFragment2 implements SearchView.OnQueryTextListener
{
	private List<GraphObject> groups;
	
	public Groups()
	{
		setRequestType(Query.GROUPS);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		
		MultiObjectAdapter moa = new MultiObjectAdapter(getListView());
		moa.setFilter(new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results)
			{
				setFilteredResults((List<GraphObject>) results.values);
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{
				FilterResults results = new FilterResults();
				List<Group> filteredGroups = new ArrayList<Group>();

				// perform your search here using the searchConstraint String.

				constraint = constraint.toString().toLowerCase();
				for (int i = 0; i < groups.size(); i++)
				{
					Group group = (Group) groups.get(i);
					if (group.getName().toLowerCase().startsWith(constraint.toString()))
					{
						filteredGroups.add(group);
					}
				}

				results.count = filteredGroups.size();
				results.values = filteredGroups;

				return results;
			}
		});
		
		setListAdapter(moa);
		
		defineEmptyText(R.string.empty_list_no_group);
		
		setRequestType(Query.GROUPS);
		setListVisible(false);

		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onGridItemClick(KlyphGridView l, View v, int position, long id)
	{
		Group group = (Group) l.getItemAtPosition(position);

		startActivity(Klyph.getIntentForGraphObject(getActivity(), group));
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		if (getAdapter() != null && getAdapter().getCount() > 0)
		{
			// Create the search view
			SearchView searchView = new SearchView(((FragmentActivity) getActivity()).getActionBar()
					.getThemedContext());
			searchView.setQueryHint("Search for groups");
			searchView.setOnQueryTextListener(this);

			menu.add("Search").setIcon(AttrUtil.getResourceId(getActivity(), R.attr.searchIcon))
					.setActionView(searchView)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}
	
	private void setFilteredResults(List<GraphObject> data)
	{
		getAdapter().clear();
		super.populate(data);
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		super.populate(data);
		groups = data;
		((FragmentActivity) getActivity()).invalidateOptionsMenu();
	}

	@Override
	public boolean  onQueryTextSubmit(String query)
	{
		return false;
	}

	@Override
	public boolean  onQueryTextChange(String newText)
	{
		if (newText.length() > 0)
		{
			getAdapter().getFilter().filter(newText);
		}
		else
		{
			setFilteredResults(groups);
		}
		
		return true;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		groups = null;
	}
}
