package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.ConversationAdapter;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.fql.Message;
import com.abewy.android.apps.klyph.core.graph.Comment;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.util.PhoneUtil;

public class ConversationFragment extends KlyphFragment
{
	public ConversationFragment()
	{
		setRequestType(Query.MESSAGES);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		MultiObjectAdapter adapter = KlyphPreferences.areBannerAdsEnabled() ? new ConversationAdapter(getListView()) : new MultiObjectAdapter(
				getListView());
		setListAdapter(adapter);

		registerForContextMenu(getListView());

		defineEmptyText(R.string.empty_list_no_message);

		getListView().setStackFromBottom(true);
		getListView().setDrawSelectorOnTop(false);
		getListView().setSelector(R.drawable.transparent_selector);
		// getListView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		setListVisible(false);

		setRequestType(Query.MESSAGES);

		setLoadingObjectAsFirstItem(true);

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	protected int getCustomLayout()
	{
		return R.layout.list_timeline;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		if (!isLoading() && !isFirstLoad() && !hasNoMoreData())
		{
			boolean loadMore = firstVisibleItem == 0;

			if (loadMore)
			{
				refresh();
			}
		}
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		int n = data.size();
		int m = getAdapter().getCount();
		for (int i = n - 1; i >= 0; i--)
		{
			getAdapter().insert(data.get(i), 0);
		}
		/*
		 * if (isFirstLoad() == true)
		 * {
		 * for (GraphObject graphObject : data)
		 * {
		 * getAdapter().add(graphObject);
		 * }
		 * }
		 * else
		 * {
		 * int n = data.size();
		 * for (int i = n - 1; i >= 0; i--)
		 * {
		 * getAdapter().insert(data.get(i), 0);
		 * }
		 * }
		 */

		// getAdapter().notifyDataSetChanged();
		endLoading();

		int size = getAdapter().getCount() - m;
		getListView().setSelection(size);

		if (data.size() == 0)
			setNoMoreData(true);
		else
			setOffset(((Message) data.get(0)).getCreated_time());
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);

		GraphObject object = (GraphObject) getAdapter().getItem(position);

		if (object instanceof Message)
		{
			final Message message = (Message) object;

			List<String> list = new ArrayList<String>();

			int copyText = -1;
			int downloadImage = -1;

			String body = message.getBody();

			if (body.length() > 0)
			{
				list.add(getString(R.string.copy_text));
				copyText = list.size() - 1;

				Spannable spannable = new SpannableString(body);
				Linkify.addLinks(spannable, Linkify.WEB_URLS);

				URLSpan[] urls = spannable.getSpans(0, spannable.length(), URLSpan.class);
				if (urls.length > 0)
				{
					for (URLSpan urlSpan : urls)
					{
						list.add(urlSpan.getURL());
					}
				}
			}

			/*
			 * if (message.getAttachment() != null)
			 * {
			 * list.add(getString(R.string.download_image));
			 * downloadImage = list.size() - 1;
			 * }
			 */

			final int fcopyText = copyText;
			final int fdownloadImage = downloadImage;

			final String[] items = list.toArray(new String[0]);

			// For Api 8 to 10
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which)
				{
					if (which == fcopyText)
					{
						handleCopyTextAction(message);
					}
					else if (which == fdownloadImage)
					{
						handleDownloadAction(message);
					}
					else
					{
						handleUrlAction(items[which]);
					}
				}
			});
			builder.create().show();
		}
	}

	@TargetApi(11)
	private void handleCopyTextAction(Message message)
	{
		ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("Message", message.getBody());
		clipboard.setPrimaryClip(clip);
	}

	private void handleDownloadAction(Message message)
	{

	}

	private void handleUrlAction(String url)
	{
		PhoneUtil.openURL(getActivity(), url);
	}
}