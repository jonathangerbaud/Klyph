package com.abewy.android.apps.klyph.core.fql;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.klyph.items.ShadowItem;

public class User extends GraphObject implements Parcelable
{
	private String			uid;
	private String			name;
	private String			pic_square;
	private String			birthday;
	private String			birthday_date;
	private String			pic_small;
	private String			pic_big;
	private String			pic;
	private String			pic_large;
	private Cover			pic_cover;
	private String			about_me;
	private String			activities;
	private String			books;
	private boolean			can_message;
	private boolean			can_post;
	private String			contact_email;
	private Location		current_address;
	private Location		current_location;
	private List<Education>	education;
	private String			email;
	private List<Relative>	family;
	private String			first_name;
	private int				friend_count;
	private int				friend_request_count;
	private Location		hometown_location;
	private String			inpirational_people;
	private String			interests;
	private boolean			is_app_user;
	private boolean			is_blocked;
	private String			last_name;
	private int				likes_count;
	private String			locale;
	private String			meeting_for;
	private String			meeting_sex;
	private String			middle_name;
	private String			movies;
	private String			music;
	private int				mutual_friend_count;
	private String			online_presence;		// active,idle,offline,error
	private String			political;
	private String			quotes;
	private String			relationship_status;
	private String			religion;
	private String			sex;
	private String			sports;
	private int				timezone;
	private String			tv;
	private String			username;
	private String			website;
	private List<Work>		work;
	private boolean			isFriend;

	public User()
	{

	}

	@Override
	public int getItemViewType()
	{
		return GraphObject.USER;
	}

