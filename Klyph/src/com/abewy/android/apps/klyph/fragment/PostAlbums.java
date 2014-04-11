package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.Album;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class PostAlbums extends Fragment implements OnItemSelectedListener
{
	private Spinner spinner;
	private ProgressBar progress;
	
	public PostAlbums()
	{
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_post_albums, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		progress = (ProgressBar) view.findViewById(android.R.id.progress);
		spinner = (Spinner) view.findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(this);
		
		String userId = getActivity().getIntent().getStringExtra(KlyphBundleExtras.USER_ID); 
		if (userId == null || userId.equals(KlyphSession.getSessionUserId()))
		{
			Album album = new Album();
			album.setObject_id("me");
			album.setName(getString(R.string.upload_photo_default_album_name));
			
			SpinnerAdapter adapter = new SpinnerAdapter(getActivity());
			adapter.add(album);
			spinner.setAdapter(adapter);

			loadAlbums();
		}
		
		super.onViewCreated(view, savedInstanceState);
	}
	
	/**
	 * 
	 * @return the selected album id
	 */
	public String getSelectedAlbum()
	{
		if (spinner.getAdapter() != null)
		{
			Album album = (Album) spinner.getSelectedItem();
			return album.getObject_id(); 
		}
		
		return null;
	}
	
	private void loadAlbums()
	{
		AsyncRequest request = new AsyncRequest(Query.UPLOADABLE_ALBUM, KlyphSession.getSessionUserId(), "", new AsyncRequest.Callback() {

			public void onComplete(Response response)
			{
				if (response.getError() == null)
				{
					onRequestSuccess(response.getGraphObjectList());
				}
				else
				{
					onRequestError(response.getError());
				}
			}
		});

		request.execute();
	}
	
	private void onRequestSuccess(List<GraphObject> result)
	{
		if (getActivity() != null)
		{
			if (progress != null)
			{
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run()
					{
						progress.setVisibility(View.GONE);
					}
				});
			}
			
			if (spinner != null) // Check if view is created
			{
				SpinnerAdapter adapter = (SpinnerAdapter) spinner.getAdapter();
				for (GraphObject graphObject : result)
				{
					adapter.add((Album) graphObject);
				}
			}
		}
	}
	
	private void onRequestError(RequestError error)
	{
		Log.d("PostAlbums", "error " + error.toString());
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		spinner = null;
		progress = null;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		
	}
	
	private static class SpinnerAdapter extends BaseAdapter
	{
		private LayoutInflater inflater;
		private List<Album> albums;
		
		public SpinnerAdapter(Context context)
		{
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			albums = new ArrayList<Album>();
		}
		
		public void add(Album album)
		{
			albums.add(album);
		}
		
		@Override
		public int getCount()
		{
			return albums.size();
		}

		@Override
		public Album getItem(int index)
		{
			return albums.get(index);
		}

		@Override
		public long getItemId(int index)
		{
			return index;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
			}
			
			CheckedTextView ct = (CheckedTextView) convertView.findViewById(android.R.id.text1);
			ct.setText(getItem(position).getName());

			return convertView;
		}
	}
}
