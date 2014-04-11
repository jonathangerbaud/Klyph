/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.core;



public class KlyphFlags
{
	/**
	 * Determine if the app is the free or paid version
	 * @return true if paid version, false if free version
	 */
	public static final boolean IS_PRO_VERSION = BaseApplication.getInstance().getPackageName().toLowerCase().contains("pro");
	
	public static final boolean IS_AMAZON_VERSION = false;
	
	/**
	 * Enable or disable banner ads
	 */ 
	public static final boolean	BANNER_ADS_ENABLED = true;
	
	/**
	 * Enable or disable interstitial ads
	 */ 
	public static final boolean	INTERSTITAL_ADS_ENABLED	= false;
	
	/**
	 * Display (in log) the Facebook Hash at startup
	 */
	public static final boolean LOG_FACEBOOK_HASH = false;
	
	/**
	 * Enable or disable the connection with Eclipse Hierarchy View
	 */
	public static final boolean ENABLE_HIERACHY_VIEW_CONNECTOR = false;
	
	/**
	 * Enable or disable ACRA bug report
	 */
	public static final boolean ENABLE_BUG_REPORT = false;
	
	/**
	 * Log the result of AsyncRequests
	 */
	public static final boolean LOG_REQUEST_RESULT = false;
	
	/**
	 * Log the params of a request when going to be executed (query, id, offset)
	 */
	public static final boolean LOG_REQUEST_EXEC = false;
	
	/**
	 * Log request start/end/total/deserialize time
	 */
	public static final boolean LOG_REQUEST_PERFORMANCE = false;
	
	/**
	 * Log adapter type for each list row created
	 */
	public static final boolean LOG_GRAPH_ADAPTERS = false;
	
	/**
	 * Log Facebook's session access token
	 */
	public static final boolean LOG_ACCESS_TOKEN = false;
}
