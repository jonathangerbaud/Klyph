package com.abewy.android.apps.klyph.adapter.fql;

import java.util.Collections;
import java.util.Comparator;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.PhotoListHolder;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Photo.Image;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.R;

public class PhotoListAdapter extends KlyphAdapter
{
	private int placeHolder = -1;
	
	public PhotoListAdapter()
	{
		super();
	}
	
	@Override
	protected int getLayout()
	{
		return R.layout.item_photo_list;
	}

	@Override
	protected void attachHolder(View view)
	{
		view.setTag(new PhotoListHolder((ImageView) view.findViewById(R.id.photo), (TextView) view.findViewById(R.id.photo_name)));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);
		
		PhotoListHolder holder = (PhotoListHolder) view.getTag();
		
		//holder.getPhoto().setImageDrawable(null);
		
		Photo photo = (Photo) data;

		Collections.sort(photo.getImages(), new Comparator<Image>() {

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

		int columnWidth = (int) KlyphDevice.getDeviceWidth() / view.getContext().getResources().getInteger(R.integer.klyph_grid_album_photos_num_column);
		Image selectedImage = photo.getImages().get(0);
		for (Image image : photo.getImages())
		{
			if (image.getWidth() > columnWidth)
			{
				selectedImage = image;
				break;
			}
		}
		
		if (placeHolder == -1)
			placeHolder = AttrUtil.getResourceId(getContext(holder.getName()), R.attr.squarePlaceHolderIcon);

		loadImage(holder.getPhoto(), selectedImage.getSource(), placeHolder, data);
		
		if (photo.getCaption().length() == 0)
		{
			holder.getName().setVisibility(View.GONE);
		}
		else
		{
			holder.getName().setText(photo.getCaption());
			holder.getName().setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void setLayoutParams(View view)
	{
		int width = (int) KlyphDevice.getDeviceWidth() / view.getContext().getResources().getInteger(R.integer.klyph_grid_album_photos_num_column);
		LayoutParams lp = view.getLayoutParams();
		lp.height = lp.width = width;
		view.setLayoutParams(lp);
	}
}
