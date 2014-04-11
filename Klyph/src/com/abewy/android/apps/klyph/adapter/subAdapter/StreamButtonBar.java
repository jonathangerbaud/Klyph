/**
 * @author Jonathan
 */

package com.abewy.android.apps.klyph.adapter.subAdapter;

import java.util.Arrays;
import java.util.List;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.adapter.holder.IStreamHolder;
import com.abewy.android.apps.klyph.app.PostActivity;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.Attachment;
import com.abewy.android.apps.klyph.core.fql.Link;
import com.abewy.android.apps.klyph.core.fql.Media;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Status;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.facebook.IFbPermissionCallback;
import com.abewy.android.apps.klyph.facebook.IFbPermissionWorker;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.facebook.Session;

public class StreamButtonBar implements IFbPermissionCallback
{
	private final MultiObjectAdapter	parentAdapter;
	private final int					specialLayout;
	private boolean						pendingLike		= false;
	private boolean						pendingDelete	= false;
	private IStreamHolder				pendingHolder;
	private Stream						pendingStream;
	private GraphObject					pendingSubStream;
	private final List<String>			PERMISSIONS		= Arrays.asList("publish_actions", "status_update");

	public StreamButtonBar(MultiObjectAdapter parentAdapter, int specialLayout)
	{
		this.parentAdapter = parentAdapter;
		this.specialLayout = specialLayout;
	}

	protected Context getContext(View view)
	{
		return view.getContext();
	}

	protected MultiObjectAdapter getParentAdapter()
	{
		return parentAdapter;
	}

	public void mergeData(IStreamHolder holder, final Stream stream)
	{
		mergeData(holder, stream, null);
	}

	public void mergeData(IStreamHolder holder, final Stream stream, final GraphObject subStream)
	{
		if (holder.getButtonBar() != null)
		{
			manageListeners(holder, stream, subStream);
			manageButtons(holder, stream, subStream);
		}
	}

