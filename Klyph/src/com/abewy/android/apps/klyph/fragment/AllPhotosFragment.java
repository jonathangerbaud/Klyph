package com.abewy.android.apps.klyph.fragment;

import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphData;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.android.apps.klyph.R;

public class AllPhotosFragment extends KlyphFragment2
{
	public AllPhotosFragment()
	{
		setRequestType(Query.ALL_PHOTOS);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		setListVisible(false);

		super.onViewCreated(view, savedInstanceState);
		
		getGridView().setAdapter(new MultiObjectAdapter(getListView(), SpecialLayout.GRID));

		defineEmptyText(R.string.empty_list_no_photo);
		
		setRequestType(Query.ALL_PHOTOS);
		
		if (KlyphData.getAllPhotos() == null)
		{
			load();
		}
		else
		{
			populate(KlyphData.getAllPhotos());
		}
	}
	
	@Override
	protected int getNumColumn()
	{
		return getResources().getInteger(R.integer.klyph_grid_album_photos_num_column);
	}

	@Override
	protected int getCustomLayout()
	{
		return R.layout.grid_simple;
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		super.populate(data);
		
		KlyphData.setAllPhotos(data);
	}
	
	@Override
	public void onGridItemClick(KlyphGridView l, View v, int position, long id)
	{
		Photo photo = (Photo) getAdapter().getItem(position);
		
		Intent intent = new Intent();
		intent.putExtra(KlyphBundleExtras.URL, photo.getLargestImageURL());
		intent.putExtra(KlyphBundleExtras.THUMB_URL, photo.getImages().get(0).getSource());
		getActivity().setResult(Activity.RESULT_OK, intent);
		getActivity().finish();
	}
}
