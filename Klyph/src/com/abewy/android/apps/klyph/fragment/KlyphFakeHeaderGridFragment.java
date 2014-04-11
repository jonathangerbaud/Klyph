/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout.LayoutParams;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.app.UserActivity;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.items.FakeHeaderItem;

public class KlyphFakeHeaderGridFragment extends KlyphFragment2
{
	protected int					fakeHeaderHeight	= -1;
	private List<FakeHeaderItem>	fakeHeaders			= new ArrayList<FakeHeaderItem>();

	/*@Override
	protected int getCustomLayout()
	{
		return R.layout.fake_header_grid_fragment;
	}*/

	public void setFakeHeaderHeight(int height)
	{
		fakeHeaderHeight = height;

		if (fakeHeaders.size() > 0)
		{
			for (int i = 0, n = getNumColumn(); i < n; i++)
			{
				fakeHeaders.get(i).setHeight(height);
			}
		}

		if (getAdapter() != null)
			getAdapter().notifyDataSetChanged();
	}

	protected int getSpecialLayout()
	{
		return 0;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		MultiObjectAdapter adapter = new MultiObjectAdapter(getListView(), getSpecialLayout());

		LayoutParams params = (LayoutParams) getGridView().getEmptyView().getLayoutParams();
		params.topMargin = fakeHeaderHeight;
		getGridView().getEmptyView().setLayoutParams(params);

		View progress = view.findViewById(android.R.id.progress);
		params = (LayoutParams) progress.getLayoutParams();
		params.topMargin = fakeHeaderHeight;
		params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
		progress.setLayoutParams(params);
		

		setListAdapter(adapter);
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		if (fakeHeaderHeight > 0 && data.size() > 0 && getAdapter().getCount() == 0)
		{
			for (int i = 0, n = getNumColumn(); i < n; i++)
			{
				FakeHeaderItem fakeHeaderItem = new FakeHeaderItem();
				fakeHeaderItem.setHeight(fakeHeaderHeight);
				fakeHeaders.add(fakeHeaderItem);
				getAdapter().add(fakeHeaderItem);
			}
		}
		super.populate(data);
	}
}
