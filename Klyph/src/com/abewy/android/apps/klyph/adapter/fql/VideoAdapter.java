package com.abewy.android.apps.klyph.adapter.fql;

import java.util.Collections;
import java.util.Comparator;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.VideoHolder;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.core.fql.Video.Format;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.R;

public class VideoAdapter extends KlyphAdapter
{
	private int columnWidth;
	private int placeHolder = -1;
	
	public VideoAdapter()
	{
		super();
		columnWidth = Klyph.getGridColumnWidth();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_video;
	}

	@Override
	protected void attachHolder(View view)
	{
		view.setTag(new VideoHolder((ImageView) view.findViewById(R.id.video_picture), (TextView) view
				.findViewById(R.id.video_name)));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		VideoHolder holder = (VideoHolder) view.getTag();
		
		//holder.getPhoto().setImageDrawable(null);

		Video video = (Video) data;

		Collections.sort(video.getFormat(), new Comparator<Format>() {

			@Override
			public int compare(Format lhs, Format rhs)
			{
				if (lhs.getWidth() > rhs.getWidth())
					return 1;
				else if (lhs.getWidth() < rhs.getWidth())
					return -1;

				return 0;
			}
		});

		Format selectedFormat = video.getFormat().get(0);
		for (Format format : video.getFormat())
		{
			if (format.getWidth() > columnWidth)
			{
				selectedFormat = format;
				break;
			}
		}
		
		if (placeHolder == -1)
			placeHolder = AttrUtil.getResourceId(getContext(holder.getName()), R.attr.squarePlaceHolderIcon);
		
		loadImage(holder.getPhoto(), selectedFormat.getPicture(), placeHolder, data);
		
		if (video.getTitle().length() == 0)
		{
			holder.getName().setVisibility(View.GONE);
		}
		else
		{
			holder.getName().setText(video.getTitle());
			holder.getName().setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void setLayoutParams(View view)
	{
		LayoutParams lp = view.getLayoutParams();
		lp.height = columnWidth;
		view.setLayoutParams(lp);
	}
}
