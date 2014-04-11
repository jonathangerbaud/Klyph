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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.FriendRequest;
import com.abewy.android.apps.klyph.core.fql.User;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.imageloader.SimpleListener;
import com.abewy.android.apps.klyph.fragment.ElementAlbums;
import com.abewy.android.apps.klyph.fragment.ElementEvents;
import com.abewy.android.apps.klyph.fragment.IKlyphFragment;
import com.abewy.android.apps.klyph.fragment.KlyphFakeHeaderGridFragment;
import com.abewy.android.apps.klyph.fragment.KlyphFakeHeaderListFragment;
import com.abewy.android.apps.klyph.fragment.Pages;
import com.abewy.android.apps.klyph.fragment.UserAbout;
import com.abewy.android.apps.klyph.fragment.UserTimeline;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.util.KlyphUtil;
import com.abewy.android.apps.klyph.util.facebook.DialogError;
import com.abewy.android.apps.klyph.util.facebook.FacebookError;
import com.abewy.android.apps.klyph.util.facebook.FbDialog;
import com.abewy.android.apps.klyph.util.facebook.Util;
import com.abewy.android.apps.klyph.widget.coverImage.UserCoverImageView;
import com.facebook.Session;
import com.viewpagerindicator.PageIndicator;

public class UserActivity extends ProfileActivity
{
	private static final String	ADD_FRIEND					= "friends/";
	private static final String	USER						= "user";
	private static final String	HAS_RECEIVED_FRIEND_REQUEST	= "hasReceivedFriendRequest";
	private static final String	HAS_SENT_FRIEND_REQUEST		= "hasSentFriendRequest";

	private TabsAdapter			adapter;

	// Newsstand effect
	private TextView			headerName;
	private Button				headerButton;
	private int					listGridHeaderHeight;
	private User				user;

	private boolean				hasReceivedFriendRequest	= false;
	private boolean				hasSentFriendRequest		= false;

	private OnClickListener		friendButtonListener		= new OnClickListener() {

																@Override
																public void onClick(View v)
																{
																	handleAddFriend();
																}
															};

	@Override
	protected float getRatio()
	{
		return UserCoverImageView.RATIO;
	}

	@Override
	protected int getQuery()
	{
		return Query.USER_PROFILE;
	}

	@Override
	protected String getQueryParam()
	{
		return String.valueOf((int) (KlyphDevice.getDeviceDensity() * 96));
	}

	@Override
	protected String getBundleIdParameter()
	{
		return KlyphBundleExtras.USER_ID;
	}

	@Override
	protected String getBundleNameParameter()
	{
		return KlyphBundleExtras.USER_NAME;
	}

	@Override
	protected void initComponents()
	{
		headerName = (TextView) findViewById(R.id.header_name);
		headerButton = (Button) findViewById(R.id.header_button);
		
		if (getElementId().equals(KlyphSession.getSessionUserId()))
		{
			headerButton.setVisibility(View.GONE);
		}
	}

	@Override
	protected boolean hasCachedData(Bundle savedInstanceState)
	{
		return savedInstanceState != null && savedInstanceState.getParcelable("user") != null;
	}

	@Override
	protected List<GraphObject> getCachedDataFromInstanceState(Bundle savedInstanceState)
	{
		User user = savedInstanceState.getParcelable(USER);
		hasReceivedFriendRequest = savedInstanceState.getBoolean(HAS_RECEIVED_FRIEND_REQUEST);
		hasSentFriendRequest = savedInstanceState.getBoolean(HAS_SENT_FRIEND_REQUEST);
		List<GraphObject> data = new ArrayList<GraphObject>();
		data.add(user);
		return data;
	}

	@Override
	protected void initComponentsOnRequestSucces(List<GraphObject> result)
	{
		user = (User) result.get(0);

		if (result.size() > 1)
		{
			FriendRequest fr = (FriendRequest) result.get(1);
			setFriendButtonState(user.isFriend(), fr.getUid_to().equals(KlyphSession.getSessionUserId()),
					fr.getUid_from().equals(KlyphSession.getSessionUserId()));
		}
		else
		{
			setFriendButtonState(user.isFriend(), hasReceivedFriendRequest, hasSentFriendRequest);
		}

		headerName.setText(user.getName());

		// ImageLoader.display((ImageView) mHeaderPicture, ImageLoader.FAKE_URI, true, KlyphUtil.getPlaceHolder(this));
		ImageLoader.display(getHeaderPicture(), user.getPic_cover().getSource(), true, R.drawable.cover_place_holder, new SimpleListener() {

			@Override
			public void onSuccess()
			{
				super.onSuccess();
				
				// In case we have on orientation change
				// that would destroy the view
				if (getHeaderPicture() != null)
					((UserCoverImageView) getHeaderPicture()).setOffset(user.getPic_cover().getOffset_y());
			}

		});

		ImageLoader.display(getHeaderLogo(), user.getPic(), true, KlyphUtil.getProfilePlaceHolder(this));

		Log.d("UserActivity", "initComponentsOnRequestSucces: " + listGridHeaderHeight);

		((TabsAdapter) getPagerAdapter()).setUser(user);
		((TabsAdapter) getPagerAdapter()).init(listGridHeaderHeight);;
		((TabsAdapter) getPagerAdapter()).setInitialPositionAndShow();

	}

