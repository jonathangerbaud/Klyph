package com.abewy.android.apps.klyph.adapter.items;

import android.content.Context;
import android.view.View;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;

public class ProgressAdapter extends KlyphAdapter
{
	public ProgressAdapter()
	{
		super();
	}
	
	@Override
	protected int getLayout()
	{
		return R.layout.item_progress;
	}
}
