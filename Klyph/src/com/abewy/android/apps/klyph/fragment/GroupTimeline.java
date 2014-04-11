package com.abewy.android.apps.klyph.fragment;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.app.PostActivity;
import com.abewy.android.apps.klyph.core.fql.Group;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class GroupTimeline extends UserTimeline
{
	private Group group;
		
	public GroupTimeline()
	{
		setRequestType(Query.GROUP_TIMELINE);
		setNewestRequestType(Query.GROUP_TIMELINE);
	}
	
	public void setGroup(Group group)
	{
		this.group = group;
		
		if (getActivity() != null)
			getActivity().invalidateOptionsMenu();
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		setRequestType(Query.GROUP_TIMELINE);
		setNewestRequestType(Query.GROUP_TIMELINE);
		
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

		if (group != null && menu.findItem(R.id.menu_share) == null)
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
			//Group group = (Group) getElement();

			Intent intent = new Intent(getActivity(), PostActivity.class);
			intent.putExtra(KlyphBundleExtras.SHARE, true);
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_URL, "http://www.facebook.com/groups/" + group.getGid());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_IMAGE_URL, group.getPic_big());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_NAME, group.getName());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_DESC, group.getDescription());

			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected boolean  canPost()
	{
		return true;
	}
	
	@Override
	protected String getIntentParam()
	{
		return KlyphBundleExtras.GROUP_ID;
	}
}
