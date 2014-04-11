package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.app.AlbumActivity;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.android.apps.klyph.R;

public class GroupPhotos extends KlyphFakeHeaderGridFragment
{
	private ArrayList<Photo>	data;
	
	public GroupPhotos()
	{
		setRequestType(Query.GROUP_PHOTOS);
		data = new ArrayList<Photo>();
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		setListVisible(false);
		
		setRequestType(Query.GROUP_PHOTOS);
		
		super.onViewCreated(view, savedInstanceState);
		
		defineEmptyText(R.string.empty_list_no_photo);
		
		getGridView().setAdapter(new MultiObjectAdapter(getListView(), SpecialLayout.GRID));
		
	}
	
	@Override
	protected int getNumColumn()
	{
		return getResources().getInteger(R.integer.klyph_grid_album_photos_num_column);
	}

	/*@Override
	protected int getCustomLayout()
	{
		return R.layout.grid_simple;
	}*/

	@Override
	protected void populate(List<GraphObject> data)
	{
		super.populate(data);
		
		for (GraphObject graphObject : data)
		{
			this.data.add((Photo) graphObject);
		}
	}

	@Override
	public void onGridItemClick(KlyphGridView gridView, View view, int position, long id)
	{
		Intent intent = new Intent(getActivity(), AlbumActivity.class);
		intent.putParcelableArrayListExtra(KlyphBundleExtras.ALBUM_PHOTOS, data);
		intent.putExtra(KlyphBundleExtras.START_POSITION, position);

		startActivity(intent);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		data = null;
	}
}
