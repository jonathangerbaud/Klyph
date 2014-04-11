package com.abewy.util;

import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class ApplicationUtil {

	public static String getAppVersion(ContextWrapper cw)
	{
		PackageInfo pinfo = null;
        
        try {
        	pinfo = cw.getPackageManager().getPackageInfo(cw.getPackageName(), 0);
        }
        catch (NameNotFoundException e) 
        {
        	return "";
        }
        
        return pinfo.versionName;
	}
}
