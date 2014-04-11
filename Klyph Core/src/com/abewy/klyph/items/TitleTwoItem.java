package com.abewy.klyph.items;


public class TitleTwoItem extends ShadowItem
{
	private String					title;
	private String					firstName;
	private String					firstDesc;
	private int						firstIcon;
	private String					secondName;
	private String					secondDesc;
	private int						secondIcon;

	public TitleTwoItem()
	{

	}
	
	@Override
	public boolean isSelectable(int layout)
	{
		return false;
	}

	public int getItemViewType()
	{
		return ItemType.TITLE_TWO_ITEM;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getFirstDesc()
	{
		return firstDesc;
	}

	public void setFirstDesc(String firstDesc)
	{
		this.firstDesc = firstDesc;
	}

	public int getFirstIcon()
	{
		return firstIcon;
	}

	public void setFirstIcon(int firstIcon)
	{
		this.firstIcon = firstIcon;
	}

	public String getSecondName()
	{
		return secondName;
	}

	public void setSecondName(String secondName)
	{
		this.secondName = secondName;
	}

	public String getSecondDesc()
	{
		return secondDesc;
	}

	public void setSecondDesc(String secondDesc)
	{
		this.secondDesc = secondDesc;
	}

	public int getSecondIcon()
	{
		return secondIcon;
	}

	public void setSecondIcon(int secondIcon)
	{
		this.secondIcon = secondIcon;
	}
}
