/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.adapter.subAdapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.holder.IStreamHolder;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.util.TextViewUtil;

public class StreamMessage
{
	private final boolean  linksEnabled;

	public StreamMessage(boolean linksEnabled)
	{
		super();
		this.linksEnabled = linksEnabled;
	}

	protected Context getContext(View view)
	{
		return view.getContext();
	}

	public void mergeData(IStreamHolder holder, Stream stream)
	{
		final View view = holder.getAuthorProfileImage();
		if (stream.getMessage().length() > 0)
		{
			holder.getMessage().setText(stream.getMessage());

			if (linksEnabled && stream.getMessage_tags().size() > 0)
			{
				TextViewUtil.setTextClickableForTags(getContext(view), holder.getMessage(), stream.getMessage_tags(), linksEnabled);
			}

			holder.getMessage().setVisibility(View.VISIBLE);

			// manage place
		}

		if (stream.getMessage_tags().size() == 0 && stream.getTagged_tags().size() > 0)
		{
			List<Tag> tags = stream.getTagged_tags();
			switch (tags.size())
			{
				case 1:
				{
					Tag tag = tags.get(0);
					holder.getMessage().append(getContext(view).getString(R.string.post_with_one_friend, tag.getName()));
					TextViewUtil.setElementClickable(getContext(view), holder.getMessage(), tag.getName(), tag.getId(),
							tag.getType(), linksEnabled);
					break;
				}
				case 2:
				{
					Tag tag = tags.get(0);
					Tag tag2 = tags.get(1);
					holder.getMessage().append(
							getContext(view).getString(R.string.post_with_two_friends, tag.getName(), tag2.getName()));
					TextViewUtil.setElementClickable(getContext(view), holder.getMessage(), tag.getName(), tag.getId(),
							tag.getType(), linksEnabled);
					TextViewUtil.setElementClickable(getContext(view), holder.getMessage(), tag2.getName(), tag2.getId(),
							tag2.getType(), linksEnabled);
					break;
				}
				default:
				{
					Tag tag = tags.get(0);
					holder.getMessage().append(
							getContext(view).getString(R.string.post_with_several_friends, tag.getName(), tags.size()));
					TextViewUtil.setElementClickable(getContext(view), holder.getMessage(), tag.getName(), tag.getId(), tag.getType(), linksEnabled);
					TextViewUtil.setElementClickable(getContext(view), holder.getMessage(),
							getContext(view).getString(R.string.with_several_friends, tags.size()), tags, linksEnabled);
					break;
				}
			}
		}
		
		manageCheckin(holder, stream);
	}

	private void manageCheckin(IStreamHolder holder, Stream stream)
	{
		if (stream.getPlace() != null && stream.getPlace().length() > 0)
		{
			if (stream.getMessage().indexOf(stream.getPlace_name()) == -1)
			{
				holder.getMessage().append(" " + getContext(holder.getAuthorProfileImage()).getString(R.string.at_place, stream.getPlace_name()));
			}

			if (linksEnabled)
				TextViewUtil.setElementClickable(getContext(holder.getAuthorProfileImage()), holder.getMessage(), stream.getPlace_name(), stream.getPlace(), "page", linksEnabled);
		}
	}
}
