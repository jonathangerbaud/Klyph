package com.abewy.android.apps.klyph.adapter.items;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.TextButtonItemHolder;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.TextButtonItem;
import com.abewy.android.apps.klyph.R;

public class TextButtonItemAdapter extends KlyphAdapter
{
	public TextButtonItemAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_text_button_item;
	}

	@Override
	protected void attachHolder(View view)
	{
		view.setTag(new TextButtonItemHolder((TextView) view.findViewById(R.id.text), (ImageButton) view.findViewById(R.id.button)));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		TextButtonItemHolder holder = (TextButtonItemHolder) view.getTag();

		TextButtonItem item = (TextButtonItem) data;

		holder.getText().setText(item.getText());
		
		if (item.getButtonListener() != null)
		{
			holder.getButton().setOnClickListener(item.getButtonListener());
		}
	}
}
