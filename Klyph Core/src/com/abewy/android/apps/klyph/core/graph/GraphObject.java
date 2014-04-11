package com.abewy.android.apps.klyph.core.graph;

public class GraphObject
{
	public static final int	HEADER				= 0;
	public static final int	PROGRESS			= 1;
	public static final int	STREAM				= 2;
	public static final int	COMMENT				= 3;
	public static final int	USER				= 4;
	public static final int	PAGE				= 5;
	public static final int	NOTIFICATION		= 6;
	public static final int	MESSAGE				= 7;
	public static final int	MESSAGE_THREAD		= 8;
	public static final int	EVENT				= 9;
	public static final int	ALBUM				= 10;
	public static final int	PHOTO				= 11;
	public static final int	TITLE				= 12;
	public static final int	ITEM				= 13;
	public static final int	VIDEO				= 14;
	public static final int	TEXT_ITEM			= 15;
	public static final int	TITLE_TWO_ITEM		= 16;
	public static final int	WORK				= 17;
	public static final int	EDUCATION			= 18;
	public static final int	EVENT_RESPONSE		= 19;
	public static final int	TEXT_BUTTON_ITEM	= 20;
	public static final int	FRIEND				= 21;
	public static final int	TAG					= 22;
	public static final int	LINK				= 23;
	public static final int	STATUS				= 24;
	public static final int	GROUP				= 25;
	public static final int	APPLICATION			= 26;
	public static final int	PROFILE				= 27;
	public static final int	UNIFIED_MESSAGE		= 28;
	public static final int	UNIFIED_THREAD		= 29;
	public static final int	TITLE_TEXT_ITEM		= 30;
	public static final int	FRIEND_REQUEST		= 31;
	public static final int	RELATIVE			= 32;
	public static final int	MEDIA_PHOTO			= 33;
	public static final int	FRIEND_LIST			= 34;

	// GraphObject
	public static final int	POST				= 100;
	public static final int	GRAPH_PHOTO			= 101;
	public static final int	GRAPH_COMMENT		= 102;

	private boolean 			loading				= false;
	private boolean 			selected			= false;
	private boolean 			toDelete			= false;
	private boolean 			displayedOnce		= false;
	private boolean 			showDivider			= true;

	public void setLoading(boolean  loading)
	{
		this.loading = loading;
	}

	public boolean  isLoading()
	{
		return loading;
	}

	public int getItemViewType()
	{
		return 0;
	}

	public boolean  isSelected()
	{
		return selected;
	}

	public void setSelected(boolean  selected)
	{
		this.selected = selected;
	}

	public boolean  isSelectable(int layout)
	{
		return true;
	}

	public boolean  getToDelete()
	{
		return toDelete;
	}

	public void setToDelete(boolean  toDelete)
	{
		this.toDelete = toDelete;
	}

	public boolean  getDisplayedOnce()
	{
		return displayedOnce;
	}

	public void setDisplayedOnce(boolean  displayedOnce)
	{
		this.displayedOnce = displayedOnce;
	}

	public boolean  mustShowDivider()
	{
		return showDivider;
	}

	public void setShowDivider(boolean  showDivider)
	{
		this.showDivider = showDivider;
	}
}
