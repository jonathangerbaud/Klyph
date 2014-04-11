package com.abewy.android.apps.klyph.core.fql;

import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Event extends GraphObject implements Parcelable
{
	public static enum EventResponse
	{
		NOT_REPLIED,
		ATTENDING,
		UNSURE,
		DECLINED
	}

	private String				eid;
	private String				name;
	private String				pic_small;
	private String				pic_big;
	private Cover				pic_cover;
	private String				pic_square;
	private String				pic;
	private String				host;
	private String				description;
	private String				start_time;
	private String				end_time;
	private String				creator;
	private String				update_time;
	private String				location;
	private Venue				venue;
	private String				privacy;
	private boolean				hide_guest_list = false;
	private boolean				can_invite_friends = false;
	private int					all_members_count;
	private int					attending_count;
	private int					unsure_count;
	private int					declined_count;
	private int					not_replied_count;
	private String				userResponse;

	public Event()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.EVENT;
	}

	public String getEid()
	{
		return eid;
	}

	public void setEid(String eid)
	{
		this.eid = eid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public Cover getPic_cover()
	{
		return pic_cover;
	}

	public void setPic_cover(Cover pic_cover)
	{
		this.pic_cover = pic_cover;
	}

	public String getPic_square()
	{
		return pic_square;
	}

	public void setPic_square(String pic_square)
	{
		this.pic_square = pic_square;
	}

	public String getPic()
	{
		return pic;
	}

	public void setPic(String pic)
	{
		this.pic = pic;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getStart_time()
	{
		return start_time;
	}

	public void setStart_time(String start_time)
	{
		this.start_time = start_time;
	}

	public String getEnd_time()
	{
		return end_time;
	}

	public void setEnd_time(String end_time)
	{
		this.end_time = end_time;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public String getUpdate_time()
	{
		return update_time;
	}

	public void setUpdate_time(String update_time)
	{
		this.update_time = update_time;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public Venue getVenue()
	{
		return venue;
	}

	public void setVenue(Venue venue)
	{
		this.venue = venue;
	}

	public String getPrivacy()
	{
		return privacy;
	}

	public void setPrivacy(String privacy)
	{
		this.privacy = privacy;
	}

	public boolean getHide_guest_list()
	{
		return hide_guest_list;
	}

	public void setHide_guest_list(boolean hide_guest_list)
	{
		this.hide_guest_list = hide_guest_list;
	}

	public boolean getCan_invite_friends()
	{
		return can_invite_friends;
	}

	public void setCan_invite_friends(boolean can_invite_friends)
	{
		this.can_invite_friends = can_invite_friends;
	}

	public int getAll_members_count()
	{
		return all_members_count;
	}

	public void setAll_members_count(int all_members_count)
	{
		this.all_members_count = all_members_count;
	}

	public int getAttending_count()
	{
		return attending_count;
	}

	public void setAttending_count(int attending_count)
	{
		this.attending_count = attending_count;
	}

	public int getUnsure_count()
	{
		return unsure_count;
	}

	public void setUnsure_count(int unsure_count)
	{
		this.unsure_count = unsure_count;
	}

	public int getDeclined_count()
	{
		return declined_count;
	}

	public void setDeclined_count(int declined_count)
	{
		this.declined_count = declined_count;
	}

	public int getNot_replied_count()
	{
		return not_replied_count;
	}

	public void setNot_replied_count(int not_replied_count)
	{
		this.not_replied_count = not_replied_count;
	}

	public String getUserResponse()
	{
		return userResponse;
	}

	public void setUserResponse(String userResponse)
	{
		this.userResponse = userResponse;
	}

	public boolean isUserAttendingEvent()
	{
		return userResponse.equals(EventResponse.ATTENDING.toString().toLowerCase());
	}

	public boolean isUserUnsureEvent()
	{
		return userResponse.equals(EventResponse.UNSURE.toString().toLowerCase());
	}

	public boolean isUserDeclinedEvent()
	{
		return userResponse.equals(EventResponse.DECLINED.toString().toLowerCase());
	}

	public boolean isUserNotRepliedToEvent()
	{
		return userResponse.equals(EventResponse.NOT_REPLIED.toString().toLowerCase());
	}

	public static class Venue extends GraphObject implements Parcelable
	{
		private String	name;
		private String	street;
		private String	city;
		private String	state;
		private String	zip;
		private String	country;
		private String	latitude;
		private String	longitude;
		
		public Venue()
		{
			
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getStreet()
		{
			return street;
		}

		public void setStreet(String street)
		{
			this.street = street;
		}

		public String getCity()
		{
			return city;
		}

		public void setCity(String city)
		{
			this.city = city;
		}

		public String getState()
		{
			return state;
		}

		public void setState(String state)
		{
			this.state = state;
		}

		public String getZip()
		{
			return zip;
		}

		public void setZip(String zip)
		{
			this.zip = zip;
		}

		public String getCountry()
		{
			return country;
		}

		public void setCountry(String country)
		{
			this.country = country;
		}

		public String getLatitude()
		{
			return latitude;
		}

		public void setLatitude(String latitude)
		{
			this.latitude = latitude;
		}

		public String getLongitude()
		{
			return longitude;
		}

		public void setLongitude(String longitude)
		{
			this.longitude = longitude;
		}
		
		@Override
		public int describeContents()
		{
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			dest.writeString(name);
			dest.writeString(street);
			dest.writeString(city);
			dest.writeString(state);
			dest.writeString(zip);
			dest.writeString(country);
			dest.writeString(latitude);
			dest.writeString(longitude);
		}

		public static final Parcelable.Creator<Venue>	CREATOR	= new Parcelable.Creator<Venue>() {
																	public Venue createFromParcel(Parcel in)
																	{
																		return new Venue(in);
																	}

																	public Venue[] newArray(int size)
																	{
																		return new Venue[size];
																	}
																};

		private Venue(Parcel in)
		{
			name = in.readString();
			street = in.readString();
			city = in.readString();
			state = in.readString();
			zip = in.readString();
			country = in.readString();
			latitude = in.readString();
			longitude = in.readString();
		}
	}

	public static class Cover extends GraphObject implements Parcelable
	{
		private String	cover_id;
		private String	source;
		private int		offset_x;
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

		public int getOffset_x()
		{
			return offset_x;
		}

		public void setOffset_x(int offset_x)
		{
			this.offset_x = offset_x;
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
			dest.writeInt(offset_x);
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
			offset_x = in.readInt();
			offset_y = in.readInt();
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
		dest.writeString(eid);
		dest.writeString(name);
		dest.writeString(pic_small);
		dest.writeString(pic_big);
		dest.writeParcelable(pic_cover, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(pic_square);
		dest.writeString(pic);
		dest.writeString(host);
		dest.writeString(description);
		dest.writeString(start_time);
		dest.writeString(end_time);
		dest.writeString(creator);
		dest.writeString(update_time);
		dest.writeString(location);
		dest.writeParcelable(venue, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(privacy);
		dest.writeByte((byte) (hide_guest_list == true ? 1 : 0));
		dest.writeByte((byte) (can_invite_friends == true ? 1 : 0));
		dest.writeInt(all_members_count);
		dest.writeInt(attending_count);
		dest.writeInt(unsure_count);
		dest.writeInt(declined_count);
		dest.writeInt(not_replied_count);
		dest.writeString(userResponse);
	}

	public static final Parcelable.Creator<Event>	CREATOR	= new Parcelable.Creator<Event>() {
																public Event createFromParcel(Parcel in)
																{
																	return new Event(in);
																}

																public Event[] newArray(int size)
																{
																	return new Event[size];
																}
															};

	private Event(Parcel in)
	{
		eid = in.readString();
		name = in.readString();
		pic_small = in.readString();
		pic_big = in.readString();
		pic_cover = in.readParcelable(Cover.class.getClassLoader());
		pic_square = in.readString();
		pic = in.readString();
		host = in.readString();
		description = in.readString();
		start_time = in.readString();
		end_time = in.readString();
		creator = in.readString();
		update_time = in.readString();
		location = in.readString();
		venue = in.readParcelable(Venue.class.getClassLoader());
		privacy = in.readString();
		hide_guest_list = in.readByte() == 1;
		can_invite_friends = in.readByte() == 1;
		all_members_count = in.readInt();
		attending_count = in.readInt();
		unsure_count = in.readInt();
		declined_count = in.readInt();
		not_replied_count = in.readInt();
		userResponse = in.readString();
	}
}
