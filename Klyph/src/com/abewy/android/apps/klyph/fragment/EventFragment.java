package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.AdapterSelector;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.NewsfeedAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.adapter.animation.GoogleCardStyleAdapter;
import com.abewy.android.apps.klyph.app.PostActivity;
import com.abewy.android.apps.klyph.core.fql.Event;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Event.EventResponse;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.facebook.IFbPermissionCallback;
import com.abewy.android.apps.klyph.facebook.IFbPermissionWorker;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.facebook.Session;

public class EventFragment extends KlyphFragment2 implements IFbPermissionCallback
{
	private static final List<String>	PERMISSIONS		= Arrays.asList("rsvp_event");

	private Event						event;
	private OnClickListener				attendButtonListener;
	private OnClickListener				unsureButtonListener;
	private OnClickListener				declinedButtonListener;

	private OnClickListener				invitedGuestListener;
	private OnClickListener				goingGuestListener;
	private OnClickListener				unsureGuestListener;
	private OnClickListener				declinedGuestListener;

	private boolean						pendingAnnounce	= false;
	private int							pendingRequest;

	public EventFragment()
	{
		super();
		setRequestType(Query.EVENT_TIMELINE);
	}

	@Override
	protected int getCustomLayout()
	{
		return R.layout.grid_timeline;
	}

	@Override
	protected boolean updateNumColumnOnOrientationChange()
	{
		return false;
	}

	@Override
	protected int getNumColumn()
	{
		return 1;
	}

	@Override
	public void onPermissionsChange()
	{
		if (pendingAnnounce)
		{
			handleResponseClick(pendingRequest);
		}
	}

	@Override
	public void onCancelPermissions()
	{

	}

	private void requestPublishPermissions(Session session)
	{
		((IFbPermissionWorker) getActivity()).requestPublishPermissions(this, PERMISSIONS);
	}

	private void handleResponseClick(int request)
	{
		pendingAnnounce = false;
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingAnnounce = true;
			pendingRequest = request;
			requestPublishPermissions(session);
			return;
		}

