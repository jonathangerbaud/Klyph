package com.abewy.android.apps.klyph.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.GridView;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.view.ListEmptyView;

public class KlyphGridFragment extends KlyphFragment implements OnScrollListener
{
	private GridView	gridView;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		gridView = (GridView) view.findViewById(R.id.grid);

		//gridView.setNumColumns(Ckoobafe.getNumGridColumn());

		gridView.setColumnWidth(220);
		gridView.setVisibility(View.GONE);
		
		gridView.setOnScrollListener(this);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				onGridItemClick(gridView, v, position, id);
			}
		});
		
		defineEmptyView();
	}
	
	protected void defineEmptyView()
	{
		if(gridView != null)
		{
			ListEmptyView lev = new ListEmptyView(getActivity());
			((ViewGroup) gridView.getParent()).addView(lev);
			
			Log.d("KlyphGridFragment", "define empty view " + lev);
			gridView.setEmptyView(lev);
		}
	}
	
	@Override
	protected void setEmptyText(int resId)
	{
		if (gridView.getEmptyView() != null)
		{
			((ListEmptyView) gridView.getEmptyView()).setText(resId);
		}
	}
	
	public GridView getGridView()
	{
		return gridView;
	}

	/**
	 * Override this method to define this activity's layout
	 * 
	 * @return the activity's layout. Example : <code>R.layout.main</code>
	 */
	protected int getCustomLayout()
	{
		return R.layout.layout_grid;
	}

	@Override
	protected MultiObjectAdapter getAdapter()
	{
		return (MultiObjectAdapter) gridView.getAdapter();
	}
	
	@Override
	protected void ensureList()
	{
		// getListView().getEmptyView().setVisibility(View.GONE);
		//getGridView().setEmptyView(getView().findViewById(android.R.id.empty));
	}
	
	public void onGridItemClick(GridView gridView, View v, int position, long id)
	{
		
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		//if (getAdapter() != null)
			//getAdapter().setData(new ArrayList<GraphObject>());
		
		gridView.setAdapter(null);
		gridView = null;
	}
}
