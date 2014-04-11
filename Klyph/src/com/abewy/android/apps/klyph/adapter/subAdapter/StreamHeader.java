/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.adapter.subAdapter;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.adapter.holder.IStreamHolder;
import com.abewy.android.apps.klyph.adapter.holder.StreamHolder;
import com.abewy.android.apps.klyph.core.fql.Link;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Status;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.util.DateUtil;
import com.abewy.android.apps.klyph.util.KlyphUtil;
import com.abewy.android.apps.klyph.util.TextViewUtil;

public class StreamHeader extends StreamMedia
{
	private final int specialLayout;
	
	public StreamHeader(int specialLayout)
	{
		super();
		this.specialLayout = specialLayout;
	}

	public void mergeData(IStreamHolder holder, final Stream stream)
	{
		HeaderHolder headerHolder = new HeaderHolder(
				holder.getAuthorProfileImage(), holder.getStory(),
				holder.getPostTime(), stream.getActor_id(),
				stream.getActor_name(), stream.getActor_type(),
				stream.getActor_pic(), stream.getTarget_id(),
				stream.getTarget_name(), stream.getTarget_type(),
				stream.getDescription(), stream.getCreated_time(),
				stream.getDescription_tags());
		
		manageAuthorImage(headerHolder, stream);
		manageStory(headerHolder);
		manageTime(headerHolder);
	}
	
	public void mergeData(StreamHolder holder, final Stream stream, boolean  subStream)
	{
		HeaderHolder headerHolder = new HeaderHolder(
				holder.getSharedAuthorProfileImage(), holder.getSharedStory(),
				holder.getSharedPostTime(), stream.getActor_id(),
				stream.getActor_name(), stream.getActor_type(),
				stream.getActor_pic(), stream.getTarget_id(),
				stream.getTarget_name(), stream.getTarget_type(),
				stream.getDescription(), stream.getCreated_time(),
				stream.getDescription_tags());
		
		manageAuthorImage(headerHolder, stream);
		manageStory(headerHolder);
		manageTime(headerHolder);
		
		((ViewGroup) holder.getSharedAuthorProfileImage().getParent().getParent()).setVisibility(View.VISIBLE);
	}

	public void mergeData(StreamHolder holder, final Stream stream, final Link link)
	{
		HeaderHolder headerHolder = new HeaderHolder(
				holder.getSharedAuthorProfileImage(), holder.getSharedStory(),
				holder.getSharedPostTime(), link.getOwner(),
				link.getOwner_name(), link.getOwner_type(),
				link.getOwner_pic(), link.getVia_id(), link.getVia_name(),
				link.getVia_type(), null, link.getCreated_time(), null);

		manageAuthorImage(headerHolder, stream);
		manageStory(headerHolder);
		manageTime(headerHolder);
		((ViewGroup) holder.getSharedAuthorProfileImage().getParent().getParent()).setVisibility(View.VISIBLE);
	}

	public void mergeData(StreamHolder holder, final Stream stream, final Photo photo)
	{
		HeaderHolder headerHolder = new HeaderHolder(
				holder.getSharedAuthorProfileImage(), holder.getSharedStory(),
				holder.getSharedPostTime(), photo.getOwner(),
				photo.getOwner_name(), photo.getOwner_type(),
				photo.getOwner_pic(), photo.getTarget_id(),
				photo.getTarget_name(), photo.getTarget_type(), null,
				photo.getCreated(), null);

		manageAuthorImage(headerHolder, stream);
		manageStory(headerHolder);
		manageTime(headerHolder);
		((ViewGroup) holder.getSharedAuthorProfileImage().getParent().getParent()).setVisibility(View.VISIBLE);
	}

	public void mergeData(StreamHolder holder, final Stream stream, final Video video)
	{
		HeaderHolder headerHolder = new HeaderHolder(
				holder.getSharedAuthorProfileImage(), holder.getSharedStory(),
				holder.getSharedPostTime(), video.getOwner(),
				video.getOwner_name(), video.getOwner_type(),
				video.getOwner_pic(), null, null, null, null,
				video.getCreated_time(), null);

		manageAuthorImage(headerHolder, stream);
		manageStory(headerHolder);
		manageTime(headerHolder);
		((ViewGroup) holder.getSharedAuthorProfileImage().getParent().getParent()).setVisibility(View.VISIBLE);
	}
	
	public void mergeData(StreamHolder holder, final Stream stream, final Status status)
	{
		HeaderHolder headerHolder = new HeaderHolder(
				holder.getSharedAuthorProfileImage(), holder.getSharedStory(),
				holder.getSharedPostTime(), status.getUid(),
				status.getUid_name(), status.getUid_type(),
				status.getUid_pic(), null, null, null, null,
				status.getTime(), null);

		manageAuthorImage(headerHolder, stream);
		manageStory(headerHolder);
		manageTime(headerHolder);
		((ViewGroup) holder.getSharedAuthorProfileImage().getParent().getParent()).setVisibility(View.VISIBLE);
	}

