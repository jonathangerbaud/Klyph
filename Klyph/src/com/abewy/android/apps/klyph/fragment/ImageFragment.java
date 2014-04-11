package com.abewy.android.apps.klyph.fragment;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphDownloadManager;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Photo.Image;
import com.abewy.android.apps.klyph.core.graph.Comment;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.imageloader.SimpleListener;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.facebook.IFbPermissionCallback;
import com.abewy.android.apps.klyph.facebook.IFbPermissionWorker;
import com.abewy.android.apps.klyph.fragment.StreamFragment.StreamLikeCount;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.util.DateUtil;
import com.abewy.android.apps.klyph.util.TextViewUtil;
import com.abewy.android.apps.klyph.view.ListEmptyView;
import com.abewy.android.apps.klyph.widget.KlyphSlidingDrawer;
import com.abewy.util.Android;
import com.facebook.Session;

public class ImageFragment extends KlyphFragment implements IFbPermissionCallback
{
	// Facebook permissions
	private static final List<String>	PERMISSIONS				= Arrays.asList("publish_actions", "publish_stream");
	private boolean						pendingAnnounce			= false;

	private ImageViewTouch				imageView;
	private ProgressBar					progress;
	private ListEmptyView				emptyView;
	private StreamLikeCount				likeTitle;
	private Photo						photo;
	private IToggleBarVisibility		toggleActivity;
	private MultiObjectAdapter			adapter;

	// Sliding Drawer
	private KlyphSlidingDrawer			slidingDrawer;

	// Author widgets
	private ViewGroup					captionInfo;
	private ImageView					profileImage;
	private TextView					authorName;
	private TextView					postTime;

	// Comment bar
	private EditText					sendTextEdit;
	private ImageButton					sendButton;

	private boolean						pendingDeleteComment	= false;
	private boolean						pendingLikeComment		= false;
	private boolean						pendingReplyComment		= false;
	private Comment						pendingComment;
	private Comment						pendingCommentLike;
	private Comment						pendingCommentReply;

	private DataSetObserver				observer				= new DataSetObserver() {

																	@Override
																	public void onChanged()
																	{
																		super.onChanged();

																		if (photo != null)
																		{
																			refreshLikeCount();
																		}
																	}
																};

	public ImageFragment()
	{

	}

	private int	index;

	public ImageFragment(int index)
	{
		this.index = index;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		progress = (ProgressBar) view.findViewById(R.id.progress);
		emptyView = (ListEmptyView) view.findViewById(R.id.emptyView);
		imageView = (ImageViewTouch) view.findViewById(R.id.image);

		slidingDrawer = (KlyphSlidingDrawer) view.findViewById(R.id.sliding_drawer);

		captionInfo = (ViewGroup) view.findViewById(R.id.caption_info);
		profileImage = (ImageView) view.findViewById(R.id.author_profile_image);
		authorName = (TextView) view.findViewById(R.id.author_name);
		postTime = (TextView) view.findViewById(R.id.post_time);

		sendTextEdit = (EditText) view.findViewById(R.id.send_text_edit);
		sendButton = (ImageButton) view.findViewById(R.id.send_button);

		emptyView.setText(R.string.cannot_find_photo);

		getListView().setCacheColorHint(AttrUtil.getColor(getActivity(), android.R.attr.windowBackground));

		// imageView.setOnScaleListener(this);
		imageView.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {

			@Override
			public void onSingleTapConfirmed()
			{
				toggleBarVisibility();
			}
		});

		sendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				handlePostComment();
			}
		});

		defineEmptyText(R.string.no_comment);

		adapter = new MultiObjectAdapter(getListView(), SpecialLayout.PHOTO);
		setListAdapter(adapter);

		// setRequestType(Query.PHOTO_AND_COMMENTS);
		setRequestType(Query.PHOTO);

		super.onViewCreated(view, savedInstanceState);
		
		//Crash if before super.onViewCreated(view, savedInstanceState);
		getListView().getAdapter().registerDataSetObserver(observer);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		if (getElementId() != null && getElementId().length() > 0)
		{
			setIsFirstLoad(true);
			setOffset(null);
			setNoMoreData(false);
			getAdapter().clear();
			setListVisible(false);
			load();
		}
	}

	private void beginLoading()
	{
		photo = null;

		if (imageView != null)
			imageView.setImageDrawable(null);

		emptyView.setVisibility(View.GONE);

		setProgressVisibility(true);
		setSlidingDrawerVisibility(false);
		setCommentEnabled(false);
	}

	private void setProgressVisibility(boolean visible)
	{
		if (progress != null)
			progress.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	private void setCommentEnabled(boolean enabled)
	{
		int textResId = enabled ? R.string.post_comment : R.string.comments_disabled;

		sendTextEdit.setHint(textResId);

		sendTextEdit.setEnabled(enabled);
		sendButton.setEnabled(enabled);
	}

	private void setSlidingDrawerVisibility(boolean visible)
	{
		if (slidingDrawer != null)
			slidingDrawer.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	@Override
	public void load()
	{
		removeClickListener();

		beginLoading();

		if (photo != null)
		{
			load(photo);
		}

		super.load();
		/*
		 * if (isFirstLoad()) { if (getView() == null) { loadOnCreate = true; }
		 * else { Log.d("ImageFragment", "load"); beginLoading(); super.load();
		 * } }
		 */
	}

	private void load(Photo photo)
	{
		setPhoto(photo);

		setElementId(photo.getObject_id());
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		if (data.size() > 0)
		{
			GraphObject object = data.get(0);

			if (object instanceof Photo)
			{
				Photo photo = (Photo) data.get(0);

				// if (this.photo == null)
				// {
				setPhoto(photo);
				// }

				getAdapter().add(photo);

				likeTitle = new StreamLikeCount();
				getAdapter().add(likeTitle);
				refreshLikeCount();

				data.remove(0);

				super.populate(data);
				setRequestType(Query.COMMENTS);
				setNoMoreData(false);

				(getActivity()).invalidateOptionsMenu();
				refresh();
			}
			else
			{
				super.populate(data);

				setNoMoreData(!isFirstLoad()
								&& (data.size() == 0 || (getRequest().getPagingNext() == null || getRequest().getPagingNext().length() == 0)));
				setOffset(getAfterCursor());
			}
		}
		else
		{
			super.populate(data);

			setNoMoreData(!isFirstLoad()
							&& (data.size() == 0 || (getRequest().getPagingNext() == null || getRequest().getPagingNext().length() == 0)));
			setOffset(getAfterCursor());
		}
	}

	private void setPhoto(Photo photo)
	{
		this.photo = photo;

		setAuthorDetails();

		removeClickListener();
		loadImage();

		setSlidingDrawerVisibility(true);
		setCommentEnabled(photo.getComment_info().getCan_comment());

		getActivity().invalidateOptionsMenu();

		if (toggleActivity != null)
		{
			toggleBottomBarVisibility(toggleActivity.isBarVisible(), false);
		}
	}

	private void toggleBottomBarVisibility(final boolean visible, boolean animate)
	{
		int height = captionInfo.getHeight();

		slidingDrawer.setVisibility(visible ? View.VISIBLE : View.GONE);

		if (animate == false)
		{
			captionInfo.setTranslationY(visible ? 0 : height);
		}
		else
		{
			Interpolator interpolator = visible ? new DecelerateInterpolator() : new AccelerateInterpolator();
			captionInfo.animate().translationY(visible ? 0 : height).setDuration(250).setInterpolator(interpolator);
		}
		/*
		 * final View bottomBar = captionInfo;
		 * float y = getView().getHeight();
		 * 
		 * if (slidingDrawer.isOpened() == false)
		 * {
		 * View handle = (View) getListView().getParent().getParent();
		 * View commentBar = (View) sendTextEdit.getParent().getParent();
		 * 
		 * y = handle.getHeight() + commentBar.getHeight();
		 * }
		 * 
		 * float dy = y;
		 * 
		 * if (visible == true)
		 * {
		 * y = 0;
		 * }
		 * 
		 * if (animate == false)
		 * {
		 * setCommentVisibility(visible ? true : false);
		 * setSlidingDrawerVisibility(visible ? true : false);
		 * }
		 * else
		 * {
		 * // Set initial position before setting visible
		 * if (visible == false)
		 * dy = 0;
		 * 
		 * bottomBar.setTranslationY(dy);
		 * setCommentVisibility(true);
		 * setSlidingDrawerVisibility(true);
		 * }
		 * 
		 * if (animate == true)
		 * {
		 * // bottomBar.setVisibility(View.GONE);
		 * Interpolator interpolator = visible ? new DecelerateInterpolator() : new AccelerateInterpolator();
		 * 
		 * bottomBar.animate().setDuration(250).setInterpolator(interpolator).translationY(y).setListener(new Animator.AnimatorListener() {
		 * 
		 * @Override
		 * public void onAnimationStart(Animator animation)
		 * {
		 * 
		 * }
		 * 
		 * @Override
		 * public void onAnimationRepeat(Animator animation)
		 * {}
		 * 
		 * @Override
		 * public void onAnimationEnd(Animator animation)
		 * {
		 * Log.d("ImageFragment", "onAnimationEnd");
		 * setCommentVisibility(visible);
		 * setSlidingDrawerVisibility(visible);
		 * 
		 * }
		 * 
		 * @Override
		 * public void onAnimationCancel(Animator animation)
		 * {
		 * 
		 * }
		 * });
		 * }
		 * else
		 * {
		 * bottomBar.setTranslationY(y);
		 * setCommentVisibility(visible ? true : false);
		 * setSlidingDrawerVisibility(visible ? true : false);
		 * }
		 */
	}

	private void setAuthorDetails()
	{
		if (photo.getCaption().length() > 0)
		{
			postTime.setText(photo.getCaption());
		}
		else
		{
			postTime.setText(DateUtil.timeAgoInWords(postTime.getContext(), photo.getCreated()));
		}
		ImageLoader.display(profileImage, photo.getOwner_pic());

		authorName.setText(photo.getOwner_name());

		TextViewUtil.setElementClickable(authorName.getContext(), authorName, photo.getOwner_name(), photo.getOwner(), photo.getOwner_type(), false);

		if (photo.getTarget_id() != null && photo.getTarget_id().length() > 0 && photo.getTarget_name().length() > 0
			&& !photo.getTarget_id().equals(photo.getOwner()))
		{
			authorName.append(" > " + photo.getTarget_name());

			TextViewUtil.setElementClickable(authorName.getContext(), authorName, photo.getTarget_name(), photo.getTarget_id(),
					photo.getTarget_type(), false);
		}
	}

	private void loadImage()
	{
		progress.setVisibility(View.VISIBLE);

		ImageLoader.display(imageView, getLargestImageURL(), true, 0, new SimpleListener() {

			@Override
			public void onError()
			{
				Log.d("ImageFragment", "Failed to load image ");
			}

			@Override
			public void onSuccess()
			{
				Log.d("ImageFragment", "onSuccess: ");
				setProgressVisibility(false);
			}
		});
	}

	private void removeClickListener()
	{
		if (imageView != null)
		{
			imageView.setOnClickListener(null);
		}
	}

	private void toggleBarVisibility()
	{
		if (toggleActivity != null)
		{
			boolean visible = toggleActivity.toggleBarVisibility(this);

			toggleBottomBarVisibility(visible, true);
		}
	}

	public void setBarVisibility(boolean visible)
	{
		if (getView() != null)
		{
			toggleBottomBarVisibility(visible, false);
		}
	}

	private String getLargestImageURL()
	{
		Collections.sort(photo.getImages(), new Comparator<Image>() {

			@Override
			public int compare(Image lhs, Image rhs)
			{
				if (lhs.getWidth() < rhs.getWidth())
					return 1;
				else if (lhs.getWidth() > rhs.getWidth())
					return -1;

				return 0;
			}
		});

		Image selectedImage = photo.getImages().get(0);

		return selectedImage.getSource();
	}

	private void handlePostComment()
	{
		pendingAnnounce = false;
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingAnnounce = true;
			requestPublishPermissions(session);
			return;
		}

		postComment();
	}

	private void requestPublishPermissions(Session session)
	{
		((IFbPermissionWorker) getActivity()).requestPublishPermissions(this, PERMISSIONS);
	}

	private void postComment()
	{
		if (sendTextEdit.getText().toString().length() > 0)
		{
			Bundle params = new Bundle();
			params.putString("message", sendTextEdit.getText().toString());

			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(sendTextEdit.getWindowToken(), 0);

			final AlertDialog publishing = new AlertDialog.Builder(getActivity()).setTitle(R.string.publish_comment_dialog_title)
					.setMessage(R.string.publish_comment_dialog_message).setCancelable(false).create();
			publishing.show();

			new AsyncRequest(Query.POST_COMMENT, getElementId(), params, new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					publishing.hide();
					onCommentRequestComplete(response);
				}
			}).execute();
		}
		else
		{
			new AlertDialog.Builder(getActivity()).setTitle(R.string.error).setMessage(R.string.define_comment_before_publish)
					.setPositiveButton(R.string.ok, null).create().show();
		}
	}

	private void onCommentRequestComplete(final Response response)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onCommentRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						onCommentRequestError(response.getError());
					}
				}
			});
		}
	}

	private void onCommentRequestSuccess(List<GraphObject> results)
	{
		photo.getComment_info().setComment_count(photo.getComment_info().getComment_count() + 1);
		getAdapter().notifyDataSetChanged();

		sendTextEdit.setText("");

		if (getActivity() != null)
			Toast.makeText(getActivity(), R.string.publish_comment_ok, Toast.LENGTH_SHORT).show();

		setNoMoreData(false);
		refresh();
	}

	private void onCommentRequestError(RequestError error)
	{
		if (getActivity() != null)
		{
			new AlertDialog.Builder(getActivity()).setTitle(R.string.error).setMessage(R.string.publish_comment_error)
					.setPositiveButton(R.string.ok, null).create().show();
		}
	}

	private void refreshLikeCount()
	{
		if (getActivity() != null && photo != null)
		{
			int likeCount = photo.getLike_info().getLike_count();

			if (likeCount == 0)
			{
				likeTitle.setText(getString(R.string.no_like));
			}
			else if (likeCount == 1)
			{
				likeTitle.setText(getString(R.string.one_like));
			}
			else
			{
				likeTitle.setText(getString(R.string.several_likes, likeCount));
			}
		}
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		if (photo != null && menu.findItem(R.id.menu_delete) == null)
		{
			/*
			 * if (Klyph.FACEBOOK_APP_ID.equals(photo.getApp_id()) &&
			 * stream.getSource_id().equals(Klyph.getSessionUserId())) {
			 * menu.add(Menu.NONE, R.id.menu_delete, Menu.NONE, "Delete")
			 * .setIcon(AttrUtil.getResourceId(getActivity(),
			 * R.attr.discardIcon))
			 * .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); }
			 */
		}

		if (Android.isMinAPI(9) && photo != null && menu.findItem(R.id.menu_download) == null)
		{
			menu.add(Menu.NONE, R.id.menu_download, 2, R.string.menu_download).setIcon(AttrUtil.getResourceId(getActivity(), R.attr.downloadIcon))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean  onOptionsItemSelected(MenuItem item)
	{
		/*
		 * if (item.getItemId() == R.id.menu_delete) { handleDeleteAction();
		 * return true; } else
		 */
		if (item.getItemId() == R.id.menu_download)
		{
			KlyphDownloadManager.downloadPhoto(getActivity(), getLargestImageURL(), photo.getObject_id(), photo.getCaption(), true, true);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View view, int position, long id)
	{
		super.onListItemClick(l, view, position, id);

		if (l.getItemAtPosition(position) instanceof StreamLikeCount)
		{
			UserLikeDialog uld = new UserLikeDialog();
			uld.setElementId(getElementId());
			uld.show(getActivity().getFragmentManager(), "userlike");
		}
		else if (l.getItemAtPosition(position) instanceof Comment)
		{
			final Comment comment = (Comment) getAdapter().getItem(position);
			handleClickComment(view, comment);
		}
	}

	private void handleClickComment(View view, final Comment comment)
	{
		List<String> list = new ArrayList<String>();

		int like = -1;
		int reply = -1;
		int copy = -1;
		int see = -1;
		int download = -1;
		int share = -1;
		int view_profile = -1;
		int delete = -1;

		// if (comment.getCan_like())
		// {
		list.add(comment.getUser_likes() ? getString(R.string.unlike) : getString(R.string.like));
		like = list.size() - 1;
		// }

		if (comment.getCan_comment())
		{
			list.add(getString(R.string.reply));
			reply = list.size() - 1;
		}

		if (comment.getMessage().length() > 0)
		{
			list.add(getString(R.string.copy_text));
			copy = list.size() - 1;
		}

		if (comment.getLike_count() > 0)
		{
			list.add(getString(R.string.see_who_likes_this));
			see = list.size() - 1;
		}

		if (comment.getAttachment().getMedia().getImage().getSrc().length() > 0)
		{
			list.add(getString(R.string.download_image));
			download = list.size() - 1;
		}

		// list.add(getString(R.string.share));
		// share = list.size() - 1;

		if (comment.getFrom().getId().equals(KlyphSession.getSessionUserId()))
		{
			list.add(getString(R.string.delete));
			delete = list.size() - 1;
		}

		list.add(getString(R.string.view_profile));
		view_profile = list.size() - 1;

		final int flike = like;
		final int freply = reply;
		final int fcopy = copy;
		final int fsee = see;
		final int fdownload = download;
		final int fshare = share;
		final int fview_profile = view_profile;
		final int fdelete = delete;

		// For Api 8 to 10
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(list.toArray(new String[0]), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which)
			{
				if (which == flike)
				{
					handleLikeCommentAction(comment);
				}
				else if (which == freply)
				{
					handleReplyAction(comment);
				}
				else if (which == fcopy)
				{
					handleCopyTextAction(comment);
				}
				else if (which == fsee)
				{
					handleSeeWhoLikesThisAction(comment);
				}
				else if (which == fdownload)
				{
					handleDownloadAction(comment);
				}
				else if (which == fshare)
				{
					handleShareAction(comment);
				}
				else if (which == fview_profile)
				{
					handleViewProfileAction(comment);
				}
				else if (which == fdelete)
				{
					handleDeleteCommentAction(comment);
				}
			}
		});
		builder.create().show();
	}

	private void handleLikeCommentAction(final Comment comment)
	{
		pendingLikeComment = false;
		pendingCommentLike = comment;
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingLikeComment = true;
			requestPublishPermissions(session);
			return;
		}

		doLikeCommentAction(comment);
		pendingLikeComment = false;
	}

	private void doLikeCommentAction(final Comment comment)
	{
		if (comment.getUser_likes() == false)
		{
			comment.setUser_likes(true);
			comment.setLike_count(comment.getLike_count() + 1);

			new AsyncRequest(Query.POST_LIKE, comment.getId(), "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					Log.d("onComplete", "" + response.getError());
					onCommentLikeRequestComplete(response, comment);
				}
			}).execute();
		}
		else
		{
			comment.setUser_likes(false);
			comment.setLike_count(comment.getLike_count() - 1);
			getAdapter().notifyDataSetChanged();

			new AsyncRequest(Query.POST_UNLIKE, comment.getId(), "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					Log.d("onComplete", "" + response.getError());
					onCommentUnlikeRequestComplete(response, comment);
				}
			}).execute();
		}
	}

	private void onCommentLikeRequestComplete(final Response response, final Comment comment)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onCommentLikeRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						onCommentLikeRequestError(response.getError(), comment);
					}

					getAdapter().notifyDataSetChanged();
				}
			});
		}
	}

	private void onCommentLikeRequestSuccess(List<GraphObject> result)
	{

	}

	private void onCommentLikeRequestError(RequestError error, Comment comment)
	{
		Toast.makeText(getActivity(), R.string.like_error, Toast.LENGTH_SHORT).show();
		comment.setUser_likes(false);
		comment.setLike_count(comment.getLike_count() - 1);
	}

	private void onCommentUnlikeRequestComplete(final Response response, final Comment comment)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onCommentUnlikeRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						onCommentUnlikeRequestError(response.getError(), comment);
					}

					getAdapter().notifyDataSetChanged();
				}
			});
		}
	}

	private void onCommentUnlikeRequestSuccess(List<GraphObject> result)
	{

	}

	private void onCommentUnlikeRequestError(RequestError error, Comment comment)
	{
		Toast.makeText(getActivity(), R.string.unlike_error, Toast.LENGTH_SHORT).show();
		comment.setUser_likes(true);
		comment.setLike_count(comment.getLike_count() + 1);
	}

	private void handleReplyAction(final Comment comment)
	{
		pendingReplyComment = false;
		pendingCommentReply = comment;

		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingReplyComment = true;
			requestPublishPermissions(session);
			return;
		}

		replyToComment(comment);
	}

	private void replyToComment(final Comment comment)
	{
		final EditText editText = new EditText(getActivity());
		editText.setHint(R.string.post_hint);
		editText.setLines(5);
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		int padding = getResources().getDimensionPixelSize(R.dimen.dip_8);
		editText.setPadding(padding, padding, padding, padding);

		AlertDialog dialog = AlertUtil.showAlert(getActivity(), R.string.post_a_reply, AlertUtil.NONE, editText, R.string.menu_send,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						handleSendReplyAction(comment, editText.getText().toString());

					}
				}, R.string.cancel, null);

		dialog.show();
	}

	private void handleSendReplyAction(final Comment comment, String reply)
	{
		if (reply.length() > 0)
		{
			Bundle params = new Bundle();
			params.putString("message", reply);

			final AlertDialog publishing = new AlertDialog.Builder(getActivity()).setTitle(R.string.publish_comment_dialog_title)
					.setMessage(R.string.publish_comment_dialog_message).setCancelable(false).create();
			publishing.show();

			new AsyncRequest(Query.POST_COMMENT, comment.getId(), params, new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					publishing.hide();

					onReplyCommentRequestComplete(response, comment);
				}
			}).execute();
		}
		else
		{
			new AlertDialog.Builder(getActivity()).setTitle(R.string.error).setMessage(R.string.define_comment_before_publish)
					.setPositiveButton(R.string.ok, null).create().show();
		}
	}

	private void onReplyCommentRequestComplete(final Response response, final Comment comment)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onReplyCommentRequestSuccess(comment);
					}
					else
					{
						onReplyCommentRequestError(response.getError());
					}
				}
			});
		}
	}

	private void onReplyCommentRequestSuccess(Comment comment)
	{
		int time = Integer.parseInt(comment.getCreated_time()) - 1;

		setOffset(String.valueOf(time));

		int pos = getAdapter().getItemPosition(comment);
		for (int i = pos; i < getAdapter().getCount(); i++)
		{
			getAdapter().removeAt(i);
			i--;
		}

		photo.getComment_info().setComment_count(photo.getComment_info().getComment_count() + 1);
		getAdapter().notifyDataSetChanged();

		Toast.makeText(getActivity(), R.string.publish_comment_ok, Toast.LENGTH_SHORT);

		setNoMoreData(false);
		refresh();
	}

	private void onReplyCommentRequestError(RequestError error)
	{
		Log.i("StreamFragment", "Post Reply Comment error " + error.getMessage());
		new AlertDialog.Builder(getActivity()).setTitle(R.string.error).setMessage(R.string.publish_comment_error)
				.setPositiveButton(R.string.ok, null).create().show();
	}

	private void handleCopyTextAction(Comment comment)
	{
		ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("Comment", comment.getMessage());
		clipboard.setPrimaryClip(clip);
	}

	private void handleSeeWhoLikesThisAction(Comment comment)
	{
		UserLikeDialog uld = new UserLikeDialog();
		uld.setElementId(comment.getId());
		uld.show(getActivity().getFragmentManager(), "userlike");
	}

	private void handleDownloadAction(Comment comment)
	{
		com.abewy.android.apps.klyph.core.graph.Comment.Attachment.Media.Image image = comment.getAttachment().getMedia().getImage();
		KlyphDownloadManager.downloadPhoto(getActivity(), image.getSrc(), image.getSrc().substring(image.getSrc().lastIndexOf("/") + 1), comment
				.getAttachment().getTitle(), true, true);

	}

	private void handleDeleteCommentAction(Comment comment)
	{
		pendingDeleteComment = false;
		pendingComment = comment;
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingDeleteComment = true;
			requestPublishPermissions(session);
			return;
		}

		askDeleteComment(comment);
		pendingComment = null;
	}

	private void askDeleteComment(final Comment comment)
	{
		AlertUtil.showAlert(getActivity(), R.string.delete_post_confirmation_title, R.string.delete_post_confirmation_message, R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						deleteComment(comment);
					}

				}, R.string.no, null);
	}

	private void deleteComment(final Comment comment)
	{
		final AlertDialog dialog = AlertUtil.showAlert(getActivity(), R.string.delete, R.string.deleting);

		new AsyncRequest(Query.DELETE_POST, comment.getId(), "", new AsyncRequest.Callback() {

			@Override
			public void onComplete(Response response)
			{
				dialog.dismiss();

				onDeleteCommentRequestComplete(response, comment);
			}
		}).execute();
	}

	private void onDeleteCommentRequestComplete(final Response response, final Comment comment)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onDeleteCommentRequestSuccess(response.getGraphObjectList(), comment);
					}
					else
					{
						onDeleteCommentRequestError(response.getError());
					}
				}
			});
		}
	}

	private void onDeleteCommentRequestSuccess(List<GraphObject> results, Comment comment)
	{
		Toast.makeText(getActivity().getApplication(), R.string.post_deleted, Toast.LENGTH_SHORT).show();
		photo.getComment_info().setComment_count(photo.getComment_info().getComment_count() - 1);

		getAdapter().remove(comment);
		getAdapter().notifyDataSetChanged();
	}

	private void onDeleteCommentRequestError(RequestError error)
	{
		AlertUtil.showAlert(getActivity(), R.string.error, R.string.delete_post_error, R.string.ok);
	}

	private void handleShareAction(Comment comment)
	{

	}

	private void handleViewProfileAction(Comment comment)
	{
		startActivity(Klyph.getIntentForParams(getActivity(), comment.getFrom().getId(), comment.getFrom().getName(), "user"));
	}

	@Override
	protected int getCustomLayout()
	{
		return R.layout.fragment_image;
	}

	@Override
	public void onStop()
	{
		super.onStop();
		// imageView.setImageDrawable(null);
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (activity instanceof IToggleBarVisibility)
		{
			toggleActivity = (IToggleBarVisibility) activity;
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		toggleActivity = null;
	}

	@Override
	public void onDestroy()
	{
		if (adapter != null && observer != null)
			adapter.unregisterDataSetObserver(observer);
		
		super.onDestroy();
		imageView.setImageDrawable(null);
		removeClickListener();
		imageView = null;
		progress = null;
		emptyView = null;
		likeTitle = null;
		toggleActivity = null;
		profileImage = null;
		photo = null;
		slidingDrawer = null;
		authorName = null;
		postTime = null;
		sendButton = null;
		sendTextEdit = null;
		captionInfo = null;
		adapter = null;
		observer = null;
	}

	@Override
	public void onPause()
	{
		// Log.d("ImageFragment", "onPause");
		super.onPause();
		imageView.setImageDrawable(null);
	}

	@Override
	public void onResume()
	{
		// Log.d("ImageFragment", "onResume");
		super.onResume();

		if (photo != null && isLoading() == false)
		{
			setPhoto(photo);
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
	}

	/*
	 * @Override
	 * public void onTouchImageScale()
	 * {
	 * if (toggleActivity.isBarVisible())
	 * {
	 * toggleBarVisibility();
	 * }
	 * }
	 */

	@Override
	public void onPermissionsChange()
	{
		if (pendingAnnounce)
		{
			handlePostComment();
		}
		else if (pendingDeleteComment)
		{
			handleDeleteCommentAction(pendingComment);
		}
		else if (pendingLikeComment)
		{
			handleLikeCommentAction(pendingCommentLike);
		}
		else if (pendingReplyComment)
		{
			handleReplyAction(pendingCommentReply);
		}
	}

	@Override
	public void onCancelPermissions()
	{
		pendingAnnounce = false;
		pendingDeleteComment = false;
		pendingLikeComment = false;
		pendingReplyComment = false;
	}
}
