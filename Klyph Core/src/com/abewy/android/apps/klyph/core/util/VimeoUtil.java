package com.abewy.android.apps.klyph.core.util;


public class VimeoUtil
{
	public static final int THUMB_WIDTH = 640; 
	public static final int THUMB_HEIGHT = 476;
	
	public static String getThumbUrl(String videoUrl)
	{
		videoUrl = videoUrl.replace("moogaloop.swf?clip_id=", "");

		int start = videoUrl.indexOf("vimeo.com/") + 10;
		int end = videoUrl.indexOf("&", start) != -1 ? videoUrl.indexOf("&", start) : videoUrl.length();

		return "http://www.abewy.com/apps/klyph/services/vimeo_thumbnail.php?id=" + videoUrl.substring(start, end);
	}
	
	public static boolean isVimeoLink(String videoUrl)
	{
		return videoUrl.indexOf("vimeo.com") != -1;
	}
}
