package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.Album;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.fragment.NewAlbumDialog.NewAlbumDialogListener;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class AlbumSpinner extends Fragment implements NewAlbumDialogListener, OnItemSelectedListener
{
	public static interface AlbumSpinnerListener
	{
		public void onAlbumSpinnerSelectionChange();
	}
	
	private static final int	REQUEST_CODE	= 935;

	private Spinner				spinner;
	private ProgressBar			progress;
	private ImageButton			addButton;

	private String				defaultId;
	private AlbumSpinnerListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_album_spinner, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		progress = (ProgressBar) view.findViewById(android.R.id.progress);
		spinner = (Spinner) view.findViewById(R.id.spinner);
		addButton = (ImageButton) view.findViewById(R.id.add_album_button);

		SpinnerAdapter adapter = new SpinnerAdapter(getActivity());
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				NewAlbumDialog dialog = new NewAlbumDialog();
				dialog.setTargetFragment(AlbumSpinner.this, REQUEST_CODE);
				dialog.show(getActivity().getFragmentManager(), "newAlbum");
			}
		});

		loadAlbums();

		super.onViewCreated(view, savedInstanceState);
	}

	/**
	 * 
	 * @return the selected album id
	 */
	public String getSelectedAlbum()
	{
		Album album = (Album) spinner.getSelectedItem();
		return album.getObject_id();
	}

	public void setDefaultAlbumId(String id)
	{
		this.defaultId = id;
		setDefaultId();
	}

	private void setDefaultId()
	{
		if (spinner != null && defaultId != null)
		{
			SpinnerAdapter adapter = (SpinnerAdapter) spinner.getAdapter();

			int n = adapter.getCount();
			for (int i = 0; i < n; i++)
			{
				Album album = adapter.getItem(i);
				
				if (defaultId.equals(album.getObject_id()))
				{
					spinner.setSelection(i);
					break;
				}
			}
		}
	}

	private void loadAlbums()
	{
		AsyncRequest request = new AsyncRequest(Query.UPLOADABLE_ALBUM, KlyphSession.getSessionUserId(), "",
				new AsyncRequest.Callback() {

					public void onComplete(Response response)
					{
						onRequestComplete(response);
					}
				});

		request.execute();
	}
	
	private void onRequestComplete(final Response response)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
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
		}
	}

	private void onRequestSuccess(List<GraphObject> result)
	{
		if (progress != null)
		{
			progress.setVisibility(View.GONE);
		}

		if (spinner != null && getActivity() != null) // Check if view is created
		{
			SpinnerAdapter adapter = (SpinnerAdapter) spinner.getAdapter();

			Album album = new Album();
			album.setObject_id("me");
			album.setName(getString(R.string.upload_photo_default_album_name));
			adapter.add(album);

			for (GraphObject graphObject : result)
			{
				adapter.add((Album) graphObject);
			}

			adapter.notifyDataSetChanged();
			
			setDefaultId();

			spinner.setVisibility(View.VISIBLE);
		}
	}

	private void onRequestError(RequestError error)
	{
		if (progress != null)
		{
			progress.setVisibility(View.GONE);
		}

		if (spinner != null && getView() != null) // Check if view is created
		{
			SpinnerAdapter adapter = (SpinnerAdapter) spinner.getAdapter();

			Album album = new Album();
			album.setObject_id("me");
			album.setName(getString(R.string.upload_photo_default_album_name));
			adapter.add(album);

			adapter.notifyDataSetChanged();
			
			setDefaultId();

			spinner.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		if (spinner != null)
		{
			spinner.setOnItemSelectedListener(null);
			spinner = null;
		}
		
		progress = null;
		addButton = null;
		listener = null;
	}

	@Override
	public void createAlbum(final Album album, String privacy)
	{
		Bundle bundle = new Bundle();
		bundle.putString("name", album.getName());
		bundle.putString("location", album.getLocation());
		bundle.putString("message", album.getDescription());
		bundle.putString("privacy", privacy);

		final AlertDialog dialog = AlertUtil.showAlert(getActivity(), R.string.new_album, R.string.creating_album);

		new AsyncRequest(Query.NEW_ALBUM, "", bundle, new AsyncRequest.Callback() {

			@Override
			public void onComplete(Response response)
			{
				dialog.dismiss();
				
				if (response.getError() == null)
				{
					Log.d("AlbumSpinner", "createAlbum id = " + response.getReturnedId());
					album.setObject_id(response.getReturnedId());
					((SpinnerAdapter) spinner.getAdapter()).add(album);
					((SpinnerAdapter) spinner.getAdapter()).notifyDataSetChanged();
					spinner.setSelection(spinner.getCount() - 1);
				}
				else
				{
					Toast.makeText(getActivity(), R.string.request_error, Toast.LENGTH_SHORT).show();
				}
			}
		}).execute();
	}

	private static class SpinnerAdapter extends BaseAdapter
	{
		private LayoutInflater	inflater;
		private List<Album>		albums;

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
				convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
			}

			TextView ct = (TextView) convertView.findViewById(android.R.id.text1);
			ct.setText(getItem(position).getName());

			return convertView;
		}
	}
	
	public void setOnSelectionChangeListener(AlbumSpinnerListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		if (listener != null)
		{
			listener.onAlbumSpinnerSelectionChange();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		
	}
}