	@Override
	public boolean isSelectable(int layout)
	{
		return false;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPic_square()
	{
		return pic_square;
	}

	public void setPic_square(String pic_square)
	{
		this.pic_square = pic_square;
	}

	public String getBirthday()
	{
		return birthday;
	}

	public void setBirthday(String birthday)
	{
		this.birthday = birthday;
	}

	public String getBirthday_date()
	{
		return birthday_date;
	}

	public void setBirthday_date(String birthday_date)
	{
		this.birthday_date = birthday_date;
	}

	public String getPic_small()
	{
		return pic_small;
	}

	public void setPic_small(String pic_small)
	{
		this.pic_small = pic_small;
	}

	public String getPic_big()
	{
		return pic_big;
	}

	public void setPic_big(String pic_big)
	{
		this.pic_big = pic_big;
	}

	public String getPic()
	{
		return pic;
	}

	public void setPic(String pic)
	{
		this.pic = pic;
	}

	public String getPic_large()
	{
		return pic_large;
	}

	public void setPic_large(String pic_large)
	{
		this.pic_large = pic_large;
	}

	public Cover getPic_cover()
	{
		return pic_cover;
	}

	public void setPic_cover(Cover pic_cover)
	{
		this.pic_cover = pic_cover;
	}

	public String getAbout_me()
	{
		return about_me;
	}

	public void setAbout_me(String about_me)
	{
		this.about_me = about_me;
	}

	public String getActivities()
	{
		return activities;
	}

	public void setActivities(String activities)
	{
		this.activities = activities;
	}

	public String getBooks()
	{
		return books;
	}

	public void setBooks(String books)
	{
		this.books = books;
	}

	public boolean getCan_message()
	{
		return can_message;
	}

	public void setCan_message(boolean can_message)
	{
		this.can_message = can_message;
	}

	public boolean getCan_post()
	{
		return can_post;
	}

	public void setCan_post(boolean can_post)
	{
		this.can_post = can_post;
	}

	public String getContact_email()
	{
		return contact_email;
	}

	public void setContact_email(String contact_email)
	{
		this.contact_email = contact_email;
	}

	public Location getCurrent_address()
	{
		return current_address;
	}

	public void setCurrent_address(Location current_address)
	{
		this.current_address = current_address;
	}

	public Location getCurrent_location()
	{
		return current_location;
	}

	public void setCurrent_location(Location current_location)
	{
		this.current_location = current_location;
	}

	public List<Education> getEducation()
	{
		return education;
	}

	public void setEducation(List<Education> education)
	{
		this.education = education;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public List<Relative> getFamily()
	{
		return family;
	}

	public void setFamily(List<Relative> family)
	{
		this.family = family;
	}

	public String getFirst_name()
	{
		return first_name;
	}

	public void setFirst_name(String first_name)
	{
		this.first_name = first_name;
	}

	public int getFriend_count()
	{
		return friend_count;
	}

	public void setFriend_count(int friend_count)
	{
		this.friend_count = friend_count;
	}

	public int getFriend_request_count()
	{
		return friend_request_count;
	}

	public void setFriend_request_count(int friend_request_count)
	{
		this.friend_request_count = friend_request_count;
	}

	public Location getHometown_location()
	{
		return hometown_location;
	}

	public void setHometown_location(Location hometown_location)
	{
		this.hometown_location = hometown_location;
	}

	public String getInpirational_people()
	{
		return inpirational_people;
	}

	public void setInpirational_people(String inpirational_people)
	{
		this.inpirational_people = inpirational_people;
	}

	public boolean getIs_app_user()
	{
		return is_app_user;
	}

	public void setIs_app_user(boolean is_app_user)
	{
		this.is_app_user = is_app_user;
	}

	public boolean getIs_blocked()
	{
		return is_blocked;
	}

	public void setIs_blocked(boolean is_blocked)
	{
		this.is_blocked = is_blocked;
	}

	public String getLast_name()
	{
		return last_name;
	}

	public void setLast_name(String last_name)
	{
		this.last_name = last_name;
	}

	public int getLikes_count()
	{
		return likes_count;
	}

	public void setLikes_count(int likes_count)
	{
		this.likes_count = likes_count;
	}

	public String getLocale()
	{
		return locale;
	}

	public void setLocale(String locale)
	{
		this.locale = locale;
	}

	public String getMeeting_for()
	{
		return meeting_for;
	}

	public void setMeeting_for(String meeting_for)
	{
		this.meeting_for = meeting_for;
	}

	public String getMeeting_sex()
	{
		return meeting_sex;
	}

	public void setMeeting_sex(String meeting_sex)
	{
		this.meeting_sex = meeting_sex;
	}

	public String getMiddle_name()
	{
		return middle_name;
	}

	public void setMiddle_name(String middle_name)
	{
		this.middle_name = middle_name;
	}

	public String getMovies()
	{
		return movies;
	}

	public void setMovies(String movies)
	{
		this.movies = movies;
	}

	public String getMusic()
	{
		return music;
	}

	public void setMusic(String music)
	{
		this.music = music;
	}

	public int getMutual_friend_count()
	{
		return mutual_friend_count;
	}

	public void setMutual_friend_count(int mutual_friend_count)
	{
		this.mutual_friend_count = mutual_friend_count;
	}

	public String getOnline_presence()
	{
		return online_presence;
	}

	public void setOnline_presence(String online_presence)
	{
		this.online_presence = online_presence;
	}

	public String getPolitical()
	{
		return political;
	}

	public void setPolitical(String political)
	{
		this.political = political;
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

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public String getSports()
	{
		return sports;
	}

	public void setSports(String sports)
	{
		this.sports = sports;
	}

	public int getTimezone()
	{
		return timezone;
	}

	public void setTimezone(int timezone)
	{
		this.timezone = timezone;
	}

	public String getTv()
	{
		return tv;
	}

	public void setTv(String tv)
	{
		this.tv = tv;
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

	public List<Work> getWork()
	{
		return work;
	}

	public void setWork(List<Work> work)
	{
		this.work = work;
	}

	public String getInterests()
	{
		return interests;
	}

	public void setInterests(String interests)
	{
		this.interests = interests;
	}

	public boolean isFriend()
	{
		return isFriend;
	}

	public void setIsFriend(boolean isFriend)
	{
		this.isFriend = isFriend;
	}

	public static class Cover extends GraphObject  implements Parcelable
	{
		private String	cover_id;
		private String	source;
		private int		offset_y;

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
		
		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeString(cover_id);
			dest.writeString(source);
			dest.writeInt(offset_y);
		}

		private Cover(Parcel in)
		{
			cover_id = in.readString();
			source = in.readString();
			offset_y = in.readInt();
		}

		public Cover()
		{
		}
	}

	public static class IdName extends GraphObject  implements Parcelable
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
		
		@Override
		public int describeContents()
		{
			return 0;
		}
		
		public static final Parcelable.Creator<IdName>	CREATOR	= new Parcelable.Creator<IdName>() {
			public IdName createFromParcel(Parcel in)
			{
				return new IdName(in);
			}

			public IdName[] newArray(int size)
			{
				return new IdName[size];
			}
		};
		
		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeString(id);
			dest.writeString(name);
		}

		private IdName(Parcel in)
		{
			id = in.readString();
			name = in.readString();
		}

		public IdName()
		{
		}
	}

	public static class Work extends ShadowItem  implements Parcelable
	{
		private IdName	employer;
		private IdName	location;
		private IdName	position;
		private String	start_date;
		private String	end_date;

		@Override
		public int getItemViewType()
		{
			return GraphObject.WORK;
		}

		public IdName getEmployer()
		{
			return employer;
		}

		public void setEmployer(IdName employer)
		{
			this.employer = employer;
		}

		public IdName getLocation()
		{
			return location;
		}

		public void setLocation(IdName location)
		{
			this.location = location;
		}

		public IdName getPosition()
		{
			return position;
		}

		public void setPosition(IdName position)
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
		
		@Override
		public int describeContents()
		{
			return 0;
		}
		
		public static final Parcelable.Creator<Work>	CREATOR	= new Parcelable.Creator<Work>() {
			public Work createFromParcel(Parcel in)
			{
				return new Work(in);
			}

			public Work[] newArray(int size)
			{
				return new Work[size];
			}
		};
		
		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeParcelable(employer, PARCELABLE_WRITE_RETURN_VALUE);
			dest.writeParcelable(location, PARCELABLE_WRITE_RETURN_VALUE);
			dest.writeParcelable(position, PARCELABLE_WRITE_RETURN_VALUE);
			dest.writeString(start_date);
			dest.writeString(end_date);
		}

		private Work(Parcel in)
		{
			employer = in.readParcelable(IdName.class.getClassLoader());
			location = in.readParcelable(IdName.class.getClassLoader());
			position = in.readParcelable(IdName.class.getClassLoader());
			start_date = in.readString();
			end_date = in.readString();
		}

		public Work()
		{
		}
	}

