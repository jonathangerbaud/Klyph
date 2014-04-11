package com.abewy.android.apps.klyph.app;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphData;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphSession;
import com.abewy.android.apps.klyph.core.fql.FriendList;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.GraphType;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.request.BaseAsyncRequest.Callback;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.facebook.IFbPermissionCallback;
import com.abewy.android.apps.klyph.fragment.PostAlbums;
import com.abewy.android.apps.klyph.fragment.PostLink;
import com.abewy.android.apps.klyph.fragment.PostLink.PostLinkListener;
import com.abewy.android.apps.klyph.fragment.PostPhotos;
import com.abewy.android.apps.klyph.fragment.PostPhotos.PostPhotoListener;
import com.abewy.android.apps.klyph.fragment.PostShare;
import com.abewy.android.apps.klyph.fragment.UserListDialog;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.service.UploadPhotoService;
import com.abewy.android.apps.klyph.util.TextViewUtil;
import com.abewy.android.apps.klyph.util.TextViewUtil.TagCallback;
import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;

public class PostActivity extends TitledFragmentActivity implements PostPhotoListener, PostLinkListener, IFbPermissionCallback
{
	private enum Privacy
	{
		EVERYONE,
		ALL_FRIENDS,
		SELF,
		CUSTOM
	}

	private final int					GALLERY					= 0;
	private final int					FRIEND_PICKER			= 1;
	private final int					PLACE_PICKER			= 2;

	private static final List<String>	PERMISSIONS				= Arrays.asList("read_mailbox", "publish_stream", "publish_actions"/*
																																	 * ,
																																	 * "photo_upload",
																																	 * "status_update",
																																	 * "share_item"
																																	 */);

	private final int					DUPLICATE_MESSAGE_CODE	= 506;

	private TextView					messageTextView;
	private TextView					friendsTextView;
	private TextView					placeTextView;
	private ImageButton					friendsButton;
	private ImageButton					privacyButton;
	private ImageButton					photosButton;
	private ImageButton					placeButton;
	private ImageButton					linkButton;
	private PostPhotos					photosFragment;
	private PostAlbums					albumsFragment;
	private PostLink					linkFragment;
	private PostShare					shareFragment;
	private Privacy						privacy					= Privacy.SELF;
	private Map<String, String>			friends;
	private String						placeId;
	private ArrayList<String>			photoUris;
	private boolean						isEventMessage			= false;
	private boolean						isPageMessage			= false;
	private boolean						isGroupMessage			= false;
	private boolean						pendingAnnounce			= false;
	private int							numTry					= 0;
	private boolean						linkFragmentVisible		= false;
	private boolean						shareFragmentVisible	= false;

	private String						elementId;
	private List<GraphObject>			friendLists;
	private String						privacyFriendListId;

	private TagCallback					friendCallback			= new TagCallback() {

																	@Override
																	public void onTagClick(List<Tag> tags)
																	{
																		Tag tag = tags.get(0);

																		if (tag.getId().length() == 0)
																		{
																			List<Tag> users = new ArrayList<Tag>();

																			for (Iterator<String> iterator = friends.keySet().iterator(); iterator
																					.hasNext();)
																			{
																				String key = (String) iterator.next();

																				Tag t = new Tag();
																				t.setId(key);
																				t.setName(friends.get(key));
																				t.setType(GraphType.FQL_USER.toString());
																				users.add(t);
																			}

																			UserListDialog dialog = new UserListDialog();
																			dialog.loadList(users);
																			dialog.setCustomTitle(R.string.friends);
																			dialog.show(getFragmentManager(), "poststatusDialog");
																		}
																		else
																		{
																			TextViewUtil.onTagClick(PostActivity.this, tags);
																		}
																	}
																};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();

