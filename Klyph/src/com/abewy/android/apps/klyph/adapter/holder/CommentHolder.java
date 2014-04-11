package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class CommentHolder
{
	private ImageView	depthBar;
	private ImageView	authorPicture;
	private TextView	authorName;
	private TextView	commentText;
	private ImageView	commentImage;
	private ImageView	commentImagePlay;
	private ImageView	commentLinkImage;
	private ImageView	commentLinkImageBackground;
	private TextView	commentLinkName;
	private TextView	commentLinkUrl;
	private TextView	commentLinkDescription;

	public CommentHolder(ImageView depthBar, ImageView authorPicture, TextView authorName, TextView commentText, ImageView commentImage, ImageView commentImagePlay, ImageView commentLinkImage,
			ImageView commentLinkImageBackground, TextView commentLinkName, TextView commentLinkUrl, TextView commentLinkDescription)
	{
		this.depthBar = depthBar;
		this.authorPicture = authorPicture;
		this.authorName = authorName;
		this.commentText = commentText;
		this.commentImage = commentImage;
		this.commentImagePlay = commentImagePlay;
		this.commentLinkImage = commentLinkImage;
		this.commentLinkImageBackground = commentLinkImageBackground;
		this.commentLinkName = commentLinkName;
		this.commentLinkUrl = commentLinkUrl;
		this.commentLinkDescription = commentLinkDescription;
	}

	public ImageView getDepthBar()
	{
		return depthBar;
	}

	public ImageView getAuthorPicture()
	{
		return authorPicture;
	}

	public TextView getAuthorName()
	{
		return authorName;
	}

	public TextView getCommentText()
	{
		return commentText;
	}

	public ImageView getCommentImage()
	{
		return commentImage;
	}
	
	public ImageView getCommentImagePlay()
	{
		return commentImagePlay;
	}

	public ImageView getCommentLinkImage()
	{
		return commentLinkImage;
	}

	public ImageView getCommentLinkImageBackground()
	{
		return commentLinkImageBackground;
	}

	public TextView getCommentLinkName()
	{
		return commentLinkName;
	}

	public TextView getCommentLinkUrl()
	{
		return commentLinkUrl;
	}

	public TextView getCommentLinkDescription()
	{
		return commentLinkDescription;
	}
}
