package com.abewy.android.apps.klyph.app;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.fql.Group;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.imageloader.SimpleListener;
import com.abewy.android.apps.klyph.fragment.ElementEvents;
import com.abewy.android.apps.klyph.fragment.GroupMembers;
import com.abewy.android.apps.klyph.fragment.GroupPhotos;
import com.abewy.android.apps.klyph.fragment.GroupTimeline;
import com.abewy.android.apps.klyph.fragment.IKlyphFragment;
import com.abewy.android.apps.klyph.fragment.KlyphFakeHeaderGridFragment;
import com.abewy.android.apps.klyph.fragment.KlyphFakeHeaderListFragment;
import com.abewy.android.apps.klyph.fragment.PageTimeline;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.coverImage.GroupCoverImageView;
import com.viewpagerindicator.PageIndicator;

public class GroupActivity extends ProfileActivity
{
	private TabsAdapter					adapter;

	// Newsstand effect
	private TextView					headerName;
	private int							listGridHeaderHeight;
	private Group						group;
	
	@Override
	protected float getRatio()
	{
		return GroupCoverImageView.RATIO;
	}
	
	@Override
	protected int getQuery()
	{
		return Query.GROUP_PROFILE;
	}
	

	@Override
	protected String getQueryParam()
	{
		return String.valueOf((int) (KlyphDevice.getDeviceDensity() * 96));
	}
	
	@Override
	protected String getBundleIdParameter()
	{
		return KlyphBundleExtras.GROUP_ID;
	}
	
	@Override
	protected String getBundleNameParameter()
	{
		return KlyphBundleExtras.GROUP_NAME;
	}
	
	@Override
	protected void initComponents()
	{
		headerName = (TextView) findViewById(R.id.header_name);
	}
	
	@Override
	protected boolean hasCachedData(Bundle savedInstanceState)
	{
		return savedInstanceState != null && savedInstanceState.getParcelable("group") != null;
	}
	
	@Override
	protected List<GraphObject> getCachedDataFromInstanceState(Bundle savedInstanceState)
	{
		Group group = savedInstanceState.getParcelable("group");
		List<GraphObject> data = new ArrayList<GraphObject>();
		data.add(group);
		return data;
	}
	
	@Override
	protected void initComponentsOnRequestSucces(List<GraphObject> result)
	{
		group = (Group) result.get(0);
		
		headerName.setText(group.getName());

		// ImageLoader.display((ImageView) mHeaderPicture, ImageLoader.FAKE_URI, true, KlyphUtil.getPlaceHolder(this));
		ImageLoader.display(getHeaderPicture(), group.getPic_cover().getSource(), true, R.drawable.cover_place_holder, new SimpleListener() {

			@Override
			public void onSuccess()
			{
				super.onSuccess();
				
				// In case we have on orientation change
				// that would destroy the view
				if (getHeaderPicture() != null)
					((GroupCoverImageView) getHeaderPicture()).setOffset(group.getPic_cover().getOffset_y());
			}

		});
		
		((TabsAdapter) getPagerAdapter()).setGroup(group);
		((TabsAdapter) getPagerAdapter()).init(listGridHeaderHeight);
		((TabsAdapter) getPagerAdapter()).setInitialPositionAndShow();

	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_group;
	}

	@Override
	protected FragmentPagerAdapter getPagerAdapter()
	{
		if (adapter == null)
			adapter = new TabsAdapter(this, getFragmentManager(), getPageIndicator());

		return adapter;
	}

	public static class TabsAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener
	{
		private Context					context;
		private List<IKlyphFragment>	fragments;
		private List<String>			titles;
		private PageIndicator			pageIndicator;
		private String					id;
		private IKlyphFragment			previousFragment;
		private GroupTimeline			timelineFragment;

