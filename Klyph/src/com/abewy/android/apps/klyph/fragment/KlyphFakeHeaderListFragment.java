/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.fragment;

import uk.co.senab.actionbarpulltorefresh.library.Options;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.app.MainActivity;

public class KlyphFakeHeaderListFragment extends KlyphFragment
{
	protected int	fakeHeaderHeight	= -1;
	private boolean	hasFakeHeader		= false;
	private View	fakeHeaderView;

	public void setFakeHeader(boolean hasFakeHeader)
	{
		this.hasFakeHeader = hasFakeHeader;
	}

	public void setFakeHeaderHeight(int height)
	{
		this.fakeHeaderHeight = height;

		if (fakeHeaderView != null)
		{
			android.view.ViewGroup.LayoutParams params = fakeHeaderView.getLayoutParams();
			params.height = height;
			fakeHeaderView.setLayoutParams(params);
		}
	}

	protected int getSpecialLayout()
	{
		return 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = super.onCreateView(inflater, container, savedInstanceState);

		if (hasFakeHeader)
		{
			fakeHeaderView = getActivity().getLayoutInflater().inflate(R.layout.fake_header_item, getListView(), false);

			if (fakeHeaderHeight != -1)
			{
				android.view.ViewGroup.LayoutParams params = (android.view.ViewGroup.LayoutParams) fakeHeaderView.getLayoutParams();
				params.height = fakeHeaderHeight;
				fakeHeaderView.setLayoutParams(params);
			}
			
			getListView().addHeaderView(fakeHeaderView, null, false);
			
			LayoutParams params = (LayoutParams) getListView().getEmptyView().getLayoutParams();
			params.topMargin = fakeHeaderHeight;
			getListView().getEmptyView().setLayoutParams(params);
			
			View progress = view.findViewById(android.R.id.progress);
			params = (LayoutParams) progress.getLayoutParams();
			params.topMargin = fakeHeaderHeight;
			params.gravity = Gravity.CENTER_HORIZONTAL|Gravity.TOP;
			progress.setLayoutParams(params);
		}

		return view;
	}

	@Override
	protected Options getPullToRefreshOptions()
	{
		if (!(getActivity() instanceof MainActivity))
			return Options.create().headerLayout(R.layout.ptr_profile_header).build();
		
		return super.getPullToRefreshOptions();
	}
}
