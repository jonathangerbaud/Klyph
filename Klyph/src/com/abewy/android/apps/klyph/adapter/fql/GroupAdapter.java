package com.abewy.android.apps.klyph.adapter.fql;

import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.GroupHolder;
import com.abewy.android.apps.klyph.core.fql.Group;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.widget.coverImage.GroupCoverImageView;

public class GroupAdapter extends KlyphAdapter
{
	public GroupAdapter()
	{
		super();
	}

	protected int getLayout()
	{
		return R.layout.item_group;
	}

	protected void attachHolder(View view)
	{
		ImageView groupCover = (ImageView) view.findViewById(R.id.group_cover);
		TextView groupName = (TextView) view.findViewById(R.id.group_name);
		TextView groupDescription = (TextView) view.findViewById(R.id.group_description);

		setHolder(view, new GroupHolder(groupCover, groupName, groupDescription));
	}

	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);

		GroupHolder holder = (GroupHolder) getHolder(view);

		Group group = (Group) data;
		
		holder.getGroupName().setText(group.getName());
		
		if (group.getDescription().length() > 0)
		{
			holder.getGroupDescription().setText(group.getDescription());
			holder.getGroupDescription().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getGroupDescription().setVisibility(View.GONE);
		}

		int placeHolder = AttrUtil.getResourceId(getContext(holder.getGroupCover()), R.attr.squarePlaceHolderIcon);

		if (group.getPic_cover() != null && group.getPic_cover().getSource() != null && group.getPic_cover().getSource().length() > 0)
		{
			GroupCoverImageView groupCoverImageView = (GroupCoverImageView) holder.getGroupCover();
			groupCoverImageView.setOffset(group.getPic_cover().getOffset_y());
			
			loadImage(holder.getGroupCover(), group.getPic_cover().getSource(), placeHolder, true);
		}
		else
		{
			loadImage(holder.getGroupCover(), group.getPic_big(), placeHolder, true);
		}
	}
}
