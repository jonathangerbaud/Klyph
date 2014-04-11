package com.abewy.android.extended.items;


public class Progress extends BaseType
{
	public Progress()
	{

	}
	
	@Override
	public int getItemViewType()
	{
		return BaseType.PROGRESS;
	}

	@Override
	public String getItemPrimaryLabel()
	{
		return "";
	}
}