	private void manageAuthorImage(final HeaderHolder holder, final Stream stream)
	{
		if (holder.getImageView().get() != null)
		{
			final ImageView view = holder.getImageView().get();

			loadImage(view, holder.getAuthorPic(), KlyphUtil.getProfilePlaceHolder(view.getContext()), stream);

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					Intent intent = Klyph.getIntentForParams(getContext(view),
							holder.getAuthorId(), holder.getAuthorName(),
							holder.getAuthorType());
					view.getContext().startActivity(intent);
				}
			});

			view.setVisibility(View.VISIBLE);
			((ViewGroup) view.getParent()).setVisibility(View.VISIBLE);
		}

	}

	private void manageStory(HeaderHolder holder)
	{
		if (holder.getStoryView().get() != null)
		{
			TextView view = holder.getStoryView().get();

			if (holder.getStory() != null && holder.getStory().length() > 0)
			{
				view.setText(holder.getStory());

				if (holder.getTags() != null && holder.getTags().size() > 0)
				{
					TextViewUtil.setTextClickableForTags(getContext(view), view,
							holder.getTags(), specialLayout == SpecialLayout.STREAM_DETAIL);
				}
			}
			else
			{
				view.setText(holder.getAuthorName());

					TextViewUtil.setElementClickable(getContext(view), view,
						holder.getAuthorName(), holder.getAuthorId(),
						holder.getAuthorType(), specialLayout == SpecialLayout.STREAM_DETAIL);

				if (holder.getTargetId() != null && holder.getTargetId().length() > 0
						&& holder.getTargetName().length() > 0
						&& !holder.getTargetId().equals(holder.getAuthorId()))
				{
					view.append(" > " + holder.getTargetName());

						TextViewUtil.setElementClickable(getContext(view), view,
							holder.getTargetName(), holder.getTargetId(),
							holder.getTargetType(), specialLayout == SpecialLayout.STREAM_DETAIL);
				}
			}

			view.setVisibility(View.VISIBLE);
		}
	}

	private void manageTime(HeaderHolder holder)
	{
		if (holder.getTimeView().get() != null)
		{
			holder.getTimeView()
					.get()
					.setText(
							DateUtil.timeAgoInWords(getContext(holder.getTimeView().get()),
									holder.getTime()));

			holder.getTimeView().get().setVisibility(View.VISIBLE);
		}

	}

	private static class HeaderHolder
	{
		private final WeakReference<ImageView>	imageView;
		private final WeakReference<TextView>	storyView;
		private final WeakReference<TextView>	timeView;
		private final String					authorId;
		private final String					authorName;
		private final String					authorType;
		private final String					authorPic;
		private final String					targetId;
		private final String					targetName;
		private final String					targetType;
		private final String					story;
		private final String					time;
		private final Map<String, List<Tag>>	tags;

		public HeaderHolder(ImageView imageView, TextView storyView,
				TextView timeView, String authorId, String authorName,
				String authorType, String authorPic, String targetId,
				String targetName, String targetType, String story,
				String time, Map<String, List<Tag>> tags)
		{
			this.imageView = new WeakReference<ImageView>(imageView);
			this.storyView = new WeakReference<TextView>(storyView);
			this.timeView = new WeakReference<TextView>(timeView);
			this.authorId = authorId;
			this.authorName = authorName;
			this.authorType = authorType;
			this.authorPic = authorPic;
			this.targetId = targetId;
			this.targetName = targetName;
			this.targetType = targetType;
			this.story = story;
			this.time = time;
			this.tags = tags;
		}

		public WeakReference<ImageView> getImageView()
		{
			return imageView;
		}

		public WeakReference<TextView> getStoryView()
		{
			return storyView;
		}

		public WeakReference<TextView> getTimeView()
		{
			return timeView;
		}

		public String getAuthorId()
		{
			return authorId;
		}

		public String getAuthorName()
		{
			return authorName;
		}

		public String getAuthorType()
		{
			return authorType;
		}

		public String getAuthorPic()
		{
			return authorPic;
		}

		public String getTargetId()
		{
			return targetId;
		}

		public String getTargetName()
		{
			return targetName;
		}

		public String getTargetType()
		{
			return targetType;
		}

		public String getStory()
		{
			return story;
		}

		public String getTime()
		{
			return time;
		}

		public Map<String, List<Tag>> getTags()
		{
			return tags;
		}
	}
}
