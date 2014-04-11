package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.ElementTimelineHolder;
import com.abewy.android.apps.klyph.core.fql.Group;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.widget.coverImage.GroupCoverImageView;

public class GroupTimelineAdapter extends KlyphAdapter
{
	private int placeHolder = -1;
	
	public GroupTimelineAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_group_timeline;
	}
	
	@Override
	public boolean  isEnabled(GraphObject object)
	{
		return false;
	}

	@Override
	protected void attachHolder(View view)
	{
		ImageView elementCoverImage = (ImageView) view.findViewById(R.id.element_cover_image);
		ImageView elementProfileImage = (ImageView) view.findViewById(R.id.element_profile_image);
		TextView elementDetail1 = (TextView) view.findViewById(R.id.element_detail_1);
		TextView elementDetail2 = (TextView) view.findViewById(R.id.element_detail_2);
		TextView elementDetail3 = (TextView) view.findViewById(R.id.element_detail_3);
		TextView elementDetail4 = (TextView) view.findViewById(R.id.element_detail_4);
		TextView likes = (TextView) view.findViewById(R.id.likes);
		TextView talkAbout = (TextView) view.findViewById(R.id.talk_about);

		view.setTag(new ElementTimelineHolder(elementCoverImage, elementProfileImage, elementDetail1, elementDetail2,
				elementDetail3, elementDetail4, likes, talkAbout));
	}

	@Override
	protected void mergeViewWithData(View view, final GraphObject data)
	{
		super.mergeViewWithData(view, data);

		ElementTimelineHolder holder = (ElementTimelineHolder) view.getTag();
		Group group = (Group) data;
		
		if (placeHolder == -1)
			placeHolder = AttrUtil.getResourceId(getContext(holder.getElementCoverImage()), R.attr.squarePlaceHolderIcon);

		holder.getElementProfileImage().setVisibility(View.GONE);

		// group can have no cover image
		String source = group.getPic_cover().getSource();
		if (source != null && source.length() > 0)
		{
			GroupCoverImageView groupCoverImageView = (GroupCoverImageView) holder.getElementCoverImage();
			groupCoverImageView.setOffset(group.getPic_cover().getOffset_y());
			
			loadImage(holder.getElementCoverImage(), source, placeHolder, true);
		}
		else
		{
			holder.getElementCoverImage().setImageResource(placeHolder);
		}
		

		holder.getElementDetail1().setText(group.getDescription());
		holder.getElementDetail1().setVisibility(View.VISIBLE);
		holder.getElementDetail2().setVisibility(View.GONE);
		holder.getElementDetail3().setVisibility(View.GONE);
		holder.getElementDetail4().setVisibility(View.GONE);

		if (group.getEmail().length() > 0)
		{
			holder.getLikes().setText(group.getEmail());
			holder.getLikes().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getLikes().setVisibility(View.GONE);
		}

		if (group.getWebsite().length() > 0)
		{
			holder.getTalkAbout().setText(group.getWebsite());
			holder.getTalkAbout().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getTalkAbout().setVisibility(View.GONE);
		}
	}
}