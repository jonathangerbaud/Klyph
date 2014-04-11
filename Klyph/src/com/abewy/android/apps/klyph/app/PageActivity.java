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
import com.abewy.android.apps.klyph.core.fql.Page;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.imageloader.SimpleListener;
import com.abewy.android.apps.klyph.fragment.ElementAlbums;
import com.abewy.android.apps.klyph.fragment.ElementEvents;
import com.abewy.android.apps.klyph.fragment.IKlyphFragment;
import com.abewy.android.apps.klyph.fragment.KlyphFakeHeaderGridFragment;
import com.abewy.android.apps.klyph.fragment.KlyphFakeHeaderListFragment;
import com.abewy.android.apps.klyph.fragment.PageAbout;
import com.abewy.android.apps.klyph.fragment.PageTimeline;
import com.abewy.android.apps.klyph.fragment.Pages;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.util.KlyphUtil;
import com.abewy.android.apps.klyph.widget.coverImage.UserCoverImageView;
import com.viewpagerindicator.PageIndicator;

public class PageActivity extends ProfileActivity
{
	private TabsAdapter					adapter;

	// Newsstand effect
	private TextView					headerName;
	private int							listGridHeaderHeight;
	private Page						page;
	
	@Override
	protected float getRatio()
	{
		return UserCoverImageView.RATIO;
	}
	
	@Override
	protected int getQuery()
	{
		return Query.PAGE_PROFILE;
	}
	

	@Override
	protected String getQueryParam()
	{
		return String.valueOf((int) (KlyphDevice.getDeviceDensity() * 96));
	}
	
	@Override
	protected String getBundleIdParameter()
	{
		return KlyphBundleExtras.PAGE_ID;
	}
	
	@Override
	protected String getBundleNameParameter()
	{
		return KlyphBundleExtras.PAGE_NAME;
	}
	
	@Override
	protected void initComponents()
	{
		headerName = (TextView) findViewById(R.id.header_name);
	}
	
	@Override
	protected boolean hasCachedData(Bundle savedInstanceState)
	{
		return savedInstanceState != null && savedInstanceState.getParcelable("page") != null;
	}
	
	@Override
	protected List<GraphObject> getCachedDataFromInstanceState(Bundle savedInstanceState)
	{
		Page page = savedInstanceState.getParcelable("page");
		List<GraphObject> data = new ArrayList<GraphObject>();
		data.add(page);
		return data;
	}
	
	@Override
	protected void initComponentsOnRequestSucces(List<GraphObject> result)
	{
		page = (Page) result.get(0);
		
		headerName.setText(page.getName());

		// ImageLoader.display((ImageView) mHeaderPicture, ImageLoader.FAKE_URI, true, KlyphUtil.getPlaceHolder(this));
		ImageLoader.display(getHeaderPicture(), page.getPic_cover().getSource(), true, R.drawable.cover_place_holder, new SimpleListener() {

			@Override
			public void onSuccess()
			{
				super.onSuccess();
				
				// In case we have on orientation change
				// that would destroy the view
				if (getHeaderPicture() != null)
					((UserCoverImageView) getHeaderPicture()).setOffset(page.getPic_cover().getOffset_y());
			}

		});
		
		ImageLoader.display(getHeaderLogo(), page.getPic(), true, KlyphUtil.getProfilePlaceHolder(this));
		
		((TabsAdapter) getPagerAdapter()).setPage(page);
		((TabsAdapter) getPagerAdapter()).init(listGridHeaderHeight);;
		((TabsAdapter) getPagerAdapter()).setInitialPositionAndShow();

	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_page;
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
		private PageTimeline			timelineFragment;

		public TabsAdapter(Context context, FragmentManager fm, PageIndicator pageIndicator)
		{
			super(fm);

			this.context = context;
			this.pageIndicator = pageIndicator;

			List<String> headerValues = new ArrayList<String>();
			List<String> headerTitles = new ArrayList<String>();
			List<IKlyphFragment> fragmentList = new ArrayList<IKlyphFragment>();

			headerValues.add(context.getString(R.string.fragment_header_about_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_timeline_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_albums_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_pages_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_events_preference_value));

			headerTitles.add(context.getString(R.string.fragment_header_about));
			headerTitles.add(context.getString(R.string.fragment_header_timeline));
			headerTitles.add(context.getString(R.string.fragment_header_albums));
			headerTitles.add(context.getString(R.string.fragment_header_pages));
			headerTitles.add(context.getString(R.string.fragment_header_events));

			fragmentList.add(new PageAbout());
			
			timelineFragment = new PageTimeline();
			fragmentList.add(timelineFragment);
			
			fragmentList.add(new ElementAlbums());
			fragmentList.add(new Pages());
			fragmentList.add(new ElementEvents());

			List<String> tabs = KlyphPreferences.getPageActivityTabs();

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
					((KlyphFakeHeaderGridFragment) fragment).setOnScrollListener((PageActivity) context);
				}
				else if (fragment instanceof KlyphFakeHeaderListFragment)
				{
					((KlyphFakeHeaderListFragment) fragment).setFakeHeader(true);
					((KlyphFakeHeaderListFragment) fragment).setFakeHeaderHeight(fakeHeaderHeight);
					((KlyphFakeHeaderListFragment) fragment).setOnScrollListener((PageActivity) context);
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

		public void setPage(Page page)
		{
			this.id = page.getPage_id();
			
			timelineFragment.setPage(page);
			
			for (IKlyphFragment fragment : fragments)
			{
				fragment.setElementId(id);
			}
		}

		@Override
		public int getCount()
		{
			if (titles != null)
				return titles.size();
			
			return 0;
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
		outState.putParcelable("page", page);
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
