package com.abewy.android.apps.klyph.core.graph;

import java.util.ArrayList;

public class Application extends GraphObject
{
	private String				id;
	private String				name;
	private String				description;
	private String				category;
	private String				company;
	private String				icon_url;
	private String				subcategory;
	private String				link;
	private String				logo_url;
	private String				daily_active_users;
	private String				weekly_active_users;
	private String				monthly_active_users;
	private ArrayList<Object>	migration;
	private String				namespace;
	// private Restrictions restrictions;
	private ArrayList<Object>	app_domains;
	private String				auth_dialog_data_help_url;
	private String				auth_dialog_description;
	private String				auth_dialog_headline;
	private String				auth_dialog_perms_explanation;
	private ArrayList<String>	auth_referal_user_perms;
	private ArrayList<String>	auth_referal_friend_perms;
	private String				auth_referal_default_activity_privacy;
	private boolean				auth_referal_enabled;
	private ArrayList<String>	auth_referal_extended_perms;
	private String				auth_referal_response_type;
	private boolean				canvas_fluid_height;
	private boolean				canvas_fluid_width;
	private String				canvas_fluid_url;
	private String				contact_email;
	private int					created_time;
	private int					creator_uid;
	private String				deauth_callback_url;
	private String				iphone_app_store_id;
	private String				hosting_url;
	private String				mobile_web_url;
	private String				page_tab_default_name;
	private String				page_tab_url;
	private String				privacy_policy_url;
	private String				secure_canvas_url;
	private String				secure_page_tab_url;
	private String				server_ip_whitelist;
	private boolean				social_discovery;
	private String				terms_of_service_url;
	private String				user_support_email;
	private String				user_support_url;
	private String				website_url;

