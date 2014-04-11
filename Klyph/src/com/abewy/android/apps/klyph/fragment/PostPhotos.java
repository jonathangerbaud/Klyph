package com.abewy.android.apps.klyph.fragment;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.abewy.android.adapter.TypeAdapter;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.BaseAdapterSelector;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.CheckableGalleryLayout;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.util.Android;
	
public class PostPhotos extends KlyphFragment2
{
	private PostPhotoListener	listener;

	public interface PostPhotoListener
	{
		public void onPostPhotosItemClick();

		public void onPostPhotosDeleteClick();
	}

	public PostPhotos()
	{
		setRequestType(Query.GROUP_PHOTOS);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		view.setBackgroundResource(AttrUtil.getResourceId(getActivity(), R.attr.actionBarStackedBackground));

		setListVisible(false);

		ImageButton deleteButton = (ImageButton) view.findViewById(R.id.delete_button);
		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (listener != null)
					listener.onPostPhotosDeleteClick();
			}
		});

		super.onViewCreated(view, savedInstanceState);

		getGridView().setHorizontalSpacing(0);

		defineEmptyText(R.string.empty_list_no_photo);
	}
	
	@Override
	protected int getNumColumn()
	{
		return 6;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		// Do not display refresh menu icon
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (activity instanceof PostPhotoListener)
		{
			listener = (PostPhotoListener) activity;
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		listener = null;
	}

	public void setImages(List<String> uris)
	{
		MultiObjectAdapter adapter = new MultiObjectAdapter(getListView())
		{

			@Override
			protected TypeAdapter<GraphObject> getAdapter(GraphObject object, int layoutType)
			{
				TypeAdapter<GraphObject> adapter = BaseAdapterSelector.getAdapter(object, layoutType);
				
				if (adapter != null)
					return adapter;
				
				if (object instanceof Picture)
					return new GalleryAdapter();
				
				return null;
			}
		};

		for (String uri : uris)
		{
			adapter.add(new Picture(uri));
			
		}

		getGridView().setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		setListVisible(true);
	}

	public void updateLayout()
	{
		if (getGridView() != null && getGridView().getAdapter() != null)
		{
			LayoutParams params = getGridView().getLayoutParams();

			if (KlyphDevice.isLandscapeMode() || getGridView().getAdapter().getCount() <= getNumColumn())
			{
				params.height = (int) ((KlyphDevice.getDeviceWidth() - (48 + 8 + 2) * KlyphDevice.getDeviceDensity()) / getNumColumn());
			}
			else
			{
				params.height = (int) ((KlyphDevice.getDeviceWidth() - (48 + 8 + 2) * KlyphDevice.getDeviceDensity()) / (getNumColumn()/2));
			}
			
			getGridView().setLayoutParams(params);
		}
	}

	@Override
	public void onGridItemClick(KlyphGridView gridView, View view, int position, long id)
	{
		if (listener != null)
		{
			listener.onPostPhotosItemClick();
		}
	}

	@Override
	protected int getCustomLayout()
	{
		return R.layout.fragment_post_photos;
	}

	private static class Picture extends GraphObject
	{
		private static final int VIEW_TYPE = 65874126;
		private String					uri;

		public Picture(String uri)
		{
			this.uri = uri;
		}

		public String getUri()
		{
			return uri;
		}
		
		@Override
		public int getItemViewType()
		{
			return VIEW_TYPE;
		}	
	}

	private static class GalleryAdapter extends KlyphAdapter
	{
		private int	placeHolder	= -1;
		
		public GalleryAdapter()
		{
			super();
		}

		@Override
		protected int getLayout()
		{
			return R.layout.item_gallery;
		}

		@Override
		protected void attachHolder(View view)
		{
			ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
			ImageView checkView = (ImageView) view.findViewById(R.id.checkmark);
			checkView.setVisibility(View.GONE);

			setHolder(view, new GalleryHolder(imageView));
		}

		@Override
		protected void mergeViewWithData(View view, GraphObject data)
		{
			if (!Android.isMinAPI(11))
			{
				((CheckableGalleryLayout) view).setChecked(data.isSelected());
			}

			GalleryHolder holder = (GalleryHolder) getHolder(view);

			Picture mi = (Picture) data;

			holder.getImageView().setImageDrawable(null);
			
			if (placeHolder == -1)
				placeHolder = AttrUtil.getResourceId(holder.getImageView().getContext(), R.attr.squarePlaceHolderIcon);

			String uri = "file://" + mi.getUri();

			ImageLoader.display(holder.getImageView(), uri, placeHolder);
		}

		private class GalleryHolder
		{
			private ImageView	imageView;

			public GalleryHolder(ImageView imageView)
			{
				this.imageView = imageView;
			}

			public ImageView getImageView()
			{
				return imageView;
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
