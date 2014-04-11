package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.PicturePrimarySecondaryTextHolder;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.fql.User.Education;
import com.abewy.android.apps.klyph.core.fql.User.Work;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.R;

public class MemberAdapter extends KlyphAdapter
{
	public MemberAdapter()
	{
		super();
	}
	
	@Override
	protected int getLayout()
	{
		return R.layout.item_grid_picture_primary_secondary_text;
	}
	
	@Override
	protected void attachHolder(View view)
	{
		ImageView friendPicture = (ImageView) view.findViewById(R.id.picture);
		TextView name = (TextView) view.findViewById(R.id.primary_text);
		TextView description = (TextView) view.findViewById(R.id.secondary_text);

		setHolder(view, new PicturePrimarySecondaryTextHolder(friendPicture, name, description));
	}
	
	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);
		
		PicturePrimarySecondaryTextHolder holder = (PicturePrimarySecondaryTextHolder) getHolder(view);
		
		//holder.getPicture().setImageDrawable(null);
		
		Friend friend = (Friend) data;

		holder.getPrimaryText().setText(friend.getName());
		
		if (friend.getWork().size() > 0)
		{
			Work work = friend.getWork().get(0);
			
			StringBuilder str = new StringBuilder(work.getEmployer().getName());
			
			if (work.getPosition().getName().length() > 0)
				str.append(", ").append(work.getPosition().getName());
			
			holder.getSecondaryText().setText(str);
			holder.getSecondaryText().setVisibility(View.VISIBLE);
		}
		else if (friend.getEducation().size() > 0)
		{
			Education education = friend.getEducation().get(0);
			
			StringBuilder str = new StringBuilder(education.getSchool().getName());
			
			if (education.getYear().getName().length() > 0)
				str.append(", ").append(education.getYear().getName());
			
			if (education.getConcentration().getName().length() > 0)
				str.append(", ").append(education.getConcentration().getName());
			
			holder.getSecondaryText().setText(str);
			holder.getSecondaryText().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getSecondaryText().setVisibility(View.GONE);
		}

		String url = friend.getPic();//FacebookUtil.getProfilePictureURLForId(friend.getUid());
		loadImage(holder.getPicture(), url, AttrUtil.getResourceId(getContext(view), R.attr.squarePlaceHolderIcon), data);
	}
}
