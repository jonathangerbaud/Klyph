/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph;

import java.io.File;
import com.abewy.util.Android;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class KlyphDownloadManager
{
	private static final String	DEFAULT_DIR_NAME	= "Klyph";

	public static void downloadPhoto(Context context, String url, String title)
	{
		downloadPhoto(context, url, title, "", true, true);
	}

	public static void downloadPhoto(Context context, String url, String title, String desc, boolean notifOnProgress,
			boolean notifOnComplete)
	{
		downloadFile(context, url, title, desc, notifOnProgress, notifOnComplete, Environment.DIRECTORY_PICTURES, DEFAULT_DIR_NAME);
	}

	public static void downloadVideo(Context context, String url, String title)
	{
		downloadVideo(context, url, title, "", true, true);
	}

	public static void downloadVideo(Context context, String url, String title, String desc, boolean notifOnProgress,
			boolean notifOnComplete)
	{
		downloadFile(context, url, title, desc, notifOnProgress, notifOnComplete, Environment.DIRECTORY_MOVIES, DEFAULT_DIR_NAME);

	}

	private static void downloadFile(Context context, String url, String title, String desc, boolean notifOnProgress,
			boolean notifOnComplete, String systemDir, String appDir)
	{
		File direct = new File(Environment.getExternalStorageDirectory() + systemDir + "/" + appDir);

		if (!direct.exists())
		{
			boolean success = direct.mkdirs();
			
			if (!success)
			{
				direct = new File(Environment.getExternalStoragePublicDirectory(systemDir) + "/" + appDir);
				
				if (!direct.exists())
				{
					success  = direct.mkdirs();
				}
			}
		}

		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

		if (!Android.isMinAPI(14))
		{
			url = url.replace("https://", "http://");
		}

		Uri Download_Uri = Uri.parse(url);

		DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

		if (Android.isMinAPI(11))
		{
			request.allowScanningByMediaScanner();

			int visibility = 0;

			if (notifOnComplete == true)
			{
				visibility = DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;
			}
			else if (notifOnProgress == true)
			{
				visibility = DownloadManager.Request.VISIBILITY_VISIBLE;
			}

			request.setNotificationVisibility(visibility);
		}

		request.setTitle(title);
		request.setDescription(desc);

		request.setDestinationInExternalPublicDir(systemDir, "/" + appDir + "/" + url.substring(url.lastIndexOf("/") + 1));

		downloadManager.enqueue(request);
	}
}