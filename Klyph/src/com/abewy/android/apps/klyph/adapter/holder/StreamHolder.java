package com.abewy.android.apps.klyph.adapter.holder;

import it.sephiroth.android.library.widget.HListView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class StreamHolder implements IStreamHolder
{
	private ImageView	authorProfileImage;
	private TextView	story;
	private TextView	postTime;
	private ImageView	sharedAuthorProfileImage;
	private TextView	sharedStory;
	private TextView	sharedPostTime;
	private TextView	message;
	private ImageView	postPhoto;
	private ImageView	postVideoPlay;
	private TextView	videoTitle;
	private TextView	videoUrl;
	private ImageView	postPicturePlay;
	private ImageView	postLinkBackground;
	private TextView	postName;
	private TextView	postCaption;
	private TextView	postDescription;
	private Button		likeButton;
	private Button		commentButton;
	private ImageButton	shareButton;
	private ImageButton	overflowButton;
	private HListView	streamAlbum;
	private ViewGroup	streamLink;
	private ViewGroup	buttonBar;
	private View		buttonBarDivider;

	public StreamHolder(ImageView authorProfileImage, TextView story, TextView postTime, ImageView sharedAuthorProfileImage, TextView sharedStory,
			TextView sharedPostTime, TextView message, ImageView postPhoto, ImageView postVideoPlay, TextView videoTitle, TextView videoUrl,
			ImageView postPicturePlay, ImageView postLinkBackground, TextView postName, TextView postCaption,
			TextView postDescription, Button likeButton, Button commentButton,
			ImageButton shareButton, ImageButton overflowButton, HListView streamAlbum, ViewGroup streamLink, ViewGroup buttonBar,
			View buttonBarDivider)
	{
		this.authorProfileImage = authorProfileImage;
		this.story = story;
		this.postTime = postTime;
		this.sharedAuthorProfileImage = sharedAuthorProfileImage;
		this.sharedStory = sharedStory;
		this.sharedPostTime = sharedPostTime;
		this.message = message;
		this.postPhoto = postPhoto;
		this.postVideoPlay = postVideoPlay;
		this.videoTitle = videoTitle;
		this.videoUrl = videoUrl;
		this.postPicturePlay = postPicturePlay;
		this.postLinkBackground = postLinkBackground;
		this.postName = postName;
		this.postCaption = postCaption;
		this.postDescription = postDescription;
		this.likeButton = likeButton;
		this.commentButton = commentButton;
		this.shareButton = shareButton;
		this.overflowButton = overflowButton;
		this.streamAlbum = streamAlbum;
		this.streamLink = streamLink;
		this.buttonBar = buttonBar;
		this.buttonBarDivider = buttonBarDivider;
	}

	public ImageView getAuthorProfileImage()
	{
		return authorProfileImage;
	}

	public TextView getStory()
	{
		return story;
	}

	public TextView getPostTime()
	{
		return postTime;
	}

	public ImageView getSharedAuthorProfileImage()
	{
		return sharedAuthorProfileImage;
	}

	public TextView getSharedStory()
	{
		return sharedStory;
	}

	public TextView getSharedPostTime()
	{
		return sharedPostTime;
	}

	public TextView getMessage()
	{
		return message;
	}

	public ImageView getPostPhoto()
	{
		return postPhoto;
	}

	public ImageView getPostVideoPlay()
	{
		return postVideoPlay;
	}

	public TextView getVideoTitle()
	{
		return videoTitle;
	}

	public TextView getVideoUrl()
	{
		return videoUrl;
	}

	public ImageView getPostPicturePlay()
	{
		return postPicturePlay;
	}

	public ImageView getPostLinkBackground()
	{
		return postLinkBackground;
	}

	public TextView getPostName()
	{
		return postName;
	}

	public TextView getPostCaption()
	{
		return postCaption;
	}

	public TextView getPostDescription()
	{
		return postDescription;
	}

	public Button getLikeButton()
	{
		return likeButton;
	}

	public Button getCommentButton()
	{
		return commentButton;
	}

	public ImageButton getShareButton()
	{
		return shareButton;
	}

	public ImageButton getOverflowButton()
	{
		return overflowButton;
	}

	public HListView getStreamAlbum()
	{
		return streamAlbum;
	}

	public ViewGroup getStreamLink()
	{
		return streamLink;
	}

	public ViewGroup getButtonBar()
	{
		return buttonBar;
	}

	public View getButtonBarDivider()
	{
		return buttonBarDivider;
	}
}
