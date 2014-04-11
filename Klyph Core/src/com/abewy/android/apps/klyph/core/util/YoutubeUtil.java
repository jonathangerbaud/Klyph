package com.abewy.android.apps.klyph.core.util;

public class YoutubeUtil
{
	public static final int	THUMB_WIDTH		= 480;
	public static final int	THUMB_HEIGHT	= 360;

	public static String getThumbUrl(String videoUrl)
	{
		if (videoUrl.indexOf("v=") != -1)
		{
			int end = videoUrl.indexOf("&", videoUrl.indexOf("v=")) != -1 ? videoUrl.indexOf("&",
					videoUrl.indexOf("v=")) : videoUrl.length();
			return "http://i.ytimg.com/vi/" + videoUrl.substring(videoUrl.indexOf("v=") + 2, end) + "/hqdefault.jpg";
		}
		else if (videoUrl.indexOf("/v/") != -1)
		{
			int end = videoUrl.indexOf("?") != -1 ? videoUrl.indexOf("?") : videoUrl.length();
			return "http://i.ytimg.com/vi/" + videoUrl.substring(videoUrl.indexOf("/v/") + 3, end) + "/hqdefault.jpg";
		}
		
		return "";
	}

	public static boolean isYoutubeLink(String url)
	{
		return url.indexOf("www.youtube.com") != -1 && !url.equals("www.youtube.com") && !url.equals("http://www.youtube.com") && !url.equals("www.youtube.com/") && !url.equals("http://www.youtube.com/");
	}

	public static String getVideoIdFromUrl(String url)
	{
		if (url.length() > 0)
		{
			int start = url.indexOf("v=");
			int end = -1;

			if (start != -1)
			{
				start += 2;
				end = url.indexOf("&", start);
			}
			else
			{
				start = url.indexOf("/v/") + 3;
				end = url.indexOf("&", start);
			}

			if (end != -1)
				return url.substring(start, end);
			else if (start != -1)
				return url.substring(start);
		}

		return "";
	}
}
