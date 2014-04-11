package com.abewy.android.apps.klyph.adapter.fql;

import java.util.Collections;
import java.util.Comparator;
import android.view.View;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.core.fql.Media.Image;
import com.abewy.android.apps.klyph.core.fql.Media.Photo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.extended.widget.RatioImageView;

public class PhotoAlbumAdapter extends KlyphAdapter
{
	private int placeHolder = -1;
	
	public PhotoAlbumAdapter()
	{
		super();
	}
	
	@Override
	protected int getLayout()
	{
		return R.layout.item_photo_album;
	}

	@Override
	protected void attachHolder(View view)
	{
		//view.setTag(new PhotoListHolder((ImageView) view.findViewById(R.id.photo), (TextView) view.findViewById(R.id.photo_name)));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);
		
		final RatioImageView rImageView = (RatioImageView) view;
		
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

		Image selectedImage = photo.getImages().get(0);
		rImageView.setImageSize(selectedImage.getWidth(), selectedImage.getHeight());
				
		if (placeHolder == -1)
			placeHolder = AttrUtil.getResourceId(getContext(view), R.attr.squarePlaceHolderIcon);

		loadImage(rImageView, selectedImage.getSrc(), placeHolder, data);
	}
}