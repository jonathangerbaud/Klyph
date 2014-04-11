/**
 * @author Jonathan
 */

package com.abewy.android.extended.items;

public abstract class BaseType
{
	public static final int	HEADER			= 1;
	public static final int	PROGRESS		= 2;
	public static final int	TEXT_BUTTON		= 3;
	public static final int	TEXT			= 4;
	public static final int	TITLE			= 5;
	public static final int	TITLE_TEXT		= 6;
	public static final int	TITLE_TWO_ITEM	= 7;

	/**
	 * Return an unique data type to associate with a list view item
	 */
	public abstract int getItemViewType();

	public abstract String getItemPrimaryLabel();
}
