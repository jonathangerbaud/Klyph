/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.adapter.subAdapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.holder.StreamHolder;
import com.abewy.android.apps.klyph.core.fql.Application;
import com.abewy.android.apps.klyph.core.fql.Attachment;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.fql.Link;
import com.abewy.android.apps.klyph.core.fql.Media;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.graph.GraphType;
import com.abewy.android.apps.klyph.core.util.DailymotionUtil;
import com.abewy.android.apps.klyph.core.util.FacebookUtil;
import com.abewy.android.apps.klyph.core.util.VimeoUtil;
import com.abewy.android.apps.klyph.core.util.YoutubeUtil;
import com.abewy.android.apps.klyph.text.LinkMovementMethod;
import com.abewy.android.apps.klyph.util.DateUtil;
import com.abewy.android.apps.klyph.util.KlyphUtil;
import com.abewy.android.apps.klyph.util.TextViewUtil;
import com.abewy.android.extended.widget.RatioImageView;
import com.abewy.util.PhoneUtil;

public class StreamLink extends StreamMedia
{
	private final MultiObjectAdapter	parentAdapter;
	private final int		specialLayout;

	public StreamLink(MultiObjectAdapter parentAdapter, int specialLayout)
	{
		super();
		this.parentAdapter = parentAdapter;
		this.specialLayout = specialLayout;
	}

	private MultiObjectAdapter getParentAdapter()
	{
		return parentAdapter;
	}

	public void mergeData(StreamHolder holder, Stream stream, final Link link)
	{
		if (link.isVideoLink())
		{
			manageVideoLink(holder, stream);
		}
		else
		{
			manageLink(holder, stream);
		}
	}

	public void mergeData(StreamHolder holder, Stream stream)
	{
		if (stream.getType() == 161 || (stream.getType() == 0 && stream.getLiked_pages().size() > 0))
		{
			manageLikedPage(holder, stream);
		}
		else if (stream.getEvent().getEid() != null && stream.getEvent().getEid().length() > 0)
		{
			manageEvent(holder, stream);
		}
		else if (stream.getAttachment().getMedia().size() == 1)
		{
			if (stream.getApp_id().length() > 0 && stream.getDescription_tags().size() > 1 && stream.getApplication().getApp_id().length() > 0)
			{
				String href = stream.getAttachment().getMedia().get(0).getHref();
				int index = href.indexOf("application.php?id=");

				if (index != -1 && href.substring(index + 19).equals(stream.getApp_id()))
				{
					manageApplication(holder, stream);
				}
				else
				{
					manageAttachmentLink(holder, stream);
				}
			}
			else
			{
				manageAttachmentLink(holder, stream);
			}
		}
	}

