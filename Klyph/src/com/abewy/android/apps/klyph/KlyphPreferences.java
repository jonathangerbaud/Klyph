package com.abewy.android.apps.klyph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.abewy.android.apps.klyph.core.KlyphFlags;
import com.abewy.android.apps.klyph.R;
import com.amazon.device.messaging.ADM;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class KlyphPreferences
{
	public static final String	PREFERENCE_THEME							= "preference_theme";
	public static final String	PREFERENCE_NOTIFICATIONS					= "preference_notifications";
	public static final String	PREFERENCE_NOTIFICATIONS_BIRTHDAY			= "preference_notifications_birthday";
	public static final String	PREFERENCE_NOTIFICATIONS_BIRTHDAY_TIME		= "preference_notifications_birthday_time";
	public static final String	PREFERENCE_NOTIFICATIONS_APP_REQUESTS		= "preference_notifications_apprequest";
	public static final String	PREFERENCE_NOTIFICATIONS_ALBUMS				= "preference_notifications_album";
	public static final String	PREFERENCE_NOTIFICATIONS_EVENTS				= "preference_notifications_event";
	public static final String	PREFERENCE_NOTIFICATIONS_FRIEND_REQUEST		= "preference_notifications_friend";
	public static final String	PREFERENCE_NOTIFICATIONS_GROUPS				= "preference_notifications_groups";
	public static final String	PREFERENCE_NOTIFICATIONS_PAGES				= "preference_notifications_page";
	public static final String	PREFERENCE_NOTIFICATIONS_PHOTOS				= "preference_notifications_photo";
	public static final String	PREFERENCE_NOTIFICATIONS_POKES				= "preference_notifications_pokes";
	public static final String	PREFERENCE_NOTIFICATIONS_STREAMS			= "preference_notifications_stream";
	public static final String	PREFERENCE_NOTIFICATIONS_VIDEOS				= "preference_notifications_video";
	public static final String	PREFERENCE_NOTIFICATIONS_GROUP				= "preference_notifications_group";
	public static final String	PREFERENCE_NOTIFICATIONS_SYSTEM				= "preference_notifications_system";
	public static final String	PREFERENCE_NOTIFICATIONS_INTERVAL			= "preferences_notifications_interval";
	public static final String	PREFERENCE_PRIVACY							= "preference_privacy";
	public static final String	PREFERENCE_NEWSFEED_PAGES					= "preference_newsfeed_pages";
	public static final String	PREFERENCE_NEWSFEED_GROUPS					= "preference_newsfeed_groups";
	public static final String	PREFERENCE_NEWSFEED_EVENTS					= "preference_newsfeed_events";
	public static final String	PREFERENCE_LAST_STORIES						= "preference_last_stories";
	public static final String	PREFERENCE_GCM_REG_ID						= "preference_gcm_reg_id";
	public static final String	PREFERENCE_NOTIFICATION_SERVICE_OFFSET		= "preference_notification_service_offset";
	public static final String	PREFERENCE_FRIENDREQUEST_SERVICE_OFFSET		= "preference_friendrequest_service_offset";
	public static final String	PERFORMANCES_PHOTO_EFFECT					= "preference_performances_photo_effect";
	public static final String	PERFORMANCES_ROUNDED_PICTURES				= "preference_performances_rounded_profile_picture";
	public static final String	PREFERENCE_VIEW_PAGER_INDICATOR_STYLE		= "preference_view_pager_indicator_style";
	public static final String	PERFORMANCES_CARD_ANIMATION					= "preference_performances_card_animation";
	public static final String	PREFERENCE_USER_DONATED						= "preference_user_donated";
	public static final String	PREFERENCE_MAIN_ACTIVITY_TABS				= "preference_main_activity_tabs";
	public static final String	PREFERENCE_USER_ACTIVITY_TABS				= "preference_user_activity_tabs";
	public static final String	PREFERENCE_PAGE_ACTIVITY_TABS				= "preference_page_activity_tabs";
	public static final String	PREFERENCE_GROUP_ACTIVITY_TABS				= "preference_group_activity_tabs";
	public static final String	PREFERENCE_IMAGE_SAVE_DIRECTORY				= "preference_image_save_directory";
	public static final String	PREFERENCE_IMAGE_CLEAR_CACHE				= "preference_image_clear_cache";
	public static final String	PREFERENCE_NOTIFICATIONS_LAST_CHECKED_TIME	= "preference_notifications_last_checked_time";
	public static final String	PREFERENCE_NOTIFICATIONS_SYSTEM_PUSH		= "push";
	public static final String	PREFERENCE_NOTIFICATIONS_SYSTEM_PERIODIC	= "periodic";
	public static final String	PREFERENCE_NOTIFICATIONS_SYSTEM_BOTH		= "both";
	public static final String	PREFERENCE_NOTIFICATIONS_VIBRATE			= "preference_notifications_vibrate";
	public static final String	PREFERENCE_NOTIFICATIONS_RINGTONE			= "preference_notifications_ringtone";
	public static final String	PREFERENCE_NOTIFICATIONS_RINGTONE_URI		= "preference_notifications_ringtone_uri";
	public static final String	PREFERENCE_NEWSFEED_GO_TO_TOP				= "preference_newsfeed_go_to_top";
	public static final String	PREFERENCE_NEWSFEED_CUT_LONG_POST			= "preference_newsfeed_cut_long_post";
	public static final String	PREFERENCE_APP_LANGUAGE						= "preference_app_language";
	public static final String	PREFERENCE_FB_LANGUAGE						= "preference_fb_language";
	public static final String	NOTIFICATION_READ_STATUS_CHANGE				= "notification_read_status_change";
	public static final String	PREFERENCE_LEFT_DRAWER_MENU_CLASSES			= "preference_left_drawer_items";
	public static final String	PREFERENCE_ALTERNATIVE_NEWSFEED				= "preference_alternative_newsfeed";

	static SharedPreferences getPreferences()
	{
		return PreferenceManager.getDefaultSharedPreferences(KlyphApplication.getInstance());
	}

	public static boolean isNewsfeedPagesEnabled()
	{
		return getPreferences().getBoolean(PREFERENCE_NEWSFEED_PAGES, true);
	}

	public static boolean isNewsfeedGroupsEnabled()
	{
		return getPreferences().getBoolean(PREFERENCE_NEWSFEED_GROUPS, true);
	}

	public static boolean isNewsfeedEventsEnabled()
	{
		return getPreferences().getBoolean(PREFERENCE_NEWSFEED_EVENTS, true);
	}

	public static void setLastStories(String jsonStreams)
	{
		Editor editor = getPreferences().edit();
		editor.putString(PREFERENCE_LAST_STORIES, jsonStreams);
		editor.commit();
	}

	public static String getLastStories()
	{
		return getPreferences().getString(PREFERENCE_LAST_STORIES, "");
	}

	public static boolean notifyAppRequests()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_APP_REQUESTS, false);
	}

	public static boolean notifyAlbums()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_ALBUMS, false);
	}

	public static boolean notifyEvents()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_EVENTS, false);
	}

	public static boolean notifyFriendRequest()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_FRIEND_REQUEST, false);
	}

	public static boolean notifyGroups()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_GROUPS, false);
	}

	public static boolean notifyPages()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_PAGES, false);
	}

	public static boolean notifyPhotos()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_PHOTOS, false);
	}

	public static boolean notifyPokes()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_POKES, false);
	}

	public static boolean notifyStreams()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_STREAMS, false);
	}

	public static boolean notifyVideos()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_VIDEOS, false);
	}

	public static boolean notifyBirthdays()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_BIRTHDAY, false);
	}

	public static boolean notifyNotifications()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS, false);
	}

	public static int getBirthdayNotificationTime()
	{
		String value = getPreferences().getString(PREFERENCE_NOTIFICATIONS_BIRTHDAY_TIME, "10");

		return Integer.parseInt(value);
	}

	public static void setGCMRegId(String regId)
	{
		Editor editor = getPreferences().edit();
		editor.putString(PREFERENCE_GCM_REG_ID, regId);
		editor.commit();
	}

	public static String getGCMRegId()
	{
		return getPreferences().getString(PREFERENCE_GCM_REG_ID, "");
	}

	public static void setNotificationServiceOffset(String offset)
	{
		Editor editor = getPreferences().edit();
		editor.putString(PREFERENCE_NOTIFICATION_SERVICE_OFFSET, offset);
		editor.commit();
	}

	public static String getNotificationServiceOffset()
	{
		String offset = getPreferences().getString(PREFERENCE_NOTIFICATION_SERVICE_OFFSET, "");

		return offset;
	}

	public static void setFriendRequestServiceOffset(String offset)
	{
		Editor editor = getPreferences().edit();
		editor.putString(PREFERENCE_FRIENDREQUEST_SERVICE_OFFSET, offset);
		editor.commit();
	}

	public static String getFriendRequestServiceOffset()
	{
		String offset = getPreferences().getString(PREFERENCE_FRIENDREQUEST_SERVICE_OFFSET, "0");

		return offset;
	}

	public static boolean isPhotoEffectEnabled()
	{
		return getPreferences().getBoolean(PERFORMANCES_PHOTO_EFFECT, true);
	}

	public static boolean isRoundedPictureEnabled()
	{
		return getPreferences().getBoolean(PERFORMANCES_ROUNDED_PICTURES, true);
	}

	public static boolean showTabPageIndicator()
	{
		return getPreferences().getString(PREFERENCE_VIEW_PAGER_INDICATOR_STYLE, "Tabs").equals("Tabs");
	}

	public static boolean showTitlePageIndicator()
	{
		return !showTabPageIndicator();
	}

	public static boolean animateCards()
	{
		return getPreferences().getBoolean(PERFORMANCES_CARD_ANIMATION, true);
	}

	public static void setUserDonated(boolean donated)
	{
		Editor editor = getPreferences().edit();
		editor.putBoolean(PREFERENCE_USER_DONATED, donated);
		editor.commit();
	}

	public static boolean hasUserDonated()
	{
		return getPreferences().getBoolean(PREFERENCE_USER_DONATED, false);
	}

	public static boolean areBannerAdsEnabled()
	{
		return KlyphFlags.BANNER_ADS_ENABLED && KlyphFlags.IS_PRO_VERSION == false && hasUserDonated() == false;
	}

	public static boolean areInterstitialAdsEnabled()
	{
		return KlyphFlags.INTERSTITAL_ADS_ENABLED && KlyphFlags.IS_PRO_VERSION == false && hasUserDonated() == false;
	}

	public static List<String> getMainActivityTabs()
	{
		String defaultSet = StringUtils.join(KlyphApplication.getInstance().getResources().getStringArray(R.array.preferences_main_activity_values),
				",");

		return Arrays.asList(getPreferences().getString(PREFERENCE_MAIN_ACTIVITY_TABS, defaultSet).split(","));
	}

	public static List<String> getUserActivityTabs()
	{
		String defaultSet = StringUtils.join(KlyphApplication.getInstance().getResources().getStringArray(R.array.preferences_user_activity_values),
				",");

		return Arrays.asList(getPreferences().getString(PREFERENCE_USER_ACTIVITY_TABS, defaultSet).split(","));
	}

	public static List<String> getPageActivityTabs()
	{
		String defaultSet = StringUtils.join(KlyphApplication.getInstance().getResources().getStringArray(R.array.preferences_page_activity_values),
				",");

		return Arrays.asList(getPreferences().getString(PREFERENCE_PAGE_ACTIVITY_TABS, defaultSet).split(","));
	}

	public static List<String> getGroupActivityTabs()
	{
		String defaultSet = StringUtils.join(KlyphApplication.getInstance().getResources().getStringArray(R.array.preferences_group_activity_values),
				",");

		return Arrays.asList(getPreferences().getString(PREFERENCE_GROUP_ACTIVITY_TABS, defaultSet).split(","));
	}

	public static boolean mustGroupNotifications()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_GROUP, false);
	}

	public static long getLastCheckedNotificationTime()
	{
		return getPreferences().getLong(PREFERENCE_NOTIFICATIONS_LAST_CHECKED_TIME, 0);
	}

	public static void setLastCheckedNotificationTime(long time)
	{
		Editor editor = getPreferences().edit();
		editor.putLong(PREFERENCE_NOTIFICATIONS_LAST_CHECKED_TIME, time);
		editor.commit();
	}

	public static boolean arePushNotificationsAvailable()
	{
		if (KlyphFlags.IS_AMAZON_VERSION)
		{
			try
			{
				Class.forName("com.amazon.device.messaging.ADM");
			}
			catch (ClassNotFoundException e)
			{
				return false;
			}

			final ADM adm = new ADM(KlyphApplication.getInstance());
			return adm.isSupported();
		}

		return true;
	}

	public static String getNotificationSystem()
	{
		if (arePushNotificationsAvailable())
			return getPreferences().getString(PREFERENCE_NOTIFICATIONS_SYSTEM, PREFERENCE_NOTIFICATIONS_SYSTEM_BOTH);

		return PREFERENCE_NOTIFICATIONS_SYSTEM_PERIODIC;
	}

	public static boolean areNotificationsEnabled()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS, true);
	}

	public static boolean arePushNotificationsEnabled()
	{
		String system = getNotificationSystem();
		return areNotificationsEnabled() && system.equals(PREFERENCE_NOTIFICATIONS_SYSTEM_PUSH)
				|| system.equals(PREFERENCE_NOTIFICATIONS_SYSTEM_BOTH);
	}

	public static boolean arePeriodicNotificationsEnabled()
	{
		String system = getNotificationSystem();
		return areNotificationsEnabled() && system.equals(PREFERENCE_NOTIFICATIONS_SYSTEM_PERIODIC)
				|| system.equals(PREFERENCE_NOTIFICATIONS_SYSTEM_BOTH);
	}

	public static boolean areBirthdayNotificationsEnabled()
	{
		return areNotificationsEnabled() && getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_BIRTHDAY, true);
	}

	public static int getPeriodicNotificationsInterval()
	{
		return Integer.parseInt(getPreferences().getString(PREFERENCE_NOTIFICATIONS_INTERVAL, "10"));
	}

	public static boolean isNotificationVibrationEnabled()
	{
		return getPreferences().getBoolean(PREFERENCE_NOTIFICATIONS_VIBRATE, true);
	}

	public static String getNotificationRingtone()
	{
		return getPreferences().getString(PREFERENCE_NOTIFICATIONS_RINGTONE, "default");
	}

	public static void setNotificationRingtone(String ringtone)
	{
		Editor editor = getPreferences().edit();
		editor.putString(PREFERENCE_NOTIFICATIONS_RINGTONE, ringtone);
		editor.commit();
	}

	public static String getNotificationRingtoneUri()
	{
		return getPreferences().getString(PREFERENCE_NOTIFICATIONS_RINGTONE_URI, null);
	}

	public static void setNotificationRingtoneUri(String uri)
	{
		Editor editor = getPreferences().edit();
		editor.putString(PREFERENCE_NOTIFICATIONS_RINGTONE_URI, uri);
		editor.commit();
	}

	public static boolean getNewsfeedGoToTop()
	{
		return getPreferences().getBoolean(PREFERENCE_NEWSFEED_GO_TO_TOP, true);
	}

	public static boolean getNewsfeedCutLongPost()
	{
		return getPreferences().getBoolean(PREFERENCE_NEWSFEED_CUT_LONG_POST, true);
	}

	public static void setPrivacy(int privacy)
	{
		Editor editor = getPreferences().edit();
		editor.putInt(KlyphPreferences.PREFERENCE_PRIVACY, privacy);
		editor.commit();
	}

	public static int getPrivacy()
	{
		return getPreferences().getInt(KlyphPreferences.PREFERENCE_PRIVACY, 0);
	}

	public static int getTheme()
	{
		if (getPreferences() != null)
		{
			String theme = getPreferences().getString(KlyphPreferences.PREFERENCE_THEME,
					KlyphApplication.getInstance().getString(R.string.theme_light_blue));

			if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_light_blue)))
			{
				return R.style.Klyph_Light_Blue;
			}
			else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_dark_blue)))
			{
				return R.style.Klyph_Dark_Blue;
			}
			else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_black_blue)))
			{
				return R.style.Klyph_Black_Blue;
			}
			else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_light_green)))
			{
				return R.style.Klyph_Light_Green;
			}
			else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_dark_green)))
			{
				return R.style.Klyph_Dark_Green;
			}
			else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_black_green)))
			{
				return R.style.Klyph_Black_Green;
			}
		}

		return R.style.Klyph_Light_Blue;
	}

	public static int getPreferencesTheme()
	{
		String theme = getPreferences().getString(KlyphPreferences.PREFERENCE_THEME,
				KlyphApplication.getInstance().getString(R.string.theme_light_blue));

		if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_light_blue)))
		{
			return android.R.style.Theme_Holo_Light;
		}
		else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_dark_blue)))
		{
			return android.R.style.Theme_Holo;
		}
		else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_black_blue)))
		{
			return android.R.style.Theme_Holo;
		}
		else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_light_green)))
		{
			return android.R.style.Theme_Holo_Light;
		}
		else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_dark_green)))
		{
			return android.R.style.Theme_Holo;
		}
		else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_black_green)))
		{
			return android.R.style.Theme_Holo;
		}

		return android.R.style.Theme_Holo_Light;
	}

	public static int getProfileTheme()
	{
		if (getPreferences() != null)
		{
			String theme = getPreferences().getString(KlyphPreferences.PREFERENCE_THEME,
					KlyphApplication.getInstance().getString(R.string.theme_light_blue));

			if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_light_blue)))
			{
				return R.style.Klyph_Light_Blue_Profile;
			}
			else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_dark_blue)))
			{
				return R.style.Klyph_Dark_Blue_Profile;
			}
			else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_black_blue)))
			{
				return R.style.Klyph_Black_Blue_Profile;
			}
			else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_light_green)))
			{
				return R.style.Klyph_Light_Green_Profile;
			}
			else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_dark_green)))
			{
				return R.style.Klyph_Dark_Green_Profile;
			}
			else if (theme.equals(KlyphApplication.getInstance().getString(R.string.theme_black_green)))
			{
				return R.style.Klyph_Black_Green_Profile;
			}
		}

		return R.style.Klyph_Light_Blue_Profile;
	}

	public static void setNotificationReadStatusChanged(boolean changed)
	{
		Editor editor = getPreferences().edit();
		editor.putBoolean(KlyphPreferences.NOTIFICATION_READ_STATUS_CHANGE, changed);
		editor.commit();
	}

	public static boolean hasNotificationReadStatusChanged()
	{
		return getPreferences().getBoolean(KlyphPreferences.NOTIFICATION_READ_STATUS_CHANGE, false);
	}

	public static List<String> getLeftDrawerMenuClasses()
	{
		String defaultSet = StringUtils.join(KlyphApplication.getInstance().getResources().getStringArray(R.array.nav_classes), ",");

		return Arrays.asList(getPreferences().getString(PREFERENCE_LEFT_DRAWER_MENU_CLASSES, defaultSet).split(","));
	}

	public static List<String> getLeftDrawerMenuLabels()
	{
		final String[] names = KlyphApplication.getInstance().getResources().getStringArray(R.array.nav_names);
		final String[] classnames = KlyphApplication.getInstance().getResources().getStringArray(R.array.nav_classes);

		final List<String> classes = getLeftDrawerMenuClasses();

		List<String> items = new ArrayList<String>();

		for (String string : classes)
		{
			items.add(names[ArrayUtils.indexOf(classnames, string)]);
		}

		return items;
	}
	
	public static boolean isAlternativeNewsfeed()
	{
		return getPreferences().getBoolean(KlyphPreferences.PREFERENCE_ALTERNATIVE_NEWSFEED, false);
	}
}
