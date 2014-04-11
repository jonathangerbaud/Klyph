/**
* @author Jonathan
*/

package com.abewy.android.apps.klyph.adapter.holder;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public interface IStreamHolder
{
	public ImageView getAuthorProfileImage();
	public TextView getStory();
	public TextView getPostTime();
	
	public TextView getMessage();
	
	public Button getLikeButton();
	public Button getCommentButton();
	public ImageButton getShareButton();
	public ImageButton getOverflowButton();
	public ViewGroup getButtonBar();
}
