package com.abewy.android.apps.klyph.core.fql;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.R;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Page extends GraphObject implements Parcelable
{
	private String					about;
	private String					access_token;
	private String					affiliation;
	private String					app_id;
	private String					artists_we_like;
	private String					attire;
	private String					awards;
	private String					band_interests;
	private String					band_members;
	private String					bio;
	private String					birthday;
	private String					booking_agent;
	private String					built;
	private boolean					can_post;
	private List<String>			categories;
	private int						checkins;
	private String					company_overview;
	private String					culinary_team;
	private String					current_location;
	private String					description;
	private String					description_html;
	private String					directed_by;
	private int						fan_count;
	private String					features;
	private List<String>			food_styles;
	private String					founded;
	private String					general_info;
	private String					general_manager;
	private String					genre;
	private int						global_brand_page_name;
	private int						global_brand_parent_page_id;
	private boolean					has_added_app;
	private String					hometown;
	private Hours					hours;
	private String					influences;
	private boolean					is_community_page;
	private boolean					is_fan;
	private boolean					is_published;
	private String					keywords;
	private Location				location;
	private String					members;
	private String					mission;
	private String					mpg;
	private String					name;
	private String					network;
	private int						new_like_count;
	private boolean					offer_eligible;
	private String					page_id;
	private String					page_url;
	private String					parent_page;
	private Parking					parking;
	private PaymentOptions			payment_options;
	private String					personal_info;
	private String					personal_interests;
	private String					pharma_safety_info;
	private String					phone;
	private String					pic;
	private String					pic_big;
	private Cover					pic_cover;
	private String					pic_large;
	private String					pic_small;
	private String					pic_square;
	private String					plot_outline;
	private String					press_contact;
	private String					price_range;
	private String					produced_by;
	private String					products;
	private String					public_transit;
	private String					record_label;
	private String					release_date;
	private RestaurantServices		restaurant_services;
	private RestaurantSpecialties	restaurant_specialties;
	private String					schedule;
	private String					screenplay_by;
	private String					season;
	private String					starring;
	private String					studio;
	private int						talking_about_count;
	private String					type;
	private int						unread_message_count;
	private int						unseen_message_count;
	private int						unread_notif_count;
	private String					username;
	private String					website;
	private int						were_here_count;
	private String					written_by;
	private int						global_brand_like_count;
	private int						global_brand_talking_about_count;

	public Page()
	{

	}

	@Override
	public int getItemViewType()
	{
		return GraphObject.PAGE;
	}

	public String getAbout()
	{
		return about;
	}

	public void setAbout(String about)
	{
		this.about = about;
	}

	public String getAccess_token()
	{
		return access_token;
	}

	public void setAccess_token(String access_token)
	{
		this.access_token = access_token;
	}

	public String getAffiliation()
	{
		return affiliation;
	}

	public void setAffiliation(String affiliation)
	{
		this.affiliation = affiliation;
	}

	public String getApp_id()
	{
		return app_id;
	}

	public void setApp_id(String app_id)
	{
		this.app_id = app_id;
	}

	public String getArtists_we_like()
	{
		return artists_we_like;
	}

	public void setArtists_we_like(String artists_we_like)
	{
		this.artists_we_like = artists_we_like;
	}

	public String getAttire()
	{
		return attire;
	}

	public void setAttire(String attire)
	{
		this.attire = attire;
	}

	public String getAwards()
	{
		return awards;
	}

	public void setAwards(String awards)
	{
		this.awards = awards;
	}

	public String getBand_interests()
	{
		return band_interests;
	}

	public void setBand_interests(String band_interests)
	{
		this.band_interests = band_interests;
	}

	public String getBand_members()
	{
		return band_members;
	}

	public void setBand_members(String band_members)
	{
		this.band_members = band_members;
	}

	public String getBio()
	{
		return bio;
	}

	public void setBio(String bio)
	{
		this.bio = bio;
	}

	public String getBirthday()
	{
		return birthday;
	}

	public void setBirthday(String birthday)
	{
		this.birthday = birthday;
	}

	public String getBooking_agent()
	{
		return booking_agent;
	}

	public void setBooking_agent(String booking_agent)
	{
		this.booking_agent = booking_agent;
	}

	public String getBuilt()
	{
		return built;
	}

	public void setBuilt(String built)
	{
		this.built = built;
	}

	public boolean getCan_post()
	{
		return can_post;
	}

	public void setCan_post(boolean can_post)
	{
		this.can_post = can_post;
	}

	public List<String> getCategories()
	{
		return categories;
	}

	public void setCategories(List<String> categories)
	{
		this.categories = categories;
	}

	public int getCheckins()
	{
		return checkins;
	}

	public void setCheckins(int checkins)
	{
		this.checkins = checkins;
	}

	public String getCompany_overview()
	{
		return company_overview;
	}

	public void setCompany_overview(String company_overview)
	{
		this.company_overview = company_overview;
	}

	public String getCulinary_team()
	{
		return culinary_team;
	}

	public void setCulinary_team(String culinary_team)
	{
		this.culinary_team = culinary_team;
	}

	public String getCurrent_location()
	{
		return current_location;
	}

	public void setCurrent_location(String current_location)
	{
		this.current_location = current_location;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription_html()
	{
		return description_html;
	}

	public void setDescription_html(String description_html)
	{
		this.description_html = description_html;
	}

	public String getDirected_by()
	{
		return directed_by;
	}

	public void setDirected_by(String directed_by)
	{
		this.directed_by = directed_by;
	}

	public int getFan_count()
	{
		return fan_count;
	}

	public void setFan_count(int fan_count)
	{
		this.fan_count = fan_count;
	}

	public String getFeatures()
	{
		return features;
	}

	public void setFeatures(String features)
	{
		this.features = features;
	}

	public List<String> getFood_styles()
	{
		return food_styles;
	}

	public void setFood_styles(List<String> food_styles)
	{
		this.food_styles = food_styles;
	}

	public String getFounded()
	{
		return founded;
	}

	public void setFounded(String founded)
	{
		this.founded = founded;
	}

	public String getGeneral_info()
	{
		return general_info;
	}

	public void setGeneral_info(String general_info)
	{
		this.general_info = general_info;
	}

	public String getGeneral_manager()
	{
		return general_manager;
	}

	public void setGeneral_manager(String general_manager)
	{
		this.general_manager = general_manager;
	}

	public String getGenre()
	{
		return genre;
	}

	public void setGenre(String genre)
	{
		this.genre = genre;
	}

	public int getGlobal_brand_page_name()
	{
		return global_brand_page_name;
	}

	public void setGlobal_brand_page_name(int global_brand_page_name)
	{
		this.global_brand_page_name = global_brand_page_name;
	}

	public int getGlobal_brand_parent_page_id()
	{
		return global_brand_parent_page_id;
	}

	public void setGlobal_brand_parent_page_id(int global_brand_parent_page_id)
	{
		this.global_brand_parent_page_id = global_brand_parent_page_id;
	}

	public boolean getHas_added_app()
	{
		return has_added_app;
	}

	public void setHas_added_app(boolean has_added_app)
	{
		this.has_added_app = has_added_app;
	}

	public String getHometown()
	{
		return hometown;
	}

	public void setHometown(String hometown)
	{
		this.hometown = hometown;
	}

	public Hours getHours()
	{
		return hours;
	}

	public void setHours(Hours hours)
	{
		this.hours = hours;
	}

	public String getInfluences()
	{
		return influences;
	}

	public void setInfluences(String influences)
	{
		this.influences = influences;
	}

	public boolean getIs_community_page()
	{
		return is_community_page;
	}

	public void setIs_community_page(boolean is_community_page)
	{
		this.is_community_page = is_community_page;
	}

	public boolean getIs_fan()
	{
		return is_fan;
	}

	public void setIs_fan(boolean is_fan)
	{
		this.is_fan = is_fan;
	}

	public boolean getIs_published()
	{
		return is_published;
	}

	public void setIs_published(boolean is_published)
	{
		this.is_published = is_published;
	}

	public String getKeywords()
	{
		return keywords;
	}

	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}

	public Location getLocation()
	{
		return location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

	public String getMembers()
	{
		return members;
	}

	public void setMembers(String members)
	{
		this.members = members;
	}

	public String getMission()
	{
		return mission;
	}

	public void setMission(String mission)
	{
		this.mission = mission;
	}

	public String getMpg()
	{
		return mpg;
	}

	public void setMpg(String mpg)
	{
		this.mpg = mpg;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNetwork()
	{
		return network;
	}

	public void setNetwork(String network)
	{
		this.network = network;
	}

	public int getNew_like_count()
	{
		return new_like_count;
	}

	public void setNew_like_count(int new_like_count)
	{
		this.new_like_count = new_like_count;
	}

	public boolean getOffer_eligible()
	{
		return offer_eligible;
	}

	public void setOffer_eligible(boolean offer_eligible)
	{
		this.offer_eligible = offer_eligible;
	}

	public String getPage_id()
	{
		return page_id;
	}

	public void setPage_id(String page_id)
	{
		this.page_id = page_id;
	}

	public String getPage_url()
	{
		return page_url;
	}

	public void setPage_url(String page_url)
	{
		this.page_url = page_url;
	}

	public String getParent_page()
	{
		return parent_page;
	}

	public void setParent_page(String parent_page)
	{
		this.parent_page = parent_page;
	}

	public Parking getParking()
	{
		return parking;
	}

	public void setParking(Parking parking)
	{
		this.parking = parking;
	}

	public PaymentOptions getPayment_options()
	{
		return payment_options;
	}

	public void setPayment_options(PaymentOptions payments_options)
	{
		this.payment_options = payments_options;
	}

	public String getPersonal_info()
	{
		return personal_info;
	}

	public void setPersonal_info(String personal_info)
	{
		this.personal_info = personal_info;
	}

	public String getPersonal_interests()
	{
		return personal_interests;
	}

	public void setPersonal_interests(String personal_interests)
	{
		this.personal_interests = personal_interests;
	}

	public String getPharma_safety_info()
	{
		return pharma_safety_info;
	}

	public void setPharma_safety_info(String pharma_safety_info)
	{
		this.pharma_safety_info = pharma_safety_info;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getPic()
	{
		return pic;
	}

	public void setPic(String pic)
	{
		this.pic = pic;
	}

	public String getPic_big()
	{
		return pic_big;
	}

	public void setPic_big(String pic_big)
	{
		this.pic_big = pic_big;
	}

	public Cover getPic_cover()
	{
		return pic_cover;
	}

	public void setPic_cover(Cover pic_cover)
	{
		this.pic_cover = pic_cover;
	}

	public String getPic_large()
	{
		return pic_large;
	}

	public void setPic_large(String pic_large)
	{
		this.pic_large = pic_large;
	}

	public String getPic_small()
	{
		return pic_small;
	}

	public void setPic_small(String pic_small)
	{
		this.pic_small = pic_small;
	}

	public String getPic_square()
	{
		return pic_square;
	}

	public void setPic_square(String pic_square)
	{
		this.pic_square = pic_square;
	}

	public String getPlot_outline()
	{
		return plot_outline;
	}

	public void setPlot_outline(String plot_outline)
	{
		this.plot_outline = plot_outline;
	}

	public String getPress_contact()
	{
		return press_contact;
	}

	public void setPress_contact(String press_contact)
	{
		this.press_contact = press_contact;
	}

	public String getPrice_range()
	{
		return price_range;
	}

	public void setPrice_range(String price_range)
	{
		this.price_range = price_range;
	}

	public String getProduced_by()
	{
		return produced_by;
	}

	public void setProduced_by(String produced_by)
	{
		this.produced_by = produced_by;
	}

	public String getProducts()
	{
		return products;
	}

	public void setProducts(String products)
	{
		this.products = products;
	}

	public String getPublic_transit()
	{
		return public_transit;
	}

	public void setPublic_transit(String public_transit)
	{
		this.public_transit = public_transit;
	}

	public String getRecord_label()
	{
		return record_label;
	}

	public void setRecord_label(String record_label)
	{
		this.record_label = record_label;
	}

	public String getRelease_date()
	{
		return release_date;
	}

	public void setRelease_date(String release_date)
	{
		this.release_date = release_date;
	}

	public RestaurantServices getRestaurant_services()
	{
		return restaurant_services;
	}

	public void setRestaurant_services(RestaurantServices restaurant_services)
	{
		this.restaurant_services = restaurant_services;
	}

	public RestaurantSpecialties getRestaurant_specialties()
	{
		return restaurant_specialties;
	}

	public void setRestaurant_specialties(RestaurantSpecialties restaurant_specialties)
	{
		this.restaurant_specialties = restaurant_specialties;
	}

	public String getSchedule()
	{
		return schedule;
	}

	public void setSchedule(String schedule)
	{
		this.schedule = schedule;
	}

	public String getScreenplay_by()
	{
		return screenplay_by;
	}

	public void setScreenplay_by(String screenplay_by)
	{
		this.screenplay_by = screenplay_by;
	}

	public String getSeason()
	{
		return season;
	}

	public void setSeason(String season)
	{
		this.season = season;
	}

	public String getStarring()
	{
		return starring;
	}

	public void setStarring(String starring)
	{
		this.starring = starring;
	}

	public String getStudio()
	{
		return studio;
	}

	public void setStudio(String studio)
	{
		this.studio = studio;
	}

	public int getTalking_about_count()
	{
		return talking_about_count;
	}

	public void setTalking_about_count(int talking_about_count)
	{
		this.talking_about_count = talking_about_count;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getUnread_message_count()
	{
		return unread_message_count;
	}

	public void setUnread_message_count(int unread_message_count)
	{
		this.unread_message_count = unread_message_count;
	}

	public int getUnseen_message_count()
	{
		return unseen_message_count;
	}

	public void setUnseen_message_count(int unseen_message_count)
	{
		this.unseen_message_count = unseen_message_count;
	}

	public int getUnread_notif_count()
	{
		return unread_notif_count;
	}

	public void setUnread_notif_count(int unread_notif_count)
	{
		this.unread_notif_count = unread_notif_count;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getWebsite()
	{
		return website;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}

	public int getWere_here_count()
	{
		return were_here_count;
	}

	public void setWere_here_count(int were_here_count)
	{
		this.were_here_count = were_here_count;
	}

	public String getWritten_by()
	{
		return written_by;
	}

	public void setWritten_by(String written_by)
	{
		this.written_by = written_by;
	}

	public int getGlobal_brand_like_count()
	{
		return global_brand_like_count;
	}

	public void setGlobal_brand_like_count(int global_brand_like_count)
	{
		this.global_brand_like_count = global_brand_like_count;
	}

	public int getGlobal_brand_talking_about_count()
	{
		return global_brand_talking_about_count;
	}

	public void setGlobal_brand_talking_about_count(int global_brand_talking_about_count)
	{
		this.global_brand_talking_about_count = global_brand_talking_about_count;
	}

	public static class Cover extends GraphObject implements Parcelable
	{
		private String	cover_id;
		private String	source;
		private int		offset_y;

		public Cover()
		{
			
		}
		
		public String getCover_id()
		{
			return cover_id;
		}

		public void setCover_id(String cover_id)
		{
			this.cover_id = cover_id;
		}

		public String getSource()
		{
			return source;
		}

		public void setSource(String source)
		{
			this.source = source;
		}

		public int getOffset_y()
		{
			return offset_y;
		}

		public void setOffset_y(int offset_y)
		{
			this.offset_y = offset_y;
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeString(cover_id);
			dest.writeString(source);
			dest.writeInt(offset_y);
		}

		public static final Parcelable.Creator<Cover>	CREATOR	= new Parcelable.Creator<Cover>() {
																	public Cover createFromParcel(Parcel in)
																	{
																		return new Cover(in);
																	}

																	public Cover[] newArray(int size)
																	{
																		return new Cover[size];
																	}
																};

		private Cover(Parcel in)
		{
			cover_id = in.readString();
			source = in.readString();
			offset_y = in.readInt();
		}
	}

	public static class Hours extends GraphObject implements Parcelable
	{
		private String	mon_1_open;
		private String	mon_1_close;
		private String	tue_1_open;
		private String	tue_1_close;
		private String	wed_1_open;
		private String	wed_1_close;
		private String	thu_1_open;
		private String	thu_1_close;
		private String	fri_1_open;
		private String	fri_1_close;
		private String	sat_1_open;
		private String	sat_1_close;
		private String	sun_1_open;
		private String	sun_1_close;
		private String	mon_2_open;
		private String	mon_2_close;
		private String	tue_2_open;
		private String	tue_2_close;
		private String	wed_2_open;
		private String	wed_2_close;
		private String	thu_2_open;
		private String	thu_2_close;
		private String	fri_2_open;
		private String	fri_2_close;
		private String	sat_2_open;
		private String	sat_2_close;
		private String	sun_2_open;
		private String	sun_2_close;

		public Hours()
		{
			
		}
		
		public String getMon_1_open()
		{
			return mon_1_open;
		}

		public void setMon_1_open(String mon_1_open)
		{
			this.mon_1_open = mon_1_open;
		}

		public String getMon_1_close()
		{
			return mon_1_close;
		}

		public void setMon_1_close(String mon_1_close)
		{
			this.mon_1_close = mon_1_close;
		}

		public String getTue_1_open()
		{
			return tue_1_open;
		}

		public void setTue_1_open(String tue_1_open)
		{
			this.tue_1_open = tue_1_open;
		}

		public String getTue_1_close()
		{
			return tue_1_close;
		}

		public void setTue_1_close(String tue_1_close)
		{
			this.tue_1_close = tue_1_close;
		}

		public String getWed_1_open()
		{
			return wed_1_open;
		}

		public void setWed_1_open(String wed_1_open)
		{
			this.wed_1_open = wed_1_open;
		}

		public String getWed_1_close()
		{
			return wed_1_close;
		}

		public void setWed_1_close(String wed_1_close)
		{
			this.wed_1_close = wed_1_close;
		}

		public String getThu_1_open()
		{
			return thu_1_open;
		}

		public void setThu_1_open(String thu_1_open)
		{
			this.thu_1_open = thu_1_open;
		}

		public String getThu_1_close()
		{
			return thu_1_close;
		}

		public void setThu_1_close(String thu_1_close)
		{
			this.thu_1_close = thu_1_close;
		}

		public String getFri_1_open()
		{
			return fri_1_open;
		}

		public void setFri_1_open(String fri_1_open)
		{
			this.fri_1_open = fri_1_open;
		}

		public String getFri_1_close()
		{
			return fri_1_close;
		}

		public void setFri_1_close(String fri_1_close)
		{
			this.fri_1_close = fri_1_close;
		}

		public String getSat_1_open()
		{
			return sat_1_open;
		}

		public void setSat_1_open(String sat_1_open)
		{
			this.sat_1_open = sat_1_open;
		}

		public String getSat_1_close()
		{
			return sat_1_close;
		}

		public void setSat_1_close(String sat_1_close)
		{
			this.sat_1_close = sat_1_close;
		}

		public String getSun_1_open()
		{
			return sun_1_open;
		}

		public void setSun_1_open(String sun_1_open)
		{
			this.sun_1_open = sun_1_open;
		}

		public String getSun_1_close()
		{
			return sun_1_close;
		}

		public void setSun_1_close(String sun_1_close)
		{
			this.sun_1_close = sun_1_close;
		}

		public String getMon_2_open()
		{
			return mon_2_open;
		}

		public void setMon_2_open(String mon_2_open)
		{
			this.mon_2_open = mon_2_open;
		}

		public String getMon_2_close()
		{
			return mon_2_close;
		}

		public void setMon_2_close(String mon_2_close)
		{
			this.mon_2_close = mon_2_close;
		}

		public String getTue_2_open()
		{
			return tue_2_open;
		}

		public void setTue_2_open(String tue_2_open)
		{
			this.tue_2_open = tue_2_open;
		}

		public String getTue_2_close()
		{
			return tue_2_close;
		}

		public void setTue_2_close(String tue_2_close)
		{
			this.tue_2_close = tue_2_close;
		}

		public String getWed_2_open()
		{
			return wed_2_open;
		}

		public void setWed_2_open(String wed_2_open)
		{
			this.wed_2_open = wed_2_open;
		}

		public String getWed_2_close()
		{
			return wed_2_close;
		}

		public void setWed_2_close(String wed_2_close)
		{
			this.wed_2_close = wed_2_close;
		}

		public String getThu_2_open()
		{
			return thu_2_open;
		}

		public void setThu_2_open(String thu_2_open)
		{
			this.thu_2_open = thu_2_open;
		}

		public String getThu_2_close()
		{
			return thu_2_close;
		}

		public void setThu_2_close(String thu_2_close)
		{
			this.thu_2_close = thu_2_close;
		}

		public String getFri_2_open()
		{
			return fri_2_open;
		}

		public void setFri_2_open(String fri_2_open)
		{
			this.fri_2_open = fri_2_open;
		}

		public String getFri_2_close()
		{
			return fri_2_close;
		}

		public void setFri_2_close(String fri_2_close)
		{
			this.fri_2_close = fri_2_close;
		}

		public String getSat_2_open()
		{
			return sat_2_open;
		}

		public void setSat_2_open(String sat_2_open)
		{
			this.sat_2_open = sat_2_open;
		}

		public String getSat_2_close()
		{
			return sat_2_close;
		}

		public void setSat_2_close(String sat_2_close)
		{
			this.sat_2_close = sat_2_close;
		}

		public String getSun_2_open()
		{
			return sun_2_open;
		}

		public void setSun_2_open(String sun_2_open)
		{
			this.sun_2_open = sun_2_open;
		}

		public String getSun_2_close()
		{
			return sun_2_close;
		}

		public void setSun_2_close(String sun_2_close)
		{
			this.sun_2_close = sun_2_close;
		}

		public Hours(Parcel in)
		{
			mon_1_open = in.readString();
			mon_1_close = in.readString();
			tue_1_open = in.readString();
			tue_1_close = in.readString();
			wed_1_open = in.readString();
			wed_1_close = in.readString();
			thu_1_open = in.readString();
			thu_1_close = in.readString();
			fri_1_open = in.readString();
			fri_1_close = in.readString();
			sat_1_open = in.readString();
			sat_1_close = in.readString();
			sun_1_open = in.readString();
			sun_1_close = in.readString();
			mon_2_open = in.readString();
			mon_2_close = in.readString();
			tue_2_open = in.readString();
			tue_2_close = in.readString();
			wed_2_open = in.readString();
			wed_2_close = in.readString();
			thu_2_open = in.readString();
			thu_2_close = in.readString();
			fri_2_open = in.readString();
			fri_2_close = in.readString();
			sat_2_open = in.readString();
			sat_2_close = in.readString();
			sun_2_open = in.readString();
			sun_2_close = in.readString();
		}

		public int describeContents()
		{
			return 0;
		}

		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeString(mon_1_open);
			dest.writeString(mon_1_close);
			dest.writeString(tue_1_open);
			dest.writeString(tue_1_close);
			dest.writeString(wed_1_open);
			dest.writeString(wed_1_close);
			dest.writeString(thu_1_open);
			dest.writeString(thu_1_close);
			dest.writeString(fri_1_open);
			dest.writeString(fri_1_close);
			dest.writeString(sat_1_open);
			dest.writeString(sat_1_close);
			dest.writeString(sun_1_open);
			dest.writeString(sun_1_close);
			dest.writeString(mon_2_open);
			dest.writeString(mon_2_close);
			dest.writeString(tue_2_open);
			dest.writeString(tue_2_close);
			dest.writeString(wed_2_open);
			dest.writeString(wed_2_close);
			dest.writeString(thu_2_open);
			dest.writeString(thu_2_close);
			dest.writeString(fri_2_open);
			dest.writeString(fri_2_close);
			dest.writeString(sat_2_open);
			dest.writeString(sat_2_close);
			dest.writeString(sun_2_open);
			dest.writeString(sun_2_close);
		}

		public static final Parcelable.Creator<Hours>	CREATOR	= new Parcelable.Creator<Hours>() {
																	public Hours createFromParcel(Parcel in)
																	{
																		return new Hours(in);
																	}

																	public Hours[] newArray(int size)
																	{
																		return new Hours[size];
																	}
																};
	}

	public static class Parking extends GraphObject implements Parcelable
	{
		private int	street;
		private int	lot;
		private int	valet;

		public Parking()
		{
			
		}
		
		public int getStreet()
		{
			return street;
		}

		public void setStreet(int street)
		{
			this.street = street;
		}

		public int getLot()
		{
			return lot;
		}

		public void setLot(int lot)
		{
			this.lot = lot;
		}

		public int getValet()
		{
			return valet;
		}

		public void setValet(int valet)
		{
			this.valet = valet;
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeInt(lot);
			dest.writeInt(street);
			dest.writeInt(valet);
		}

		public static final Parcelable.Creator<Parking>	CREATOR	= new Parcelable.Creator<Parking>() {
																	public Parking createFromParcel(Parcel in)
																	{
																		return new Parking(in);
																	}

																	public Parking[] newArray(int size)
																	{
																		return new Parking[size];
																	}
																};

		private Parking(Parcel in)
		{
			lot = in.readInt();
			street = in.readInt();
			valet = in.readInt();
		}
	}

	public static class PaymentOptions extends GraphObject implements Parcelable
	{
		private int	cash_only;
		private int	visa;
		private int	amex;
		private int	mastercard;
		private int	discover;

		public PaymentOptions()
		{
			
		}
		
		public int getCash_only()
		{
			return cash_only;
		}

		public void setCash_only(int cash_only)
		{
			this.cash_only = cash_only;
		}

		public int getVisa()
		{
			return visa;
		}

		public void setVisa(int visa)
		{
			this.visa = visa;
		}

		public int getAmex()
		{
			return amex;
		}

		public void setAmex(int amex)
		{
			this.amex = amex;
		}

		public int getMastercard()
		{
			return mastercard;
		}

		public void setMastercard(int mastercard)
		{
			this.mastercard = mastercard;
		}

		public int getDiscover()
		{
			return discover;
		}

		public void setDiscover(int discover)
		{
			this.discover = discover;
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeInt(amex);
			dest.writeInt(cash_only);
			dest.writeInt(discover);
			dest.writeInt(mastercard);
			dest.writeInt(visa);
		}

		public static final Parcelable.Creator<PaymentOptions>	CREATOR	= new Parcelable.Creator<PaymentOptions>() {
																	public PaymentOptions createFromParcel(Parcel in)
																	{
																		return new PaymentOptions(in);
																	}

																	public PaymentOptions[] newArray(int size)
																	{
																		return new PaymentOptions[size];
																	}
																};

		private PaymentOptions(Parcel in)
		{
			amex = in.readInt();
			cash_only = in.readInt();
			discover = in.readInt();
			mastercard = in.readInt();
			visa = in.readInt();
		}
	}

	public static class RestaurantServices extends GraphObject implements Parcelable
	{
		private int	reserve;
		private int	walkins;
		private int	groups;
		private int	kids;
		private int	takeout;
		private int	delivery;
		private int	catering;
		private int	waiter;
		private int	outdoor;

		public RestaurantServices()
		{
			
		}
		
		public int getReserve()
		{
			return reserve;
		}

		public void setReserve(int reserve)
		{
			this.reserve = reserve;
		}

		public int getWalkins()
		{
			return walkins;
		}

		public void setWalkins(int walkins)
		{
			this.walkins = walkins;
		}

		public int getGroups()
		{
			return groups;
		}

		public void setGroups(int groups)
		{
			this.groups = groups;
		}

		public int getKids()
		{
			return kids;
		}

		public void setKids(int kids)
		{
			this.kids = kids;
		}

		public int getTakeout()
		{
			return takeout;
		}

		public void setTakeout(int takeout)
		{
			this.takeout = takeout;
		}

		public int getDelivery()
		{
			return delivery;
		}

		public void setDelivery(int delivery)
		{
			this.delivery = delivery;
		}

		public int getCatering()
		{
			return catering;
		}

		public void setCatering(int catering)
		{
			this.catering = catering;
		}

		public int getWaiter()
		{
			return waiter;
		}

		public void setWaiter(int waiter)
		{
			this.waiter = waiter;
		}

		public int getOutdoor()
		{
			return outdoor;
		}

		public void setOutdoor(int outdoor)
		{
			this.outdoor = outdoor;
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeInt(catering);
			dest.writeInt(delivery);
			dest.writeInt(groups);
			dest.writeInt(kids);
			dest.writeInt(outdoor);
			dest.writeInt(reserve);
			dest.writeInt(takeout);
			dest.writeInt(waiter);
			dest.writeInt(walkins);
		}

		public static final Parcelable.Creator<RestaurantServices>	CREATOR	= new Parcelable.Creator<RestaurantServices>() {
																				public RestaurantServices createFromParcel(
																						Parcel in)
																				{
																					return new RestaurantServices(in);
																				}

																				public RestaurantServices[] newArray(
																						int size)
																				{
																					return new RestaurantServices[size];
																				}
																			};

		private RestaurantServices(Parcel in)
		{
			catering = in.readInt();
			delivery = in.readInt();
			groups = in.readInt();
			kids = in.readInt();
			outdoor = in.readInt();
			reserve = in.readInt();
			takeout = in.readInt();
			waiter = in.readInt();
			walkins = in.readInt();
		}
	}

	public static class RestaurantSpecialties extends GraphObject implements Parcelable
	{
		private int	breakfast;
		private int	lunch;
		private int	dinner;
		private int	coffee;
		private int	drinks;

		public RestaurantSpecialties()
		{
			
		}
		
		public int getBreakfast()
		{
			return breakfast;
		}

		public void setBreakfast(int breakfast)
		{
			this.breakfast = breakfast;
		}

		public int getLunch()
		{
			return lunch;
		}

		public void setLunch(int lunch)
		{
			this.lunch = lunch;
		}

		public int getDinner()
		{
			return dinner;
		}

		public void setDinner(int dinner)
		{
			this.dinner = dinner;
		}

		public int getCoffee()
		{
			return coffee;
		}

		public void setCoffee(int coffee)
		{
			this.coffee = coffee;
		}

		public int getDrinks()
		{
			return drinks;
		}

		public void setDrinks(int drinks)
		{
			this.drinks = drinks;
		}

		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeInt(breakfast);
			dest.writeInt(coffee);
			dest.writeInt(dinner);
			dest.writeInt(drinks);
			dest.writeInt(lunch);
		}

		public static final Parcelable.Creator<RestaurantSpecialties>	CREATOR	= new Parcelable.Creator<RestaurantSpecialties>() {
																					public RestaurantSpecialties createFromParcel(
																							Parcel in)
																					{
																						return new RestaurantSpecialties(
																								in);
																					}

																					public RestaurantSpecialties[] newArray(
																							int size)
																					{
																						return new RestaurantSpecialties[size];
																					}
																				};

		private RestaurantSpecialties(Parcel in)
		{
			breakfast = in.readInt();
			coffee = in.readInt();
			dinner = in.readInt();
			drinks = in.readInt();
			lunch = in.readInt();
		}
	}

	public Page(Parcel in)
	{
		about = in.readString();
		access_token = in.readString();
		affiliation = in.readString();
		app_id = in.readString();
		artists_we_like = in.readString();
		attire = in.readString();
		awards = in.readString();
		band_interests = in.readString();
		band_members = in.readString();
		bio = in.readString();
		birthday = in.readString();
		booking_agent = in.readString();
		built = in.readString();
		can_post = in.readByte() != 0x00;
		categories = new ArrayList<String>();
		in.readStringList(categories);
		checkins = in.readInt();
		company_overview = in.readString();
		culinary_team = in.readString();
		current_location = in.readString();
		description = in.readString();
		description_html = in.readString();
		directed_by = in.readString();
		fan_count = in.readInt();
		features = in.readString();
		food_styles = new ArrayList<String>();
		in.readStringList(food_styles);
		founded = in.readString();
		general_info = in.readString();
		general_manager = in.readString();
		genre = in.readString();
		global_brand_page_name = in.readInt();
		global_brand_parent_page_id = in.readInt();
		has_added_app = in.readByte() != 0x00;
		hometown = in.readString();
		in.readParcelable(Hours.class.getClassLoader());
		influences = in.readString();
		is_community_page = in.readByte() != 0x00;
		is_fan = in.readByte() != 0x00;
		is_published = in.readByte() != 0x00;
		keywords = in.readString();
		location = in.readParcelable(Location.class.getClassLoader());
		members = in.readString();
		mission = in.readString();
		mpg = in.readString();
		name = in.readString();
		network = in.readString();
		new_like_count = in.readInt();
		offer_eligible = in.readByte() != 0x00;
		page_id = in.readString();
		page_url = in.readString();
		parent_page = in.readString();
		in.readParcelable(Parking.class.getClassLoader());
		in.readParcelable(PaymentOptions.class.getClassLoader());
		personal_info = in.readString();
		personal_interests = in.readString();
		pharma_safety_info = in.readString();
		phone = in.readString();
		pic = in.readString();
		pic_big = in.readString();
		in.readParcelable(Cover.class.getClassLoader());
		pic_large = in.readString();
		pic_small = in.readString();
		pic_square = in.readString();
		plot_outline = in.readString();
		press_contact = in.readString();
		price_range = in.readString();
		produced_by = in.readString();
		products = in.readString();
		public_transit = in.readString();
		record_label = in.readString();
		release_date = in.readString();
		in.readParcelable(RestaurantServices.class.getClassLoader());
		in.readParcelable(RestaurantSpecialties.class.getClassLoader());
		schedule = in.readString();
		screenplay_by = in.readString();
		season = in.readString();
		starring = in.readString();
		studio = in.readString();
		talking_about_count = in.readInt();
		type = in.readString();
		unread_message_count = in.readInt();
		unseen_message_count = in.readInt();
		unread_notif_count = in.readInt();
		username = in.readString();
		website = in.readString();
		were_here_count = in.readInt();
		written_by = in.readString();
		global_brand_like_count = in.readInt();
		global_brand_talking_about_count = in.readInt();
	}

	public int describeContents()
	{
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(about);
		dest.writeString(access_token);
		dest.writeString(affiliation);
		dest.writeString(app_id);
		dest.writeString(artists_we_like);
		dest.writeString(attire);
		dest.writeString(awards);
		dest.writeString(band_interests);
		dest.writeString(band_members);
		dest.writeString(bio);
		dest.writeString(birthday);
		dest.writeString(booking_agent);
		dest.writeString(built);
		dest.writeByte((byte) (can_post ? 0x01 : 0x00));
		dest.writeStringList(categories);
		dest.writeInt(checkins);
		dest.writeString(company_overview);
		dest.writeString(culinary_team);
		dest.writeString(current_location);
		dest.writeString(description);
		dest.writeString(description_html);
		dest.writeString(directed_by);
		dest.writeInt(fan_count);
		dest.writeString(features);
		dest.writeStringList(food_styles);
		dest.writeString(founded);
		dest.writeString(general_info);
		dest.writeString(general_manager);
		dest.writeString(genre);
		dest.writeInt(global_brand_page_name);
		dest.writeInt(global_brand_parent_page_id);
		dest.writeByte((byte) (has_added_app ? 0x01 : 0x00));
		dest.writeString(hometown);
		dest.writeParcelable(hours, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(influences);
		dest.writeByte((byte) (is_community_page ? 0x01 : 0x00));
		dest.writeByte((byte) (is_fan ? 0x01 : 0x00));
		dest.writeByte((byte) (is_published ? 0x01 : 0x00));
		dest.writeString(keywords);
		dest.writeParcelable(location, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(members);
		dest.writeString(mission);
		dest.writeString(mpg);
		dest.writeString(name);
		dest.writeString(network);
		dest.writeInt(new_like_count);
		dest.writeByte((byte) (offer_eligible ? 0x01 : 0x00));
		dest.writeString(page_id);
		dest.writeString(page_url);
		dest.writeString(parent_page);
		dest.writeParcelable(parking, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeParcelable(payment_options, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(personal_info);
		dest.writeString(personal_interests);
		dest.writeString(pharma_safety_info);
		dest.writeString(phone);
		dest.writeString(pic);
		dest.writeString(pic_big);
		dest.writeParcelable(pic_cover, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(pic_large);
		dest.writeString(pic_small);
		dest.writeString(pic_square);
		dest.writeString(plot_outline);
		dest.writeString(press_contact);
		dest.writeString(price_range);
		dest.writeString(produced_by);
		dest.writeString(products);
		dest.writeString(public_transit);
		dest.writeString(record_label);
		dest.writeString(release_date);
		dest.writeParcelable(restaurant_services, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeParcelable(restaurant_specialties, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(schedule);
		dest.writeString(screenplay_by);
		dest.writeString(season);
		dest.writeString(starring);
		dest.writeString(studio);
		dest.writeInt(talking_about_count);
		dest.writeString(type);
		dest.writeInt(unread_message_count);
		dest.writeInt(unseen_message_count);
		dest.writeInt(unread_notif_count);
		dest.writeString(username);
		dest.writeString(website);
		dest.writeInt(were_here_count);
		dest.writeString(written_by);
		dest.writeInt(global_brand_like_count);
		dest.writeInt(global_brand_talking_about_count);
	}

	public static final Parcelable.Creator<Page>	CREATOR	= new Parcelable.Creator<Page>() {
																public Page createFromParcel(Parcel in)
																{
																	return new Page(in);
																}

																public Page[] newArray(int size)
																{
																	return new Page[size];
																}
															};
}
