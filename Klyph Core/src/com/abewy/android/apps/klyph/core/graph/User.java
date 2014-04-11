package com.abewy.android.apps.klyph.core.graph;

import java.util.ArrayList;

public class User extends GraphObject
{
	private String					id;
	private String					name;
	private String					first_name;
	private String					middle_name;
	private String					last_name;
	private String					gender;
	private String					locale;
	private ArrayList<IdNameObject>	languages;
	private String					link;
	private String					username;
	private String					third_party_id;
	private Object					installed;
	private Number					timezone;
	private String					updated_time;
	private boolean					verified;
	private String					bio;
	private String					birthday;
	private Cover					cover;
	private Object					currency;
	private ArrayList<Object>		devices;
	private ArrayList<Object>		education;
	private IdNameObject			hometown;
	private String					email;
	private ArrayList<String>		interested_in;
	private IdNameObject			location;
	private String					political;
	private ArrayList<Object>		payment_pricepoints;
	private ArrayList<IdNameObject>	favorite_athletes;
	private ArrayList<IdNameObject>	favorite_teams;
	private String					picture;
	private String					quotes;
	private String					relationship_status;
	private String					religion;
	private Object					security_settings;
	private Object					significant_other;
	private Object					video_upload_limits;
	private String					website;
	private ArrayList<Work>			work;

	public User()
	{

	}

	@Override
	public int getItemViewType()
	{
		return GraphObject.USER;
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

	public String getFirst_name()
	{
		return first_name;
	}

	public void setFirst_name(String first_name)
	{
		this.first_name = first_name;
	}

	public String getMiddle_name()
	{
		return middle_name;
	}

	public void setMiddle_name(String middle_name)
	{
		this.middle_name = middle_name;
	}

	public String getLast_name()
	{
		return last_name;
	}

	public void setLast_name(String last_name)
	{
		this.last_name = last_name;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getLocale()
	{
		return locale;
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	public ArrayList<IdNameObject> getLanguages()
	{
		return languages;
	}

	public void setLanguages(ArrayList<IdNameObject> languages)
	{
		this.languages = languages;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getThird_party_id()
	{
		return third_party_id;
	}

	public void setThird_party_id(String third_party_id)
	{
		this.third_party_id = third_party_id;
	}

	public Object getInstalled()
	{
		return installed;
	}

	public void setInstalled(Object installed)
	{
		this.installed = installed;
	}

	public Number getTimezone()
	{
		return timezone;
	}

	public void setTimezone(Number timezone)
	{
		this.timezone = timezone;
	}

	public String getUpdated_time()
	{
		return updated_time;
	}

	public void setUpdated_time(String updated_time)
	{
		this.updated_time = updated_time;
	}

	public boolean getVerified()
	{
		return verified;
	}

	public void setVerified(boolean verified)
	{
		this.verified = verified;
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

	public Cover getCover()
	{
		return cover;
	}

	public void setCover(Cover cover)
	{
		this.cover = cover;
	}

	public Object getCurrency()
	{
		return currency;
	}

	public void setCurrency(Object currency)
	{
		this.currency = currency;
	}

	public ArrayList<Object> getDevices()
	{
		return devices;
	}

	public void setDevices(ArrayList<Object> devices)
	{
		this.devices = devices;
	}

	public ArrayList<Object> getEducation()
	{
		return education;
	}

	public void setEducation(ArrayList<Object> education)
	{
		this.education = education;
	}

	public IdNameObject getHometown()
	{
		return hometown;
	}

	public void setHometown(IdNameObject hometown)
	{
		this.hometown = hometown;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public ArrayList<String> getInterested_in()
	{
		return interested_in;
	}

	public void setInterested_in(ArrayList<String> interested_in)
	{
		this.interested_in = interested_in;
	}

	public IdNameObject getLocation()
	{
		return location;
	}

	public void setLocation(IdNameObject location)
	{
		this.location = location;
	}

	public String getPolitical()
	{
		return political;
	}

	public void setPolitical(String political)
	{
		this.political = political;
	}

	public ArrayList<Object> getPayment_pricepoints()
	{
		return payment_pricepoints;
	}

	public void setPayment_pricepoints(ArrayList<Object> payment_pricepoints)
	{
		this.payment_pricepoints = payment_pricepoints;
	}

	public ArrayList<IdNameObject> getFavorite_athletes()
	{
		return favorite_athletes;
	}

	public void setFavorite_athletes(ArrayList<IdNameObject> favorite_athletes)
	{
		this.favorite_athletes = favorite_athletes;
	}

	public ArrayList<IdNameObject> getFavorite_teams()
	{
		return favorite_teams;
	}

	public void setFavorite_teams(ArrayList<IdNameObject> favorite_teams)
	{
		this.favorite_teams = favorite_teams;
	}

	public String getPicture()
	{
		return picture;
	}

	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	public String getQuotes()
	{
		return quotes;
	}

	public void setQuotes(String quotes)
	{
		this.quotes = quotes;
	}

	public String getRelationship_status()
	{
		return relationship_status;
	}

	public void setRelationship_status(String relationship_status)
	{
		this.relationship_status = relationship_status;
	}

	public String getReligion()
	{
		return religion;
	}

	public void setReligion(String religion)
	{
		this.religion = religion;
	}

	public Object getSecurity_settings()
	{
		return security_settings;
	}

	public void setSecurity_settings(Object security_settings)
	{
		this.security_settings = security_settings;
	}

	public Object getSignificant_other()
	{
		return significant_other;
	}

	public void setSignificant_other(Object significant_other)
	{
		this.significant_other = significant_other;
	}

	public Object getVideo_upload_limits()
	{
		return video_upload_limits;
	}

	public void setVideo_upload_limits(Object video_upload_limits)
	{
		this.video_upload_limits = video_upload_limits;
	}

	public String getWebsite()
	{
		return website;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}

	public ArrayList<Work> getWork()
	{
		return work;
	}

	public void setWork(ArrayList<Work> work)
	{
		this.work = work;
	}

	public static class Cover
	{
		private String	id;
		private String	source;
		private Number	offset_y;

		public String getId()
		{
			return id;
		}

		public void setId(String id)
		{
			this.id = id;
		}

		public String getSource()
		{
			return source;
		}

		public void setSource(String source)
		{
			this.source = source;
		}

		public Number getOffset_y()
		{
			return offset_y;
		}

		public void setOffset_y(Number offset_y)
		{
			this.offset_y = offset_y;
		}
	}

	public static class Work
	{
		private String	employer;
		private String	location;
		private String	position;
		private String	start_date;
		private String	end_date;

		public String getEmployer()
		{
			return employer;
		}

		public void setEmployer(String employer)
		{
			this.employer = employer;
		}

		public String getLocation()
		{
			return location;
		}

		public void setLocation(String location)
		{
			this.location = location;
		}

		public String getPosition()
		{
			return position;
		}

		public void setPosition(String position)
		{
			this.position = position;
		}

		public String getStart_date()
		{
			return start_date;
		}

		public void setStart_date(String start_date)
		{
			this.start_date = start_date;
		}

		public String getEnd_date()
		{
			return end_date;
		}

		public void setEnd_date(String end_date)
		{
			this.end_date = end_date;
		}
	}

	public static class IdNameObject
	{
		private String	id;
		private String	name;

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
	}
}
