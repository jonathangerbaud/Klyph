package com.abewy.android.apps.klyph.adapter.holder;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WorkEducationHolder
{
	private ImageView		employerPicture;
	private TextView		employer;
	private TextView		job;
	private TextView		placeDate;
	private RelativeLayout	shadow;

	public WorkEducationHolder(ImageView employerPicture, TextView employer, TextView job, TextView placeDate, RelativeLayout shadow)
	{
		this.employerPicture = employerPicture;
		this.employer = employer;
		this.job = job;
		this.placeDate = placeDate;
		this.shadow = shadow;
	}

	public ImageView getEmployerPicture()
	{
		return employerPicture;
	}

	public void setEmployerPicture(ImageView employerPicture)
	{
		this.employerPicture = employerPicture;
	}

	public TextView getEmployer()
	{
		return employer;
	}

	public void setEmployer(TextView employer)
	{
		this.employer = employer;
	}

	public TextView getJob()
	{
		return job;
	}

	public void setJob(TextView job)
	{
		this.job = job;
	}

	public TextView getPlaceDate()
	{
		return placeDate;
	}

	public void setPlaceDate(TextView placeDate)
	{
		this.placeDate = placeDate;
	}

	public RelativeLayout getShadow()
	{
		return shadow;
	}

	public void setShadow(RelativeLayout shadow)
	{
		this.shadow = shadow;
	}
}
