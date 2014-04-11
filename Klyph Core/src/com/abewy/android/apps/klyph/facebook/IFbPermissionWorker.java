/**
* @author Jonathan
*/

package com.abewy.android.apps.klyph.facebook;

import java.util.List;

public interface IFbPermissionWorker
{
	public static final int REAUTH_ACTIVITY_CODE = 15932;
	
	public void requestPublishPermissions(IFbPermissionCallback callback, List<String> permissions);
}
