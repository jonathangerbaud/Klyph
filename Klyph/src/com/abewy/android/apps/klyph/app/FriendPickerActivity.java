package com.abewy.android.apps.klyph.app;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class FriendPickerActivity extends KlyphListActivity
{
	private static List<GraphObject>	FRIEND_LIST;
	private ActionMode					actionMode;
	private EditText					searchText;
	private List<GraphObject>			friends;
	private ArrayList<String>			initialIds;
	private boolean						selectionConfirmed	= false;
	private boolean						singleChoice		= false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		singleChoice = getIntent().getBooleanExtra(KlyphBundleExtras.SINGLE_CHOICE, false);

		setTitle(singleChoice ? R.string.friend_picker_single_choice_title : R.string.friend_picker_title);
		
		setListAdapter(new MultiObjectAdapter(getListView(), singleChoice ? SpecialLayout.FRIEND_PICKER_SINGLE : SpecialLayout.FRIEND_PICKER));

		getListView().setItemsCanFocus(false);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		searchText = (EditText) findViewById(R.id.search_text);
		searchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				filter(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{

			}
		});

		setRequestType(Query.ALL_FRIENDS);

		if (getIntent().hasExtra(KlyphBundleExtras.FRIEND_PICKER_IDS))
		{
			ArrayList<String> ids = getIntent().getStringArrayListExtra(KlyphBundleExtras.FRIEND_PICKER_IDS);

			if (ids != null && ids.size() > 0)
			{
				initialIds = ids;
			}
		}

		if (FRIEND_LIST == null)
		{
			load();
		}
		else
		{
			// reset previously selected objects
			for (GraphObject graphObject : FRIEND_LIST)
			{
				graphObject.setSelected(false);
			}

			populate(FRIEND_LIST);
		}
	}

	private void filter(String filterText)
	{
		if (filterText.length() == 0)
		{
			getAdapter().setData(friends);
		}
		else
		{
			List<GraphObject> filteredUsers = new ArrayList<GraphObject>();
			final String f = filterText.toLowerCase();

			for (GraphObject graphObject : friends)
			{
				Friend u = (Friend) graphObject;

				if (u.getName().toLowerCase().contains(f) == true)
				{
					filteredUsers.add(u);
				}
			}

			getAdapter().setData(filteredUsers);
		}

		refreshCheckedViews();
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		super.populate(data);
		setNoMoreData(true);

		FRIEND_LIST = data;
		friends = data;

		boolean friendsSelected = false;
		if (initialIds != null)
		{
			int n = data.size();

			for (int i = 0; i < n; i++)
			{
				Friend friend = (Friend) data.get(i);

				for (int j = 0; j < initialIds.size(); j++)
				{
					if (friend.getUid().equals(initialIds.get(j)))
					{
						friendsSelected = true;
						friend.setSelected(true);
						getListView().setItemChecked(i, true);
						initialIds.remove(j);
						j--;
						break;
					}
				}
			}
		}

		if (friendsSelected == true)
		{
			startActionMode();
			refreshCheckedViews();
			refreshActionModeTitle();
		}

		searchText.setVisibility(View.VISIBLE);
	}

	private void refreshCheckedViews()
	{
		int i = 0;
		for (GraphObject graphObject : getAdapter().getItems())
		{
			getListView().setItemChecked(i, graphObject.isSelected());
			i++;
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Friend friend = (Friend) getAdapter().getItem(position);

		if (singleChoice)
		{
			Intent intent = new Intent();
			intent.putExtra(KlyphBundleExtras.USER_ID, friend.getUid());
			intent.putExtra(KlyphBundleExtras.USER_NAME, friend.getName());

			setResult(RESULT_OK, intent);
			finish();
		}
		else
		{
			startActionMode();

			friend.setSelected(!friend.isSelected());

			refreshActionModeTitle();
		}
	}

	private void startActionMode()
	{
		if (actionMode == null)
		{
			actionMode = startActionMode(new FriendPickerActionMode());
			int doneButtonId = Resources.getSystem().getIdentifier("action_mode_close_button", "id", "android");
			View doneButton = findViewById(doneButtonId);

			if (doneButton != null)
			{
				doneButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v)
					{
						selectionConfirmed = true;
						actionMode.finish();
					}
				});
			}
		}
	}

	private void refreshActionModeTitle()
	{
		int n = 0;

		for (int i = 0; i < friends.size(); i++)
		{
			GraphObject friend = (GraphObject) friends.get(i);
			if (friend.isSelected())
				n++;
		}

		if (n == 0)
		{
			actionMode.setTitle(R.string.no_friend_selected);
		}
		else if (n == 1)
		{
			actionMode.setTitle(R.string.one_friend_selected);
		}
		else
		{
			actionMode.setTitle(getString(R.string.several_friends_selected, n));
		}
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_friend_picker;
	}

	private List<Friend> getSelectedUsers()
	{
		List<Friend> retUsers = new ArrayList<Friend>();

		for (int i = 0; i < friends.size(); i++)
		{
			Friend friend = (Friend) friends.get(i);
			if (friend.isSelected())
			{
				retUsers.add(friend);
			}
		}

		return retUsers;
	}

	private void deselectAll()
	{
		int n = friends.size();
		for (int i = 0; i < n; i++)
		{
			Friend friend = (Friend) friends.get(i);
			friend.setSelected(false);
		}

		refreshCheckedViews();
		refreshActionModeTitle();
	}

	private void goBack()
	{
		if (selectionConfirmed == true && friends.size() > 0)
		{
			List<Friend> friends = getSelectedUsers();

			String[] names = new String[friends.size()];
			String[] ids = new String[friends.size()];

			for (int i = 0; i < friends.size(); i++)
			{
				Friend friend = friends.get(i);
				names[i] = friend.getName();
				ids[i] = friend.getUid();

			}

			Intent intent = new Intent();
			intent.putExtra(KlyphBundleExtras.FRIEND_PICKER_NAMES, names);
			intent.putExtra(KlyphBundleExtras.FRIEND_PICKER_IDS, ids);

			setResult(RESULT_OK, intent);
		}
		else
		{
			setResult(RESULT_CANCELED, null);
		}
		finish();
	}

	private final class FriendPickerActionMode implements ActionMode.Callback
	{
		@Override
		public boolean  onCreateActionMode(ActionMode mode, Menu menu)
		{
			// Used to put dark icons on light action bar

			menu.add("Clear").setIcon(AttrUtil.getResourceId(FriendPickerActivity.this, R.attr.eventDeclinedIcon))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

			return true;
		}

		@Override
		public boolean  onPrepareActionMode(ActionMode mode, Menu menu)
		{
			return false;
		}

		@Override
		public boolean  onActionItemClicked(ActionMode mode, MenuItem item)
		{
			deselectAll();

			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode)
		{
			goBack();
		}
	}
}
