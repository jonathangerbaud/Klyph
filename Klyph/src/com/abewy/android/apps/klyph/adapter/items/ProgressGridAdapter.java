package com.abewy.android.apps.klyph.adapter.items;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;

public class ProgressGridAdapter extends KlyphAdapter
{
	int columnWidth;
	
	public ProgressGridAdapter()
	{
		super();
		
		columnWidth = Klyph.getGridColumnWidth();
	}
	
	@Override
	protected int getLayout()
	{
		return R.layout.item_progress_grid;
	}
	
	@Override
	public void setLayoutParams(View view)
	{
		LayoutParams lp = view.getLayoutParams();
		lp.height = lp.width = columnWidth;
		view.setLayoutParams(lp);
	}
}