	private void setFriendButtonState(boolean isFriend, boolean hasReceivedFriendRequest, boolean hasSentFriendRequest)
	{
		this.hasReceivedFriendRequest = hasReceivedFriendRequest;
		this.hasSentFriendRequest = hasSentFriendRequest;

		if (!isFriend && hasReceivedFriendRequest)
		{
			// FriendRequest fr = (FriendRequest) result.get(1);
			headerButton.setText(R.string.confirm_friend_request);
			headerButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			headerButton.setOnClickListener(friendButtonListener);
		}
		else if (!isFriend && hasSentFriendRequest)
		{
			headerButton.setText(R.string.friend_request_sent);
			headerButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			headerButton.setOnClickListener(null);
		}
		else if (!isFriend)
		{
			headerButton.setText(R.string.send_friend_request);
			headerButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			headerButton.setOnClickListener(friendButtonListener);
		}
		else
		{
			headerButton.setText(getString(R.string.is_friend));
			headerButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cab_done_holo_dark, 0, 0, 0);
			headerButton.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.dip_8));
			headerButton.setOnClickListener(null);
		}
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_user;
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
		private int						currentPosition;
		private UserTimeline			timelineFragment;

		public TabsAdapter(Context context, FragmentManager fm, PageIndicator pageIndicator)
		{
			super(fm);

			this.context = context;
			this.pageIndicator = pageIndicator;

			fragments = new ArrayList<IKlyphFragment>();
			titles = new ArrayList<String>();

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
			
			timelineFragment = new UserTimeline();
			fragmentList.add(timelineFragment);
			
			fragmentList.add(new ElementAlbums());
			fragmentList.add(new Pages());

			List<String> tabs = KlyphPreferences.getUserActivityTabs();

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
					((KlyphFakeHeaderGridFragment) fragment).setOnScrollListener((UserActivity) context);
				}
				else if (fragment instanceof KlyphFakeHeaderListFragment)
				{
					((KlyphFakeHeaderListFragment) fragment).setFakeHeader(true);
					((KlyphFakeHeaderListFragment) fragment).setFakeHeaderHeight(fakeHeaderHeight);
					((KlyphFakeHeaderListFragment) fragment).setOnScrollListener((UserActivity) context);
				}
			}
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
			currentPosition = position;

			notifyDataSetChanged();

			onPageSelected(position);
			// ((KlyphFragment) getItem(2)).setElementId(id);
			// ((KlyphFragment) getItem(2)).load();
		}
		
		public void setUser(User user)
		{
			this.id = user.getUid();
			
			timelineFragment.setUser(user);
			
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

		public int getCurrentPosition()
		{
			return currentPosition;
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
			currentPosition = position;
			IKlyphFragment fragment = fragments.get(position);
			if (id != null)
			{
				fragment.load();

				if (previousFragment != null)
					previousFragment.onSetToBack((Activity) context);

				fragment.onSetToFront((Activity) context);
				previousFragment = fragment;
			}
			// fragment.setOnScrollListener((UserActivity) context);
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
		outState.putParcelable(USER, user);
		outState.putBoolean(HAS_RECEIVED_FRIEND_REQUEST, hasReceivedFriendRequest);
		outState.putBoolean(HAS_SENT_FRIEND_REQUEST, hasSentFriendRequest);
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

	private void handleAddFriend()
	{
		Bundle parameters = new Bundle();
		String endpoint = FbDialog.DIALOG_BASE_URL + ADD_FRIEND;
		parameters.putString("id", user.getUid());
		parameters.putString("redirect_uri", FbDialog.REDIRECT_URI);
		parameters.putString("app_id", Session.getActiveSession().getApplicationId());
		parameters.putString("display", "popup");

		String url = endpoint + "?" + Util.encodeUrl(parameters);

		new FbDialog(this, url, new FbDialog.DialogListener() {

			@Override
			public void onFacebookError(FacebookError e)
			{
				Log.d("UserTimeline", "FbDialog onFacebookError: " + e);
			}

			@Override
			public void onError(DialogError e)
			{
				Log.d("UserTimeline", "FbDialog onError: " + e);
			}

			@Override
			public void onComplete(Bundle values)
			{
				if (values != null)
				{
					String result = values.getString("action");

					if (result != null && result.equals("1"))
					{
						user.setIsFriend(hasReceivedFriendRequest);
						setFriendButtonState(hasReceivedFriendRequest, hasReceivedFriendRequest, hasReceivedFriendRequest ? false : true);
					}
					else if (result != null && result.equals("0"))
					{
						user.setIsFriend(false);
						setFriendButtonState(false, false, false);
					}
				}
				Log.d("UserTimeline", "FbDialog onComplete: " + values.getString("action"));
				Log.d("UserTimeline", "FbDialog onComplete: " + values);
			}

			@Override
			public void onCancel()
			{
				Log.d("UserTimeline", "FbDialog onCancel: ");
			}
		}).show();
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
