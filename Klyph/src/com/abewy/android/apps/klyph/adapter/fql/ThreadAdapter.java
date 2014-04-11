package com.abewy.android.apps.klyph.adapter.fql;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.ThreadHolder;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.fql.MessageThread;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.util.DateUtil;
import com.abewy.android.apps.klyph.util.EmojiUtil;
import com.abewy.android.apps.klyph.util.KlyphUtil;

public class ThreadAdapter extends KlyphAdapter
{
	public ThreadAdapter()
	{
		super();
	}

	@Override
	protected int getLayoutRes()
	{
		return R.layout.item_conversation_list;
	}

	@Override
	protected void attachViewHolder(View view)
	{
		ImageView authorPicture = (ImageView) view.findViewById(R.id.avatar);
		TextView authorName = (TextView) view.findViewById(R.id.conversation_name);
		TextView date = (TextView) view.findViewById(R.id.conversation_date);
		TextView message = (TextView) view.findViewById(R.id.message_snippet);
		TextView unreadCount = (TextView) view.findViewById(R.id.unread_count);

		setHolder(view, new ThreadHolder(authorPicture, authorName, date, message, unreadCount));
	}

	@Override
	public void bindData(View view, GraphObject data)
	{
		ThreadHolder holder = (ThreadHolder) getHolder(view);

		MessageThread thread = (MessageThread) data;

		List<Friend> friends = new ArrayList<Friend>();
		friends.addAll(thread.getRecipients_friends());

		String pic = null;

		if (friends.size() > 0)
		{
			for (int i = 0; i < friends.size(); i++)
			{
				Friend friend = friends.get(i);

				if (friend.getUid().equals(KlyphSession.getSessionUserId()))
				{
					friends.remove(friend);
					i--;
				}
				else
				{
					if (pic == null)
					{
						pic = friend.getPic();
					}
				}
			}
		}

		if (pic == null)
		{
			pic = "";
		}

		Resources res = getContext(view).getResources();

		holder.getAuthorName().setText("");

		int n = friends.size();

		Friend friend = null;

		if (n > 0)
		{
			friend = friends.get(0);
		}
		else
		{
			n = 1;
			friend = thread.getRecipients_friends().get(0);
			pic = friend.getPic();
		}
		
		String friendName = friend.getFirst_name().length() > 0 ? friend.getFirst_name() : friend.getName();

		if (n == 1)
		{
			holder.getAuthorName().setText(String.format(res.getString(R.string.thread_one_user), friend.getName()));

		}
		else if (n > 1)
		{
			Friend friend2 = friends.get(1);
			String friendName2 = friend2.getFirst_name().length() > 0 ? friend2.getFirst_name() : friend2.getName();
			if (n == 2)
			{
				holder.getAuthorName().setText(String.format(res.getString(R.string.thread_two_users), friendName, friendName2));
			}
			else if (n > 2)
			{
				holder.getAuthorName().setText(String.format(res.getString(R.string.thread_many_users), friendName, friendName2, n - 2));
			}
		}

		String message = "";
		if (thread.getSnippet_author().equals(KlyphSession.getSessionUserId()))
		{
			message = view.getContext().getString(R.string.my_last_message, thread.getSnippet());
		}
		else if (thread.isMultiUserConversation())
		{
			for (Friend friend2 : thread.getRecipients_friends())
			{
				if (friend2.getUid().equals(thread.getSnippet_author()))
				{
					message = view.getContext().getString(R.string.friend_last_message, friend2.getFirst_name(), thread.getSnippet()); 
					break;
				}
			}
		}
		else
		{
			message = thread.getSnippet();
		}
		
		holder.getMessageTextView().setText(message);
		EmojiUtil.convertTextToEmoji(holder.getMessageTextView(), false);

		holder.getDateTextView().setText(DateUtil.getShortDate(thread.getUpdated_time()));

		int unreadCount = thread.getUnread();
		if (unreadCount > 0)
		{
			if (unreadCount <= 99)
				holder.getUnreadCountTextView().setText(thread.getUnread() + "");
			else
				holder.getUnreadCountTextView().setText("99+");

			holder.getUnreadCountTextView().setVisibility(View.VISIBLE);
			holder.getAuthorName().setTypeface(null, Typeface.BOLD);
			holder.getMessageTextView().setTypeface(null, Typeface.BOLD);
			holder.getDateTextView().setTypeface(null, Typeface.BOLD);
		}
		else
		{
			holder.getUnreadCountTextView().setVisibility(View.GONE);
			holder.getAuthorName().setTypeface(null, Typeface.NORMAL);
			holder.getMessageTextView().setTypeface(null, Typeface.NORMAL);
			holder.getDateTextView().setTypeface(null, Typeface.NORMAL);
		}

		ImageLoader.display(holder.getAuthorPicture(), pic, KlyphUtil.getPlaceHolder(holder.getAuthorName().getContext()));
	}
}
