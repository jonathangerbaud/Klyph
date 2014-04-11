package com.abewy.android.extended.items;

public class TitleTwoItem extends BaseType
{
	public final String	title;
	public final String	firstName;
	public final String	firstDesc;
	public final int	firstIcon;
	public final String	secondName;
	public final String	secondDesc;
	public final int	secondIcon;

	public TitleTwoItem(String title, String firstName, String firstDesc, int firstIcon, String secondName, String secondDesc, int secondIcon)
	{
		this.title = title;
		this.firstName = firstName;
		this.firstDesc = firstDesc;
		this.firstIcon = firstIcon;
		this.secondName = secondName;
		this.secondDesc = secondDesc;
		this.secondIcon = secondIcon;
	}

	public int getItemViewType()
	{
		return BaseType.TITLE_TWO_ITEM;
	}

	@Override
	public String getItemPrimaryLabel()
	{
		return title;
	}
}
