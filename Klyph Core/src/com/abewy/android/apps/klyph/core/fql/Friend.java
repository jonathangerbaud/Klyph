package com.abewy.android.apps.klyph.core.fql;

import java.util.List;
import com.abewy.android.apps.klyph.core.fql.User.Education;
import com.abewy.android.apps.klyph.core.fql.User.Work;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Friend extends GraphObject
{
	private String				uid;
	private String				name;
	private String				first_name;
	private String				birthday;
	private String				birthday_date;
	private String				pic_small;
	private String				pic_big;
	private String				pic;
	private String				pic_large;
	private List<Work>			work;
	private List<Education>		education;

	public Friend()
	{

	}

	@Override
	public int getItemViewType()
	{
		return GraphObject.FRIEND;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getFirst_name()
	{
		return first_name;
	}

	public void setFirst_name(String first_name)
	{
		this.first_name = first_name;
	}

	public String getBirthday()
	{
		return birthday;
	}

	public void setBirthday(String birthday)
	{
		this.birthday = birthday;
	}

	public String getBirthday_date()
	{
		return birthday_date;
	}

	public void setBirthday_date(String birthday_date)
	{
		this.birthday_date = birthday_date;
	}

	public String getPic_small()
	{
		return pic_small;
	}

	public void setPic_small(String pic_small)
	{
		this.pic_small = pic_small;
	}

	public String getPic_big()
	{
		return pic_big;
	}

	public void setPic_big(String pic_big)
	{
		this.pic_big = pic_big;
	}

	public String getPic()
	{
		return pic;
	}

	public void setPic(String pic)
	{
		this.pic = pic;
	}

	public String getPic_large()
	{
		return pic_large;
	}

	public void setPic_large(String pic_large)
	{
		this.pic_large = pic_large;
	}

	public List<Work> getWork()
	{
		return work;
	}

	public void setWork(List<Work> work)
	{
		this.work = work;
	}

	public List<Education> getEducation()
	{
		return education;
	}

	public void setEducation(List<Education> education)
	{
		this.education = education;
	}
}
