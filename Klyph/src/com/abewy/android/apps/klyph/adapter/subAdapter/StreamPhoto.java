/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.adapter.subAdapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.holder.StreamHolder;
import com.abewy.android.apps.klyph.app.ImageActivity;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.fql.Attachment;
import com.abewy.android.apps.klyph.core.fql.Media;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.core.fql.Video.Format;
import com.abewy.android.apps.klyph.core.util.YoutubeUtil;
import com.abewy.android.extended.widget.RatioImageView;
import com.abewy.util.PhoneUtil;

public class StreamPhoto extends StreamMedia
{
	private final MultiObjectAdapter	parentAdapter;
	private final int	specialLayout;

	public StreamPhoto(MultiObjectAdapter parentAdapter, int specialLayout)
	{
		super();
		this.parentAdapter = parentAdapter;
		this.specialLayout = specialLayout;
	}

	private MultiObjectAdapter getParentAdapter()
	{
		return parentAdapter;
	}

	public void mergeData(StreamHolder holder, final Stream stream, final Photo photo)
	{
		if (!stream.getActor_id().equals(photo.getOwner()))
		{
			new StreamHeader(specialLayout).mergeData(holder, stream, photo);
		}

		new StreamButtonBar(getParentAdapter(), specialLayout).mergeData(holder, stream, photo);

		holder.getPostPhoto().setScaleType(ScaleType.FIT_XY);
		
		RatioImageView i = (RatioImageView) holder.getPostPhoto();
		i.setImageSize(photo.getSrc_big_width(), photo.getSrc_big_height());
		loadImage(holder.getPostPhoto(), photo.getSrc_big());

		holder.getPostPhoto().setVisibility(View.VISIBLE);
		((ViewGroup) holder.getPostPhoto().getParent()).setVisibility(View.VISIBLE);

		if (photo.getCaption().length() > 0)
		{
			holder.getMessage().setText(photo.getCaption());
			holder.getMessage().setVisibility(View.VISIBLE);
		}

		final View view = holder.getAuthorProfileImage();
		holder.getPostPhoto().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getContext(view), ImageActivity.class);
				intent.putExtra(KlyphBundleExtras.PHOTO_ID, photo.getObject_id());
				intent.putExtra(KlyphBundleExtras.URL_IF_NOT_FOUND, getSource(photo.getSrc_big()));
				getContext(view).startActivity(intent);
			}
		});
	}

	public void mergeData(StreamHolder holder, final Stream stream, final Video video)
	{
		if (!stream.getActor_id().equals(video.getOwner()))
		{
			new StreamHeader(specialLayout).mergeData(holder, stream, video);
		}

		new StreamButtonBar(getParentAdapter(), specialLayout).mergeData(holder, stream, video);

		Format selectedFormat = null;

		for (Format format : video.getFormat())
		{
			if (format.getWidth() > KlyphDevice.getDeviceWidth())
			{
				selectedFormat = format;
				break;
			}
		}

		if (selectedFormat == null)
		{
			selectedFormat = video.getFormat().get(video.getFormat().size() - 1);
		}

		holder.getPostPhoto().setScaleType(ScaleType.FIT_XY);
		
		RatioImageView ratioImageView = (RatioImageView) holder.getPostPhoto();
		ratioImageView.setImageSize(selectedFormat.getWidth(), selectedFormat.getHeight());

		loadImage(holder.getPostPhoto(), selectedFormat.getPicture());

		holder.getPostPhoto().setVisibility(View.VISIBLE);
		holder.getPostVideoPlay().setVisibility(View.VISIBLE);
		((ViewGroup) holder.getPostPhoto().getParent()).setVisibility(View.VISIBLE);

		if (video.getTitle().length() > 0)
		{
			holder.getVideoTitle().setText(video.getTitle());
			holder.getVideoTitle().setVisibility(View.VISIBLE);
			((ViewGroup) holder.getVideoTitle().getParent()).setVisibility(View.VISIBLE);
		}

		if (video.getDescription().length() > 0)
		{
			holder.getMessage().setText(video.getDescription());
			holder.getMessage().setVisibility(View.VISIBLE);
		}

		final View view = holder.getAuthorProfileImage();
		holder.getPostPhoto().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d("StreamPhoto", "onClickVideo " + video.getSrc_hq());
				if (video.getSrc_hq().contains(".mp4") == true)
				{
					
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getSrc_hq()));
					intent.setDataAndType(Uri.parse(video.getSrc_hq()), "video/mp4");
					try
					{
					getContext(view).startActivity(intent);
					}
					catch(ActivityNotFoundException e)
					{
						PhoneUtil.openURL(getContext(view), video.getSrc_hq());
					}
				}
				else
				{
					PhoneUtil.openURL(getContext(view), video.getSrc_hq());
				}
			}
		});
	}

	public void mergeData(StreamHolder holder, final Stream stream)
	{
		/*
		 * holder.getPostPicture().setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * PhoneUtil.openURL(getContext(),
		 * stream.getAttachment().getMedia().get(0).getHref()); } });
		 */

		final Attachment attachment = stream.getAttachment();

		if (attachment.getMedia().size() > 0)
		{
			final Media media = attachment.getMedia().get(0);

			holder.getPostPhoto().setScaleType(ScaleType.FIT_XY);

			loadMedia(holder.getPostPhoto(), media, stream, true);

			holder.getPostPhoto().setVisibility(View.VISIBLE);
			((ViewGroup) holder.getPostPhoto().getParent()).setVisibility(View.VISIBLE);

			final View view = holder.getAuthorProfileImage();

			if (attachment.isPhoto())
			{
				holder.getPostPhoto().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent(getContext(view), ImageActivity.class);

						String id = media.getPhoto().getFbid();
						if (id.length() == 0)
							id = media.getPhoto().getPid();

						intent.putExtra(KlyphBundleExtras.PHOTO_ID, id);
						intent.putExtra(KlyphBundleExtras.URL_IF_NOT_FOUND, getSource(media.getSrc()));
						getContext(view).startActivity(intent);
					}
				});
			}
			else
			{
				holder.getPostPhoto().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v)
					{
						String href = media.isSwf() == false ? media.getHref() : attachment.getHref();
						Log.d("StreamPhoto", "isYoutubeVideo " + media.getHref());
						Log.d("StreamPhoto", "isYoutubeVideo " + attachment.getHref());
						Log.d("StreamPhoto", "isYoutubeVideo " + YoutubeUtil.getVideoIdFromUrl(href));
						
						String url = media.isSwf() ? media.getSwf().getSource_url() : media.getVideo().getSource_url();
						Log.d("StreamPhoto", "isYoutubeVideo " + url);
						if (media.isYoutubeVideo() && YoutubeUtil.getVideoIdFromUrl(url).length() > 0)
						{
							Log.d("StreamPhoto", "isYoutubeVideo");
							try
							{
								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + YoutubeUtil.getVideoIdFromUrl(url)));
								getContext(view).startActivity(intent);
							}
							catch (ActivityNotFoundException ex)
							{
								Log.d("StreamPhoto", "ActivityNotFoundException");
								PhoneUtil.openURL(getContext(view), href);
							}
						}
						else
						{
							Log.d("StreamPhoto", "not YoutubeVideo");
							PhoneUtil.openURL(getContext(view), href);
						}
					}
				});

				if (attachment.getName().length() > 0)
				{
					holder.getVideoTitle().setText(attachment.getName());
					holder.getVideoTitle().setVisibility(View.VISIBLE);
					((ViewGroup) holder.getVideoTitle().getParent()).setVisibility(View.VISIBLE);
				}

				if (attachment.getDescription().length() > 0)
				{
					holder.getVideoUrl().setText(attachment.getDescription());
					holder.getVideoUrl().setVisibility(View.VISIBLE);
					((ViewGroup) holder.getVideoTitle().getParent()).setVisibility(View.VISIBLE);
				}

				holder.getPostVideoPlay().setVisibility(View.VISIBLE);
			}

			if (stream.getMessage().length() == 0 && media.getAlt().length() > 0)
			{
				holder.getMessage().setText(media.getAlt());
				holder.getMessage().setVisibility(View.VISIBLE);
			}
			else if (stream.getMessage().length() == 0 && stream.getAttachment().getCaption().length() > 0)
			{
				holder.getMessage().setText(stream.getAttachment().getCaption());
				holder.getMessage().setVisibility(View.VISIBLE);
			}
		}

	}
}
