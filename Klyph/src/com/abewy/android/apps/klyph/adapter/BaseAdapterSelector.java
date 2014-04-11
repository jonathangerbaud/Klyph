/**
* @author Jonathan
*/

package com.abewy.android.apps.klyph.adapter;

import com.abewy.android.adapter.TypeAdapter;
import com.abewy.android.apps.klyph.adapter.items.AdvertisingAdapter;
import com.abewy.android.apps.klyph.adapter.items.HeaderAdapter;
import com.abewy.android.apps.klyph.adapter.items.ItemAdapter;
import com.abewy.android.apps.klyph.adapter.items.ProgressAdapter;
import com.abewy.android.apps.klyph.adapter.items.ProgressGridAdapter;
import com.abewy.android.apps.klyph.adapter.items.TextButtonItemAdapter;
import com.abewy.android.apps.klyph.adapter.items.TextItemAdapter;
import com.abewy.android.apps.klyph.adapter.items.TitleAdapter;
import com.abewy.android.apps.klyph.adapter.items.TitleTextItemAdapter;
import com.abewy.android.apps.klyph.adapter.items.TitleTwoItemAdapter;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.ItemType;

public class BaseAdapterSelector
{

	public BaseAdapterSelector()
	{
		// TODO Auto-generated constructor stub
	}
	
	public static TypeAdapter<GraphObject> getAdapter(GraphObject object, int layoutType)
	{
		switch (object.getItemViewType())
		{
			case ItemType.PROGRESS:
			{
				if (layoutType == SpecialLayout.GRID)
					return new ProgressGridAdapter();
				
				return new ProgressAdapter();
			}
			case ItemType.HEADER:
			{
				return new HeaderAdapter();
			}
			case ItemType.ITEM:
			{
				return new ItemAdapter();
			}
			case ItemType.TEXT_BUTTON:
			{
				return new TextButtonItemAdapter();
			}
			case ItemType.TEXT:
			{
				return new TextItemAdapter();
			}
			case ItemType.TITLE:
			{
				return new TitleAdapter();
			}
			case ItemType.TITLE_TEXT:
			{
				return new TitleTextItemAdapter();
			}
			case ItemType.TITLE_TWO_ITEM:
			{
				return new TitleTwoItemAdapter();
			}
			case ItemType.ADVERTISING:
			{
				return new AdvertisingAdapter();
			}
		}
		
		return null;
	}

}
