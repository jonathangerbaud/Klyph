/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.adapter.subAdapter;

import android.view.View;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.holder.StreamHolder;
import com.abewy.android.apps.klyph.core.fql.Status;
import com.abewy.android.apps.klyph.core.fql.Stream;

public class StreamStatus extends StreamMedia
{
	private final MultiObjectAdapter	parentAdapter;
	private final int specialLayout;

	public StreamStatus(MultiObjectAdapter parentAdapter, int specialLayout)
	{
		super();
		this.parentAdapter = parentAdapter;
		this.specialLayout = specialLayout;
	}

	private MultiObjectAdapter getParentAdapter()
	{
		return parentAdapter;
	}

	public void mergeData(StreamHolder holder, Stream stream)
	{
		final Status status = stream.getStatus();

		new StreamHeader(specialLayout).mergeData(holder, stream, status);
		new StreamButtonBar(getParentAdapter(), specialLayout).mergeData(holder, stream, status);

		if (status.getMessage().length() > 0)
		{
			holder.getMessage().setText(status.getMessage());
			holder.getMessage().setVisibility(View.VISIBLE);
		}
	}
}
