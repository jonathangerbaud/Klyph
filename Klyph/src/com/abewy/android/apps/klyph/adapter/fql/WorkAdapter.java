package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.WorkEducationHolder;
import com.abewy.android.apps.klyph.core.fql.User.Work;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.FacebookUtil;
import com.abewy.android.apps.klyph.R;

public class WorkAdapter extends KlyphAdapter
{
	public WorkAdapter()
	{
		super();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_work_education;
	}
	
	@Override
	public boolean  isEnabled(GraphObject object)
	{
		return false;
	}

	@Override
	protected void attachHolder(View view)
	{
		view.setTag(new WorkEducationHolder((ImageView) view.findViewById(R.id.work_employer_profile_picture),
				(TextView) view.findViewById(R.id.work_employer), (TextView) view.findViewById(R.id.work_job),
				(TextView) view.findViewById(R.id.work_place_date), (RelativeLayout) view
						.findViewById(R.id.item_shadow)));
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		WorkEducationHolder holder = (WorkEducationHolder) view.getTag();
		
		//holder.getEmployerPicture().setImageDrawable(null);

		Work item = (Work) data;

		holder.getEmployer().setText(item.getEmployer().getName());

		if (item.getPosition().getName() != null && item.getPosition().getName().length() > 0)
		{
			holder.getJob().setText(item.getPosition().getName());
			holder.getJob().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getJob().setVisibility(View.GONE);
		}

		if (item.getLocation().getName() != null && item.getLocation().getName().length() > 0
			&& item.getStart_date() != null && item.getStart_date().length() > 0)
		{
			int dateRes = R.string.work_date;

			if (item.getEnd_date().length() == 0)
				dateRes = R.string.work_date_until_today;

			String date = String.format(getContext(view).getResources().getString(dateRes), item.getStart_date(),
					item.getEnd_date());

			holder.getPlaceDate().setText(item.getLocation().getName() + " - " + date);
			holder.getPlaceDate().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getPlaceDate().setVisibility(View.GONE);
		}

		String url = FacebookUtil.getImageURLForId(item.getEmployer().getId());
		loadImage(holder.getEmployerPicture(), url, data);

		holder.getShadow().setVisibility(item.getShadow() == true ? View.VISIBLE : View.GONE);
	}
}
