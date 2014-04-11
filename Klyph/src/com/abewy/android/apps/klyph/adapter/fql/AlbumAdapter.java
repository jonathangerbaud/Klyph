package com.abewy.android.apps.klyph.adapter.fql;

import java.util.Collections;
import java.util.Comparator;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.AlbumHolder;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.fql.Album;
import com.abewy.android.apps.klyph.core.fql.Photo.Image;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.R;

public class AlbumAdapter extends KlyphAdapter
{
	public AlbumAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_album;
	}

	@Override
	protected void attachHolder(View view)
	{
		ImageView albumCover = (ImageView) view.findViewById(R.id.album_cover);
		TextView albumName = (TextView) view.findViewById(R.id.album_name);
		TextView numPhoto = (TextView) view.findViewById(R.id.album_num_photo);
		TextView numVideo = (TextView) view.findViewById(R.id.album_num_video);
		ImageView photoIcon = (ImageView) view.findViewById(R.id.album_photo_icon);
		ImageView videoIcon = (ImageView) view.findViewById(R.id.album_video_icon);
		view.setTag(new AlbumHolder(albumCover, albumName, numPhoto, numVideo, photoIcon, videoIcon));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);

		AlbumHolder holder = (AlbumHolder) view.getTag();

		Album album = (Album) data;

		if (album.getIs_video_album() == true)
			album.setName(getContext(holder.getAlbumName()).getString(R.string.video_album_name));

		holder.getAlbumName().setText(album.getName());

		holder.getNumPhoto().setVisibility(album.getPhoto_count() > 0 ? View.VISIBLE : View.GONE);
		holder.getPhotoIcon().setVisibility(album.getPhoto_count() > 0 ? View.VISIBLE : View.GONE);
		holder.getNumVideo().setVisibility(album.getVideo_count() > 0 ? View.VISIBLE : View.GONE);
		holder.getVideoIcon().setVisibility(album.getVideo_count() > 0 ? View.VISIBLE : View.GONE);

		holder.getNumPhoto().setText(album.getPhoto_count() + "");
		holder.getNumVideo().setText(album.getVideo_count() + "");

		/*
		 * String url = "https://graph.facebook.com/" +
		 * album.getCover_object_id() + "/picture?type=normal&access_token=" +
		 * Session.getActiveSession().getAccessToken();
		 * loadImage(holder.getAlbumCover(), url);
		 */
		Collections.sort(album.getCover_images(), new Comparator<Image>() {

			@Override
			public int compare(Image lhs, Image rhs)
			{
				if (lhs.getWidth() > rhs.getWidth())
					return 1;
				else if (lhs.getWidth() < rhs.getWidth())
					return -1;

				return 0;
			}
		});

		int cellWidth = getCellWidth(view);
		if (album.getCover_images().size() > 0)
		{
			Image selectedImage = null;
			for (Image image : album.getCover_images())
			{
				selectedImage = image;

				if (image.getWidth() > cellWidth)
				{
					break;
				}
			}
			
			int placeHolder = AttrUtil.getResourceId(holder.getAlbumCover().getContext(), R.attr.squarePlaceHolderIcon);

			loadImage(holder.getAlbumCover(), selectedImage.getSource(), placeHolder, data);
		}
	}

	@Override
	public void setLayoutParams(View view)
	{
		int cellWidth = getCellWidth(view);

		LayoutParams lp = view.getLayoutParams();
		lp.height = (int) cellWidth / 2;
		view.setLayoutParams(lp);

		// int padding = (int) getContext(view).getResources().getDimension(R.dimen.ckoobafe_list_padding) / 2;
		// view.setPadding(view.getPaddingLeft(), padding, view.getPaddingRight(), padding);
	}

	private int getCellWidth(View view)
	{
		int w = (int) (KlyphDevice.getDeviceWidth() - view.getContext().getResources().getDimension(R.dimen.klyph_grid_padding_left_right)
														* view.getContext().getResources().getInteger(R.integer.klyph_grid_album_num_column));
		w /= view.getContext().getResources().getInteger(R.integer.klyph_grid_album_num_column);

		return w;
	}
}