	public static class Education extends ShadowItem  implements Parcelable
	{
		private IdName	school;
		private IdName	year;
		private IdName	concentration;
		private String	type;

		@Override
		public int getItemViewType()
		{
			return GraphObject.EDUCATION;
		}

		public IdName getSchool()
		{
			return school;
		}

		public void setSchool(IdName school)
		{
			this.school = school;
		}

		public IdName getYear()
		{
			return year;
		}

		public void setYear(IdName year)
		{
			this.year = year;
		}

		public IdName getConcentration()
		{
			return concentration;
		}

		public void setConcentration(IdName concentration)
		{
			this.concentration = concentration;
		}

		public String getType()
		{
			return type;
		}

		public void setType(String type)
		{
			this.type = type;
		}
		
		@Override
		public int describeContents()
		{
			return 0;
		}
		
		public static final Parcelable.Creator<Education>	CREATOR	= new Parcelable.Creator<Education>() {
			public Education createFromParcel(Parcel in)
			{
				return new Education(in);
			}

			public Education[] newArray(int size)
			{
				return new Education[size];
			}
		};
		
		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeParcelable(school, PARCELABLE_WRITE_RETURN_VALUE);
			dest.writeParcelable(year, PARCELABLE_WRITE_RETURN_VALUE);
			dest.writeParcelable(concentration, PARCELABLE_WRITE_RETURN_VALUE);
			dest.writeString(type);
		}

		private Education(Parcel in)
		{
			school = in.readParcelable(IdName.class.getClassLoader());
			year = in.readParcelable(IdName.class.getClassLoader());
			concentration = in.readParcelable(IdName.class.getClassLoader());
			type = in.readString();
		}

