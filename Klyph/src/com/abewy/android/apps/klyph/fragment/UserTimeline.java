package com.abewy.android.apps.klyph.fragment;

import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.adapter.animation.GoogleCardStyleAdapter;
import com.abewy.android.apps.klyph.app.MainActivity;
import com.abewy.android.apps.klyph.app.PostActivity;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.User;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.facebook.IFbPermissionCallback;
import com.abewy.android.apps.klyph.facebook.IFbPermissionWorker;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;

public class UserTimeline extends KlyphFakeHeaderListFragment implements IFbPermissionCallback
{
	private static final List<String>	PERMISSIONS		= Arrays.asList("publish_actions", "publish_stream");
	private static final int			POST_CODE		= 101;
	private static final int			STREAM_CODE		= 102;

	private GraphObject					element;
	private boolean						pendingAnnounce	= false;
	private User						user;

	public UserTimeline()
	{
		setRequestType(Query.USER_TIMELINE_FEED);
		setNewestRequestType(Query.USER_TIMELINE_FEED);
	}

	/*
	 * public ElementTimeline(GraphType elementType)
	 * {
	 * this();
	 * this.elementType = elementType;
	 * }
	 */

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setNewestInsertIndex(1);
	}
	
	public void setUser(User user)
	{
		this.user = user;
		
		if (getActivity() != null)
			getActivity().invalidateOptionsMenu();
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK && requestCode == POST_CODE)
		{
			if (isNewestLoading() == false)
			{
				setPullToRefreshRefreshing(true);
				loadNewest();
			}
		}
		else if (resultCode == Activity.RESULT_OK && requestCode == STREAM_CODE)
		{
			if (data != null)
			{
				boolean deleted = data.getBooleanExtra(KlyphBundleExtras.DELETED, false);

				if (deleted == true)
				{
					String id = data.getStringExtra(KlyphBundleExtras.STREAM_ID);

					for (GraphObject stream : getAdapter().getItems())
					{
						if (((Stream) stream).getPost_id().equals(id))
						{
							stream.setToDelete(true);
							getAdapter().remove(stream, true);
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void onPermissionsChange()
	{
		if (pendingAnnounce)
		{
			handleNewPostClick();
		}
	}

	@Override
	public void onCancelPermissions()
	{
		pendingAnnounce = false;
	}

	private void handleNewPostClick()
	{
		pendingAnnounce = false;
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingAnnounce = true;
			((IFbPermissionWorker) getActivity()).requestPublishPermissions(this, PERMISSIONS);
			return;
		}

		if (getIntentParam() == KlyphBundleExtras.USER_ID)
		{
			if (getElementId().equals(KlyphSession.getSessionUserId()))
			{
				Intent intent = new Intent(getActivity(), PostActivity.class);
				intent.putExtra(getIntentParam(), getElementId());
				startActivityForResult(intent, POST_CODE);
			}
			else
			{
				publishFeedDialog();
			}
		}
		else
		{
			Intent intent = new Intent(getActivity(), PostActivity.class);
			intent.putExtra(getIntentParam(), getElementId());
			startActivityForResult(intent, POST_CODE);
		}
	}

	protected boolean canPost()
	{
		return user != null && user.getCan_post();
	}

	protected String getIntentParam()
	{
		return KlyphBundleExtras.USER_ID;
	}

	private void publishFeedDialog()
	{
		Bundle params = new Bundle();
		params.putString("from", KlyphSession.getSessionUserId());
		params.putString("to", getElementId());

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(getActivity(), Session.getActiveSession(), params)).setOnCompleteListener(
				new WebDialog.OnCompleteListener() {

					@Override
					public void onComplete(Bundle values, FacebookException error)
					{
						if (error == null)
						{
							final String postId = values.getString("post_id");

							if (postId != null)
							{
								Toast.makeText(getActivity(), R.string.message_successfully_published, Toast.LENGTH_SHORT).show();

								loadNewest();
							}
							else
							{
								// User clicked the Cancel button
								Toast.makeText(getActivity().getApplicationContext(), "Publish cancelled", Toast.LENGTH_SHORT).show();
							}
						}
						else if (error instanceof FacebookOperationCanceledException)
						{
							// User clicked the "x" button
							// Toast.makeText(getActivity().getApplicationContext(),
							// "Publish cancelled",
							// Toast.LENGTH_SHORT).show();
						}
						else
						{
							AlertUtil.showAlert(getActivity(), R.string.error, R.string.publish_message_unknown_error, R.string.ok);
						}
					}

				}).build();
		feedDialog.show();
	}

	@Override
	protected int getCustomLayout()
	{
		return R.layout.list_timeline;
	}

	protected GraphObject getElement()
	{
		return element;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		defineEmptyText(R.string.empty_list_no_stream);

		setListVisible(false);
		getListView().setDrawSelectorOnTop(true);

		getListView().setSelector(AttrUtil.getResourceId(getActivity(), R.attr.streamSelector));

		super.onViewCreated(view, savedInstanceState);

		MultiObjectAdapter adapter = /*KlyphPreferences.areBannerAdsEnabled() ? new NewsfeedAdapter(getListView(), SpecialLayout.ELEMENT_TIMELINE)
				: */new MultiObjectAdapter(getListView(), SpecialLayout.ELEMENT_TIMELINE);

		if (KlyphPreferences.animateCards())
		{
			GoogleCardStyleAdapter gcsAdapter = new GoogleCardStyleAdapter(adapter);
			gcsAdapter.setAbsListView(getListView());
			setListAdapter(gcsAdapter);
		}
		else
		{
			setListAdapter(adapter);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		if (canPost())
		{
			if (!(getActivity() instanceof MainActivity))
			{
				menu.add(Menu.NONE, R.id.menu_post, Menu.NONE, "Post").setIcon(R.drawable.ic_edit_dark)
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			}
			else
			{
				menu.add(Menu.NONE, R.id.menu_post, Menu.NONE, "Post").setIcon(AttrUtil.getResourceId(getActivity(), R.attr.editIcon))
						.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			}
		}

		// Does not work at the moment

		// menu.add(Menu.NONE, R.id.menu_add_friend, Menu.NONE, "Add Friend").setIcon(AttrUtil.getResourceId(getActivity(), R.attr.addFriendIcon))
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_post)
		{
			handleNewPostClick();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		super.populate(data);

		if (getAdapter().getCount() > 1)
			setOffset(((Stream) getAdapter().getLastItem()).getCreated_time());
		else
			setNoMoreData(true);
	}

	@Override
	protected String getNewestOffset(GraphObject graphObject)
	{
		Stream stream = (Stream) graphObject;
		return stream.getCreated_time();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		GraphObject object = (GraphObject) l.getItemAtPosition(position);

		if (object instanceof Stream)
		{
			Stream stream = (Stream) object;

			if (stream.isSelectable(0) == true)
			{
				startActivityForResult(Klyph.getIntentForGraphObject(getActivity(), stream), STREAM_CODE);
			}
		}
	}
}