	private void manageListeners(final IStreamHolder holder, final Stream stream, final GraphObject subStream)
	{
		final Context context = getContext(holder.getAuthorProfileImage());

		/*
		 * ((FlipImageView) holder.getLikeButton()).setOnFlipListener(new FlipImageView.OnFlipListener() {
		 * 
		 * @Override
		 * public void onClick(FlipImageView view)
		 * {
		 * handleLikeAction(holder, stream, subStream);
		 * }
		 * 
		 * @Override
		 * public void onFlipStart(FlipImageView view)
		 * {
		 * 
		 * }
		 * 
		 * @Override
		 * public void onFlipEnd(FlipImageView view)
		 * {
		 * 
		 * }
		 * });
		 */

		holder.getLikeButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				handleLikeAction(holder, stream, subStream);
			}
		});

		if (specialLayout != SpecialLayout.STREAM_DETAIL)
		{
			holder.getCommentButton().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					context.startActivity(Klyph.getIntentForGraphObject((Activity) context, stream));
				}
			});
		}

		holder.getShareButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				handleShareAction(holder, stream);
			}
		});

		if (specialLayout != SpecialLayout.STREAM_DETAIL)
		{
			holder.getOverflowButton().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					handleOverflowAction(holder, stream);
					// handleDeleteAction(holder, stream);
				}
			});
		}
	}

	private void handleLikeAction(IStreamHolder holder, final Stream stream, final GraphObject subStream)
	{
		pendingLike = false;
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingLike = true;
			requestPermissions(holder, stream, subStream);
			return;
		}

		doLikeAction(holder, stream, subStream);
	}

	private void doLikeAction(final IStreamHolder holder, final Stream stream, final GraphObject subStream)
	{
		manageLikeButton(holder, stream, subStream, false);

		final Context context = getContext(holder.getAuthorProfileImage());

		if (getUserLikes(stream, subStream) == false)
		{
			setLikes(stream, subStream, true);
			manageLikeButton(holder, stream, subStream, false);

			Log.d("StreamButtonBar", "doLikeAction: " + stream.getPost_id());
			Log.d("StreamButtonBar", "doLikeAction: " + subStream);
			Log.d("StreamButtonBar", "doLikeAction: " + getId(stream, subStream));
			new AsyncRequest(Query.POST_LIKE, getId(stream, subStream), "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					Log.d("onComplete", "" + response.getError());
					if (response.getError() != null)
					{
						Toast.makeText(context, R.string.like_error, Toast.LENGTH_SHORT).show();
						setLikes(stream, subStream, false);
					}

					manageLikeButton(holder, stream, subStream, true);
					getParentAdapter().notifyDataSetChanged();
				}
			}).execute();
		}
		else
		{
			setLikes(stream, subStream, false);
			manageLikeButton(holder, stream, subStream, false);

			new AsyncRequest(Query.POST_UNLIKE, getId(stream, subStream), "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					Log.d("onComplete", "" + response.getError());
					if (response.getError() != null)
					{
						Toast.makeText(context, R.string.unlike_error, Toast.LENGTH_SHORT).show();
						setLikes(stream, subStream, true);
					}

					manageLikeButton(holder, stream, subStream, true);
					getParentAdapter().notifyDataSetChanged();
				}
			}).execute();
		}
	}

	private void handleOverflowAction(final IStreamHolder holder, final Stream stream)
	{
		PopupMenu popup = new PopupMenu(holder.getOverflowButton().getContext(), holder.getOverflowButton());

		if (getCanDelete(stream))
			popup.getMenu().add(Menu.NONE, R.id.menu_delete, Menu.NONE, R.string.delete);

		if (getCanShare(stream))
			popup.getMenu().add(Menu.NONE, R.id.menu_share_with, Menu.NONE, R.string.share_with);

		popup.getMenu().add(Menu.NONE, R.id.menu_view_profile, Menu.NONE, R.string.view_profile);

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean  onMenuItemClick(MenuItem item)
			{
				int id = item.getItemId();

				if (id == R.id.menu_delete)
				{
					handleDeleteAction(holder, stream);
					return true;
				}
				if (id == R.id.menu_share_with)
				{
					handleShareWithAction(holder, stream);
					return true;
				}
				if (id == R.id.menu_view_profile)
				{
					handleViewProfileAction(holder, stream);
					return true;
				}

				return false;
			}
		});

		holder.getOverflowButton().setTag(popup);

		popup.show();
	}

	private void handleDeleteAction(final IStreamHolder holder, final Stream stream)
	{
		pendingDelete = false;
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingDelete = true;
			requestPermissions(holder, stream, null);
			return;
		}

		doDeleteAction(holder, stream);
	}

	private void doDeleteAction(final IStreamHolder holder, final Stream stream)
	{
		final Context context = getContext(holder.getAuthorProfileImage());

		AlertUtil.showAlert(context, R.string.delete_post_confirmation_title, R.string.delete_post_confirmation_message, R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						delete(holder.getOverflowButton(), stream);
					}

				}, R.string.no, null);
	}

	private void handleShareAction(final IStreamHolder holder, final Stream stream)
	{
		Intent intent = new Intent(getContext(holder.getShareButton()), PostActivity.class);
		intent.putExtra(KlyphBundleExtras.SHARE, true);

		Attachment att = stream.getAttachment();

		if (stream.isLink())
		{
			Link link = stream.getLink();
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_URL, link.getUrl());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_IMAGE_URL, link.getPicture());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_NAME, link.getTitle());
			intent.putExtra(KlyphBundleExtras.SHARE_LINK_DESC, link.getSummary());
		}
		else if (stream.isPhoto()/* || att.isPhoto() */)
		{
			Photo photo = stream.getPhoto();
			intent.putExtra(KlyphBundleExtras.SHARE_PHOTO_ID, stream.isPhoto() ? photo.getObject_id() : att.getFb_object_id());
		}
		else if (stream.isVideo()/* || att.isVideo() */)
		{
			Video video = stream.getVideo();
			intent.putExtra(KlyphBundleExtras.SHARE_VIDEO_ID, stream.isVideo() ? video.getVid() : att.getFb_object_id());
		}
		/*
		 * else if (att.isAlbum())
		 * {
		 * intent.putExtra(KlyphBundleExtras.SHARE_ALBUM_ID, att.getFb_object_id());
		 * }
		 */
		else
		{
			intent.putExtra(KlyphBundleExtras.SHARE_POST_ID, stream);
			Log.d("StreamButtonBar", "Share something but I do not what");
		}

		getContext(holder.getShareButton()).startActivity(intent);
	}

	private void handleShareWithAction(final IStreamHolder holder, final Stream stream)
	{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);

		Attachment att = stream.getAttachment();

		if (stream.isLink())
		{
			Link link = stream.getLink();

			intent.putExtra(Intent.EXTRA_TEXT, link.getUrl());
			intent.putExtra(Intent.EXTRA_SUBJECT, link.getTitle());
			intent.setType("text/plain");

		}
		else if (stream.isPhoto()/* || att.isPhoto() */)
		{
			Photo photo = stream.getPhoto();
			intent.putExtra(Intent.EXTRA_TEXT, photo.getLargestImageURL());

			if (photo.getCaption().length() > 0)
				intent.putExtra(Intent.EXTRA_SUBJECT, photo.getCaption());
			else if (stream.getMessage().length() > 0)
				intent.putExtra(Intent.EXTRA_SUBJECT, stream.getMessage());

			intent.setType("text/plain");
			// intent.putExtra(KlyphBundleExtras.SHARE_PHOTO_ID, stream.isPhoto() ? photo.getObject_id() : att.getFb_object_id());
		}
		else if (stream.isVideo()/* || att.isVideo() */)
		{
			Video video = stream.getVideo();
			intent.putExtra(Intent.EXTRA_TEXT, video.getSrc_hq());

			if (video.getTitle().length() > 0)
				intent.putExtra(Intent.EXTRA_SUBJECT, video.getTitle());
			else if (stream.getMessage().length() > 0)
				intent.putExtra(Intent.EXTRA_SUBJECT, stream.getMessage());

			intent.setType("text/plain");
			// intent.putExtra(KlyphBundleExtras.SHARE_VIDEO_ID, stream.isVideo() ? video.getVid() : att.getFb_object_id());
		}
		/*
		 * else if (att.isAlbum())
		 * {
		 * intent.putExtra(KlyphBundleExtras.SHARE_ALBUM_ID, att.getFb_object_id());
		 * }
		 */
		else if (att.getMedia().size() == 1)
		{
			Media m = att.getMedia().get(0);

			intent.putExtra(Intent.EXTRA_TEXT, m.getSrc());
			intent.putExtra(Intent.EXTRA_SUBJECT, m.getAlt());
			intent.setType("text/plain");
		}
		else
		{
			if (stream.getMessage().length() > 0)
			{
				intent.putExtra(Intent.EXTRA_TEXT, stream.getMessage());
				intent.setType("text/plain");
			}
			else if (stream.getDescription().length() > 0)
			{
				intent.putExtra(Intent.EXTRA_TEXT, stream.getDescription());
				intent.setType("text/plain");
			}
		}

		try
		{
			getContext(holder.getShareButton()).startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{

		}
	}

	private void handleViewProfileAction(final IStreamHolder holder, Stream stream)
	{
		holder.getOverflowButton()
				.getContext()
				.startActivity(
						Klyph.getIntentForParams(holder.getOverflowButton().getContext(), stream.getActor_id(), stream.getActor_name(),
								stream.getActor_type()));
	}

	private void requestPermissions(final IStreamHolder holder, final Stream stream, final GraphObject subStream)
	{
		pendingHolder = holder;
		pendingStream = stream;
		pendingSubStream = subStream;

		((IFbPermissionWorker) getContext(holder.getAuthorProfileImage())).requestPublishPermissions(this, PERMISSIONS);
	}

	@Override
	public void onPermissionsChange()
	{
		if (pendingLike == true)
		{
			pendingLike = false;
			Session session = Session.getActiveSession();

			List<String> permissions = session.getPermissions();
			if (permissions.containsAll(PERMISSIONS))
			{
				handleLikeAction(pendingHolder, pendingStream, pendingSubStream);
				pendingHolder = null;
				pendingStream = null;
				pendingSubStream = null;
			}
		}
		else if (pendingDelete == true)
		{
			pendingDelete = false;
			Session session = Session.getActiveSession();

			List<String> permissions = session.getPermissions();
			if (permissions.containsAll(PERMISSIONS))
			{
				handleDeleteAction(pendingHolder, pendingStream);
				pendingHolder = null;
				pendingStream = null;
				pendingSubStream = null;
			}
		}
	}

	@Override
	public void onCancelPermissions()
	{

	}

	private void manageButtons(IStreamHolder holder, Stream stream, GraphObject subStream)
	{
		int likeCount = getLikeCount(stream, subStream);
		String numLike = String.valueOf(likeCount);
		int res = likeCount > 1 ? R.string.number_of_likes : R.string.zero_or_one_like;
		String likeStr = getContext(holder.getLikeButton()).getString(res, numLike);
		final SpannableString likeText = new SpannableString(likeStr);
		int index = likeStr.indexOf(numLike);
		likeText.setSpan(new RelativeSizeSpan(1.3f), index, index + numLike.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		// holder.getButtonBarText1().setText(likeText);

		int commentCount = getCommentCount(stream, subStream);
		String numComment = String.valueOf(commentCount);
		res = commentCount > 1 ? R.string.number_of_comments : R.string.zero_or_one_comment;
		String commentStr = getContext(holder.getLikeButton()).getString(res, numComment);
		final SpannableString commentText = new SpannableString(commentStr);
		index = commentStr.indexOf(numComment);
		commentText.setSpan(new RelativeSizeSpan(1.3f), index, index + numComment.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

		// holder.getButtonBarText2().setText(commentText);
		// holder.getButtonBarText2().setSingleLine();
		// holder.getButtonBarText2().setEllipsize(TruncateAt.MARQUEE);

		manageLikeButton(holder, stream, subStream);
		manageCommentButton(holder, stream, subStream);
		manageShareButton(holder, stream, subStream);

		manageOverflowButton(holder, stream);

		boolean likeVisible = holder.getLikeButton().getVisibility() == View.VISIBLE;
		boolean commentVisible = holder.getCommentButton().getVisibility() == View.VISIBLE;
		boolean shareVisible = holder.getShareButton().getVisibility() == View.VISIBLE;
		boolean overflowVisible = holder.getOverflowButton() != null ? holder.getOverflowButton().getVisibility() == View.VISIBLE : false;

		if (likeVisible || commentVisible || shareVisible || overflowVisible)
		{
			holder.getButtonBar().setVisibility(View.VISIBLE);
		}
	}

	private int	padding8	= -1;
	private int	padding12	= -1;

	private void manageLikeButton(IStreamHolder holder, Stream stream, GraphObject subStream)
	{
		if (getCanLike(stream, subStream) == true)
		{
			int iconId = R.attr.cardLikeIcon;

			if (getUserLikes(stream, subStream) == true)
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

			int likeCount = getLikeCount(stream, subStream);

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

	private void manageLikeButton(IStreamHolder holder, Stream stream, GraphObject subStream, boolean enabled)
	{
		manageLikeButton(holder, stream, subStream);

		holder.getLikeButton().setEnabled(enabled);
	}

	private void manageCommentButton(IStreamHolder holder, Stream stream, GraphObject subStream)
	{
		if (getCanComment(stream, subStream) == true)
		{

			int commentCount = getCommentCount(stream, subStream);

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

	private void manageOverflowButton(IStreamHolder holder, Stream stream)
	{
		holder.getOverflowButton().setVisibility(specialLayout == SpecialLayout.STREAM_DETAIL ? View.GONE : View.VISIBLE);
		
		if (holder.getOverflowButton().getTag() != null)
		{
			removeOverflowPopup(holder.getOverflowButton());
		}
	}

	@TargetApi(11)
	private void removeOverflowPopup(View view)
	{
		((PopupMenu) view.getTag()).dismiss();
		view.setTag(null);
	}

	private void manageShareButton(IStreamHolder holder, Stream stream, GraphObject subStream)
	{
		if (getCanShare(stream))
		{
			holder.getShareButton().setVisibility(View.VISIBLE);
		}
		else
		{
			holder.getShareButton().setVisibility(View.GONE);
		}
	}

	private void delete(View button, final Stream stream)
	{
		final Context context = getContext(button);
		final AlertDialog dialog = AlertUtil.showAlert(context, R.string.delete, R.string.deleting);

		new AsyncRequest(Query.DELETE_POST, stream.getPost_id(), "", new AsyncRequest.Callback() {

			@Override
			public void onComplete(Response response)
			{
				Log.d("onComplete", "" + response.getError());
				dialog.dismiss();

				if (response.getError() != null)
				{
					AlertUtil.showAlert(context, R.string.error, R.string.delete_post_error, R.string.ok);
				}
				else
				{
					Toast.makeText(context, R.string.post_deleted, Toast.LENGTH_SHORT).show();

					getParentAdapter().remove(stream, true);
				}
			}
		}).execute();
	}

	private String getId(Stream stream, GraphObject subStream)
	{
		if (subStream == null)
		{
			return stream.getObjectId();
		}
		else
		{
			if (subStream instanceof Link)
			{
				return ((Link) subStream).getLink_id();
			}
			else if (subStream instanceof Photo)
			{
				return ((Photo) subStream).getObject_id();
			}
			else if (subStream instanceof Video)
			{
				return ((Video) subStream).getVid();
			}
			else if (subStream instanceof Status)
			{
				return ((Status) subStream).getStatus_id();
			}

			return "";
		}
	}

	private void setLikes(Stream stream, GraphObject subStream, boolean like)
	{
		int factor = 1 * (like == true ? 1 : -1);

		if (subStream == null)
		{
			stream.getLike_info().setUser_likes(like);
			stream.getLike_info().setLike_count(stream.getLike_info().getLike_count() + factor);
		}
		else
		{
			if (subStream instanceof Link)
			{
				((Link) subStream).getLike_info().setUser_likes(like);
				((Link) subStream).getLike_info().setLike_count(((Link) subStream).getLike_info().getLike_count() + factor);
			}
			else if (subStream instanceof Photo)
			{
				((Photo) subStream).getLike_info().setUser_likes(like);
				((Photo) subStream).getLike_info().setLike_count(((Photo) subStream).getLike_info().getLike_count() + factor);
			}
			else if (subStream instanceof Video)
			{

			}
			else if (subStream instanceof Status)
			{
				((Status) subStream).getLike_info().setUser_likes(like);
				((Status) subStream).getLike_info().setLike_count(((Status) subStream).getLike_info().getLike_count() + factor);
			}
		}
	}

	private boolean getUserLikes(Stream stream, GraphObject subStream)
	{
		if (stream.getLike_info().getCan_like() == false && subStream != null)
		{
			if (subStream instanceof Link)
			{
				return ((Link) subStream).getLike_info().getUser_likes();
			}
			else if (subStream instanceof Photo)
			{
				return ((Photo) subStream).getLike_info().getUser_likes();
			}
			else if (subStream instanceof Video)
			{
				return false;
			}
			else if (subStream instanceof Status)
			{
				return ((Status) subStream).getLike_info().getUser_likes();
			}

			return false;
		}
		else
		{
			return stream.getLike_info().getUser_likes();
		}
	}

	private boolean getCanLike(Stream stream, GraphObject subStream)
	{
		if (stream.getLike_info().getCan_like() == false && subStream != null)
		{
			if (subStream instanceof Link)
			{
				return ((Link) subStream).getLike_info().getCan_like();
			}
			else if (subStream instanceof Photo)
			{
				return ((Photo) subStream).getLike_info().getCan_like();
			}
			else if (subStream instanceof Video)
			{
				return false;
			}
			else if (subStream instanceof Status)
			{
				return ((Status) subStream).getLike_info().getCan_like();
			}

			return false;
		}
		else
		{
			return stream.getLike_info().getCan_like();
		}
	}

	private boolean getCanComment(Stream stream, GraphObject subStream)
	{
		if (stream.getComment_info().getCan_comment() == false && subStream != null)
		{
			if (subStream instanceof Link)
			{
				return ((Link) subStream).getComment_info().getCan_comment();
			}
			else if (subStream instanceof Photo)
			{
				return ((Photo) subStream).getComment_info().getCan_comment();
			}
			else if (subStream instanceof Video)
			{
				return false;
			}
			else if (subStream instanceof Status)
			{
				return ((Status) subStream).getComment_info().getCan_comment();
			}

			return false;
		}
		else
		{
			return stream.getComment_info().getCan_comment();
		}
	}

	private boolean  getCanShare(Stream stream)
	{
		return stream.getPermalink() != null && stream.getPermalink().length() > 0;
	}

	private boolean  getCanDelete(Stream stream)
	{
		return stream.getApp_id().equals(Klyph.FACEBOOK_APP_ID) && stream.getSource_id().equals(KlyphSession.getSessionUserId());
	}

	private int getLikeCount(Stream stream, GraphObject subStream)
	{
		if (stream.getLike_info().getCan_like() == false && subStream != null)
		{
			if (subStream instanceof Link)
			{
				return ((Link) subStream).getLike_info().getLike_count();
			}
			else if (subStream instanceof Photo)
			{
				return ((Photo) subStream).getLike_info().getLike_count();
			}
			else if (subStream instanceof Video)
			{
				return 0;
			}
			else if (subStream instanceof Status)
			{
				return ((Status) subStream).getLike_info().getLike_count();
			}

			return 0;
		}
		else
		{
			return stream.getLike_info().getLike_count();
		}
	}

	private int getCommentCount(Stream stream, GraphObject subStream)
	{
		if (stream.getComment_info().getCan_comment() == false && subStream != null)
		{
			if (subStream instanceof Link)
			{
				return ((Link) subStream).getComment_info().getComment_count();
			}
			else if (subStream instanceof Photo)
			{
				return ((Photo) subStream).getComment_info().getComment_count();
			}
			else if (subStream instanceof Video)
			{
				return 0;
			}
			else if (subStream instanceof Status)
			{
				return ((Status) subStream).getComment_info().getComment_count();
			}

			return 0;
		}
		else
		{
			return stream.getComment_info().getComment_count();
		}
	}
}
