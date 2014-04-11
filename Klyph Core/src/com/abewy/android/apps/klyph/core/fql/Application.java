package com.abewy.android.apps.klyph.core.fql;

import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Application extends GraphObject implements Parcelable
{
	// private static KlyphAdapter adapter;

	private String	api_key;
	private String	app_id;
	private String	app_name;
	private String	app_type;
	private String	appcenter_icon_url;
	private String	category;
	private String	company_name;
	private String	contact_email;
	private String	created_time;
	private String	creator_uid;
	private int		daily_active_users;
	private String	description;
	private String	display_name;
	private String	icon_url;
	private String	link;
	private String	logo_url;
	private int		monthly_active_users;
	private String	namespace;
	private String	subcategory;
	private int		weekly_active_users;

	public Application()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.APPLICATION;
	}

	/*
	 * @Override public KlyphAdapter getAdapter(Context context, int
	 * specialLayout) { if (adapter == null) adapter = new
	 * AlbumAdapter(context); return adapter; }
	 * @Override public void resetAdapter() { adapter = null; }
	 */

	public String getApi_key()
	{
		return api_key;
	}

	public void setApi_key(String api_key)
	{
		this.api_key = api_key;
	}

	public String getApp_id()
	{
		return app_id;
	}

	public void setApp_id(String app_id)
	{
		this.app_id = app_id;
	}

	public String getApp_name()
	{
		return app_name;
	}

	public void setApp_name(String app_name)
	{
		this.app_name = app_name;
	}

	public String getApp_type()
	{
		return app_type;
	}

	public void setApp_type(String app_type)
	{
		this.app_type = app_type;
	}

	public String getAppcenter_icon_url()
	{
		return appcenter_icon_url;
	}

	public void setAppcenter_icon_url(String appcenter_icon_url)
	{
		this.appcenter_icon_url = appcenter_icon_url;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getCompany_name()
	{
		return company_name;
	}

	public void setCompany_name(String company_name)
	{
		this.company_name = company_name;
	}

	public String getContact_email()
	{
		return contact_email;
	}

	public void setContact_email(String contact_email)
	{
		this.contact_email = contact_email;
	}

	public String getCreated_time()
	{
		return created_time;
	}

	public void setCreated_time(String created_time)
	{
		this.created_time = created_time;
	}

	public String getCreator_uid()
	{
		return creator_uid;
	}

	public void setCreator_uid(String creator_uid)
	{
		this.creator_uid = creator_uid;
	}

	public int getDaily_active_users()
	{
		return daily_active_users;
	}

	public void setDaily_active_users(int daily_active_users)
	{
		this.daily_active_users = daily_active_users;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDisplay_name()
	{
		return display_name;
	}

	public void setDisplay_name(String display_name)
	{
		this.display_name = display_name;
	}

	public String getIcon_url()
	{
		return icon_url;
	}

	public void setIcon_url(String icon_url)
	{
		this.icon_url = icon_url;
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

	public int getMonthly_active_users()
	{
		return monthly_active_users;
	}

	public void setMonthly_active_users(int monthly_active_users)
	{
		this.monthly_active_users = monthly_active_users;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	public String getSubcategory()
	{
		return subcategory;
	}

	public void setSubcategory(String subcategory)
	{
		this.subcategory = subcategory;
	}

	public int getWeekly_active_users()
	{
		return weekly_active_users;
	}

	public void setWeekly_active_users(int weekly_active_users)
	{
		this.weekly_active_users = weekly_active_users;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(api_key);
		dest.writeString(app_id);
		dest.writeString(app_name);
		dest.writeString(app_type);
		dest.writeString(appcenter_icon_url);
		dest.writeString(category);
		dest.writeString(company_name);
		dest.writeString(contact_email);
		dest.writeString(created_time);
		dest.writeString(creator_uid);
		dest.writeInt(daily_active_users);
		dest.writeString(description);
		dest.writeString(display_name);
		dest.writeString(icon_url);
		dest.writeString(link);
		dest.writeString(logo_url);
		dest.writeInt(monthly_active_users);
		dest.writeString(namespace);
		dest.writeString(subcategory);
		dest.writeInt(weekly_active_users);
	}

	public static final Parcelable.Creator<Application>	CREATOR	= new Parcelable.Creator<Application>() {
																	public Application createFromParcel(Parcel in)
																	{
																		return new Application(in);
																	}

																	public Application[] newArray(int size)
																	{
																		return new Application[size];
																	}
																};

	private Application(Parcel in)
	{
		api_key = in.readString();
		app_id = in.readString();
		app_name = in.readString();
		app_type = in.readString();
		appcenter_icon_url = in.readString();
		category = in.readString();
		company_name = in.readString();
		contact_email = in.readString();
		created_time = in.readString();
		creator_uid = in.readString();
		daily_active_users = in.readInt();
		description = in.readString();
		display_name = in.readString();
		icon_url = in.readString();
		link = in.readString();
		logo_url = in.readString();
		monthly_active_users = in.readInt();
		namespace = in.readString();
		subcategory = in.readString();
		weekly_active_users = in.readInt();
	}
}
