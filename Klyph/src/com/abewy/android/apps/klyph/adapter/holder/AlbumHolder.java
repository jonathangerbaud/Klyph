package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class AlbumHolder
{
	private ImageView	albumCover;
	private TextView	albumName;
	private TextView	numPhoto;
	private TextView	numVideo;
	private ImageView	photoIcon;
	private ImageView	videoIcon;

	public AlbumHolder(ImageView cover, TextView name, TextView numPhoto, TextView numVideo, ImageView photoIcon,
			ImageView videoIcon)
	{
		albumCover = cover;
		albumName = name;
		this.numPhoto = numPhoto;
		this.numVideo = numVideo;
		this.photoIcon = photoIcon;
		this.videoIcon = videoIcon;
	}

	public ImageView getAlbumCover()
	{
		return albumCover;
	}

	public TextView getAlbumName()
	{
		return albumName;
	}

	public TextView getNumPhoto()
	{
		return numPhoto;
	}

	public TextView getNumVideo()
	{
		return numVideo;
	}

	public ImageView getPhotoIcon()
	{
		return photoIcon;
	}

	public ImageView getVideoIcon()
	{
		return videoIcon;
	}
}
