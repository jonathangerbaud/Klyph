/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.adapter.helper;

import java.util.ArrayList;
import java.util.List;
import com.abewy.android.apps.klyph.core.fql.Tag;

public class StreamRecycler
{
	private String		story;
	private List<Tag>	storyTags		= new ArrayList<Tag>();
	private String		date;
	private String		profilePictureUrl;
	private String		message;
	private List<Tag>	messageTags		= new ArrayList<Tag>();
	private boolean		photoVisible;
	private String		photoUrl;
	private String		photoLayout;
	private String		linkPictureUrl;
	private String		linkPictureLayout;
	private String		linkName;
	private String		linkCaption;
	private String		linkDecription;
	private List<Tag>	linkNameTags	= new ArrayList<Tag>();
	private boolean		linkIsVideo;
	private boolean		photoAlbumVisible;
	private boolean		photoAlbum1Visible;
	private String		photoAlbum1Url;
	private String		photoAlbum1Layout;
	private boolean		photoAlbum2Visible;
	private String		photoAlbum2Url;
	private String		photoAlbum2Layout;
	private boolean		photoAlbum3Visible;
	private String		photoAlbum3Url;
	private String		photoAlbum3Layout;
	private int			likeCount;
	private int			commentCount;
	private boolean		likeButtonVisible;
	private boolean		commentButtonvisible;
	private boolean		deleteButtonvisible;
	private boolean		buttonBarVisible;
	
	public String getStory()
	{
		return story;
	}
	public void setStory(String story)
	{
		this.story = story;
	}
	public List<Tag> getStoryTags()
	{
		return storyTags;
	}
	public void setStoryTags(List<Tag> storyTags)
	{
		this.storyTags = storyTags;
	}
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public String getProfilePictureUrl()
	{
		return profilePictureUrl;
	}
	public void setProfilePictureUrl(String profilePictureUrl)
	{
		this.profilePictureUrl = profilePictureUrl;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public List<Tag> getMessageTags()
	{
		return messageTags;
	}
	public void setMessageTags(List<Tag> messageTags)
	{
		this.messageTags = messageTags;
	}
	public boolean getPhotoVisible()
	{
		return photoVisible;
	}
	public void setPhotoVisible(boolean photoVisible)
	{
		this.photoVisible = photoVisible;
	}
	public String getPhotoUrl()
	{
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl)
	{
		this.photoUrl = photoUrl;
	}
	public String getPhotoLayout()
	{
		return photoLayout;
	}
	public void setPhotoLayout(String photoLayout)
	{
		this.photoLayout = photoLayout;
	}
	public String getLinkPictureUrl()
	{
		return linkPictureUrl;
	}
	public void setLinkPictureUrl(String linkPictureUrl)
	{
		this.linkPictureUrl = linkPictureUrl;
	}
	public String getLinkPictureLayout()
	{
		return linkPictureLayout;
	}
	public void setLinkPictureLayout(String linkPictureLayout)
	{
		this.linkPictureLayout = linkPictureLayout;
	}
	public String getLinkName()
	{
		return linkName;
	}
	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}
	public String getLinkCaption()
	{
		return linkCaption;
	}
	public void setLinkCaption(String linkCaption)
	{
		this.linkCaption = linkCaption;
	}
	public String getLinkDecription()
	{
		return linkDecription;
	}
	public void setLinkDecription(String linkDecription)
	{
		this.linkDecription = linkDecription;
	}
	public List<Tag> getLinkNameTags()
	{
		return linkNameTags;
	}
	public void setLinkNameTags(List<Tag> linkNameTags)
	{
		this.linkNameTags = linkNameTags;
	}
	public boolean getLinkIsVideo()
	{
		return linkIsVideo;
	}
	public void setLinkIsVideo(boolean linkIsVideo)
	{
		this.linkIsVideo = linkIsVideo;
	}
	public boolean getPhotoAlbumVisible()
	{
		return photoAlbumVisible;
	}
	public void setPhotoAlbumVisible(boolean photoAlbumVisible)
	{
		this.photoAlbumVisible = photoAlbumVisible;
	}
	public boolean getPhotoAlbum1Visible()
	{
		return photoAlbum1Visible;
	}
	public void setPhotoAlbum1Visible(boolean photoAlbum1Visible)
	{
		this.photoAlbum1Visible = photoAlbum1Visible;
	}
	public String getPhotoAlbum1Url()
	{
		return photoAlbum1Url;
	}
	public void setPhotoAlbum1Url(String photoAlbum1Url)
	{
		this.photoAlbum1Url = photoAlbum1Url;
	}
	public String getPhotoAlbum1Layout()
	{
		return photoAlbum1Layout;
	}
	public void setPhotoAlbum1Layout(String photoAlbum1Layout)
	{
		this.photoAlbum1Layout = photoAlbum1Layout;
	}
	public boolean getPhotoAlbum2Visible()
	{
		return photoAlbum2Visible;
	}
	public void setPhotoAlbum2Visible(boolean photoAlbum2Visible)
	{
		this.photoAlbum2Visible = photoAlbum2Visible;
	}
	public String getPhotoAlbum2Url()
	{
		return photoAlbum2Url;
	}
	public void setPhotoAlbum2Url(String photoAlbum2Url)
	{
		this.photoAlbum2Url = photoAlbum2Url;
	}
	public String getPhotoAlbum2Layout()
	{
		return photoAlbum2Layout;
	}
	public void setPhotoAlbum2Layout(String photoAlbum2Layout)
	{
		this.photoAlbum2Layout = photoAlbum2Layout;
	}
	public boolean getPhotoAlbum3Visible()
	{
		return photoAlbum3Visible;
	}
	public void setPhotoAlbum3Visible(boolean photoAlbum3Visible)
	{
		this.photoAlbum3Visible = photoAlbum3Visible;
	}
	public String getPhotoAlbum3Url()
	{
		return photoAlbum3Url;
	}
	public void setPhotoAlbum3Url(String photoAlbum3Url)
	{
		this.photoAlbum3Url = photoAlbum3Url;
	}
	public String getPhotoAlbum3Layout()
	{
		return photoAlbum3Layout;
	}
	public void setPhotoAlbum3Layout(String photoAlbum3Layout)
	{
		this.photoAlbum3Layout = photoAlbum3Layout;
	}
	public int getLikeCount()
	{
		return likeCount;
	}
	public void setLikeCount(int likeCount)
	{
		this.likeCount = likeCount;
	}
	public int getCommentCount()
	{
		return commentCount;
	}
	public void setCommentCount(int commentCount)
	{
		this.commentCount = commentCount;
	}
	public boolean getLikeButtonVisible()
	{
		return likeButtonVisible;
	}
	public void setLikeButtonVisible(boolean likeButtonVisible)
	{
		this.likeButtonVisible = likeButtonVisible;
	}
	public boolean getCommentButtonvisible()
	{
		return commentButtonvisible;
	}
	public void setCommentButtonvisible(boolean commentButtonvisible)
	{
		this.commentButtonvisible = commentButtonvisible;
	}
	public boolean getDeleteButtonvisible()
	{
		return deleteButtonvisible;
	}
	public void setDeleteButtonvisible(boolean deleteButtonvisible)
	{
		this.deleteButtonvisible = deleteButtonvisible;
	}
	public boolean getButtonBarVisible()
	{
		return buttonBarVisible;
	}
	public void setButtonBarVisible(boolean buttonBarVisible)
	{
		this.buttonBarVisible = buttonBarVisible;
	}
}
