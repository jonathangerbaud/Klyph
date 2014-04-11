package com.abewy.android.apps.klyph.adapter.subAdapter;

import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.holder.StreamHolder;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.fql.Media;
import com.abewy.android.apps.klyph.core.fql.Media.Image;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Video.Format;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.Post;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.core.util.FacebookUtil;
import com.abewy.android.apps.klyph.util.KlyphUtil;
import com.abewy.android.extended.widget.RatioImageView;

public abstract class StreamMedia
{
	private int			placeHolder	= -1;

	public StreamMedia()
	{
		
	}
	
	protected int getMaxWidth()
	{
		return KlyphDevice.getDeviceWidth();
	}
	
	protected Context getContext(View view)
	{
		return view.getContext();
	}

	protected void setSharedAuthor(StreamHolder holder, String authorId, String authorName, String authorType)
	{
		setSharedAuthor(holder, authorId, authorName, authorType, null, null, null);
	}

	protected void setSharedAuthor(StreamHolder holder, String authorId, String authorName, String authorType,
			String targetId, String targetName, String targetType)
	{

	}

	protected void loadMedia(ImageView imageView, Media media, Stream stream, boolean  setDimensions)
	{
		int width = 0;
		int height = 0;
		String source = "";

		if (media.isVideo())
		{
			if (media.isFacebookVideo())
			{
				Format selectedFormat = null;

				for (Format format : media.getVideo().getFormat())
				{
					if (format.getWidth() > getMaxWidth())
					{
						selectedFormat = format;
						break;
					}
				}

				if (selectedFormat == null)
				{
					selectedFormat = media.getVideo().getFormat().get(media.getVideo().getFormat().size() - 1);
				}

				width = selectedFormat.getWidth();
				height = selectedFormat.getHeight();
				source = selectedFormat.getPicture();
			}
			// Youtube video
			else if (media.isYoutubeVideo())
			{
				width = 480;
				height = 360;
				source = media.getYoutubeThumbnail();
			}
			// Dailymotion video
			else if (media.isDailymotionVideo())
			{
				width = 427;
				height = 240;
				source = media.getDailymotionThumbnail();
			}
			else if (media.isVimeoVideo())
			{
				width = 640;
				height = 476;
				source = media.getVimeoThumbnail();
			}
			else
			{
				width = -1;
				height = -1;
				source = media.getHref();
				Log.d("StreamMedia", "unknownType " + media.getHref());
			}
		}
		else
		{
			if (media.getPhoto().getImages().size() > 0)
			{
				/*List<Image> images = media.getPhoto().getImages();
				Image selectedImage = images.get(0);

				for (int i = 1, n = images.size(); i < n; i++)
				{
					Image image = images.get(i);

					if (image.getWidth() < getMaxWidth())
					{
						break;
					}
					
					selectedImage = image;
				}*/
				Image selectedImage = null;

				for (Image image : media.getPhoto().getImages())
				{
					if (image.getWidth() > getMaxWidth())
					{
						selectedImage = image;
						break;
					}
				}

				if (selectedImage == null)
				{
					selectedImage = media.getPhoto().getImages().get(media.getPhoto().getImages().size() - 1);

					if (media.getPhoto().getWidth() > getMaxWidth())
					{
						/*
						 * int imageDifference = maxWidth -
						 * selectedImage.getWidth();
						 * int mediaDifference = media.getPhoto().getWidth() -
						 * maxWidth;
						 * 
						 * if (imageDifference < mediaDifference)
						 * {
						 * width = selectedImage.getWidth();
						 * height = selectedImage.getHeight();
						 * source = selectedImage.getSrc();
						 * }
						 * else
						 * {
						 */
						width = media.getPhoto().getWidth();
						height = media.getPhoto().getHeight();
						source = getSource(media.getSrc());
						// }
					}
					else
					{
						if (media.getPhoto().getWidth() > selectedImage.getWidth())
						{
							width = media.getPhoto().getWidth();
							height = media.getPhoto().getHeight();
							source = getSource(media.getSrc());
						}
						else
						{
							width = selectedImage.getWidth();
							height = selectedImage.getHeight();
							source = selectedImage.getSrc();
						}
					}
				}
				else
				{
					width = selectedImage.getWidth();
					height = selectedImage.getHeight();
					source = selectedImage.getSrc();
				}
			}
			else
			{
				width = media.getPhoto().getWidth();
				height = media.getPhoto().getHeight();
				source = getSource(media.getSrc());
			}
		}
		
		if (setDimensions == true)
		{
			RatioImageView ratioImageView = (RatioImageView) imageView;
			ratioImageView.setImageSize(width, height);
		}

		loadImage(imageView, source, stream);
	}

	protected void loadImage(ImageView imageView, String url)
	{
		if (placeHolder == -1)
			placeHolder = KlyphUtil.getPlaceHolder(imageView.getContext());

		ImageLoader.display(imageView, url, placeHolder);
	}

	protected void loadImage(ImageView imageView, String url, int placeHolder)
	{
		ImageLoader.display(imageView, url, placeHolder);
	}

	protected void loadImage(ImageView imageView, String url, GraphObject graphObject)
	{
		if (placeHolder == -1)
			placeHolder = AttrUtil.getResourceId(getContext(imageView), R.attr.squarePlaceHolderIcon);

		boolean fadeIn = false;

		if (graphObject != null)
			fadeIn = !graphObject.getDisplayedOnce();

		ImageLoader.display(imageView, url, fadeIn, placeHolder);
	}

	protected void loadImage(ImageView imageView, String url, int placeHolder, GraphObject graphObject)
	{
		boolean fadeIn = false;

		if (graphObject != null)
			fadeIn = !graphObject.getDisplayedOnce();

		ImageLoader.display(imageView, url, fadeIn, placeHolder);
	}

	protected String getSource(String src)
	{
		return FacebookUtil.getBiggestImageURL(src);
	}

	protected String getPicture(Post post)
	{
		return post.getPicture();
	}
}
