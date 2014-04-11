package com.abewy.android.apps.klyph.adapter.subAdapter;

import it.sephiroth.android.library.widget.AdapterView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.holder.StreamHolder;
import com.abewy.android.apps.klyph.app.AlbumActivity;
import com.abewy.android.apps.klyph.core.BaseApplication;
import com.abewy.android.apps.klyph.core.fql.Attachment;
import com.abewy.android.apps.klyph.core.fql.Media;
import com.abewy.android.apps.klyph.core.fql.Media.Image;
import com.abewy.android.apps.klyph.core.fql.Media.Photo;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.core.util.FacebookUtil;
import com.abewy.android.extended.widget.RatioImageView;

public class StreamAlbum extends StreamMedia
{
	private final int maxHeight;
	
	public StreamAlbum()
	{
		super();
		maxHeight = BaseApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.klyph_stream_album_height);
	}

	public void mergeData(StreamHolder holder, Stream stream)
	{
		Attachment attachment = stream.getAttachment();
		
		final List<Media> medias = attachment.getMedia();
		int n = medias.size();
		
		final ArrayList<String> imageIds = new ArrayList<String>();
		
		for (int i = 0; i < n; i++)
		{
			String pid = medias.get(i).getPhoto().getPid();
			
			if (pid != null && pid.length() > 0)
			{
				imageIds.add(pid);
			}
		}
				
		PhotoAdapter adapter = new PhotoAdapter(medias, maxHeight);
		holder.getStreamAlbum().setAdapter(adapter);
		
		holder.getStreamAlbum().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> list, View view, int position, long arg3)
			{
				Intent intent = new Intent(view.getContext(), AlbumActivity.class);
				intent.putExtra(KlyphBundleExtras.PHOTO_ID, medias.get(position).getPhoto().getPid());
				intent.putStringArrayListExtra(KlyphBundleExtras.PHOTO_LIST_ID, imageIds);
				view.getContext().startActivity(intent);
			}});

		holder.getStreamAlbum().setVisibility(View.VISIBLE);
	}
	
	public static class PhotoAdapter extends BaseAdapter
	{
		private List<Media> media;
		private final int maxHeight;
		private int placeHolder = -1;
		
		public PhotoAdapter(List<Media> media, int maxHeight)
		{
			this.media = media;
			this.maxHeight = maxHeight;
		}
		@Override
		public int getCount()
		{
			return media.size();
		}

		@Override
		public Object getItem(int position)
		{
			return media.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}
		

		@Override
		public int getViewTypeCount()
		{
			return 1;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_photo_album, parent, false);
			}
			
			final RatioImageView rImageView = (RatioImageView) convertView;
			rImageView.setPrimarySide(RatioImageView.HEIGHT);
			
			Media media = (Media) getItem(position);
			Photo photo = media.getPhoto();			

			if (placeHolder == -1)
				placeHolder = AttrUtil.getResourceId(convertView.getContext(), R.attr.squarePlaceHolderIcon);
			
			if (photo.getImages().size() > 1)
			{
				Collections.sort(photo.getImages(), new Comparator<Image>() {

					@Override
					public int compare(Image lhs, Image rhs)
					{
						if (lhs.getWidth() > rhs.getWidth())
							return -1;
						else if (lhs.getWidth() < rhs.getWidth())
							return 1;

						return 0;
					}
				});
				
				Image selectedImage = photo.getImages().get(0);
				
				int n = photo.getImages().size();
				for (int i = 1; i < n; i++)
				{
					Image image = photo.getImages().get(i);
					
					if (image.getHeight() < maxHeight)
					{
						break;
					}
					
					selectedImage = image; 
				}
				
				
				rImageView.setImageSize(selectedImage.getWidth(), selectedImage.getHeight());
				ImageLoader.display(rImageView, selectedImage.getSrc(), placeHolder);
			}
			else if (photo.getImages().size() == 1)
			{
				Image image = photo.getImages().get(0);
				
				if (image.getHeight() > photo.getHeight())
				{
					if (photo.getHeight() > maxHeight)
					{
						rImageView.setImageSize(photo.getWidth(), photo.getHeight());
						ImageLoader.display(rImageView, FacebookUtil.getBiggestImageURL(media.getSrc()), placeHolder);
					}
					
					rImageView.setImageSize(image.getWidth(), image.getHeight());
					ImageLoader.display(rImageView, image.getSrc(), placeHolder);
				}
				else
				{
					if (image.getHeight() > maxHeight)
					{
						rImageView.setImageSize(image.getWidth(), image.getHeight());
						ImageLoader.display(rImageView, image.getSrc(), placeHolder);
					}
					
					rImageView.setImageSize(photo.getWidth(), photo.getHeight());
					ImageLoader.display(rImageView, FacebookUtil.getBiggestImageURL(media.getSrc()), placeHolder);
				}
			}
			else
			{
				rImageView.setImageSize(photo.getWidth(), photo.getHeight());
				ImageLoader.display(rImageView, FacebookUtil.getBiggestImageURL(media.getSrc()), placeHolder);
			}			
			
			return convertView;
		}
	}
}
