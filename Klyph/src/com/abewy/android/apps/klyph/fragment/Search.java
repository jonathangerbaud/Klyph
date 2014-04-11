package com.abewy.android.apps.klyph.fragment;

import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.android.apps.klyph.R;

public class Search extends KlyphFragment2
{
	private EditText	searchText;
	private Spinner		spinner;
	private ImageButton	button;
	private ProgressBar	progress;

	public Search()
	{

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		searchText = (EditText) view.findViewById(R.id.search_text);
		spinner = (Spinner) view.findViewById(R.id.search_type);
		button = (ImageButton) view.findViewById(R.id.button);
		progress = (ProgressBar) view.findViewById(android.R.id.progress);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				search();
			}
		});

		searchText.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean  onKey(View v, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)
				{
					search();
					return true;

				}
				return false;
			}
		});

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.search_labels,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		setListAdapter(new MultiObjectAdapter(getGridView()));

		defineEmptyText(R.string.empty_list_no_data);

		setListVisible(false);

		setAutoLoad(false);

		super.onViewCreated(view, savedInstanceState);

		progress.setVisibility(View.GONE);
	}

	@Override
	public void onGridItemClick(KlyphGridView l, View v, int position, long id)
	{
		GraphObject graphObject = (GraphObject) l.getItemAtPosition(position);

		startActivity(Klyph.getIntentForGraphObject(getActivity(), graphObject));
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		super.populate(data);
	}

	private void search()
	{
		String keywords = searchText.getText().toString();
		
		if (keywords.length() > 0)
		{
			switch (spinner.getSelectedItemPosition())
			{
				case 0:
					setRequestType(Query.SEARCH_USER);
					defineEmptyText(R.string.empty_list_no_user);
					break;
				case 1:
					setRequestType(Query.SEARCH_PAGE);
					defineEmptyText(R.string.empty_list_no_page);
					break;
				case 2:
					setRequestType(Query.SEARCH_GROUP);
					defineEmptyText(R.string.empty_list_no_group);
					break;
				case 3:
					setRequestType(Query.SEARCH_EVENT);
					defineEmptyText(R.string.empty_list_no_event);
					break;
				default:
					break;
			}

			setElementId(keywords);
			progress.setVisibility(View.VISIBLE);

			clearAndRefresh();

			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
		}
	}

	@Override
	protected int getCustomLayout()
	{
		return R.layout.fragment_search;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		searchText = null;
		spinner = null;
		button = null;
		progress = null;
	}
}
