package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.abewy.android.apps.klyph.KlyphApplication;
import com.abewy.android.apps.klyph.KlyphDownloadManager;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.util.Android;

public class AlbumVideos extends KlyphFragment2
{
	public interface ElementVideosListener
	{
		public void onVideoSelected(Fragment fragment, Video video);
	}
	
	private ElementVideosListener listener;
	
	public AlbumVideos()
	{
		setRequestType(Query.ALBUM_VIDEOS);
	}
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		setListVisible(false);

		setRequestType(Query.ALBUM_VIDEOS);
		
		super.onViewCreated(view, savedInstanceState);
		
		getGridView().setAdapter(new MultiObjectAdapter(getListView(), SpecialLayout.GRID));
		
		defineEmptyText(R.string.empty_list_no_video);
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
	public void onGridItemClick(KlyphGridView gridView, View v, int position, long id)
	{
		Video video = (Video) getAdapter().getItem(position);
		listener.onVideoSelected(AlbumVideos.this, video);
	}
	
	@Override
	protected void populate(List<GraphObject> data)
	{
		super.populate(data);
		
		if (data.size() > 0 && requestHasMoreData())
		{
			setNoMoreData(false);
			Video lastPhoto = (Video) data.get(data.size() - 1);
			setOffset(lastPhoto.getCreated_time());
		}
		else
		{
			setNoMoreData(true);
		}
		
		Log.d("AlbumVideos", "empty view " + getGridView().getEmptyView());
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		try
		{
			listener = (ElementVideosListener) activity;
		}
		catch (ClassCastException e)
		{
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString() + " must implement ElementVideosListener");
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
	}
	
	@Override
	public boolean  onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_download)
		{
			downloadAlbum();
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void downloadAlbum()
	{
		if (hasNoMoreData() == false)
		{
			Toast.makeText(getActivity(), R.string.fetch_videos_from_album, Toast.LENGTH_SHORT).show();
			
			new AsyncRequest(Query.ALBUM_VIDEOS_ALL, getElementId(), "", new AsyncRequest.Callback() {
				
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
			List<GraphObject> videos = new ArrayList<GraphObject>();
			for (GraphObject graphObject : getAdapter().getItems())
			{
				videos.add(graphObject);
			}
			
			downloadAlbum(videos);
		}
	}
	
	private void downloadAlbum(List<GraphObject> videos)
	{
		int n = videos.size();
		for (int i = 0; i < n; i++)
		{
			Video video = (Video) videos.get(i);
			
			String url = video.getSrc_hq();
			
			if (url != null)
			{
				boolean notifOnComplete = i == n - 1;
				KlyphDownloadManager.downloadPhoto(KlyphApplication.getInstance(), url, video.getVid(), video.getDescription(), true, notifOnComplete);
			}
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		listener = null;
	}
}