		public Education()
		{
		}
	}

	public static class Relative extends ShadowItem  implements Parcelable
	{
		private String	uid;
		private String	name;
		private String	birthday;
		private String	relationship;

		@Override
		public int getItemViewType()
		{
			return GraphObject.RELATIVE;
		}

		public String getUid()
		{
			return uid;
		}

		public void setUid(String uid)
		{
			this.uid = uid;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getBirthday()
		{
			return birthday;
		}

		public void setBirthday(String birthday)
		{
			this.birthday = birthday;
		}

		public String getRelationship()
		{
			return relationship;
		}

		public void setRelationship(String relationship)
		{
			this.relationship = relationship;
		}
		
		@Override
		public int describeContents()
		{
			return 0;
		}
		
		public static final Parcelable.Creator<Relative>	CREATOR	= new Parcelable.Creator<Relative>() {
			public Relative createFromParcel(Parcel in)
			{
				return new Relative(in);
			}

			public Relative[] newArray(int size)
			{
				return new Relative[size];
			}
		};
		
		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeString(uid);
			dest.writeString(name);
			dest.writeString(birthday);
			dest.writeString(relationship);
		}

		private Relative(Parcel in)
		{
			uid = in.readString();
			name = in.readString();
			birthday = in.readString();
			relationship = in.readString();
		}

		public Relative()
		{
		}
	}
	
	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(uid);
		dest.writeString(name);
		dest.writeString(pic_square);
		dest.writeString(birthday);
		dest.writeString(birthday_date);
		dest.writeString(pic_small);
		dest.writeString(pic_big);
		dest.writeString(pic);
		dest.writeString(pic_large);
		dest.writeParcelable(pic_cover, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(about_me);
		dest.writeString(activities);
		dest.writeString(books);
		dest.writeByte((byte) (can_message ? 1 : 0));
		dest.writeByte((byte) (can_post ? 1 : 0));
		dest.writeString(contact_email);
		dest.writeParcelable(current_address, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeParcelable(current_location, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeTypedList(education);
		dest.writeString(email);
		dest.writeTypedList(family);
		dest.writeString(first_name);
		dest.writeInt(friend_count);
		dest.writeInt(friend_request_count);
		dest.writeParcelable(hometown_location, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(inpirational_people);
		dest.writeString(interests);
		dest.writeByte((byte) (is_app_user ? 1 : 0));
		dest.writeByte((byte) (is_blocked ? 1 : 0));
		dest.writeString(last_name);
		dest.writeInt(likes_count);
		dest.writeString(locale);
		dest.writeString(meeting_for);
		dest.writeString(meeting_sex);
		dest.writeString(middle_name);
		dest.writeString(movies);
		dest.writeString(music);
		dest.writeInt(mutual_friend_count);
		dest.writeString(online_presence);
		dest.writeString(political);
		dest.writeString(quotes);
		dest.writeString(relationship_status);
		dest.writeString(religion);
		dest.writeString(sex);
		dest.writeString(sports);
		dest.writeInt(timezone);
		dest.writeString(tv);
		dest.writeString(username);
		dest.writeString(website);
		dest.writeTypedList(work);
		dest.writeByte((byte) (isFriend ? 1 : 0));
	}

	public static final Parcelable.Creator<User>	CREATOR	= new Parcelable.Creator<User>() {
																public User createFromParcel(Parcel in)
																{
																	return new User(in);
																}

																public User[] newArray(int size)
																{
																	return new User[size];
																}
															};

	private User(Parcel in)
	{
		uid = in.readString();
		name = in.readString();
		pic_square = in.readString();
		birthday = in.readString();
		birthday_date = in.readString();
		pic_small = in.readString();
		pic_big = in.readString();
		pic = in.readString();
		pic_large = in.readString();
		pic_cover = in.readParcelable(Cover.class.getClassLoader());
		about_me = in.readString();
		activities = in.readString();
		books = in.readString();
		can_message = in.readByte() == 1;
		can_post = in.readByte() == 1;
		contact_email = in.readString();
		current_address = in.readParcelable(Location.class.getClassLoader());
		current_location = in.readParcelable(Location.class.getClassLoader());
		education = new ArrayList<Education>();
		in.readTypedList(education, Education.CREATOR);
		email = in.readString();
		family = new ArrayList<Relative>();
		in.readTypedList(family, Relative.CREATOR);
		first_name = in.readString();
		friend_count = in.readInt();
		friend_request_count = in.readInt();
		hometown_location = in.readParcelable(Location.class.getClassLoader());
		inpirational_people = in.readString();
		interests = in.readString();
		is_app_user = in.readByte() == 1;
		is_blocked = in.readByte() == 1;
		last_name = in.readString();
		likes_count = in.readInt();
		locale = in.readString();
		meeting_for = in.readString();
		meeting_sex = in.readString();
		middle_name = in.readString();
		movies = in.readString();
		music = in.readString();
		mutual_friend_count = in.readInt();
		online_presence = in.readString();
		political = in.readString();
		quotes = in.readString();
		relationship_status = in.readString();
		religion = in.readString();
		sex = in.readString();
		sports = in.readString();
		timezone = in.readInt();
		tv = in.readString();
		username = in.readString();
		website = in.readString();
		work = new ArrayList<Work>();
		in.readTypedList(work, Work.CREATOR);
		isFriend = in.readByte() == 1;
	}
}