	private void manageVideoLink(StreamHolder holder, Stream stream)
	{
		final Link link = stream.getLink();

		final View view = holder.getAuthorProfileImage();

		if (!stream.getActor_id().equals(link.getOwner()))
			new StreamHeader(specialLayout).mergeData(holder, stream, link);

		new StreamButtonBar(getParentAdapter(), specialLayout).mergeData(holder, stream, link);

		String url = "";
		int width = -1;
		int height = -1;

		if (link.isYoutubeLink())
		{
			url = YoutubeUtil.getThumbUrl(link.getUrl());
			Log.d("StreamLink", "Youtube Link = " + link.getUrl() + " " + url);
			width = YoutubeUtil.THUMB_WIDTH;
			height = YoutubeUtil.THUMB_HEIGHT;
		}
		else if (link.isDailymotionLink())
		{
			url = DailymotionUtil.getThumbUrl(link.getUrl());
			width = DailymotionUtil.THUMB_WIDTH;
			height = DailymotionUtil.THUMB_HEIGHT;
		}
		else if (link.isVimeoLink())
		{
			url = VimeoUtil.getThumbUrl(link.getUrl());
			width = VimeoUtil.THUMB_WIDTH;
			height = VimeoUtil.THUMB_HEIGHT;
		}

		RatioImageView ratioImageView = (RatioImageView) holder.getPostPhoto();
		ratioImageView.setImageSize(width, height);

		loadImage(holder.getPostPhoto(), url, stream);

		holder.getPostPhoto().setVisibility(View.VISIBLE);
		holder.getPostVideoPlay().setVisibility(View.VISIBLE);
		((ViewGroup) holder.getPostPhoto().getParent()).setVisibility(View.VISIBLE);

		if (link.getTitle().length() > 0)
		{
			holder.getVideoTitle().setText(link.getTitle());
			holder.getVideoTitle().setVisibility(View.VISIBLE);
			((ViewGroup) holder.getVideoTitle().getParent()).setVisibility(View.VISIBLE);
		}

		if (link.getSummary().length() > 0)
		{
			holder.getMessage().setText(link.getSummary());
			holder.getMessage().setVisibility(View.VISIBLE);
		}

		holder.getPostPhoto().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				String url = link.getUrl();
				if (YoutubeUtil.isYoutubeLink(url) && YoutubeUtil.getVideoIdFromUrl(url).length() > 0)
				{
					Log.d("StreamLink", "isYoutubeVideo " + YoutubeUtil.getVideoIdFromUrl(url));
					try
					{
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + YoutubeUtil.getVideoIdFromUrl(url)));
						getContext(view).startActivity(intent);
					}
					catch (ActivityNotFoundException ex)
					{
						Log.d("StreamLink", "ActivityNotFoundException");
						PhoneUtil.openURL(getContext(view), url);
					}
				}
				else
				{
					Log.d("StreamLink", "not YoutubeVideo");
					PhoneUtil.openURL(getContext(view), url);
				}
			}
		});
	}

	private void manageLink(final StreamHolder holder, Stream stream)
	{
		final Link link = stream.getLink();

		if (!stream.getActor_id().equals(link.getOwner()))
			new StreamHeader(specialLayout).mergeData(holder, stream, link);

		new StreamButtonBar(getParentAdapter(), specialLayout).mergeData(holder, stream, link);

		holder.getPostLinkBackground().setVisibility(View.VISIBLE);

		if (link.getPicture().length() > 0)
		{
			loadImage(holder.getPostLinkBackground(), getLargeImageUrlForLink(link.getPicture()), R.drawable.stream_link_placeholder, stream);
		}
		else
		{
			holder.getPostLinkBackground().setImageResource(R.drawable.stream_link_placeholder);
		}

		if (link.getOwner_comment().length() > 0)
		{
			holder.getMessage().setText(link.getOwner_comment());
			holder.getMessage().setVisibility(View.VISIBLE);
		}

		if (link.getTitle().length() > 0)
		{
			holder.getPostName().setText(link.getTitle());
			holder.getPostName().setVisibility(View.VISIBLE);
		}

		if (link.getCaption().length() > 0)
		{
			holder.getPostCaption().setText(link.getCaption());
			holder.getPostCaption().setVisibility(View.VISIBLE);
		}
		else if (!link.isEventLink())
		{
			holder.getPostCaption().setText(link.getUrl());
			holder.getPostCaption().setVisibility(View.VISIBLE);
		}

		if (link.getSummary().length() > 0)
		{
			holder.getPostDescription().setText(link.getSummary());
			holder.getPostDescription().setVisibility(View.VISIBLE);
		}

		final View view = holder.getAuthorProfileImage();
		holder.getStreamLink().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (link.isEventLink())
				{
					Intent intent = Klyph.getIntentForParams(holder.getStory().getContext(), link.getEventId(), link.getTitle(), "event");
					holder.getStory().getContext().startActivity(intent);
				}
				else
				{
					PhoneUtil.openURL(getContext(view), link.getUrl());
				}
			}
		});

		holder.getStreamLink().setVisibility(View.VISIBLE);
	}

	public void manageLikedPage(final StreamHolder holder, Stream stream)
	{
		final View view = holder.getAuthorProfileImage();

		if (stream.getLiked_pages().size() > 0)
		{
			final Page page = stream.getLiked_pages().get(0);

			if (page.getPic_cover() != null && page.getPic_cover().getSource().length() > 0)
			{
				loadImage(holder.getPostLinkBackground(), page.getPic_cover().getSource());

				holder.getPostLinkBackground().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v)
					{
						Intent intent = Klyph.getIntentForGraphObject(holder.getPostLinkBackground().getContext(), page);
						getContext(view).startActivity(intent);
					}
				});
			}
			else
			{
				holder.getPostLinkBackground().setImageResource(KlyphUtil.getPlaceHolder(holder.getPostLinkBackground().getContext()));
			}

			holder.getPostLinkBackground().setVisibility(View.VISIBLE);
			((ViewGroup) holder.getPostLinkBackground().getParent()).setVisibility(View.VISIBLE);

			holder.getPostName().setText(page.getName());
			holder.getPostName().setVisibility(View.VISIBLE);

			if (page.getAbout().length() > 0)
			{
				holder.getPostCaption().setText(page.getAbout());
				holder.getPostCaption().setVisibility(View.VISIBLE);
			}

			if (page.getDescription().length() > 0)
			{
				holder.getPostDescription().setText(page.getDescription());
				holder.getPostDescription().setVisibility(View.VISIBLE);
			}

			TextViewUtil.setElementClickable(getContext(view), holder.getPostName(), page.getName(), page.getPage_id(),
					GraphType.FQL_PAGE.toString(), true);

			holder.getStreamLink().setVisibility(View.VISIBLE);
		}
	}

	private void manageEvent(StreamHolder holder, Stream stream)
	{
		Event event = stream.getEvent();

		if (event.getPic_cover() != null && event.getPic_cover().getSource().length() > 0)
		{
			loadImage(holder.getPostLinkBackground(), event.getPic_cover().getSource(), stream);
			holder.getPostLinkBackground().setVisibility(View.VISIBLE);
			holder.getPostLinkBackground().setOnClickListener(null);
			((ViewGroup) holder.getPostLinkBackground().getParent()).setVisibility(View.VISIBLE);
		}
		else
		{
			// loadImage(holder.getPostPicture(), event.getPic_big(), stream);
			// holder.getPostPicture().setVisibility(View.VISIBLE);
		}

		holder.getPostName().setText(event.getName());
		holder.getPostName().setVisibility(View.VISIBLE);

		holder.getPostCaption().setText(DateUtil.getDateTime(event.getStart_time(), true));
		holder.getPostCaption().setVisibility(View.VISIBLE);

		if (event.getDescription().length() > 0)
		{
			holder.getPostDescription().setText(event.getDescription());
			holder.getPostDescription().setVisibility(View.VISIBLE);
		}

		final View view = holder.getAuthorProfileImage();
		TextViewUtil.setElementClickable(getContext(view), holder.getPostName(), event.getName(), event.getEid(), GraphType.FQL_EVENT.toString(),
				true);

		holder.getStreamLink().setVisibility(View.VISIBLE);
	}

	private void manageAttachmentLink(StreamHolder holder, Stream stream)
	{
		final Attachment attachment = stream.getAttachment();
		final Media media = attachment.getMedia().get(0);

		if (media.getAlt().length() == 0 && media.getHref().length() == 0 && attachment.getName().length() == 0
			&& attachment.getCaption().length() == 0 && attachment.getDescription().length() == 0)
			return;

		holder.getPostLinkBackground().setVisibility(View.VISIBLE);

		loadImage(holder.getPostLinkBackground(), getLargeImageUrlForLink(media.getSrc()), R.drawable.stream_link_placeholder, stream);

		holder.getPostLinkBackground().setVisibility(View.VISIBLE);

		if (attachment.getName().length() > 0)
		{
			holder.getPostName().setText(attachment.getName());
			holder.getPostName().setVisibility(View.VISIBLE);
		}

		if (attachment.getCaption().length() > 0)
		{
			holder.getPostCaption().setText(attachment.getCaption());
			holder.getPostCaption().setVisibility(View.VISIBLE);
		}

		if (attachment.getDescription().length() > 0)
		{
			holder.getPostDescription().setText(attachment.getDescription());
			holder.getPostDescription().setVisibility(View.VISIBLE);
		}

		if (media.isVideo())
		{
			holder.getPostPicturePlay().setVisibility(View.VISIBLE);
		}

		final View view = holder.getAuthorProfileImage();
		holder.getStreamLink().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				PhoneUtil.openURL(getContext(view), attachment.getHref());
			}
		});

		holder.getStreamLink().setVisibility(View.VISIBLE);
	}

	private String getLargeImageUrlForLink(String url)
	{
		if (url.indexOf("safe_image.php") != -1)
			return Uri.decode(url.substring(url.indexOf("url=") + 4));

		return FacebookUtil.getBiggestImageURL(url);
	}

	private void manageApplication(StreamHolder holder, Stream stream)
	{
		final Application app = stream.getApplication();

		//holder.getPostLinkBackground().setColorFilter(colorFilter, PorterDuff.Mode.SRC_OVER);

		// loadImage(holder.getPostPicture(), app.getAppcenter_icon_url(), R.drawable.stream_link_placeholder, stream);
		loadImage(holder.getPostLinkBackground(), app.getAppcenter_icon_url(), R.drawable.stream_link_placeholder, stream);

		// holder.getPostPicture().setVisibility(View.VISIBLE);
		holder.getPostLinkBackground().setVisibility(View.VISIBLE);

		if (app.getDisplay_name().length() > 0)
		{
			StringBuilder anchor = new StringBuilder("<a href=\"").append(app.getLink()).append("\">").append(app.getDisplay_name()).append("</a>");
			holder.getPostName().setText(Html.fromHtml(anchor.toString()));
			holder.getPostName().setMovementMethod(LinkMovementMethod.getInstance());
			holder.getPostName().setVisibility(View.VISIBLE);
		}

		StringBuilder caption = new StringBuilder(app.getCompany_name());
		if (app.getCategory().length() > 0)
		{
			if (caption.length() > 0)
				caption.append(" - ");

			caption.append(app.getCategory());

			if (app.getSubcategory().length() > 0)
				caption.append("/").append(app.getSubcategory());
		}

		if (caption.length() > 0)
		{
			holder.getPostCaption().setText(caption);
			holder.getPostCaption().setVisibility(View.VISIBLE);
		}

		if (app.getDescription().length() > 0)
		{
			holder.getPostDescription().setText(app.getDescription());
			holder.getPostDescription().setVisibility(View.VISIBLE);
		}

		final View view = holder.getAuthorProfileImage();
		/*
		 * holder.getPostPicture().setOnClickListener(new OnClickListener() {
		 * 
		 * @Override
		 * public void onClick(View v)
		 * {
		 * PhoneUtil.openURL(getContext(view), app.getLink());
		 * }
		 * });
		 */

		holder.getStreamLink().setVisibility(View.VISIBLE);
	}
}
