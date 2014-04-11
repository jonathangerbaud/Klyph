package com.abewy.android.apps.klyph.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import com.abewy.android.apps.klyph.core.BaseApplication;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.facebook.Session;

public class FacebookUtil
{
	public static final String	SQUARE	= "square";
	public static final String	SMALL	= "small";
	public static final String	NORMAL	= "normal";
	public static final String	LARGE	= "large";

	
	/**
	 * @param id id of the user, page, event, ...
	 * @param imageFormat must one of <code>FacebookUtil.SQUARE, FacebookUtil.SMALL, FacebookUtil.NORMAL, FacebookUtil.LARGE</code>
	 * @return the url of the image
	 */
	public static String getImageURLForId(String id, String imageFormat)
	{
		return new StringBuilder("https://graph.facebook.com/").append(id).append("/picture?type=").append(imageFormat).toString();
	}
	
	/**
	 * @param id id of the user, page, event, ...
	 * @return the url of a 50px square image
	 */
	public static String getImageURLForId(String id)
	{
		return getImageURLForId(id, SQUARE);
	}
	
	public static String getProfilePictureURLForId(String id)
	{
		return new StringBuilder("http://www.abewy.com/apis/facebook/profile_picture.php?id=").append(id).append("&dpi=").append(KlyphDevice.getDeviceDPI()).append("&appName=").append(BaseApplication.getInstance().getPackageName()).append("&accessToken=").append(Session.getActiveSession().getAccessToken()).toString();
	}
	
	public static String getLargeProfilePictureURLForId(String id)
	{
		return new StringBuilder("http://www.abewy.com/apis/facebook/profile_picture.php?id=").append(id).append("&dpi=").append(KlyphDevice.getDeviceDPI()).append("&appName=").append(BaseApplication.getInstance().getPackageName()).append("&type=large&accessToken=").append(Session.getActiveSession().getAccessToken()).toString();
	}
	
	public static boolean hasPermission(String permission)
	{
		return Session.getActiveSession().getPermissions().contains(permission);
	}
	
	public static boolean hasPermissions(List<String> permissions)
	{
		return Session.getActiveSession().getPermissions().containsAll(permissions);
	}
	
	public static void logHash(Context context)
	{
		try {
	        PackageInfo info = context.getPackageManager().getPackageInfo(
	                context.getPackageName(), 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
	}
	
	public static String getBiggestImageURL(String imageUrl)
	{
		return imageUrl.replace("_s.", "_o.").replace("_t", "_n");
	}
}