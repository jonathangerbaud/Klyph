package com.abewy.android.apps.klyph.adapter.fql;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.holder.WorkEducationHolder;
import com.abewy.android.apps.klyph.core.fql.User.Education;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.util.FacebookUtil;
import com.abewy.android.apps.klyph.R;

public class EducationAdapter extends KlyphAdapter
{
	public EducationAdapter()
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

		Education item = (Education) data;

		holder.getEmployer().setText(item.getSchool().getName());
		
		if (item.getConcentration().getName() != null && item.getConcentration().getName().length() > 0)
		{
			holder.getJob().setText(item.getConcentration().getName());
			holder.getJob().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getJob().setVisibility(View.GONE);
		}
		
		if (item.getYear().getName() != null && item.getYear().getName().length() > 0)
		{
			String year = String.format(getContext(view).getResources().getString(R.string.education_year), item.getYear().getName());
			holder.getPlaceDate().setText(year);
			holder.getPlaceDate().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getPlaceDate().setVisibility(View.GONE);
		}

		String url = FacebookUtil.getImageURLForId(item.getSchool().getId());
		loadImage(holder.getEmployerPicture(), url, data);

		holder.getShadow().setVisibility(item.getShadow() == true ? View.VISIBLE : View.GONE);
	}
}
