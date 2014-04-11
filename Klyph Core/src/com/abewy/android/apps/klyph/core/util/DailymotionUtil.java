package com.abewy.android.apps.klyph.core.util;


public class DailymotionUtil
{
	public static final int THUMB_WIDTH = 427; 
	public static final int THUMB_HEIGHT = 240;
	
	public static String getThumbUrl(String videoUrl)
	{
		videoUrl = videoUrl.replace("swf/", "");

		int index = videoUrl.indexOf("video/");

		if (index != -1)
		{
			return "http://www.abewy.com/apps/klyph/services/content_from_url.php?url=" + videoUrl.substring(0, index)
					+ "thumbnail/" + videoUrl.substring(index);
		}

		return "";
	}
	
	public static boolean isDailymotionLink(String url)
	{
		return url.indexOf("www.dailymotion.com") != -1;
	}
}
