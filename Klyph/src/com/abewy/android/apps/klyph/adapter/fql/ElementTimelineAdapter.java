package com.abewy.android.apps.klyph.adapter.fql;

import java.util.ArrayList;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.ElementTimelineHolder;
import com.abewy.android.apps.klyph.app.ImageActivity;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.fql.User;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.util.KlyphUtil;
import com.abewy.android.apps.klyph.widget.coverImage.UserCoverImageView;

public class ElementTimelineAdapter extends KlyphAdapter
{
	private int placeHolder = -1;

	public ElementTimelineAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_element_timeline;
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
	protected void mergeViewWithData(final View view, final GraphObject data)
	{
		super.mergeViewWithData(view, data);
		Log.d("ElementTimeline", "mergeData");
		ElementTimelineHolder holder = (ElementTimelineHolder) view.getTag();
		
		holder.getElementCoverImage().setOnClickListener(null);
		holder.getElementProfileImage().setOnClickListener(null);
		
		if (placeHolder == -1)
			placeHolder = AttrUtil.getResourceId(getContext(holder.getElementCoverImage()), R.attr.squarePlaceHolderIcon);

		if (data instanceof User)
		{
			manageUser(view, holder, (User) data);

			holder.getElementProfileImage().setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(getContext(view), ImageActivity.class);
					intent.putExtra(KlyphBundleExtras.USER_ID, ((User) data).getUid());
					getContext(view).startActivity(intent);
				}
			});
		}
		else if (data instanceof Page)
		{
			managePage(view, holder, (Page) data);

			holder.getElementProfileImage().setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(getContext(view), ImageActivity.class);
					intent.putExtra(KlyphBundleExtras.USER_ID, ((Page) data).getPage_id());
					getContext(view).startActivity(intent);
				}
			});
		}
	}

	private void manageUser(View view, ElementTimelineHolder holder, User user)
	{
		// User can have no cover image
		String source = user.getPic_cover().getSource();
		if (source != null && source.length() > 0)
		{
			UserCoverImageView userCoverImageView = (UserCoverImageView) holder.getElementCoverImage();
			userCoverImageView.setOffset(user.getPic_cover().getOffset_y());
			loadImage(holder.getElementCoverImage(), source, true);
		}
		else
			holder.getElementCoverImage().setImageResource(placeHolder);
		
		loadImage(holder.getElementProfileImage(), user.getPic(), KlyphUtil.getProfilePlaceHolder(view.getContext()), true);
		
		ArrayList<String> data = new ArrayList<String>();
		Resources res = getContext(view).getResources();
		if (user.getSex().length() > 0)
			data.add(res.getString(R.string.user_about_sex, user.getSex()));

		if (user.getBirthday().length() > 0)
			data.add(res.getString(R.string.user_about_birthday, user.getBirthday()));

		if (user.getRelationship_status().length() > 0)
			data.add(res.getString(R.string.user_about_ralationship_status, user.getRelationship_status()));

		/*
		 * if (user.getLocation() != null &&
		 * user.getLocation().getName().length() > 0) data.add("Habite à : " +
		 * user.getLocation().getName()); if (user.getHometown() != null &&
		 * user.getHometown().getName().length() > 0) data.add("Originaire de : "
		 * + user.getHometown().getName());
		 */

		// work
		// study
		ArrayList<TextView> elementDetails = new ArrayList<TextView>();
		elementDetails.add(holder.getElementDetail1());
		elementDetails.add(holder.getElementDetail2());
		elementDetails.add(holder.getElementDetail3());
		elementDetails.add(holder.getElementDetail4());
		elementDetails.add(holder.getLikes());
		elementDetails.add(holder.getTalkAbout());

		for (TextView tv : elementDetails)
		{
			tv.setVisibility(View.GONE);
		}

		for (int i = 0; i < data.size(); i++)
		{
			if (i > elementDetails.size() - 3)
				break;

			TextView tv = elementDetails.get(i);
			tv.setText(data.get(i));
			tv.setVisibility(View.VISIBLE);
		}
	}

	private void managePage(View view, ElementTimelineHolder holder, Page page)
	{
		String source = page.getPic_cover().getSource();
		
		if (source != null && source.length() > 0)
		{
			UserCoverImageView userCoverImageView = (UserCoverImageView) holder.getElementCoverImage();
			userCoverImageView.setOffset(page.getPic_cover().getOffset_y());
			loadImage(holder.getElementCoverImage(), source, 0, false);
			
		}
		else
		{
			holder.getElementCoverImage().setImageResource(placeHolder);
		}
		
		loadImage(holder.getElementProfileImage(), page.getPic(), KlyphUtil.getProfilePlaceHolder(view.getContext()), true);
		
		ArrayList<TextView> elementDetails = new ArrayList<TextView>();
		elementDetails.add(holder.getElementDetail1());
		elementDetails.add(holder.getElementDetail2());
		elementDetails.add(holder.getElementDetail3());
		elementDetails.add(holder.getElementDetail4());
		elementDetails.add(holder.getLikes());
		elementDetails.add(holder.getTalkAbout());

		for (TextView tv : elementDetails)
		{
			tv.setVisibility(View.VISIBLE);
		}

		holder.getElementDetail1().setText(page.getType().toUpperCase());
		holder.getElementDetail2().setText(page.getAbout());

		int n = page.getFan_count();

		if (n == 0)
		{
			holder.getLikes().setText(R.string.no_like);
		}
		else if (n == 1)
		{
			holder.getLikes().setText(R.string.one_like);
		}
		else
		{
			holder.getLikes().setText(getContext(view).getResources().getString(R.string.several_likes, n));
		}

		n = page.getTalking_about_count();

		if (n == 0)
		{
			holder.getTalkAbout().setText(R.string.noone_talk_about_it);
		}
		else if (n == 1)
		{
			holder.getTalkAbout().setText(R.string.one_talk_about_it);
		}
		else
		{
			holder.getTalkAbout().setText(getContext(view).getResources().getString(R.string.several_talk_about_it, n));
		}

		holder.getElementDetail3().setVisibility(View.GONE);
		holder.getElementDetail4().setVisibility(View.GONE);

		// if (page.isTvShow())
	}
}