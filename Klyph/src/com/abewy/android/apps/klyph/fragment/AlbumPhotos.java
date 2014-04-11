package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.abewy.android.apps.klyph.KlyphApplication;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphDownloadManager;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.app.AlbumActivity;
import com.abewy.android.apps.klyph.app.PostActivity;
import com.abewy.android.apps.klyph.app.PostPhotosActivity;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.util.Android;

public class AlbumPhotos extends KlyphFragment2
{
	private ArrayList<Photo>	data;
	private boolean  tagged = false;
	
	public AlbumPhotos()
	{
		setRequestType(Query.ALBUM_PHOTOS);
		data = new ArrayList<Photo>();
	}
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		setListVisible(false);
		
		setRequestType(tagged ? Query.ALBUM_TAGGED_PHOTOS : Query.ALBUM_PHOTOS);
		
		getGridView().setAdapter(new MultiObjectAdapter(getListView(), SpecialLayout.GRID));
		
		defineEmptyText(R.string.empty_list_no_photo);
		
		//getGridView().setStretchMode(GridView.NO_STRETCH);
		
		super.onViewCreated(view, savedInstanceState);
	}
	
	public void setTaggedAlbum(boolean  tagged)
	{
		this.tagged = tagged;
		setRequestType(tagged ? Query.ALBUM_TAGGED_PHOTOS : Query.ALBUM_PHOTOS);
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
		
		for (GraphObject graphObject : data)
		{
			this.data.add((Photo) graphObject);
		}
		
		if (data.size() > 0 && requestHasMoreData())
		{
			setNoMoreData(false);
			Photo lastPhoto = (Photo) data.get(data.size() - 1);
			setOffset(tagged ? String.valueOf(getAdapter().getCount()) : lastPhoto.getAlbum_object_id_cursor());
		}
		else
		{
			setNoMoreData(true);
		}
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		if (Android.isMinAPI(9) && menu.findItem(R.id.menu_download) == null)
		{
			menu.add(Menu.NONE, R.id.menu_download, 2, R.string.menu_download)
					.setIcon(AttrUtil.getResourceId(getActivity(), R.attr.downloadIcon))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
		if (menu.findItem(R.id.menu_add) == null)
		{
			menu.add(Menu.NONE, R.id.menu_add, 3, R.string.menu_add_photos)
					.setIcon(AttrUtil.getResourceId(getActivity(), R.attr.newIcon))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

		if (menu.findItem(R.id.menu_share) == null)
		{
			menu.add(Menu.NONE, R.id.menu_share, 4, getString(R.string.share)).setIcon(AttrUtil.getResourceId(getActivity(), R.attr.shareIcon))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
	}
	
	@Override
	public boolean  onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_download)
		{
			downloadAlbum();
		}
		else if (item.getItemId() == R.id.menu_add)
		{
			Intent intent = new Intent(getActivity(), PostPhotosActivity.class);
			intent.putExtra(KlyphBundleExtras.ALBUM_ID, getElementId());
			startActivity(intent);
		}
		else if (item.getItemId() == R.id.menu_share)
		{
			Intent intent = new Intent(getActivity(), PostActivity.class);
			intent.putExtra(KlyphBundleExtras.SHARE, true);
			intent.putExtra(KlyphBundleExtras.SHARE_ALBUM_ID, getElementId());

			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void downloadAlbum()
	{
		if (hasNoMoreData() == false)
		{
			Toast.makeText(getActivity(), R.string.fetch_photos_from_album, Toast.LENGTH_SHORT).show();
			
			new AsyncRequest(Query.ALBUM_PHOTOS_ALL, getElementId(), "", new AsyncRequest.Callback() {
				
				@Override
				public void onComplete(Response response)
				{
					if (response.getError() == null)
					{
						downloadAlbum(response.getGraphObjectList());
					}
				}
			}).execute();
		}
		else
		{
			List<GraphObject> photos = new ArrayList<GraphObject>();
			for (GraphObject graphObject : getAdapter().getItems())
			{
				photos.add(graphObject);
			}
			
			downloadAlbum(photos);
		}
	}
	
	private void downloadAlbum(List<GraphObject> photos)
	{
		int n = photos.size();
		for (int i = 0; i < n; i++)
		{
			Photo photo = (Photo) photos.get(i);
			
			String url = photo.getLargestImageURL();
			
			if (url != null)
			{
				boolean notifOnComplete = i == n - 1;
				KlyphDownloadManager.downloadPhoto(KlyphApplication.getInstance(), url, photo.getObject_id(), photo.getCaption(), true, notifOnComplete);
			}
		}
	}

	@Override
	public void onGridItemClick(KlyphGridView gridView, View v, int position, long id)
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