	public Application()
	{

	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getCompany()
	{
		return company;
	}

	public void setCompany(String company)
	{
		this.company = company;
	}

	public String getIcon_url()
	{
		return icon_url;
	}

	public void setIcon_url(String icon_url)
	{
		this.icon_url = icon_url;
	}

	public String getSubcategory()
	{
		return subcategory;
	}

	public void setSubcategory(String subcategory)
	{
		this.subcategory = subcategory;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getLogo_url()
	{
		return logo_url;
	}

	public void setLogo_url(String logo_url)
	{
		this.logo_url = logo_url;
	}

	public String getDaily_active_users()
	{
		return daily_active_users;
	}

	public void setDaily_active_users(String daily_active_users)
	{
		this.daily_active_users = daily_active_users;
	}

	public String getWeekly_active_users()
	{
		return weekly_active_users;
	}

	public void setWeekly_active_users(String weekly_active_users)
	{
		this.weekly_active_users = weekly_active_users;
	}

	public String getMonthly_active_users()
	{
		return monthly_active_users;
	}

	public void setMonthly_active_users(String monthly_active_users)
	{
		this.monthly_active_users = monthly_active_users;
	}

	public ArrayList<Object> getMigration()
	{
		return migration;
	}

	public void setMigration(ArrayList<Object> migration)
	{
		this.migration = migration;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	public ArrayList<Object> getApp_domains()
	{
		return app_domains;
	}

	public void setApp_domains(ArrayList<Object> app_domains)
	{
		this.app_domains = app_domains;
	}

	public String getAuth_dialog_data_help_url()
	{
		return auth_dialog_data_help_url;
	}

	public void setAuth_dialog_data_help_url(String auth_dialog_data_help_url)
	{
		this.auth_dialog_data_help_url = auth_dialog_data_help_url;
	}

	public String getAuth_dialog_description()
	{
		return auth_dialog_description;
	}

	public void setAuth_dialog_description(String auth_dialog_description)
	{
		this.auth_dialog_description = auth_dialog_description;
	}

	public String getAuth_dialog_headline()
	{
		return auth_dialog_headline;
	}

	public void setAuth_dialog_headline(String auth_dialog_headline)
	{
		this.auth_dialog_headline = auth_dialog_headline;
	}

	public String getAuth_dialog_perms_explanation()
	{
		return auth_dialog_perms_explanation;
	}

	public void setAuth_dialog_perms_explanation(
			String auth_dialog_perms_explanation)
	{
		this.auth_dialog_perms_explanation = auth_dialog_perms_explanation;
	}

	public ArrayList<String> getAuth_referal_user_perms()
	{
		return auth_referal_user_perms;
	}

	public void setAuth_referal_user_perms(ArrayList<String> auth_referal_user_perms)
	{
		this.auth_referal_user_perms = auth_referal_user_perms;
	}

	public ArrayList<String> getAuth_referal_friend_perms()
	{
		return auth_referal_friend_perms;
	}

	public void setAuth_referal_friend_perms(
			ArrayList<String> auth_referal_friend_perms)
	{
		this.auth_referal_friend_perms = auth_referal_friend_perms;
	}

	public String getAuth_referal_default_activity_privacy()
	{
		return auth_referal_default_activity_privacy;
	}

	public void setAuth_referal_default_activity_privacy(
			String auth_referal_default_activity_privacy)
	{
		this.auth_referal_default_activity_privacy = auth_referal_default_activity_privacy;
	}

	public boolean getAuth_referal_enabled()
	{
		return auth_referal_enabled;
	}

	public void setAuth_referal_enabled(boolean auth_referal_enabled)
	{
		this.auth_referal_enabled = auth_referal_enabled;
	}

	public ArrayList<String> getAuth_referal_extended_perms()
	{
		return auth_referal_extended_perms;
	}

	public void setAuth_referal_extended_perms(
			ArrayList<String> auth_referal_extended_perms)
	{
		this.auth_referal_extended_perms = auth_referal_extended_perms;
	}

	public String getAuth_referal_response_type()
	{
		return auth_referal_response_type;
	}

	public void setAuth_referal_response_type(String auth_referal_response_type)
	{
		this.auth_referal_response_type = auth_referal_response_type;
	}

	public boolean getCanvas_fluid_height()
	{
		return canvas_fluid_height;
	}

	public void setCanvas_fluid_height(boolean canvas_fluid_height)
	{
		this.canvas_fluid_height = canvas_fluid_height;
	}

	public boolean getCanvas_fluid_width()
	{
		return canvas_fluid_width;
	}

	public void setCanvas_fluid_width(boolean canvas_fluid_width)
	{
		this.canvas_fluid_width = canvas_fluid_width;
	}

	public String getCanvas_fluid_url()
	{
		return canvas_fluid_url;
	}

	public void setCanvas_fluid_url(String canvas_fluid_url)
	{
		this.canvas_fluid_url = canvas_fluid_url;
	}

	public String getContact_email()
	{
		return contact_email;
	}

	public void setContact_email(String contact_email)
	{
		this.contact_email = contact_email;
	}

	public int getCreated_time()
	{
		return created_time;
	}

	public void setCreated_time(int created_time)
	{
		this.created_time = created_time;
	}

	public int getCreator_uid()
	{
		return creator_uid;
	}

	public void setCreator_uid(int creator_uid)
	{
		this.creator_uid = creator_uid;
	}

	public String getDeauth_callback_url()
	{
		return deauth_callback_url;
	}

	public void setDeauth_callback_url(String deauth_callback_url)
	{
		this.deauth_callback_url = deauth_callback_url;
	}

	public String getIphone_app_store_id()
	{
		return iphone_app_store_id;
	}

	public void setIphone_app_store_id(String iphone_app_store_id)
	{
		this.iphone_app_store_id = iphone_app_store_id;
	}

	public String getHosting_url()
	{
		return hosting_url;
	}

	public void setHosting_url(String hosting_url)
	{
		this.hosting_url = hosting_url;
	}

	public String getMobile_web_url()
	{
		return mobile_web_url;
	}

	public void setMobile_web_url(String mobile_web_url)
	{
		this.mobile_web_url = mobile_web_url;
	}

	public String getPage_tab_default_name()
	{
		return page_tab_default_name;
	}

	public void setPage_tab_default_name(String page_tab_default_name)
	{
		this.page_tab_default_name = page_tab_default_name;
	}

	public String getPage_tab_url()
	{
		return page_tab_url;
	}

	public void setPage_tab_url(String page_tab_url)
	{
		this.page_tab_url = page_tab_url;
	}

	public String getPrivacy_policy_url()
	{
		return privacy_policy_url;
	}

	public void setPrivacy_policy_url(String privacy_policy_url)
	{
		this.privacy_policy_url = privacy_policy_url;
	}

	public String getSecure_canvas_url()
	{
		return secure_canvas_url;
	}

	public void setSecure_canvas_url(String secure_canvas_url)
	{
		this.secure_canvas_url = secure_canvas_url;
	}

	public String getSecure_page_tab_url()
	{
		return secure_page_tab_url;
	}

	public void setSecure_page_tab_url(String secure_page_tab_url)
	{
		this.secure_page_tab_url = secure_page_tab_url;
	}

	public String getServer_ip_whitelist()
	{
		return server_ip_whitelist;
	}

	public void setServer_ip_whitelist(String server_ip_whitelist)
	{
		this.server_ip_whitelist = server_ip_whitelist;
	}

	public boolean getSocial_discovery()
	{
		return social_discovery;
	}

	public void setSocial_discovery(boolean social_discovery)
	{
		this.social_discovery = social_discovery;
	}

	public String getTerms_of_service_url()
	{
		return terms_of_service_url;
	}

	public void setTerms_of_service_url(String terms_of_service_url)
	{
		this.terms_of_service_url = terms_of_service_url;
	}

	public String getUser_support_email()
	{
		return user_support_email;
	}

	public void setUser_support_email(String user_support_email)
	{
		this.user_support_email = user_support_email;
	}

	public String getUser_support_url()
	{
		return user_support_url;
	}

	public void setUser_support_url(String user_support_url)
	{
		this.user_support_url = user_support_url;
	}

	public String getWebsite_url()
	{
		return website_url;
	}

	public void setWebsite_url(String website_url)
	{
		this.website_url = website_url;
	}
}
