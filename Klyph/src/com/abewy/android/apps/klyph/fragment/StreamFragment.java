package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.abewy.android.apps.klyph.Klyph;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphDownloadManager;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.AdapterSelector;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.adapter.SpecialLayout;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.Link;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Status;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.core.graph.Comment;
import com.abewy.android.apps.klyph.core.graph.Comment.Attachment.Media.Image;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.facebook.IFbPermissionCallback;
import com.abewy.android.apps.klyph.facebook.IFbPermissionWorker;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.widget.KlyphGridView;
import com.abewy.klyph.items.TextItem;
import com.facebook.Session;

public class StreamFragment extends KlyphFragment2 implements IFbPermissionCallback
{
	private static final List<String>	PERMISSIONS					= Arrays.asList("publish_actions", "publish_stream");
	private boolean						pendingAnnounce				= false;
	private boolean						pendingLikeComment			= false;
	private boolean						pendingDelete				= false;
	private boolean						pendingDeleteComment		= false;
	private boolean						pendingReplyComment			= false;
	private Comment						pendingComment;
	private Comment						pendingCommentLike;
	private Comment						pendingCommentReply;
	private boolean						isStreamAvailableOnLaunch	= false;
	private EditText					sendEditText;
	private ImageButton					sendButton;
	private Stream						stream;
	private StreamLikeCount				likeTitle;
	private MultiObjectAdapter			adapter;
	private boolean						useObjectIdAsCursor			= false;
	private int							numTry						= 0;

	private DataSetObserver				observer					= new DataSetObserver() {

																		@Override
																		public void onChanged()
																		{
																			super.onChanged();

																			if (stream != null)
																			{
																				refreshLikeCount();
																			}
																		}
																	};