		public TabsAdapter(Context context, FragmentManager fm, PageIndicator pageIndicator)
		{
			super(fm);

			this.context = context;
			this.pageIndicator = pageIndicator;

			List<String> headerValues = new ArrayList<String>();
			List<String> headerTitles = new ArrayList<String>();
			List<IKlyphFragment> fragmentList = new ArrayList<IKlyphFragment>();

			headerValues.add(context.getString(R.string.fragment_header_events_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_members_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_timeline_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_photos_preference_value));

			headerTitles.add(context.getString(R.string.fragment_header_events));
			headerTitles.add(context.getString(R.string.fragment_header_members));
			headerTitles.add(context.getString(R.string.fragment_header_timeline));
			headerTitles.add(context.getString(R.string.fragment_header_photos));

			fragmentList.add(new ElementEvents());
			fragmentList.add(new GroupMembers());
			
			timelineFragment = new GroupTimeline(); 
			fragmentList.add(timelineFragment);
			fragmentList.add(new GroupPhotos());

			List<String> tabs = KlyphPreferences.getGroupActivityTabs();

			fragments = new ArrayList<IKlyphFragment>();
			titles = new ArrayList<String>();

			int n = tabs.size();
			for (int i = 0; i < n; i++)
			{
				String tab = tabs.get(i);

				int m = headerValues.size();
				for (int j = 0; j < m; j++)
				{
					String value = headerValues.get(j);

					if (tab.equals(value))
					{
						IKlyphFragment fragment = fragmentList.get(j);

						fragment.setAutoLoad(false);

						fragments.add(fragment);
						titles.add(headerTitles.get(j));
					}
				}
			}

			pageIndicator.setOnPageChangeListener(this);
		}
		
		public void init(int fakeHeaderHeight)
		{
			for (IKlyphFragment fragment : fragments)
			{
				if (fragment instanceof KlyphFakeHeaderGridFragment)
				{
					((KlyphFakeHeaderGridFragment) fragment).setFakeHeaderHeight(fakeHeaderHeight);
					((KlyphFakeHeaderGridFragment) fragment).setOnScrollListener((GroupActivity) context);
				}
				else if (fragment instanceof KlyphFakeHeaderListFragment)
				{
					((KlyphFakeHeaderListFragment) fragment).setFakeHeader(true);
					((KlyphFakeHeaderListFragment) fragment).setFakeHeaderHeight(fakeHeaderHeight);
					((KlyphFakeHeaderListFragment) fragment).setOnScrollListener((GroupActivity) context);
				}
			}
		}

		public void setInitialPositionAndShow()
		{
			int position = -1;

			for (IKlyphFragment fragment : fragments)
			{
				if (fragment instanceof PageTimeline)
					position = fragments.indexOf(fragment);
			}

			if (position == -1)
				position = Math.round(fragments.size() / 2);

			pageIndicator.setCurrentItem(position);

			notifyDataSetChanged();

			onPageSelected(position);
		}

		public void setGroup(Group group)
		{
			this.id = group.getGid();
			
			timelineFragment.setGroup(group);
			
			for (IKlyphFragment fragment : fragments)
			{
				fragment.setElementId(id);
			}
		}

		@Override
		public int getCount()
		{
			return titles.size();
		}

		@Override
		public Fragment getItem(int position)
		{
			return (Fragment) fragments.get(position);
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return titles.get(position).toUpperCase();
		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		@Override
		public void onPageSelected(int position)
		{
			if (id != null)
			{
				IKlyphFragment fragment = fragments.get(position);
				fragment.load();

				if (previousFragment != null)
					previousFragment.onSetToBack((Activity) context);

				fragment.onSetToFront((Activity) context);
				previousFragment = fragment;
			}
		}

		public void destroy()
		{
			context = null;
			fragments = null;
			previousFragment = null;
			titles = null;
			pageIndicator = null;
			timelineFragment = null;
		}
	}
	
	@Override
	protected void saveCachedDataToInstanceState(Bundle outState)
	{
		outState.putParcelable("group", group);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		if (adapter != null)
		{
			adapter.destroy();
			adapter = null;
		}
	}

	@Override
	protected void computeAndSetComponentsHeights()
	{
		super.computeAndSetComponentsHeights();
		
		if (KlyphDevice.isPortraitMode())
		{
			listGridHeaderHeight = getFakeHeaderHeight();
		}
		else
		{
			listGridHeaderHeight = getActionBarHeight() + ((View) getPageIndicator()).getHeight();
		}
	}
}