		sendRequest(request);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{

		defineEmptyText(R.string.empty_list_no_data);

		setListVisible(false);

		attendButtonListener = new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (!event.isUserAttendingEvent())
				{
					handleResponseClick(Query.POST_EVENT_ATTEND);
				}

			}
		};

		unsureButtonListener = new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (!event.isUserUnsureEvent())
				{
					handleResponseClick(Query.POST_EVENT_UNSURE);
				}
			}
		};
		
		declinedButtonListener = new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (!event.isUserDeclinedEvent())
				{
					handleResponseClick(Query.POST_EVENT_DECLINE);
				}
			}
		};

		invitedGuestListener = new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				UserListDialog dialog = new UserListDialog();
				dialog.setCustomTitle(getString(R.string.event_num_invitee, event.getAll_members_count()));
				dialog.setRequestType(Query.EVENT_INVITED);
				dialog.setElementId(event.getEid());
				dialog.show(getFragmentManager(), "");
				dialog.load();
			}
		};

		goingGuestListener = new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				UserListDialog dialog = new UserListDialog();
				dialog.setCustomTitle(getString(R.string.event_num_attending, event.getAttending_count()));
				dialog.setRequestType(Query.EVENT_GOING);
				dialog.setElementId(event.getEid());
				dialog.show(getFragmentManager(), "");
				dialog.load();
			}
		};

		unsureGuestListener = new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				UserListDialog dialog = new UserListDialog();
				dialog.setCustomTitle(getString(R.string.event_num_unsure, event.getUnsure_count()));
				dialog.setRequestType(Query.EVENT_MAYBE);
				dialog.setElementId(event.getEid());
				dialog.show(getFragmentManager(), "");
				dialog.load();
			}
		};

		declinedGuestListener = new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				UserListDialog dialog = new UserListDialog();
				dialog.setCustomTitle(getString(R.string.event_num_declined, event.getDeclined_count()));
				dialog.setRequestType(Query.EVENT_DECLINED);
				dialog.setElementId(event.getEid());
				dialog.show(getFragmentManager(), "");
				dialog.load();
			}
		};

		setRequestType(Query.EVENT_TIMELINE);
		setNewestRequestType(Query.EVENT_TIMELINE);

		super.onViewCreated(view, savedInstanceState);

		MultiObjectAdapter adapter = /*KlyphPreferences.areBannerAdsEnabled() ? new NewsfeedAdapter(getListView(), SpecialLayout.EVENT_ABOUT)
				: */new MultiObjectAdapter(getListView(), SpecialLayout.EVENT_ABOUT);

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
	protected void populate(List<GraphObject> data)
	{
		Log.d("EventFragment", "populate: " + data.size());
		if (data.size() > 0)
		{
			GraphObject o = data.get(0);

			if (o instanceof Event)
			{
				event = (Event) o;

				data.remove(0);

				if (getActivity() != null)
					getActivity().setTitle(event.getName());

				List<GraphObject> list = new ArrayList<GraphObject>();

				list.add(event);

				EventData eventData = new EventData(event);
				list.add(eventData);

				EventResponseItem responseItem = new EventResponseItem(event);
				responseItem.setAttendButtonListener(attendButtonListener);
				responseItem.setUnsureButtonListener(unsureButtonListener);
				responseItem.setDeclineButtonListener(declinedButtonListener);
				list.add(responseItem);

				EventAttendees attendeesItem = new EventAttendees(event);
				attendeesItem.setInvitedListener(invitedGuestListener);
				attendeesItem.setGoingListener(goingGuestListener);
				attendeesItem.setUnsureListener(unsureGuestListener);
				attendeesItem.setDeclinedListener(declinedGuestListener);
				list.add(attendeesItem);

				list.addAll(data);
				data = list;

				getActivity().invalidateOptionsMenu();
			}
		}

		super.populate(data);
		setNoMoreData(true);
	}

	@Override
	protected void populateNewest(List<GraphObject> data)
	{
		getAdapter().clear();
		populate(data);

	}

	private void sendRequest(int query)
	{
		final EventResponse eventResponse;

		switch (query)
		{
			case Query.POST_EVENT_ATTEND:
			{
				eventResponse = EventResponse.ATTENDING;
				break;
			}
			case Query.POST_EVENT_UNSURE:
			{
				eventResponse = EventResponse.UNSURE;
				break;
			}
			case Query.POST_EVENT_DECLINE:
			{
				eventResponse = EventResponse.DECLINED;
				break;
			}
			default:
			{
				eventResponse = EventResponse.NOT_REPLIED;
				break;
			}
		}

		final AlertDialog publishing = new AlertDialog.Builder(getActivity()).setTitle(R.string.event_response_dialog_title)
				.setMessage(R.string.event_response_dialog_message).setCancelable(false).create();
		publishing.show();

		new AsyncRequest(query, event.getEid(), "", new AsyncRequest.Callback() {

			@Override
			public void onComplete(Response response)
			{
				publishing.dismiss();

				onRequestComplete(response, eventResponse);
			}
		}).execute();
	}

	private void onRequestComplete(final Response response, final EventResponse eventResponse)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onEventRequestSuccess(response.getGraphObjectList(), eventResponse);
					}
					else
					{
						onEventRequestError(response.getError());
					}
				}
			});
		}
	}

	private void onEventRequestSuccess(List<GraphObject> result, EventResponse eventResponse)
	{
		event.setUserResponse(eventResponse.toString().toLowerCase());
		getAdapter().notifyDataSetChanged();
		Toast.makeText(getActivity(), R.string.event_response_ok, Toast.LENGTH_SHORT).show();
	}

	private void onEventRequestError(RequestError error)
	{
		new AlertDialog.Builder(getActivity()).setTitle(R.string.error).setMessage(R.string.event_response_error)
				.setPositiveButton(R.string.ok, null).create().show();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

		if (event != null && menu.findItem(R.id.menu_share) == null)
		{
			menu.add(Menu.NONE, R.id.menu_share, 3, getString(R.string.share)).setIcon(AttrUtil.getResourceId(getActivity(), R.attr.shareIcon))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_share)
		{
			Intent intent = new Intent(getActivity(), PostActivity.class);
			intent.putExtra(KlyphBundleExtras.SHARE, true);
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_URL, "http://www.facebook.com/events/" + event.getEid());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_IMAGE_URL, event.getPic());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_NAME, event.getName());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_DESC, event.getDescription());

			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onGridItemClick(KlyphGridView gridView, View view, int position, long id)
	{
		GraphObject o = (GraphObject) gridView.getItemAtPosition(position);

		if (o instanceof Stream)
		{
			Stream stream = (Stream) o;
			startActivity(Klyph.getIntentForGraphObject(getActivity(), stream));
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		event = null;
		attendButtonListener = null;
		unsureButtonListener = null;
		declinedButtonListener = null;
		invitedGuestListener = null;
		goingGuestListener = null;
		unsureGuestListener = null;
		declinedGuestListener = null;
		pendingRequest = Query.NONE;
	}

	public static class EventData extends GraphObject
	{
		private final Event	event;

		public EventData(Event event)
		{
			this.event = event;
		}

		@Override
		public int getItemViewType()
		{
			return AdapterSelector.EVENT_DATA;
		}

		public Event getEvent()
		{
			return event;
		}
	}

	public static class EventResponseItem extends GraphObject
	{
		private final Event		event;
		private OnClickListener	attendButtonListener;
		private OnClickListener	unsureButtonListener;
		private OnClickListener	declineButtonListener;

		public EventResponseItem(Event event)
		{
			this.event = event;
		}

		public int getItemViewType()
		{
			return AdapterSelector.EVENT_RESPONSE;
		}

		public Event getEvent()
		{
			return event;
		}

		public OnClickListener getAttendButtonListener()
		{
			return attendButtonListener;
		}

		public void setAttendButtonListener(OnClickListener attendButtonListener)
		{
			this.attendButtonListener = attendButtonListener;
		}

		public OnClickListener getUnsureButtonListener()
		{
			return unsureButtonListener;
		}

		public void setUnsureButtonListener(OnClickListener unsureButtonListener)
		{
			this.unsureButtonListener = unsureButtonListener;
		}

		public OnClickListener getDeclineButtonListener()
		{
			return declineButtonListener;
		}

		public void setDeclineButtonListener(OnClickListener declineButtonListener)
		{
			this.declineButtonListener = declineButtonListener;
		}
	}

	public static class EventAttendees extends GraphObject
	{
		private final Event		event;
		private OnClickListener	invitedListener;
		private OnClickListener	goingListener;
		private OnClickListener	unsureListener;
		private OnClickListener	declinedListener;

		public EventAttendees(Event event)
		{
			this.event = event;
		}

		public int getItemViewType()
		{
			return AdapterSelector.EVENT_ATTENDEES;
		}

		public Event getEvent()
		{
			return event;
		}

		public OnClickListener getInvitedListener()
		{
			return invitedListener;
		}

		public void setInvitedListener(OnClickListener invitedListener)
		{
			this.invitedListener = invitedListener;
		}

		public OnClickListener getGoingListener()
		{
			return goingListener;
		}

		public void setGoingListener(OnClickListener goingListener)
		{
			this.goingListener = goingListener;
		}

		public OnClickListener getUnsureListener()
		{
			return unsureListener;
		}

		public void setUnsureListener(OnClickListener unsureListener)
		{
			this.unsureListener = unsureListener;
		}

		public OnClickListener getDeclinedListener()
		{
			return declinedListener;
		}

		public void setDeclinedListener(OnClickListener declinedListener)
		{
			this.declinedListener = declinedListener;
		}
	}
}