	public StreamFragment()
	{
		setRequestType(Query.STREAM);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		defineEmptyText(R.string.cannot_find_post);

		adapter = new MultiObjectAdapter(getListView(), SpecialLayout.STREAM_DETAIL);
		setListAdapter(adapter);

		getListView().getAdapter().registerDataSetObserver(observer);

		sendButton = (ImageButton) view.findViewById(R.id.send_button);
		sendEditText = (EditText) view.findViewById(R.id.send_text_edit);

		sendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				handlePostComment();
			}
		});

		setListVisible(false);

		super.onViewCreated(view, savedInstanceState);

		getGridView().setVerticalSpacing(1);
	}

	public void setIsStreamGroup()
	{
		setRequestType(Query.STREAM_GROUP_REQUEST);
	}

	public void setStreamAndLoad(Stream stream)
	{
		this.stream = stream;
		isStreamAvailableOnLaunch = true;

		getAdapter().add(stream);
		getAdapter().add(getLoadingObject());
		getAdapter().notifyDataSetChanged();
		setListVisible(true);

		Log.d("StreamFragment", "setStreamAndLoad");

		if (stream.isStatus())
		{
			Log.d("StreamFragment", "stream is status");
			setRequestType(Query.STATUS);
			setElementId(stream.getStatus().getStatus_id());
			initVariables(stream.getStatus());
			useObjectIdAsCursor = true;

		}
		else if (stream.isLink())
		{
			setRequestType(Query.LINK);
			setElementId(stream.getLink().getLink_id());
			initVariables(stream.getLink());
			useObjectIdAsCursor = true;
		}
		else if (stream.isPhoto()/* || stream.isAttachedPhoto() */)
		{
			setRequestType(Query.PHOTO);
			setElementId(stream.getPhotoId());
			initVariables(stream.getPhoto());
			useObjectIdAsCursor = true;
		}
		else if (stream.isVideo())
		{
			setRequestType(Query.VIDEO);
			setElementId(stream.getVideo().getVid());
			initVariables(stream.getVideo());
			useObjectIdAsCursor = true;
		}
		else
		{
			Log.d("StreamFragment", "stream is stream");
			setElementId(stream.getPost_id());
			setRequestType(Query.STREAM);
		}

		load();
	}

	@Override
	public void onPermissionsChange()
	{
		if (pendingAnnounce)
		{
			handlePostComment();
		}
		else if (pendingDelete)
		{
			handleDeleteAction();
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
		pendingDelete = false;
		pendingDeleteComment = false;
		pendingLikeComment = false;
		pendingReplyComment = false;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu)
	{
		if (stream != null && menu.findItem(R.id.menu_delete) == null)
		{
			if (Klyph.FACEBOOK_APP_ID.equals(stream.getApp_id()) && stream.getSource_id().equals(KlyphSession.getSessionUserId()))
			{
				menu.add(Menu.NONE, R.id.menu_delete, 2, getString(R.string.delete))
						.setIcon(AttrUtil.getResourceId(getActivity(), R.attr.discardIcon)).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_delete)
		{
			handleDeleteAction();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void askDelete()
	{
		AlertUtil.showAlert(getActivity(), R.string.delete_post_confirmation_title, R.string.delete_post_confirmation_message, R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						delete();
					}

				}, R.string.no, null);
	}

	private void delete()
	{
		final AlertDialog dialog = AlertUtil.showAlert(getActivity(), R.string.delete, R.string.deleting);

		new AsyncRequest(Query.DELETE_POST, getElementId(), "", new AsyncRequest.Callback() {

			@Override
			public void onComplete(Response response)
			{
				dialog.dismiss();

				onDeleteRequestComplete(response);
			}
		}).execute();
	}

	private void onDeleteRequestComplete(final Response response)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onDeleteRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						onDeleteRequestError(response.getError());
					}
				}
			});
		}
	}

	private void onDeleteRequestSuccess(List<GraphObject> results)
	{
		Toast.makeText(getActivity().getApplication(), R.string.post_deleted, Toast.LENGTH_SHORT).show();
		Intent data = new Intent();
		data.putExtra(KlyphBundleExtras.DELETED, true);
		data.putExtra(KlyphBundleExtras.STREAM_ID, stream.getPost_id());
		getActivity().setResult(Activity.RESULT_OK, data);
		getActivity().finish();
	}

	private void onDeleteRequestError(RequestError error)
	{
		AlertUtil.showAlert(getActivity(), R.string.error, R.string.delete_post_error, R.string.ok);
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

	private void handleDeleteAction()
	{
		pendingDelete = false;
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingDelete = true;
			requestPublishPermissions(session);
			return;
		}

		askDelete();
	}

	private void requestPublishPermissions(Session session)
	{
		((IFbPermissionWorker) getActivity()).requestPublishPermissions(this, PERMISSIONS);
	}

	private void postComment()
	{
		if (sendEditText.getText().toString().length() > 0)
		{
			Bundle params = new Bundle();
			params.putString("message", sendEditText.getText().toString());

			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(sendEditText.getWindowToken(), 0);

			sendRequest(params);
		}
		else
		{
			AlertUtil.showAlert(getActivity(), R.string.error, R.string.define_comment_before_publish, R.string.ok);
		}
	}
	
	private AlertDialog	dialog;

	private void sendRequest(Bundle params)
	{
		if (dialog == null)
		{
			dialog = AlertUtil.showAlert(getActivity(), R.string.publish_comment_dialog_title, R.string.publish_comment_dialog_message);
			dialog.setCancelable(false);
		}

		new AsyncRequest(Query.POST_COMMENT, getElementId(), params, new AsyncRequest.Callback() {

			@Override
			public void onComplete(Response response)
			{
				onCommentRequestComplete(response);
			}
		}).execute();
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
		if (dialog != null)
		{
			dialog.dismiss();
			dialog = null;
		}
		
		sendEditText.setText("");

		Toast.makeText(getActivity(), R.string.publish_comment_ok, Toast.LENGTH_SHORT).show();

		setNoMoreData(false);
		refresh();
	}

	private void onCommentRequestError(RequestError error)
	{
		Log.i("postComment", "error " + error.getMessage());
		numTry++;

		if (numTry < 3)
		{
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run()
				{
					postComment();
				}
			}, 1500);
		}
		else
		{
			if (dialog != null)
			{
				dialog.dismiss();
				dialog = null;
			}
			
			AlertUtil.showAlert(getActivity(), R.string.error, R.string.publish_comment_error, R.string.ok);
		}
	}

	@Override
	protected void populate(List<GraphObject> data)
	{
		if (data.size() > 0)
		{
			if (isFirstLoad())
			{
				if (isStreamAvailableOnLaunch)
				{
					GraphObject object = data.get(0);

					if (object instanceof Status)
					{
						initVariables((Status) object);
					}
					else if (object instanceof Link)
					{
						initVariables((Link) object);
					}
					else if (object instanceof Photo)
					{
						initVariables((Photo) object);
					}

					// Remove loading
					getAdapter().removeAt(1);

					likeTitle = new StreamLikeCount();
					getAdapter().add(likeTitle);
					refreshLikeCount();

					data.remove(0);

					/*
					 * for (GraphObject graphObject : data)
					 * {
					 * getAdapter().add(graphObject);
					 * }
					 */

					// setOffset(String.valueOf(data.size()));

					if (stream.getComment_info().getCan_comment())
					{
						sendButton.setVisibility(View.VISIBLE);
						sendEditText.setVisibility(View.VISIBLE);
					}

					if (getActivity() != null)
						((FragmentActivity) getActivity()).invalidateOptionsMenu();

					setRequestType(Query.COMMENTS);
					setNoMoreData(false);
					endLoading();
					refresh();
				}
				else
				{
					if (data.get(0) instanceof Stream)
					{
						Stream stream = (Stream) data.get(0);
						// setIsFirstLoad(true);
						setStreamAndLoad(stream);
						refresh();
					}
					else
					{
						super.populate(new ArrayList<GraphObject>());
					}
				}
			}
			else
			{
				super.populate(data);
			}
		}
		else
		{
			if (isFirstLoad() && isStreamAvailableOnLaunch)
			{
				// Remove loading
				getAdapter().removeAt(1);
			}
			super.populate(data);
		}

		setNoMoreData(!isFirstLoad() && (data.size() == 0 || (getRequest().getPagingNext() == null || getRequest().getPagingNext().length() == 0)));

		if (getAdapter().getCount() > 2)
		{
			setOffset(getAfterCursor());
		}

	}

	@Override
	protected void endLoading()
	{
		super.endLoading();

		getAdapter().remove(getLoadingObject());
		getAdapter().remove(getLoadingObject());
		getAdapter().notifyDataSetChanged();
	}

	private void initVariables(Link link)
	{
		stream.setComment_info(link.getComment_info());
		stream.setLike_info(link.getLike_info());
	}

	private void initVariables(Photo photo)
	{
		stream.setComment_info(photo.getComment_info());
		stream.setLike_info(photo.getLike_info());
	}

	private void initVariables(Video video)
	{

	}

	private void initVariables(Status status)
	{
		// stream.setLike_info(status.getLike_info());
		// stream.setComment_info(status.getComment_info());
	}

	private void refreshLikeCount()
	{
		if (likeTitle != null)
		{
			int likeCount = stream.getLike_info().getLike_count();

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
	protected int getCustomLayout()
	{
		return R.layout.fragment_stream;
	}

	@Override
	protected boolean updateNumColumnOnOrientationChange()
	{
		return false;
	}

	@Override
	protected int getNumColumn()
	{
		return 1;
	}

	@Override
	public void onGridItemClick(KlyphGridView gridView, View view, int position, long id)
	{
		if (position == 1)
		{
			UserLikeDialog uld = new UserLikeDialog();
			uld.setElementId(getElementId());
			uld.show(getActivity().getFragmentManager(), "userlike");
		}
		else if (position > 1)
		{
			GraphObject object = (GraphObject) getAdapter().getItem(position);

			if (object instanceof Comment)
			{
				final Comment comment = (Comment) object;
				handleClickComment(view, comment);
			}
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

			String commentId = comment.getId();

			// if (!commentId.contains("_"))
			// commentId = comment.getObject_id() + "_" + commentId;

			new AsyncRequest(Query.POST_LIKE, commentId, "", new AsyncRequest.Callback() {

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

			String commentId = comment.getId();

			// if (!commentId.contains("_"))
			// commentId = comment.getObject_id() + "_" + commentId;

			new AsyncRequest(Query.POST_UNLIKE, commentId, "", new AsyncRequest.Callback() {

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

		Toast.makeText(getActivity(), R.string.publish_comment_ok, Toast.LENGTH_SHORT).show();

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
		Image image = comment.getAttachment().getMedia().getImage();
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

		stream.getComment_info().setComment_count(stream.getComment_info().getComment_count() - 1);

		getAdapter().remove(comment);
		getAdapter().notifyDataSetChanged();
	}

	private void onDeleteCommentRequestError(RequestError error)
	{
		AlertUtil.showAlert(getActivity(), R.string.error, R.string.delete_post_error, R.string.ok);
	}

	private void handleShareAction(Comment comment)
	{
		// TODO Auto-generated method stub

	}

	private void handleViewProfileAction(Comment comment)
	{
		startActivity(Klyph.getIntentForParams(getActivity(), comment.getFrom().getId(), comment.getFrom().getName(), "user"));
	}

	@Override
	public void onDestroy()
	{
		if (adapter != null && observer != null)
			adapter.unregisterDataSetObserver(observer);

		super.onDestroy();
		sendEditText = null;
		sendButton = null;
		stream = null;
		likeTitle = null;
		adapter = null;
		observer = null;
	}

	public static class StreamLikeCount extends TextItem
	{
		@Override
		public int getItemViewType()
		{
			return AdapterSelector.STREAM_LIKE_COUNT;
		}

		@Override
		public boolean isSelectable(int layout)
		{
			return true;
		}
	}
}
