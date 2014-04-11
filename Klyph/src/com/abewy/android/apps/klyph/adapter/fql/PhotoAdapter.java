package com.abewy.android.apps.klyph.adapter.fql;

import java.util.Arrays;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.holder.PhotoHolder;
import com.abewy.android.apps.klyph.app.PostActivity;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.facebook.IFbPermissionCallback;
import com.abewy.android.apps.klyph.facebook.IFbPermissionWorker;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.util.DateUtil;
import com.abewy.android.apps.klyph.util.KlyphUtil;
import com.abewy.android.apps.klyph.util.TextViewUtil;
import com.facebook.Session;

public class PhotoAdapter extends KlyphAdapter implements IFbPermissionCallback
{
	private MultiObjectAdapter	parentAdapter;
	private PhotoHolder			pendingHolder;
	private Photo				pendingPhoto;
	private boolean 				pendingLike;

	private final List<String>	PERMISSIONS	= Arrays.asList("publish_actions", "status_update");

	public PhotoAdapter(MultiObjectAdapter parentAdapter)
	{
		super();
		this.parentAdapter = parentAdapter;
	}

	@Override
	protected int getLayout()
	{
		return R.layout.item_photo;
	}

	@Override
	public boolean  isEnabled(GraphObject object)
	{
		return false;
	}

	@Override
	protected void attachHolder(View view)
	{
		ImageView authorProfileImage = (ImageView) view.findViewById(R.id.author_profile_image);
		TextView story = (TextView) view.findViewById(R.id.story);
		TextView postTime = (TextView) view.findViewById(R.id.post_time);
		TextView message = (TextView) view.findViewById(R.id.message);

		Button likeButton = (Button) view.findViewById(R.id.like_button);
		Button commentButton = (Button) view.findViewById(R.id.comment_button);
		ImageButton shareButton = (ImageButton) view.findViewById(R.id.share_button);
		ViewGroup buttonBar = (ViewGroup) view.findViewById(R.id.button_bar);

		PhotoHolder holder = new PhotoHolder(authorProfileImage, story, postTime, message, likeButton, commentButton, shareButton, buttonBar);

		setHolder(view, holder);
	}

	@Override
	protected void mergeViewWithData(View view, GraphObject data)
	{
		super.mergeViewWithData(view, data);

		final PhotoHolder holder = (PhotoHolder) getHolder(view);

		setData(view, data);

		setData(view, holder, (Photo) data);
	}

