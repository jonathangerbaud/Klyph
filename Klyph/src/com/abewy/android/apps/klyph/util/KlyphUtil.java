package com.abewy.android.apps.klyph.util;

import android.content.Context;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.util.AttrUtil;

public class KlyphUtil
{
	public static int getPlaceHolder(Context context)
	{
		return AttrUtil.getResourceId(context, R.attr.squarePlaceHolderIcon);
	}
	
	public static int getProfilePlaceHolder(Context context)
	{
		return AttrUtil.getResourceId(context, KlyphPreferences.isRoundedPictureEnabled() ? R.attr.circlePlaceHolderIcon : R.attr.squarePlaceHolderIcon);
	}
}
