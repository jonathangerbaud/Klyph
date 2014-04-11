package com.abewy.android.apps.klyph.adapter.fql;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.PicturePrimarySecondaryTextHolder;
import com.abewy.android.apps.klyph.core.fql.Notification;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.util.DateUtil;
import com.abewy.android.apps.klyph.util.KlyphUtil;
import com.abewy.android.apps.klyph.widget.ProfileImageView;
import com.abewy.android.apps.klyph.R;

public class NotificationAdapter extends KlyphAdapter
{
	public NotificationAdapter()
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
		View divider = (View) view.findViewById(R.id.divider);

		setHolder(view, new PicturePrimarySecondaryTextHolder(userPicture, notificationTitle, notificationTime, divider));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);
		
		PicturePrimarySecondaryTextHolder holder = (PicturePrimarySecondaryTextHolder) getHolder(view);
		
		//holder.getPicture().setImageDrawable(null);

		Notification notification = (Notification) data;

		holder.getPrimaryText().setText(Html.fromHtml(getFormattedHtmlTitle(notification.getTitle_html())));
		holder.getSecondaryText().setText(DateUtil.timeAgoInWords(getContext(view), notification.getUpdated_time()));

		((ProfileImageView) holder.getPicture()).disableBorder();
		loadImage(holder.getPicture(), notification.getSender_pic(), KlyphUtil.getProfilePlaceHolder(view.getContext()), data);
		
		holder.getDivider().setVisibility(notification.mustShowDivider() ? View.VISIBLE : View.GONE);
	}
	
	private String getFormattedHtmlTitle(String htmlTitle)
	{
		htmlTitle = htmlTitle.replaceAll("</a>", "</b>");
		int pos = 0;
		/*while ((pos = htmlTitle.indexOf("</", pos)) != -1)
		{
			int endPos = htmlTitle.indexOf(">", pos);
			htmlTitle = htmlTitle.substring(0, pos) + "</b>" + htmlTitle.substring(endPos + 1);
		}
		
		pos = 0;*/
		while ((pos = htmlTitle.indexOf("<a", pos)) != -1)
		{
			int endPos = htmlTitle.indexOf(">", pos);
			htmlTitle = htmlTitle.substring(0, pos) + "<b>" + htmlTitle.substring(endPos + 1);
		}
		
		return htmlTitle;
	}
}
