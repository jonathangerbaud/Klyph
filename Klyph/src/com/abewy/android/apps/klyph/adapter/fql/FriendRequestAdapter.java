package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.PicturePrimarySecondaryTextHolder;
import com.abewy.android.apps.klyph.core.fql.FriendRequest;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.util.DateUtil;
import com.abewy.android.apps.klyph.R;

public class FriendRequestAdapter extends KlyphAdapter
{
	public FriendRequestAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_notification;
	}

	@Override
	protected void attachHolder(View view)
	{
		ImageView userPicture = (ImageView) view.findViewById(R.id.picture);
		TextView notificationTitle = (TextView) view.findViewById(R.id.primary_text);
		TextView notificationTime = (TextView) view.findViewById(R.id.secondary_text);

		setHolder(view, new PicturePrimarySecondaryTextHolder(userPicture, notificationTitle, notificationTime));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);
		
		PicturePrimarySecondaryTextHolder holder = (PicturePrimarySecondaryTextHolder) getHolder(view);
		
		//holder.getPicture().setImageDrawable(null);

		FriendRequest friendRequest = (FriendRequest) data;
		
		holder.getPrimaryText().setText(friendRequest.getUid_from_name());
		
		if (friendRequest.getMessage().length() > 0)
		{
			holder.getSecondaryText().setText(DateUtil.timeAgoInWords(getContext(view), friendRequest.getMessage()));
			holder.getSecondaryText().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getSecondaryText().setVisibility(View.GONE);
		}

		loadImage(holder.getPicture(), friendRequest.getUid_from_pic(), data);
	}
}
