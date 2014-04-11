package com.abewy.android.apps.klyph.core.fql;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.DailymotionUtil;
import com.abewy.android.apps.klyph.core.util.VimeoUtil;
import com.abewy.android.apps.klyph.core.util.YoutubeUtil;

public class Media extends GraphObject implements Parcelable
{
	private String	type;	// photo, video, link
	private String	src;
	private String	href;
	private String	alt;
	private Video	video;
	private Photo	photo;
	private Swf		swf;

	public Media()
	{

	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getSrc()
	{
		return src;
	}

	public void setSrc(String src)
	{
		this.src = src;
	}

	public Video getVideo()
	{
		return video;
	}

	public void setVideo(Video video)
	{
		this.video = video;
	}

	public Photo getPhoto()
	{
		return photo;
	}

	public void setPhoto(Photo photo)
	{
		this.photo = photo;
	}

	public Swf getSwf()
	{
		return swf;
	}

	public void setSwf(Swf swf)
	{
		this.swf = swf;
	}

	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public String getAlt()
	{
		return alt;
	}

	public void setAlt(String alt)
	{
		this.alt = alt;
	}

	// ___ Public Services _____________________________________________________

	public boolean isVideo()
	{
		return type.equals(AttachmentType.VIDEO) || isSwf();
	}

	public boolean isSwf()
	{
		return type.equals(AttachmentType.SWF);
	}

	public boolean isLink()
	{
		return type.equals(AttachmentType.LINK);
	}

	/**
	 * Return true if is a facebook, youtube, dailymotion or vimeo video
	 */
	public boolean isFydv()
	{
		return isVideo() && (isFacebookVideo() || isYoutubeVideo() || isDailymotionVideo() || isVimeoVideo());
	}

	public boolean isFacebookVideo()
	{
		return isVideo() && getVideo().getFormat().size() > 0;
	}

	public boolean isYoutubeVideo()
	{
		String url = isSwf() ? getSwf().getSource_url() : getVideo().getSource_url();
		Log.d("Media", "isYoutubeVideo " + url);
		return YoutubeUtil.isYoutubeLink(url);
	}

	/**
	 * Return the youtube video thumbnail URL. Use this method only if
	 * <code>isYoutubeVideo()</code> return true
	 */
	public String getYoutubeThumbnail()
	{
		String url = isSwf() ? getSwf().getSource_url() : getVideo().getSource_url();
		return YoutubeUtil.getThumbUrl(url);
	}

	public boolean isDailymotionVideo()
	{
		String url = isSwf() ? getSwf().getSource_url() : getVideo().getSource_url();
		return DailymotionUtil.isDailymotionLink(url);
	}

	/**
	 * Return the dailymotion video thumbnail URL. Use this method only if
	 * <code>isDailymotionVideo()</code> return true
	 */
	public String getDailymotionThumbnail()
	{
		String url = isSwf() ? getSwf().getSource_url() : getVideo().getSource_url();

		return DailymotionUtil.getThumbUrl(url);
	}

	public boolean isVimeoVideo()
	{
		String url = isSwf() ? getSwf().getSource_url() : getVideo().getSource_url();
		return VimeoUtil.isVimeoLink(url);
	}

	/**
	 * Return the vimeo video thumbnail URL. Use this method only if
	 * <code>isVimeoVideo()</code> return true
	 */
	public String getVimeoThumbnail()
	{
		String url = isSwf() ? getSwf().getSource_url() : getVideo().getSource_url();

		return VimeoUtil.getThumbUrl(url);
	}

	public static class Photo extends GraphObject implements Parcelable
	{
		private int			index;
		private int			width;
		private int			height;
		private List<Image>	images;
		private String		aid;
		private String		pid;
		private String		fbid;
		private String		owner;

		public Photo()
		{

		}

		@Override
		public int getItemViewType()
		{
			return GraphObject.MEDIA_PHOTO;
		}

		public int getIndex()
		{
			return index;
		}

		public void setIndex(int index)
		{
			this.index = index;
		}

		public int getWidth()
		{
			return width;
		}

		public void setWidth(int width)
		{
			this.width = width;
		}

		public int getHeight()
		{
			return height;
		}

		public void setHeight(int height)
		{
			this.height = height;
		}

		public List<Image> getImages()
		{
			return images;
		}

		public void setImages(List<Image> images)
		{
			this.images = images;
		}

		public String getAid()
		{
			return aid;
		}

		public void setAid(String aid)
		{
			this.aid = aid;
		}

		public String getPid()
		{
			return pid;
		}

		public void setPid(String pid)
		{
			this.pid = pid;
		}

		public String getFbid()
		{
			return fbid;
		}

		public void setFbid(String fbid)
		{
			this.fbid = fbid;
		}

		public String getOwner()
		{
			return owner;
		}

		public void setOwner(String owner)
		{
			this.owner = owner;
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeString(aid);
			dest.writeString(fbid);
			dest.writeInt(height);
			dest.writeTypedList(images);
			dest.writeInt(index);
			dest.writeString(owner);
			dest.writeString(pid);
			dest.writeInt(width);
		}

		public static final Parcelable.Creator<Photo>	CREATOR	= new Parcelable.Creator<Photo>() {
																	public Photo createFromParcel(Parcel in)
																	{
																		return new Photo(in);
																	}

																	public Photo[] newArray(int size)
																	{
																		return new Photo[size];
																	}
																};

		private Photo(Parcel in)
		{
			aid = in.readString();
			fbid = in.readString();
			height = in.readInt();
			images = new ArrayList<Media.Image>();
			in.readTypedList(images, Image.CREATOR);
			index = in.readInt();
			owner = in.readString();
			pid = in.readString();
			width = in.readInt();
		}
	}

	public static class Image extends GraphObject implements Parcelable
	{
		private String	src;
		private int		width;
		private int		height;

		public Image()
		{

		}

		public String getSrc()
		{
			return src;
		}

		public void setSrc(String src)
		{
			this.src = src;
		}

		public int getWidth()
		{
			return width;
		}

		public void setWidth(int width)
		{
			this.width = width;
		}

		public int getHeight()
		{
			return height;
		}

		public void setHeight(int height)
		{
			this.height = height;
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeInt(height);
			dest.writeString(src);
			dest.writeInt(width);
		}

		public static final Parcelable.Creator<Image>	CREATOR	= new Parcelable.Creator<Image>() {
																	public Image createFromParcel(Parcel in)
																	{
																		return new Image(in);
																	}

																	public Image[] newArray(int size)
																	{
																		return new Image[size];
																	}
																};

		private Image(Parcel in)
		{
			height = in.readInt();
			src = in.readString();
			width = in.readInt();
		}
	}

	public static class Swf extends GraphObject implements Parcelable
	{
		private String	source_url;
		private String	preview_img;
		private int		width;
		private int		height;
		private int		expanded_width;
		private int		expanded_height;

		public Swf()
		{

		}

		public String getSource_url()
		{
			return source_url;
		}

		public void setSource_url(String source_url)
		{
			this.source_url = source_url;
		}

		public int getWidth()
		{
			return width;
		}

		public void setWidth(int width)
		{
			this.width = width;
		}

		public int getHeight()
		{
			return height;
		}

		public void setHeight(int height)
		{
			this.height = height;
		}

		public String getPreview_img()
		{
			return preview_img;
		}

		public void setPreview_img(String preview_img)
		{
			this.preview_img = preview_img;
		}

		public int getExpanded_width()
		{
			return expanded_width;
		}

		public void setExpanded_width(int expanded_width)
		{
			this.expanded_width = expanded_width;
		}

		public int getExpanded_height()
		{
			return expanded_height;
		}

		public void setExpanded_height(int expanded_height)
		{
			this.expanded_height = expanded_height;
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeInt(expanded_height);
			dest.writeInt(expanded_width);
			dest.writeInt(height);
			dest.writeString(preview_img);
			dest.writeString(source_url);
			dest.writeInt(width);
		}

		public static final Parcelable.Creator<Swf>	CREATOR	= new Parcelable.Creator<Swf>() {
																public Swf createFromParcel(Parcel in)
																{
																	return new Swf(in);
																}

																public Swf[] newArray(int size)
																{
																	return new Swf[size];
																}
															};

		private Swf(Parcel in)
		{
			expanded_height = in.readInt();
			expanded_width = in.readInt();
			height = in.readInt();
			preview_img = in.readString();
			source_url = in.readString();
			width = in.readInt();
		}
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(alt);
		dest.writeString(href);
		dest.writeParcelable(photo, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(src);
		dest.writeParcelable(swf, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(type);
		dest.writeParcelable(video, PARCELABLE_WRITE_RETURN_VALUE);
	}

	public static final Parcelable.Creator<Media>	CREATOR	= new Parcelable.Creator<Media>() {
																public Media createFromParcel(Parcel in)
																{
																	return new Media(in);
																}

																public Media[] newArray(int size)
																{
																	return new Media[size];
																}
															};

	private Media(Parcel in)
	{
		alt = in.readString();
		href = in.readString();
		photo= in.readParcelable(Photo.class.getClassLoader());
		src= in.readString();
		swf = in.readParcelable(Swf.class.getClassLoader());
		type = in.readString();
		video = in.readParcelable(Video.class.getClassLoader());
		
	}
}
