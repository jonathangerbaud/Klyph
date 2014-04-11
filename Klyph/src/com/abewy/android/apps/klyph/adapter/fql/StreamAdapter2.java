package com.abewy.android.apps.klyph.adapter.fql;

import it.sephiroth.android.library.widget.HListView;
import java.util.ArrayList;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.adapter.holder.StreamHolder;
import com.abewy.android.apps.klyph.adapter.subAdapter.StreamAlbum;
import com.abewy.android.apps.klyph.adapter.subAdapter.StreamButtonBar;
import com.abewy.android.apps.klyph.adapter.subAdapter.StreamHeader;
import com.abewy.android.apps.klyph.adapter.subAdapter.StreamLink;
import com.abewy.android.apps.klyph.adapter.subAdapter.StreamMessage;
import com.abewy.android.apps.klyph.adapter.subAdapter.StreamPhoto;
import com.abewy.android.apps.klyph.adapter.subAdapter.StreamStatus;
import com.abewy.android.apps.klyph.core.fql.Attachment;
import com.abewy.android.apps.klyph.core.fql.Media;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class StreamAdapter2 extends KlyphAdapter
{
	private final MultiObjectAdapter	parentAdapter;
	private final int					specialLayout;
	private StreamButtonBar				buttonBarAdapter;
	private StreamHeader				headerAdapter;
	private StreamMessage				messageAdapter;
	private StreamAlbum					albumAdapter;
	private StreamPhoto					photoAdapter;
	private StreamLink					linkAdapter;
	private StreamStatus				statusAdapter;

	public StreamAdapter2(MultiObjectAdapter parentAdapter, int specialLayout)
	{
		super();

		this.parentAdapter = parentAdapter;
		this.specialLayout = specialLayout;

		headerAdapter = new StreamHeader(specialLayout);
		messageAdapter = new StreamMessage(true);
		albumAdapter = new StreamAlbum();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_stream2;
	}

	@Override
	public boolean  isEnabled(GraphObject object)
	{
		return false;
	}

	@Override
	protected void attachHolder(View view)
	{
		ImageView authorProfileImage = (ImageView) view.findViewById(R.id.author_profile_image);
		TextView story = (TextView) view.findViewById(R.id.story);
		TextView postTime = (TextView) view.findViewById(R.id.post_time);
		ImageView sharedAuthorProfileImage = (ImageView) view.findViewById(R.id.shared_author_profile_image);
		TextView sharedStory = (TextView) view.findViewById(R.id.shared_story);
		TextView sharedPostTime = (TextView) view.findViewById(R.id.shared_post_time);
		TextView message = (TextView) view.findViewById(R.id.message);
		ImageView postPhoto = (ImageView) view.findViewById(R.id.post_photo);
		ImageView postVideoPlay = (ImageView) view.findViewById(R.id.post_video_play);
		TextView videoTitle = (TextView) view.findViewById(R.id.post_video_title);
		TextView videoUrl = (TextView) view.findViewById(R.id.post_video_url);
		ImageView postPicturePlay = (ImageView) view.findViewById(R.id.post_picture_play);
		ImageView postLinkBackground = (ImageView) view.findViewById(R.id.stream_link_image_background);
		TextView postName = (TextView) view.findViewById(R.id.post_name);
		TextView postCaption = (TextView) view.findViewById(R.id.post_caption);
		TextView postDescription = (TextView) view.findViewById(R.id.post_description);
		HListView streamAlbum = (HListView) view.findViewById(R.id.stream_album);
		ViewGroup streamLink = (ViewGroup) view.findViewById(R.id.stream_link);
		Button likeButton = (Button) view.findViewById(R.id.like_button);
		Button commentButton = (Button) view.findViewById(R.id.comment_button);
		ImageButton shareButton = (ImageButton) view.findViewById(R.id.share_button);
		ImageButton overflowButton = (ImageButton) view.findViewById(R.id.overflow_button);
		ViewGroup buttonBar = (ViewGroup) view.findViewById(R.id.button_bar);
		View buttonBarDivider = (View) view.findViewById(R.id.button_bar_divider);

		StreamHolder holder = new StreamHolder(authorProfileImage, story, postTime, sharedAuthorProfileImage, sharedStory, sharedPostTime, message,
				postPhoto, postVideoPlay, videoTitle, videoUrl, postPicturePlay, postLinkBackground, postName, postCaption, postDescription,
				likeButton, commentButton, shareButton, overflowButton, streamAlbum, streamLink, buttonBar, buttonBarDivider);

		setHolder(view, holder);
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);

		if (buttonBarAdapter == null)
			buttonBarAdapter = new StreamButtonBar(parentAdapter, specialLayout);

		if (photoAdapter == null)
			photoAdapter = new StreamPhoto(parentAdapter, specialLayout);

		if (linkAdapter == null)
			linkAdapter = new StreamLink(parentAdapter, specialLayout);

		if (statusAdapter == null)
			statusAdapter = new StreamStatus(parentAdapter, specialLayout);

		final StreamHolder holder = (StreamHolder) getHolder(view);

		setData(view, data);

		final Stream stream = (Stream) data;

		setData(holder, stream);
	}

	public void setData(StreamHolder holder, Stream stream)
	{
		holder.getStory().setAutoLinkMask(0);
		holder.getMessage().setAutoLinkMask(Linkify.WEB_URLS);
		holder.getPostName().setAutoLinkMask(0);
		holder.getPostCaption().setAutoLinkMask(Linkify.WEB_URLS);
		holder.getPostDescription().setAutoLinkMask(Linkify.WEB_URLS);

		((ViewGroup) holder.getSharedAuthorProfileImage().getParent().getParent()).setVisibility(View.GONE);
		holder.getSharedAuthorProfileImage().setVisibility(View.GONE);
		holder.getSharedStory().setVisibility(View.GONE);
		holder.getSharedPostTime().setVisibility(View.GONE);
		holder.getMessage().setVisibility(View.GONE);
		holder.getPostPhoto().setVisibility(View.GONE);
		holder.getPostVideoPlay().setVisibility(View.GONE);
		holder.getVideoTitle().setVisibility(View.GONE);
		holder.getVideoUrl().setVisibility(View.GONE);
		((ViewGroup) holder.getVideoTitle().getParent()).setVisibility(View.GONE);
		((ViewGroup) holder.getPostPhoto().getParent()).setVisibility(View.GONE);
		holder.getStreamAlbum().setVisibility(View.GONE);
		holder.getStreamLink().setVisibility(View.GONE);
		holder.getPostDescription().setVisibility(View.GONE);
		holder.getPostCaption().setVisibility(View.GONE);
		holder.getPostName().setVisibility(View.GONE);
		holder.getPostPicturePlay().setVisibility(View.GONE);
		
		holder.getMessage().setTextIsSelectable(true);

		/*
		 * holder.getPostPhoto().setImageDrawable(null);
		 * holder.getPostPicture().setImageDrawable(null);
		 * holder.getAlbumPhoto1().setImageDrawable(null);
		 * holder.getAlbumPhoto2().setImageDrawable(null);
		 * holder.getAlbumPhoto3().setImageDrawable(null);
		 * holder.getAlbumPhoto4().setImageDrawable(null);
		 * holder.getPostLinkBackground().setImageDrawable(null);
		 */

		headerAdapter.mergeData(holder, stream);

		messageAdapter.mergeData(holder, stream);

		manageAttachment(holder, stream);
	}

	private void manageAttachment(StreamHolder holder, Stream stream)
	{
		Attachment attachment = stream.getAttachment();
		Media media = attachment.getMedia().size() > 0 ? attachment.getMedia().get(0) : null;

		int type = stream.getType();
		if ((type == 245 || type == 257) && stream.getParent_stream() != null)
		{
			final Stream parentStream = stream.getParent_stream();
			headerAdapter.mergeData(holder, parentStream, true);
			messageAdapter.mergeData(holder, parentStream);

			buttonBarAdapter.mergeData(holder, stream);
		}
		if (type == 161 && stream.getDescription_tags().size() > 0 && stream.getLiked_pages().size() > 0)
		{
			linkAdapter.manageLikedPage(holder, stream);
		}
		if (stream.isStatus())
		{
			statusAdapter.mergeData(holder, stream);
		}
		else if (stream.isPhoto())
		{
			photoAdapter.mergeData(holder, stream, stream.getPhoto());
		}
		else if (stream.isVideo())
		{
			photoAdapter.mergeData(holder, stream, stream.getVideo());
		}
		else if (attachment.isPhoto() || ((media != null && media.isFydv())))
		{
			photoAdapter.mergeData(holder, stream);
			buttonBarAdapter.mergeData(holder, stream);
		}
		else if (attachment.isAlbum())
		{
			albumAdapter.mergeData(holder, stream);
			buttonBarAdapter.mergeData(holder, stream);
		}
		else if (attachment.isCheckin())
		{
			manageAttachmentCheckin(holder, stream);
			buttonBarAdapter.mergeData(holder, stream);
		}
		else if (stream.getLink().isEventLink())
		{
			linkAdapter.mergeData(holder, stream, stream.getLink());
		}
		else if (stream.getType() == 161 || attachment.getMedia().size() > 0)
		{
			linkAdapter.mergeData(holder, stream);
			buttonBarAdapter.mergeData(holder, stream);
		}
		else if (stream.isLink())
		{
			linkAdapter.mergeData(holder, stream, stream.getLink());
		}
		else
		{
			buttonBarAdapter.mergeData(holder, stream);
		}
	}

	private void manageAttachmentCheckin(StreamHolder holder, Stream stream)
	{
		Attachment attachment = stream.getAttachment();

		Tag tag = new Tag();
		tag.setName(attachment.getName());
		tag.setId(attachment.getFb_checkin().getPage_id());
		tag.setOffset(attachment.getCaption().indexOf(attachment.getName()));
		tag.setLength(attachment.getName().length());

		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(tag);

		// addClickableTextForTags(getContext(), holder.getStory(),
		// attachment.getCaption(),
		// tags);
	}
}
