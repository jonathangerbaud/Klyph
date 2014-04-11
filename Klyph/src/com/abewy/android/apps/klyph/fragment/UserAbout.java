package com.abewy.android.apps.klyph.fragment;

import java.util.HashMap;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.fql.User;
import com.abewy.android.apps.klyph.core.fql.User.Education;
import com.abewy.android.apps.klyph.core.fql.User.Relative;
import com.abewy.android.apps.klyph.core.fql.User.Work;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.items.FakeHeaderItem;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.klyph.items.Item;
import com.abewy.klyph.items.Title;
import com.abewy.klyph.items.TitleTextItem;

public class UserAbout extends KlyphFakeHeaderGridFragment
{
	public UserAbout()
	{
		setRequestType(Query.USER_PROFILE);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		defineEmptyText(R.string.empty_list_no_data);

		//getListView().setFooterDividersEnabled(false);
		
		setListVisible(false);

		setRequestType(Query.USER_PROFILE);
		
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	protected void populate(List<GraphObject> data)
	{
		if (data.size() > 0)
		{
			User user = (User) data.get(0);
			data.remove(0);

			if (user.getAbout_me() != null && user.getAbout_me().length() > 0)
			{
				TitleTextItem item = new TitleTextItem();
				item.setTitle(getResources().getString(R.string.user_about));
				item.setText(user.getAbout_me());
				item.setShadow(true);
				data.add(item);
			}

			HashMap<String, String> basicInfo = new HashMap<String, String>();
			basicInfo.put(getResources().getString(R.string.user_birthday), user.getBirthday());
			basicInfo.put(getResources().getString(R.string.user_sex), user.getSex());
			basicInfo.put(getResources().getString(R.string.user_meeting_sex), user.getMeeting_sex());
			basicInfo.put(getResources().getString(R.string.user_relationship_status), user.getRelationship_status());
			basicInfo.put(getResources().getString(R.string.user_religion), user.getReligion());
			basicInfo.put(getResources().getString(R.string.user_political), user.getPolitical());

			addItemsForMap(getResources().getString(R.string.general_infos), basicInfo, data);

			HashMap<String, String> coords = new HashMap<String, String>();
			// coords.put(getResources().getString(R.string.user_phones), user.get);
			coords.put(getResources().getString(R.string.user_address), user.getCurrent_location().getName());
			coords.put(getResources().getString(R.string.user_email), user.getEmail());
			// coords.put(getResources().getString(R.string.user_networks),
			// user.getNetwork());
			coords.put(getResources().getString(R.string.user_website), user.getWebsite());

			addItemsForMap(getResources().getString(R.string.coordinates), coords, data);

			HashMap<String, String> others = new HashMap<String, String>();
			others.put(getResources().getString(R.string.user_activities), user.getActivities());
			others.put(getResources().getString(R.string.user_books), user.getBooks());
			// others.put(getResources().getString(R.string.user_inspirational_people),
			// user.getInpirational_people());
			others.put(getResources().getString(R.string.user_interests), user.getInterests());
			others.put(getResources().getString(R.string.user_movies), user.getMovies());
			others.put(getResources().getString(R.string.user_music), user.getMusic());
			// others.put(getResources().getString(R.string.user_sports),
			// user.getSports());
			others.put(getResources().getString(R.string.user_tv), user.getTv());
			others.put(getResources().getString(R.string.user_quotes), user.getQuotes());

			addItemsForMap(getResources().getString(R.string.others), others, data);

			if (user.getFamily().size() > 0)
			{
				Title title = new Title();
				title.setName(getResources().getString(R.string.family));
				data.add(title);

				for (Relative relative : user.getFamily())
				{
					data.add(relative);
				}

				Relative last = (Relative) data.get(data.size() - 1);
				last.setShadow(true);
			}

			if (user.getWork().size() > 0)
			{
				Title title = new Title();
				title.setName(getResources().getString(R.string.work));
				data.add(title);

				for (Work work : user.getWork())
				{
					data.add(work);
				}

				Work last = (Work) data.get(data.size() - 1);
				last.setShadow(true);
			}

			if (user.getEducation().size() > 0)
			{
				Title title = new Title();
				title.setName(getResources().getString(R.string.education));
				data.add(title);

				for (Education education : user.getEducation())
				{
					data.add(education);
				}
			}	
		}

		super.populate(data);
		setNoMoreData(true);
	}

	private boolean addItemsForMap(String title, HashMap<String, String> map, List<GraphObject> data)
	{
		int originalSize = data.size();

		for (String key : map.keySet())
		{
			String value = map.get(key);
			if (value != null && value.length() > 0)
			{
				Item item = new Item();
				item.setName(key);
				item.setDesc(value);
				data.add(item);
			}
		}

		int finalSize = data.size();

		if (finalSize > originalSize)
		{
			Item item = (Item) data.get(finalSize - 1);
			item.setShadow(true);

			Title titleItem = new Title();
			titleItem.setName(title);
			data.add(originalSize, titleItem);
		}

		return finalSize > originalSize;
	}

	@Override
	public void onGridItemClick(KlyphGridView l, View v, int position, long id)
	{
		GraphObject data = (GraphObject) l.getItemAtPosition(position);

		Intent intent = Klyph.getIntentForGraphObject(getActivity(), data);

		if (intent != null)
		{
			startActivity(intent);
		}
	}

	/*@Override
	protected int getCustomLayout()
	{
		return R.layout.fake_header_grid_simple_fragment;
	}*/
	
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
}
