package com.abewy.android.apps.klyph.core.fql;

import android.os.Parcel;
import android.os.Parcelable;
import com.abewy.android.apps.klyph.core.graph.GraphObject;

public class Group extends GraphObject implements Parcelable
{
	private String				creator;
	private String				description;
	private String				email;
	private String				gid;
	private String				icon;
	private String				icon34;
	private String				icon50;
	private String				icon68;
	private String				name;
	private String				nid;
	private String				office;
	private String				parent_id;
	private String				pic;
	private String				pic_big;
	private Cover				pic_cover;
	private String				pic_small;
	private String				pic_square;
	private String				privacy;
	private String				recent_news;
	private String				update_time;
	private Location			venue;
	private String				website;
	private boolean				administrator;
	private int					unread;

	public Group()
	{

	}

	public int getItemViewType()
	{
		return GraphObject.GROUP;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getGid()
	{
		return gid;
	}

	public void setGid(String gid)
	{
		this.gid = gid;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getIcon34()
	{
		return icon34;
	}

	public void setIcon34(String icon34)
	{
		this.icon34 = icon34;
	}

	public String getIcon50()
	{
		return icon50;
	}

	public void setIcon50(String icon50)
	{
		this.icon50 = icon50;
	}

	public String getIcon68()
	{
		return icon68;
	}

	public void setIcon68(String icon68)
	{
		this.icon68 = icon68;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNid()
	{
		return nid;
	}

	public void setNid(String nid)
	{
		this.nid = nid;
	}

	public String getOffice()
	{
		return office;
	}

	public void setOffice(String office)
	{
		this.office = office;
	}

	public String getParent_id()
	{
		return parent_id;
	}

	public void setParent_id(String parent_id)
	{
		this.parent_id = parent_id;
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

	public String getPrivacy()
	{
		return privacy;
	}

	public void setPrivacy(String privacy)
	{
		this.privacy = privacy;
	}

	public String getRecent_news()
	{
		return recent_news;
	}

	public void setRecent_news(String recent_news)
	{
		this.recent_news = recent_news;
	}

	public String getUpdate_time()
	{
		return update_time;
	}

	public void setUpdate_time(String update_time)
	{
		this.update_time = update_time;
	}

	public Location getVenue()
	{
		return venue;
	}

	public void setVenue(Location venue)
	{
		this.venue = venue;
	}

	public String getWebsite()
	{
		return website;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}

	public boolean getAdministrator()
	{
		return administrator;
	}

	public void setAdministrator(boolean administrator)
	{
		this.administrator = administrator;
	}

	public int getUnread()
	{
		return unread;
	}

	public void setUnread(int unread)
	{
		this.unread = unread;
	}



	public static class Cover extends GraphObject  implements Parcelable
	{
		private String	cover_id;
		private String	source;
		private int		offset_x;
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
			dest.writeInt(offset_x);
			dest.writeInt(offset_y);
		}

		private Cover(Parcel in)
		{
			cover_id = in.readString();
			source = in.readString();
			offset_x = in.readInt();
			offset_y = in.readInt();
		}

		public Cover()
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
		dest.writeString(creator);
		dest.writeString(description);
		dest.writeString(email);
		dest.writeString(gid);
		dest.writeString(icon);
		dest.writeString(icon34);
		dest.writeString(icon50);
		dest.writeString(icon68);
		dest.writeString(name);
		dest.writeString(nid);
		dest.writeString(office);
		dest.writeString(parent_id);
		dest.writeString(pic);
		dest.writeString(pic_big);
		dest.writeParcelable(pic_cover, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(pic_small);
		dest.writeString(pic_square);
		dest.writeString(privacy);
		dest.writeString(recent_news);
		dest.writeString(update_time);
		dest.writeParcelable(venue, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(website);
		dest.writeByte((byte) (administrator ? 1 : 0));
		dest.writeInt(unread);
	}
	
	private Group(Parcel in)
	{
		creator = in.readString();
		description = in.readString();
		email = in.readString();
		gid = in.readString();
		icon = in.readString();
		icon34 = in.readString();
		icon50 = in.readString();
		icon68 = in.readString();
		name = in.readString();
		nid = in.readString();
		office = in.readString();
		parent_id = in.readString();
		pic = in.readString();
		pic_big = in.readString();
		pic_cover = in.readParcelable(Cover.class.getClassLoader());
		pic_small = in.readString();
		pic_square = in.readString();
		privacy = in.readString();
		recent_news = in.readString();
		update_time = in.readString();
		venue = in.readParcelable(Location.class.getClassLoader());
		website = in.readString();
		administrator = in.readByte() == 1;
		unread = in.readInt();
	}
	
	public static final Parcelable.Creator<Group>	CREATOR	= new Parcelable.Creator<Group>() {
		public Group createFromParcel(Parcel in)
		{
			return new Group(in);
		}

		public Group[] newArray(int size)
		{
			return new Group[size];
		}
	};
}