		if (intent.getStringExtra(KlyphBundleExtras.EVENT_ID) != null)
		{
			elementId = intent.getStringExtra(KlyphBundleExtras.EVENT_ID);
			isEventMessage = true;
		}
		else if (intent.getStringExtra(KlyphBundleExtras.PAGE_ID) != null)
		{
			elementId = intent.getStringExtra(KlyphBundleExtras.PAGE_ID);
			isPageMessage = true;
		}
		else if (intent.getStringExtra(KlyphBundleExtras.GROUP_ID) != null)
		{
			elementId = intent.getStringExtra(KlyphBundleExtras.GROUP_ID);
			isGroupMessage = true;
		}
		else
		{
			elementId = KlyphSession.getSessionUserId();
		}

		Log.d("PostActivity", "event " + isEventMessage + " page " + isPageMessage + " group " + isGroupMessage);

		boolean shareLink = intent.getBooleanExtra(KlyphBundleExtras.SHARE, false);

		String action = intent.getAction();
		String type = intent.getType();

		setTitle(isEventMessage == false ? R.string.publish_new_status : R.string.publish_new_event_message);

		photoUris = new ArrayList<String>();

		messageTextView = (TextView) findViewById(R.id.message_textview);
		friendsTextView = (TextView) findViewById(R.id.friends_textview);
		placeTextView = (TextView) findViewById(R.id.place_textview);
		photosFragment = (PostPhotos) getFragmentManager().findFragmentById(R.id.photos_fragment);
		albumsFragment = (PostAlbums) getFragmentManager().findFragmentById(R.id.albums_fragment);
		linkFragment = (PostLink) getFragmentManager().findFragmentById(R.id.link_fragment);
		shareFragment = (PostShare) getFragmentManager().findFragmentById(R.id.share_fragment);

		getFragmentManager().beginTransaction().hide(photosFragment).commitAllowingStateLoss();
		getFragmentManager().beginTransaction().hide(albumsFragment).commitAllowingStateLoss();
		getFragmentManager().beginTransaction().hide(linkFragment).commitAllowingStateLoss();

		if (shareLink == true)
		{
			shareFragment.initWithIntent(getIntent());
			shareFragmentVisible = true;
		}
		else
		{
			getFragmentManager().beginTransaction().hide(shareFragment).commit();
		}

