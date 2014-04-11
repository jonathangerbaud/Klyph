package com.abewy.android.apps.klyph.adapter.holder;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoHolder
{
	private ImageView	authorProfileImage;
	private TextView	story;
	private TextView	postTime;

	private TextView	message;

	private Button		likeButton;
	private Button		commentButton;
	private ImageButton	shareButton;

	private ViewGroup	buttonBar;

	public PhotoHolder(ImageView authorProfileImage, TextView story, TextView postTime, TextView message, Button likeButton, Button commentButton,
			ImageButton shareButton, ViewGroup buttonBar)
	{
		this.authorProfileImage = authorProfileImage;
		this.story = story;
		this.postTime = postTime;
		this.message = message;
		this.likeButton = likeButton;
		this.commentButton = commentButton;
		this.shareButton = shareButton;
		this.buttonBar = buttonBar;
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

	public TextView getMessage()
	{
		return message;
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

	public ViewGroup getButtonBar()
	{
		return buttonBar;
	}
}
