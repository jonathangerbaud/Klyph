package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class ThreadHolder
{
	private ImageView	authorPicture;
	private TextView	authorName;
	private TextView	dateTextView;
	private TextView	messageTextView;
	private TextView	unreadCountTextView;

	public ThreadHolder(ImageView authorPicture, TextView authorName, TextView dateTextView, TextView messageTextView,
			TextView unreadCountTextView)
	{
		this.authorPicture = authorPicture;
		this.authorName = authorName;
		this.dateTextView = dateTextView;
		this.messageTextView = messageTextView;
		this.unreadCountTextView = unreadCountTextView;
	}

	public ImageView getAuthorPicture()
	{
		return authorPicture;
	}

	public TextView getAuthorName()
	{
		return authorName;
	}

	public TextView getDateTextView()
	{
		return dateTextView;
	}

	public TextView getMessageTextView()
	{
		return messageTextView;
	}
	
	public TextView getUnreadCountTextView()
	{
		return unreadCountTextView;
	}
}