		if (isEventMessage == true)
		{
			LinearLayout buttonBar = (LinearLayout) findViewById(R.id.button_bar);
			buttonBar.setVisibility(View.GONE);
		}
		else
		{
			ImageButton placeDeleteButton = (ImageButton) findViewById(R.id.place_delete_button);
			ImageButton friendsDeleteButton = (ImageButton) findViewById(R.id.friends_delete_button);
			friendsButton = (ImageButton) findViewById(R.id.friends_button);
			photosButton = (ImageButton) findViewById(R.id.picture_button);
			placeButton = (ImageButton) findViewById(R.id.place_button);
			linkButton = (ImageButton) findViewById(R.id.link_button);
			privacyButton = (ImageButton) findViewById(R.id.privacy_button);

			placeDeleteButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					clearPlace();
					updateButtonStatus();
				}
			});

			friendsDeleteButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					clearFriends();
					updateButtonStatus();
				}
			});

			friendsButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(PostActivity.this, FriendPickerActivity.class);

					if (friends != null && friends.size() > 0)
					{
						ArrayList<String> ids = new ArrayList<String>();
						ids.addAll(friends.keySet());
						intent.putStringArrayListExtra(KlyphBundleExtras.FRIEND_PICKER_IDS, ids);
					}

					startActivityForResult(intent, FRIEND_PICKER);
				}
			});

			photosButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					startGallery();
				}
			});

			placeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					startActivityForResult(new Intent(PostActivity.this, PlacePickerActivity.class), PLACE_PICKER);
				}
			});

			linkButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					linkFragment.reset();

					getFragmentManager().beginTransaction().show(linkFragment).commitAllowingStateLoss();
					linkFragmentVisible = true;
					updateButtonStatus();
				}
			});

			setPrivacyListenerApi11();

			setPrivacy(KlyphPreferences.getPrivacy());

			if (Intent.ACTION_SEND.equals(action) && type != null)
			{
				displayBackArrow(false);
				setAppIconBackToHomeEnabled(false);

				if ("text/plain".equals(type))
				{
					String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
					String sharedSubject = intent.getStringExtra(Intent.EXTRA_SUBJECT);

					if (sharedText != null)
					{
						boolean isUrl = false;

						try
						{
							new URL(sharedText);
							isUrl = true;
						}
						catch (MalformedURLException e)
						{
							isUrl = false;
						}

						if (isUrl == true)
						{
							linkFragment.setUrl(sharedText);
							linkFragment.setTitle(sharedSubject);
							getFragmentManager().beginTransaction().show(linkFragment).commitAllowingStateLoss();
						}
						else
						{
							messageTextView.setText(sharedText);
						}
					}

				}
				else if (type.startsWith("image/"))
				{
					Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

					if (imageUri != null)
					{
						// Update UI to reflect image being shared
						photoUris = new ArrayList<String>();

						if (StringUtils.startsWith(imageUri.toString(), "content://"))
						{
							photoUris.add(getRealPathFromURI(imageUri));
						}
						else
						{
							photoUris.add(imageUri.getPath());
						}
						photosFragment.setImages(photoUris);
						getFragmentManager().beginTransaction().show(photosFragment).commitAllowingStateLoss();
						photosFragment.updateLayout();
					}

				}
			}
			else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null)
			{
				displayBackArrow(false);
				setAppIconBackToHomeEnabled(false);

				if (type.startsWith("image/"))
				{
					ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
					if (imageUris != null)
					{
						// Update UI to reflect multiple images being shared
						photoUris = new ArrayList<String>();

						for (Uri uri : imageUris)
						{
							if (StringUtils.startsWith(uri.toString(), "content://"))
							{
								photoUris.add(getRealPathFromURI(uri));
							}
							else
							{
								photoUris.add(uri.getPath());
							}
						}

						photosFragment.setImages(photoUris);
						getFragmentManager().beginTransaction().show(photosFragment).commitAllowingStateLoss();
						photosFragment.updateLayout();
					}

				}
			}
			else
			{
				Log.d("PostActivity", "Received other");
			}

			updateButtonStatus();
			privacyButton.setVisibility(isGroupMessage == true || isEventMessage == true ? View.GONE : View.VISIBLE);
		}

		if (KlyphData.getFriendLists() == null)
		{
			new AsyncRequest(Query.FRIEND_LISTS, "", "", new Callback() {

				@Override
				public void onComplete(Response response)
				{
					onRequestComplete(response);
				}
			}).execute();
		}
	}

	private boolean isSessionUser()
	{
		return elementId != null && elementId.equals(KlyphSession.getSessionUserId());
	}

	private String getRealPathFromURI(Uri contentUri)
	{

		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, // Which columns to return
				null,       // WHERE clause; which rows to return (all rows)
				null,       // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	@TargetApi(11)
	private void setPrivacyListenerApi11()
	{
		privacyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				PopupMenu popup = new PopupMenu(PostActivity.this, v);
				android.view.MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.privacy, popup.getMenu());

				if (friendLists == null)
					friendLists = KlyphData.getFriendLists();

				if (friendLists != null)
				{
					for (GraphObject friendList : friendLists)
					{
						popup.getMenu().add(((FriendList) friendList).getName());
					}
				}

				popup.setOnMenuItemClickListener(new PopupMenuListener());
				popup.show();
			}
		});
	}

	private void setPrivacy(int privacy)
	{
		switch (privacy)
		{
			case 0:
				setPrivacy(Privacy.EVERYONE);
				break;
			case 1:
				setPrivacy(Privacy.ALL_FRIENDS);
				break;
			case 2:
				setPrivacy(Privacy.SELF);
				break;
		}
	}

	private void setPrivacy(Privacy privacy)
	{
		this.privacy = privacy;

		// set changes
		switch (privacy)
		{
			case EVERYONE:
			{
				privacyButton.setImageDrawable(AttrUtil.getDrawable(this, R.attr.privacyPublicIcon));
				KlyphPreferences.setPrivacy(0);
				break;
			}
			case ALL_FRIENDS:
			{
				privacyButton.setImageDrawable(AttrUtil.getDrawable(this, R.attr.privacyFriendsIcon));
				KlyphPreferences.setPrivacy(1);
				break;
			}
			case SELF:
			{
				privacyButton.setImageDrawable(AttrUtil.getDrawable(this, R.attr.privacySelfIcon));
				KlyphPreferences.setPrivacy(2);
				break;
			}
			case CUSTOM:
			{
				privacyButton.setImageDrawable(AttrUtil.getDrawable(this, R.attr.privacyCustomIcon));
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.send, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_send)
		{
			if (photoUris.size() > 0 || shareFragment.isVisible())
			{
				handlePublishPost();
			}
			else if (linkFragment.isVisible())
			{
				if (linkFragment.checkEntries())
				{
					handlePublishPost();
				}
			}
			else if (messageTextView.getText() == null || messageTextView.getText().length() == 0)
			{
				AlertUtil.showAlert(this, R.string.error, R.string.please_enter_message, R.string.ok);
			}
			else if (friends != null && friends.size() > 0 && (placeId == null || placeId.length() == 0))
			{
				AlertUtil.showAlert(this, R.string.error, R.string.must_select_place_to_tag_friends, R.string.ok);
			}
			else
			{
				handlePublishPost();
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void publishPost()
	{
		Log.d("PostActivity", "Publish post try " + numTry);
		Log.d("PostActivity", "Publish post privacy " + privacy.toString());
		if (photoUris.size() > 0)
		{
			publishPhotos();
		}
		else if (shareFragment.isVisible())
		{
			Bundle params = shareFragment.getParams();

			setPrivacyParams(params);
			// params.putString("privacy", "{'value':'" + privacy.toString() + "'}");

			if (shareFragment.isFriendShare())
			{
				WebDialog dialog = new WebDialog.FeedDialogBuilder(this, Session.getActiveSession(), params).build();
				dialog.setOnCompleteListener(new WebDialog.OnCompleteListener() {

					@Override
					public void onComplete(Bundle values, FacebookException error)
					{
						if (error == null)
						{
							// When the story is posted, close activity
							final String postId = values.getString("post_id");
							if (postId != null)
							{
								finish();
							}
						}
					}
				});
				dialog.show();
			}
			else
			{
				params.putString("message", messageTextView.getText().toString());
				sendRequest(Query.POST_STATUS, elementId, params);
			}
		}
		else if (linkFragment.isVisible())
		{
			Bundle params = linkFragment.getParams();
			params.putString("message", messageTextView.getText().toString());

			if (isGroupMessage == false)
				setPrivacyParams(params);
			// params.putString("privacy", "{'value':'" + privacy.toString() + "'}");

			sendRequest(Query.POST_STATUS, elementId, params);
		}
		else
		{
			Bundle params = new Bundle();
			params.putString("message", messageTextView.getText().toString());
			// params.putString("message",
			// "Test with Happy @[1320153319:Mickael Gbd]!");
			// params.putString("message", "Test with Happy Mickael Gbd !");
			// params.putString("message_tags",
			// "[{\"id\":\"1320153319\", \"name\":\"Mickael Gbd\", \"length\":11, \"offset\":16}]");
			// params.putString("tags",
			// "[{\"tag_uid\":\"1320153319\", \"length\":10, \"offset\":2}]");

			if (placeId != null && placeId != "")
			{
				params.putString("place", placeId);

				if (friends != null && friends.size() > 0)
				{
					StringBuilder ids = new StringBuilder();
					for (String key : friends.keySet())
					{
						ids.append(key).append(",");
					}

					params.putString("tags", ids.substring(0, ids.length() - 1));
				}
			}

			if (isEventMessage == false && isGroupMessage == false && isPageMessage == false)
			{
				setPrivacyParams(params);
				// params.putString("privacy", "{'value':'" + privacy.toString() + "'}");
			}

			sendRequest(Query.POST_STATUS, elementId, params);
		}
	}

	private void setPrivacyParams(Bundle params)
	{
		params.putString("privacy", getPrivacyParam());
	}

	private String getPrivacyParam()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.putOpt("value", privacy.toString());
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		if (privacyFriendListId != null)
		{
			try
			{
				json.put("allow", privacyFriendListId);
				json.put("deny", "");
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}

		return json.toString();
	}

	private AlertDialog	dialog;

	private void sendRequest(int query, String id, Bundle params)
	{
		if (dialog == null)
		{
			dialog = AlertUtil.showAlert(this, R.string.status, R.string.publishing);
			dialog.setCancelable(false);
		}

		new AsyncRequest(query, id, params, new AsyncRequest.Callback() {

			@Override
			public void onComplete(Response response)
			{
				onRequestResponse(response);
			}
		}).execute();
	}

	private void publishPhotos()
	{
		String album = elementId;

		if (!(isPageMessage || isEventMessage || isGroupMessage))
		{
			String albumId = albumsFragment.getSelectedAlbum();
			if (albumId == null)
				album = elementId;
			else
				album = albumId;
		}

		Intent intent = new Intent(this, UploadPhotoService.class);
		intent.putStringArrayListExtra(KlyphBundleExtras.UPLOAD_PHOTO_URIS, photoUris);
		intent.putExtra(KlyphBundleExtras.UPLOAD_PHOTO_CAPTION, messageTextView.getText().toString());
		intent.putExtra(KlyphBundleExtras.UPLOAD_PHOTO_PLACE, placeId);
		intent.putExtra(KlyphBundleExtras.UPLOAD_PHOTO_PRIVACY, getPrivacyParam());
		intent.putExtra(KlyphBundleExtras.UPLOAD_PHOTO_ALBUM, album);

		if (friends != null && friends.size() > 0)
		{
			ArrayList<String> friendIds = new ArrayList<String>();
			for (String id : friends.keySet())
			{
				friendIds.add(id);
			}

			intent.putStringArrayListExtra(KlyphBundleExtras.UPLOAD_PHOTO_TAGS, friendIds);
		}

		startService(intent);
		setResult(RESULT_OK);
		finish();
	}

	private void onRequestResponse(Response response)
	{
		if (response.getError() == null)
		{
			if (dialog != null)
			{
				dialog.dismiss();
				dialog = null;
			}

			Toast.makeText(getApplication(),
					isEventMessage == false ? R.string.status_successfully_published : R.string.message_successfully_published, Toast.LENGTH_LONG)
					.show();
			setResult(RESULT_OK);
			finish();
		}
		else
		{
			numTry++;

			if (numTry < 3)
			{
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run()
					{
						publishPost();
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

				int errorRes = isEventMessage == false ? R.string.publish_unknown_error : R.string.publish_message_unknown_error;

				if (isEventMessage == false && response.getError().getErrorCode() == DUPLICATE_MESSAGE_CODE)
				{
					errorRes = R.string.publish_error_duplicate_message;
				}

				AlertUtil.showAlert(PostActivity.this, R.string.error, errorRes, R.string.ok);
			}

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == GALLERY)
		{
			if (resultCode == RESULT_OK)
			{
				photoUris = data.getStringArrayListExtra(KlyphBundleExtras.PHOTO_LIST_URI);

				if (photoUris == null)
				{
					photoUris = new ArrayList<String>();
				}
			}
		}
		else if (requestCode == FRIEND_PICKER)
		{
			if (resultCode == RESULT_OK)
			{
				friends = new HashMap<String, String>();

				String[] names = data.getStringArrayExtra(KlyphBundleExtras.FRIEND_PICKER_NAMES);
				String[] ids = data.getStringArrayExtra(KlyphBundleExtras.FRIEND_PICKER_IDS);

				for (int i = 0; i < ids.length; i++)
				{
					friends.put(ids[i], names[i]);
				}

				updateSelectedFriends();
			}
		}
		else if (requestCode == PLACE_PICKER)
		{
			if (resultCode == RESULT_OK)
			{
				placeId = data.getStringExtra(KlyphBundleExtras.PLACE_ID);
				String placeName = data.getStringExtra(KlyphBundleExtras.PLACE_NAME);

				placeTextView.setText(getString(R.string.at_place, placeName));
				((View) placeTextView.getParent()).setVisibility(View.VISIBLE);

				TextViewUtil.setElementClickable(this, placeTextView, placeName, placeId, GraphType.FQL_PAGE.toString(), true);
			}
		}

		updateButtonStatus();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		if (photosFragment != null)
		{
			photosFragment.updateLayout();
		}
	}

	private void clearPlace()
	{
		placeId = null;
		placeTextView.setText("");
		((View) placeTextView.getParent()).setVisibility(View.GONE);
	}

	private void clearFriends()
	{
		friends = null;
		friendsTextView.setText("");
		((View) friendsTextView.getParent()).setVisibility(View.GONE);
	}

	private void updateSelectedFriends()
	{
		if (friends.size() > 0)
		{
			String firstName = "";
			String firstId = "";

			Iterator<String> it = friends.values().iterator();
			Iterator<String> it2 = friends.keySet().iterator();

			firstName = it.next();
			firstId = it2.next();

			for (String name : friends.values())
			{
				firstName = name;
				break;
			}

			if (friends.size() > 2)
			{
				int size = friends.size() - 1;
				friendsTextView.setText(getString(R.string.post_with_several_friends, firstName, size));
				TextViewUtil.setElementClickable(this, friendsTextView, firstName, firstId, GraphType.FQL_USER.name(), friendCallback, true);
				TextViewUtil.setElementClickable(this, friendsTextView, getString(R.string.with_several_friends, size), "", "", friendCallback, true);
			}
			else if (friends.size() > 1)
			{
				String secondName = it.next();
				String secondId = it2.next();

				friendsTextView.setText(getString(R.string.post_with_two_friends, firstName, secondName));
				TextViewUtil.setElementClickable(this, friendsTextView, firstName, firstId, GraphType.FQL_USER.name(), friendCallback, true);
				TextViewUtil.setElementClickable(this, friendsTextView, secondName, secondId, GraphType.FQL_USER.name(), friendCallback, true);
			}
			else
			{
				friendsTextView.setText(getString(R.string.post_with_one_friend, firstName));
				TextViewUtil.setElementClickable(this, friendsTextView, firstName, firstId, GraphType.FQL_USER.name(), friendCallback, true);
			}

			((View) friendsTextView.getParent()).setVisibility(View.VISIBLE);
		}
		else
		{
			friendsTextView.setText("");
			((View) friendsTextView.getParent()).setVisibility(View.GONE);
		}
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_post;
	}

	@TargetApi(11)
	private class PopupMenuListener implements OnMenuItemClickListener
	{
		@Override
		public boolean onMenuItemClick(android.view.MenuItem item)
		{
			if (item.getItemId() == R.id.privacy_public)
			{
				setPrivacy(Privacy.EVERYONE);
				privacyFriendListId = null;
				return true;
			}
			else if (item.getItemId() == R.id.privacy_friends)
			{
				setPrivacy(Privacy.ALL_FRIENDS);
				privacyFriendListId = null;
				return true;
			}
			else if (item.getItemId() == R.id.privacy_self)
			{
				setPrivacy(Privacy.SELF);
				privacyFriendListId = null;
				return true;
			}
			else
			{
				setPrivacy(Privacy.CUSTOM);
				String flName = (String) item.getTitle();

				for (GraphObject friendList : friendLists)
				{
					FriendList fl = (FriendList) friendList;
					if (flName.equals(fl.getName()))
					{
						privacyFriendListId = fl.getFlid();
						break;
					}
				}
			}

			return false;
		}

	}

	@Override
	protected void onResumeFragments()
	{
		if (photoUris.size() > 0)
		{
			photosFragment.setImages(photoUris);
			photosFragment.updateLayout();

			getFragmentManager().beginTransaction().show(photosFragment).commitAllowingStateLoss();

			if (isSessionUser())
				getFragmentManager().beginTransaction().show(albumsFragment).commitAllowingStateLoss();
		}
		else
		{
			getFragmentManager().beginTransaction().hide(photosFragment).commitAllowingStateLoss();
			getFragmentManager().beginTransaction().hide(albumsFragment).commitAllowingStateLoss();
		}

		super.onResumeFragments();
	}

	@Override
	public void onPostPhotosItemClick()
	{
		startGallery();
	}

	private void startGallery()
	{
		Intent intent = new Intent(PostActivity.this, GalleryActivity.class);
		intent.putStringArrayListExtra(KlyphBundleExtras.PHOTO_LIST_URI, photoUris);
		startActivityForResult(intent, GALLERY);
	}

	@Override
	public void onPostPhotosDeleteClick()
	{
		photoUris = new ArrayList<String>();

		getFragmentManager().beginTransaction().hide(photosFragment).commitAllowingStateLoss();
		getFragmentManager().beginTransaction().hide(albumsFragment).commitAllowingStateLoss();

		updateButtonStatus();
	}

	@Override
	public void onPostLinkDeleteClick()
	{
		getFragmentManager().beginTransaction().hide(linkFragment).commitAllowingStateLoss();
		linkFragmentVisible = false;
		updateButtonStatus();
	}

	private void updateButtonStatus()
	{
		boolean linkEnabled = !shareFragmentVisible && !linkFragmentVisible && placeId == null && friends == null && photoUris.size() == 0;
		boolean enabled = !shareFragmentVisible && !linkFragmentVisible;

		friendsButton.setEnabled(enabled);
		photosButton.setEnabled(enabled);
		placeButton.setEnabled(enabled);
		linkButton.setEnabled(linkEnabled);

		friendsButton.setImageResource(AttrUtil.getResourceId(this, !enabled ? R.attr.addFriendDisabledIcon : R.attr.addFriendIcon));
		photosButton.setImageResource(AttrUtil.getResourceId(this, !enabled ? R.attr.addPictureDisabledIcon : R.attr.addPictureIcon));
		placeButton.setImageResource(AttrUtil.getResourceId(this, !enabled ? R.attr.locationDisabledIcon : R.attr.locationIcon));
		linkButton.setImageResource(AttrUtil.getResourceId(this, !linkEnabled ? R.attr.linkDisabledIcon : R.attr.linkIcon));
	}

	@Override
	public void onPermissionsChange()
	{
		if (pendingAnnounce)
		{
			handlePublishPost();
		}
	}

	@Override
	public void onCancelPermissions()
	{
		pendingAnnounce = false;
	}

	private void handlePublishPost()
	{
		pendingAnnounce = false;
		Session session = Session.getActiveSession();

		List<String> permissions = session.getPermissions();
		if (!permissions.containsAll(PERMISSIONS))
		{
			pendingAnnounce = true;
			requestPublishPermissions(this, PERMISSIONS);
			return;
		}

		numTry = 0;
		publishPost();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		messageTextView = null;
		friendsTextView = null;
		placeTextView = null;
		friendsButton = null;
		privacyButton = null;
		photosButton = null;
		placeButton = null;
		linkButton = null;
		photosFragment = null;
		albumsFragment = null;
		linkFragment = null;
		shareFragment = null;
		privacy = null;
		friends = null;
		placeId = null;
		photoUris = null;
		friendCallback = null;
	}

	// ___ Loading friend lists

	private void onRequestComplete(final Response response)
	{

		runOnUiThread(new Runnable() {

			@Override
			public void run()
			{
				if (response.getError() == null)
				{
					onRequestSuccess(response.getGraphObjectList());
				}
				else
				{
					onRequestError(response.getError());
				}
			}
		});

	}

	private void onRequestSuccess(List<GraphObject> result)
	{
		KlyphData.setFriendLists(result);
	}

	private void onRequestError(RequestError error)
	{

	}
}
