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
import com.abewy.android.apps.klyph.KlyphApplication;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.fragment.ElementAlbums;
import com.abewy.android.apps.klyph.fragment.ElementEvents;
import com.abewy.android.apps.klyph.fragment.IKlyphFragment;
import com.abewy.android.apps.klyph.fragment.Pages;
import com.abewy.android.apps.klyph.fragment.UserAbout;
import com.abewy.android.apps.klyph.fragment.UserTimeline;
import com.viewpagerindicator.PageIndicator;

public class UserActivity2 extends TitledViewPagerActivity
{
	private TabsAdapter	adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Let the Application class know that the first launch is complete
		// If we come from a notification, then do not show the ads
		// When going back to main activity
		KlyphApplication.getInstance().launchComplete();

		String id = getIntent().getStringExtra(KlyphBundleExtras.USER_ID);
		String name = getIntent().getStringExtra(KlyphBundleExtras.USER_NAME);

		setTitle(name);

		((TabsAdapter) getPagerAdapter()).setId(id);

		showPageIndicator();
		showViewPager();

		((TabsAdapter) getPagerAdapter()).setInitialPositionAndShow();
	}

	@Override
	protected int getInitialPosition()
	{
		return -1;
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_user2;
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
		private String					id;
		private List<IKlyphFragment>	fragments;
		private List<String>			titles;
		private PageIndicator			pageIndicator;
		private IKlyphFragment			previousFragment;

		public TabsAdapter(Context context, FragmentManager fm, PageIndicator pageIndicator)
		{
			super(fm);

			this.context = context;
			this.pageIndicator = pageIndicator;

			List<String> headerValues = new ArrayList<String>();
			List<String> headerTitles = new ArrayList<String>();
			List<IKlyphFragment> fragmentList = new ArrayList<IKlyphFragment>();

			headerValues.add(context.getString(R.string.fragment_header_events_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_about_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_timeline_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_albums_preference_value));
			headerValues.add(context.getString(R.string.fragment_header_pages_preference_value));

			headerTitles.add(context.getString(R.string.fragment_header_events));
			headerTitles.add(context.getString(R.string.fragment_header_about));
			headerTitles.add(context.getString(R.string.fragment_header_timeline));
			headerTitles.add(context.getString(R.string.fragment_header_albums));
			headerTitles.add(context.getString(R.string.fragment_header_pages));

			fragmentList.add(new ElementEvents());
			fragmentList.add(new UserAbout());
			fragmentList.add(new UserTimeline());
			fragmentList.add(new ElementAlbums());
			fragmentList.add(new Pages());

			List<String> tabs = KlyphPreferences.getUserActivityTabs();

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

		public void setInitialPositionAndShow()
		{
			int position = -1;

			for (IKlyphFragment fragment : fragments)
			{
				if (fragment instanceof UserTimeline)
					position = fragments.indexOf(fragment);
			}

			if (position == -1)
				position = Math.round(fragments.size() / 2);

			pageIndicator.setCurrentItem(position);

			notifyDataSetChanged();

			onPageSelected(position);
			// ((KlyphFragment) getItem(2)).setElementId(id);
			// ((KlyphFragment) getItem(2)).load();
		}

		public void setId(String id)
		{
			this.id = id;
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
		}
	}

	protected UserActivity2 getThis()
	{
		return this;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
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
}
