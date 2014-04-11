package com.abewy.android.apps.klyph.fragment;

import java.util.List;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.app.PostActivity;
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class PageTimeline extends UserTimeline implements OnNavigationListener
{
	private Page page;
	private int	spinnerPosition	= 0;

	public PageTimeline()
	{
		setRequestType(Query.PAGE_TIMELINE_FEED);
		setNewestRequestType(Query.PAGE_TIMELINE_FEED);
	}
	
	public void setPage(Page page)
	{
		this.page = page;
		
		if (getActivity() != null)
			getActivity().invalidateOptionsMenu();
	}

	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		setRequestType(Query.PAGE_TIMELINE_FEED);
		setNewestRequestType(Query.PAGE_TIMELINE_FEED);

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

		// Does not work at the moment, must be on Fb white list

		/*if (getElement() != null && menu.findItem(R.id.menu_like) == null)
		{
			Page page = (Page) getElement();

			int iconId = page.getIs_fan() == true ? R.attr.userLikeIcon : R.attr.likeIcon;
			iconId = AttrUtil.getResourceId(getActivity(), iconId);

			menu.add(Menu.NONE, R.id.menu_like, Menu.NONE, R.string.like).setIcon(iconId).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}*/

		if (page != null && menu.findItem(R.id.menu_share) == null)
		{
			menu.add(Menu.NONE, R.id.menu_share, 3, getString(R.string.share)).setIcon(R.drawable.ic_share_dark)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
	}

	@Override
	public boolean  onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_share)
		{
			//Page page = (Page) getElement();

			Intent intent = new Intent(getActivity(), PostActivity.class);
			intent.putExtra(KlyphBundleExtras.SHARE, true);
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_URL, "http://www.facebook.com/" + page.getUsername());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_IMAGE_URL, page.getPic_large());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_NAME, page.getName());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_DESC, page.getAbout());

			startActivity(intent);
			return true;
		}
		/*else if (item.getItemId() == R.id.menu_like)
		{
			doLikeAction();
			return true;
		}*/

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean  canPost()
	{
		return page != null && page.getCan_post();
	}

	@Override
	protected String getIntentParam()
	{
		return KlyphBundleExtras.PAGE_ID;
	}
	
	private void doLikeAction()
	{
		final Page page = (Page) getElement();

		if (page.getIs_fan() == false)
		{
			page.setIs_fan(true);

			((FragmentActivity) getActivity()).invalidateOptionsMenu();

			new AsyncRequest(Query.POST_LIKE, page.getPage_id(), "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					Log.i("onComplete", "" + response.getError());
					onLikeRequestComplete(response);
				}
			}).execute();
		}
		else
		{
			page.setIs_fan(false);

			((FragmentActivity) getActivity()).invalidateOptionsMenu();

			new AsyncRequest(Query.POST_UNLIKE, page.getPage_id(), "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					Log.i("onComplete", "" + response.getError());
					onUnlikeRequestComplete(response);
				}
			}).execute();
		}
	}
	
	private void onLikeRequestComplete(final Response response)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onLikeRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						//onLikeRequestError(response.getError());
					}
					
					((FragmentActivity) getActivity()).invalidateOptionsMenu();
				}
			});
		}
	}
	
	private void onLikeRequestSuccess(List<GraphObject> results)
	{
		Toast.makeText(getActivity(), R.string.like_error, Toast.LENGTH_SHORT).show();
		((Page) getElement()).setIs_fan(false);
	}
	
	private void onUnlikeRequestComplete(final Response response)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onUnlikeRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						//onUnlikeRequestError(response.getError());
					}
					
					((FragmentActivity) getActivity()).invalidateOptionsMenu();
				}
			});
		}
	}
	
	private void onUnlikeRequestSuccess(List<GraphObject> results)
	{
		Toast.makeText(getActivity(), R.string.unlike_error, Toast.LENGTH_SHORT).show();
		((Page) getElement()).setIs_fan(true);
		getAdapter().notifyDataSetChanged();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId)
	{
		if (spinnerPosition != itemPosition)
		{
			spinnerPosition = itemPosition;

			if (spinnerPosition == 0)
			{
				setRequestType(Query.PAGE_TIMELINE_FEED);
				setNewestRequestType(Query.PAGE_TIMELINE_FEED);
			}
			else
			{
				setRequestType(Query.PAGE_TIMELINE);
				setNewestRequestType(Query.PAGE_TIMELINE);
			}

			clearAndRefresh();

			return true;
		}
		return false;
	}

	/*@Override
	public void onSetToFront(Activity activity)
	{
		((IActionbarSpinner) activity).displaySpinnerInActionBar(R.array.user_feed_list, spinnerPosition, this);
	}

	@Override
	public void onSetToBack(Activity activity)
	{
		((IActionbarSpinner) activity).removeSpinnerInActionBar();
	}*/
}