	public void setData(View view, PhotoHolder holder, final Photo photo)
	{
		final ImageView imageView = holder.getAuthorProfileImage();

		loadImage(imageView, photo.getOwner_pic(), KlyphUtil.getProfilePlaceHolder(imageView.getContext()));

		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Intent intent = Klyph.getIntentForParams(getContext(imageView), photo.getOwner(), photo.getOwner_name(), photo.getOwner_type());
				imageView.getContext().startActivity(intent);
			}
		});

		holder.getStory().setText(photo.getOwner_name());

		TextViewUtil.setElementClickable(holder.getStory().getContext(), holder.getStory(), photo.getOwner_name(), photo.getOwner(),
				photo.getOwner_type(), false);

		if (photo.getTarget_id() != null && photo.getTarget_id().length() > 0 && photo.getTarget_name().length() > 0
			&& !photo.getTarget_id().equals(photo.getOwner()))
		{
			holder.getStory().append(" > " + photo.getTarget_name());

			TextViewUtil.setElementClickable(holder.getStory().getContext(), holder.getStory(), photo.getTarget_name(), photo.getTarget_id(),
					photo.getTarget_type(), false);
		}

		holder.getPostTime().setText(DateUtil.timeAgoInWords(holder.getPostTime().getContext(), photo.getCreated()));

		if (photo.getCaption().length() > 0)
		{
			holder.getMessage().setText(photo.getCaption());
			holder.getMessage().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getMessage().setVisibility(View.GONE);
		}

		manageLikeButton(holder, photo);
		manageCommentButton(holder, photo);
		manageListeners(holder, photo);

		holder.getLikeButton().setEnabled(true);
	}

	private int	padding8	= -1;
	private int	padding12	= -1;

	private void manageLikeButton(PhotoHolder holder, Photo photo)
	{
		if (photo.getLike_info().getCan_like())
		{
			int iconId = R.attr.cardLikeIcon;

			if (photo.getLike_info().getUser_likes())
			{
				iconId = R.attr.userLikeBaselineIcon2;
			}

			iconId = AttrUtil.getResourceId(getContext(holder.getAuthorProfileImage()), iconId);

			holder.getLikeButton().setCompoundDrawablesWithIntrinsicBounds(iconId, 0, 0, 0);

			Resources res = holder.getLikeButton().getContext().getResources();

			if (padding8 == -1)
				padding8 = res.getDimensionPixelSize(R.dimen.dip_8);

			if (padding12 == -1)
				padding12 = res.getDimensionPixelSize(R.dimen.dip_12);

			int likeCount = photo.getLike_info().getLike_count();

			if (likeCount > 0)
			{
				holder.getLikeButton().setPadding(padding8, 0, padding8, 0);

				holder.getLikeButton().setCompoundDrawablePadding(4);
				holder.getLikeButton().setText(String.valueOf(likeCount));
			}
			else
			{
				holder.getLikeButton().setPadding(padding12, 0, padding8, 0);
				holder.getLikeButton().setText("");
			}

			holder.getLikeButton().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getLikeButton().setVisibility(View.GONE);
		}
	}

	private void manageCommentButton(PhotoHolder holder, Photo photo)
	{
		if (photo.getComment_info().getCan_comment())
		{

			int commentCount = photo.getComment_info().getComment_count();

			if (commentCount > 0)
			{
				int icon = AttrUtil.getResourceId(holder.getCommentButton().getContext(), R.attr.cardCommentIcon);
				holder.getCommentButton().setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
				holder.getCommentButton().setCompoundDrawablePadding(4);
				holder.getCommentButton().setText(String.valueOf(commentCount));
			}
			else
			{
				holder.getCommentButton().setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				holder.getCommentButton().setText(holder.getCommentButton().getContext().getString(R.string.comment));
			}

			holder.getCommentButton().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getCommentButton().setVisibility(View.GONE);
		}
	}

	private void manageListeners(final PhotoHolder holder, final Photo photo)
	{
		final Context context = getContext(holder.getAuthorProfileImage());

		holder.getLikeButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				handleLikeAction(holder, photo);
			}
		});

		holder.getCommentButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				// context.startActivity(Klyph.getIntentForGraphObject((Activity) context, photo));
			}
		});

		holder.getShareButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				handleShareAction(holder, photo);
			}
		});
	}

	private void handleLikeAction(final PhotoHolder holder, final Photo photo)
	{
		pendingLike = false;
		pendingPhoto = null;
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingLike = true;
			requestPermissions(holder, photo);
			return;
		}

		doLikeAction(holder, photo);
	}

	private void doLikeAction(final PhotoHolder holder, final Photo photo)
	{
		// manageLikeButton(holder, stream, subStream, false);

		final Context context = getContext(holder.getAuthorProfileImage());

		if (!photo.getLike_info().getUser_likes())
		{
			photo.getLike_info().setUser_likes(true);
			photo.getLike_info().setLike_count(photo.getLike_info().getLike_count() + 1);
			manageLikeButton(holder, photo);
			holder.getLikeButton().setEnabled(false);

			new AsyncRequest(Query.POST_LIKE, photo.getObject_id(), "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					Log.d("onComplete", "" + response.getError());
					if (response.getError() != null)
					{
						Toast.makeText(context, R.string.like_error, Toast.LENGTH_SHORT).show();
						photo.getLike_info().setUser_likes(false);
						photo.getLike_info().setLike_count(photo.getLike_info().getLike_count() - 1);
					}

					holder.getLikeButton().setEnabled(true);
					manageLikeButton(holder, photo);
					parentAdapter.notifyDataSetChanged();
				}
			}).execute();
			
			parentAdapter.notifyDataSetChanged();
		}
		else
		{
			photo.getLike_info().setUser_likes(false);
			photo.getLike_info().setLike_count(photo.getLike_info().getLike_count() - 1);
			manageLikeButton(holder, photo);
			holder.getLikeButton().setEnabled(false);

			new AsyncRequest(Query.POST_UNLIKE, photo.getObject_id(), "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					Log.d("onComplete", "" + response.getError());
					if (response.getError() != null)
					{
						Toast.makeText(context, R.string.unlike_error, Toast.LENGTH_SHORT).show();
						photo.getLike_info().setUser_likes(true);
						photo.getLike_info().setLike_count(photo.getLike_info().getLike_count() + 1);
					}

					holder.getLikeButton().setEnabled(true);
					manageLikeButton(holder, photo);
					parentAdapter.notifyDataSetChanged();
				}
			}).execute();
			
			parentAdapter.notifyDataSetChanged();
		}
	}
	
	private void handleShareAction(final PhotoHolder holder, final Photo photo)
	{
		Intent intent = new Intent(getContext(holder.getShareButton()), PostActivity.class);
		intent.putExtra(KlyphBundleExtras.SHARE, true);

		intent.putExtra(KlyphBundleExtras.SHARE_PHOTO_ID, photo.getObject_id());
			
		getContext(holder.getShareButton()).startActivity(intent);
	}

	private void requestPermissions(final PhotoHolder holder, final Photo photo)
	{
		pendingHolder = holder;
		pendingPhoto = photo;

		((IFbPermissionWorker) getContext(holder.getAuthorProfileImage())).requestPublishPermissions(this, PERMISSIONS);
	}

	@Override
	public void onPermissionsChange()
	{
		if (pendingLike == true)
		{
			handleLikeAction(pendingHolder, pendingPhoto);
		}
	}

	@Override
	public void onCancelPermissions()
	{
		pendingLike = false;
	}
}
