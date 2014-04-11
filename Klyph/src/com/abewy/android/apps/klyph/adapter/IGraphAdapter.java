package com.abewy.android.apps.klyph.adapter;

import com.abewy.android.apps.klyph.core.graph.GraphObject;
import android.view.View;

public interface IGraphAdapter
{
	public int getLayout();
	public void setView(View view);
	public void setData(GraphObject graphObject);
	public boolean isCompatible(GraphObject graphObject);
}
