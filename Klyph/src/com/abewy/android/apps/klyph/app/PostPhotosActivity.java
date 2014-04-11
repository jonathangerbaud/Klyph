package com.abewy.android.apps.klyph.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphData;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.fql.FriendList;
import com.abewy.android.apps.klyph.core.fql.Tag;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.graph.GraphType;
import com.abewy.android.apps.klyph.core.request.BaseAsyncRequest.Callback;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.facebook.IFbPermissionCallback;
import com.abewy.android.apps.klyph.fragment.AlbumSpinner;
import com.abewy.android.apps.klyph.fragment.AlbumSpinner.AlbumSpinnerListener;
import com.abewy.android.apps.klyph.fragment.PostPhotos;
import com.abewy.android.apps.klyph.fragment.PostPhotos.PostPhotoListener;
import com.abewy.android.apps.klyph.fragment.UserListDialog;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.service.UploadPhotoService;
import com.abewy.android.apps.klyph.util.TextViewUtil;
import com.abewy.android.apps.klyph.util.TextViewUtil.TagCallback;
import com.facebook.Session;

public class PostPhotosActivity extends TitledFragmentActivity implements PostPhotoListener, AlbumSpinnerListener, IFbPermissionCallback
{
	private enum Privacy
	{
		EVERYONE,
		ALL_FRIENDS,
		SELF,
		CUSTOM
	}

	private final int					GALLERY			= 0;
	private final int					FRIEND_PICKER	= 1;
	private final int					PLACE_PICKER	= 2;

	private static final List<String>	PERMISSIONS		= Arrays.asList("publish_stream", "photo_upload", "status_update");

	private TextView					messageTextView;
	private TextView					friendsTextView;
	private TextView					placeTextView;
	private ImageButton					friendsButton;
	private ImageButton					privacyButton;
	private PostPhotos					photosFragment;
	private AlbumSpinner				albumsFragment;
	private Privacy						privacy			= Privacy.SELF;
	private Map<String, String>			friends;
	private String						placeId;
	private List<String>				photoUris;
	private boolean						pendingAnnounce	= false;
	private List<GraphObject>			friendLists;
	private String						privacyFriendListId;

	private TagCallback					friendCallback	= new TagCallback() {

															@Override
															public void onTagClick(List<Tag> tags)
															{
																Tag tag = tags.get(0);

																if (tag.getId().length() == 0)
																{
																	List<Tag> users = new ArrayList<Tag>();

																	for (Iterator<String> iterator = friends.keySet().iterator(); iterator.hasNext();)
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
																	TextViewUtil.onTagClick(PostPhotosActivity.this, tags);
																}
															}
														};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		photoUris = new ArrayList<String>();

		setTitle(R.string.publish_new_photos);

		messageTextView = (TextView) findViewById(R.id.message_textview);
		friendsTextView = (TextView) findViewById(R.id.friends_textview);
		placeTextView = (TextView) findViewById(R.id.place_textview);
		photosFragment = (PostPhotos) getFragmentManager().findFragmentById(R.id.photos_fragment);
		albumsFragment = (AlbumSpinner) getFragmentManager().findFragmentById(R.id.albums_fragment);

		albumsFragment.setDefaultAlbumId(getIntent().getExtras().getString(KlyphBundleExtras.ALBUM_ID));
		albumsFragment.setOnSelectionChangeListener(this);

		getFragmentManager().beginTransaction().hide(photosFragment).commit();

		ImageButton placeDeleteButton = (ImageButton) findViewById(R.id.place_delete_button);
		ImageButton friendsDeleteButton = (ImageButton) findViewById(R.id.friends_delete_button);
		friendsButton = (ImageButton) findViewById(R.id.friends_button);
		ImageButton add = (ImageButton) findViewById(R.id.add_button);
		ImageButton place = (ImageButton) findViewById(R.id.place_button);
		privacyButton = (ImageButton) findViewById(R.id.privacy_button);
		privacyButton.setEnabled(false);

		placeDeleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				clearPlace();
			}
		});

		friendsDeleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				clearFriends();
			}
		});

		friendsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(PostPhotosActivity.this, FriendPickerActivity.class);

				if (friends != null && friends.size() > 0)
				{
					ArrayList<String> ids = new ArrayList<String>();
					ids.addAll(friends.keySet());
					intent.putStringArrayListExtra(KlyphBundleExtras.FRIEND_PICKER_IDS, ids);
				}

				startActivityForResult(intent, FRIEND_PICKER);
			}
		});

		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				startGallery();
			}
		});

		place.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				startActivityForResult(new Intent(PostPhotosActivity.this, PlacePickerActivity.class), PLACE_PICKER);
			}
		});

		setPrivacyListenerApi11();

		setPrivacy(KlyphPreferences.getPrivacy());

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

	@TargetApi(11)
	private void setPrivacyListenerApi11()
	{
		privacyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				PopupMenu popup = new PopupMenu(PostPhotosActivity.this, v);
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

		if (privacyButton != null)
		{
			// set changes
			switch (privacy)
			{
				case EVERYONE:
				{
					privacyButton.setImageDrawable(AttrUtil.getDrawable(this, privacyButton.isEnabled() ? R.attr.privacyPublicIcon
							: R.attr.privacyPublicDisabledIcon));
					KlyphPreferences.setPrivacy(0);
					break;
				}
				case ALL_FRIENDS:
				{
					privacyButton.setImageDrawable(AttrUtil.getDrawable(this, privacyButton.isEnabled() ? R.attr.privacyFriendsIcon
							: R.attr.privacyFriendsDisabledIcon));
					KlyphPreferences.setPrivacy(1);
					break;
				}
				case SELF:
				{
					privacyButton.setImageDrawable(AttrUtil.getDrawable(this, privacyButton.isEnabled() ? R.attr.privacySelfIcon
							: R.attr.privacySelfDisabledIcon));
					KlyphPreferences.setPrivacy(2);
					break;
				}
				case CUSTOM:
				{
					privacyButton.setImageDrawable(AttrUtil.getDrawable(this, privacyButton.isEnabled() ? R.attr.privacyCustomIcon
							: R.attr.privacyCustomIcon));
					break;
				}
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
		if (photoUris.size() == 0)
		{
			AlertUtil.showAlert(this, R.string.error, R.string.must_select_photos, R.string.ok);
		}
		/*
		 * else if (friends != null && friends.size() > 0 && (placeId == null || placeId.length() == 0))
		 * {
		 * AlertUtil.showAlert(this, R.string.error, R.string.must_select_place_to_tag_friends, R.string.ok);
		 * }
		 */
		else
		{
			handlePublishPost();
		}
		return false;
	}

	/*
	 * private ArrayList<String> getRealUris()
	 * {
	 * ArrayList<String> uris = new ArrayList<String>();
	 * 
	 * String[] columns_to_return = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
	 * String where = "";
	 * for (int i = 0; i < photou.size(); i++)
	 * {
	 * if (i != 0)
	 * where += " OR ";
	 * 
	 * where += MediaStore.Images.Media._ID + " LIKE ?";
	 * }
	 * 
	 * String[] values = photoIds.toArray(new String[0]);
	 * 
	 * Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns_to_return, where, values, null);
	 * 
	 * if (cursor != null)
	 * {
	 * for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
	 * {
	 * String id = cursor.getString(0);
	 * 
	 * for (String thumbId : photoIds)
	 * {
	 * if (id.equals(thumbId))
	 * {
	 * uris.add(cursor.getString(1));
	 * break;
	 * }
	 * }
	 * }
	 * }
	 * 
	 * return uris;
	 * }
	 */

	private void publishPhotos()
	{
		Intent intent = new Intent(this, UploadPhotoService.class);
		intent.putStringArrayListExtra(KlyphBundleExtras.UPLOAD_PHOTO_URIS, (ArrayList<String>) photoUris);
		intent.putExtra(KlyphBundleExtras.UPLOAD_PHOTO_CAPTION, messageTextView.getText().toString());
		intent.putExtra(KlyphBundleExtras.UPLOAD_PHOTO_PLACE, placeId);
		intent.putExtra(KlyphBundleExtras.UPLOAD_PHOTO_PRIVACY, getPrivacyParam());
		intent.putExtra(KlyphBundleExtras.UPLOAD_PHOTO_ALBUM, albumsFragment.getSelectedAlbum());

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
		return R.layout.activity_post_photos;
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
	protected void onSaveInstanceState(Bundle outState)
	{
		// No call for super(). Bug on API Level > 11.

	}

	@Override
	protected void onResumeFragments()
	{
		Log.d("onResumeFragments", "photo size " + photoUris.size());

		if (photoUris.size() > 0)
		{
			photosFragment.setImages(photoUris);
			/*
			 * LayoutParams params = photosFragment.getGridView().getLayoutParams();
			 * 
			 * if (photoIds.size() <= 4)
			 * {
			 * params.height = (int) ((KlyphDevice.getDeviceWidth() - (48 + 8 + 2) * KlyphDevice.getDeviceDensity()) / 4);
			 * }
			 * else
			 * {
			 * params.height = (int) ((KlyphDevice.getDeviceWidth() - (48 + 8 + 2) * KlyphDevice.getDeviceDensity()) / 2);
			 * }
			 * photosFragment.getGridView().setLayoutParams(params);
			 */

			getFragmentManager().beginTransaction().show(photosFragment).commit();
		}
		else
		{
			getFragmentManager().beginTransaction().hide(photosFragment).commit();
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
		Intent intent = new Intent(PostPhotosActivity.this, GalleryActivity.class);
		intent.putStringArrayListExtra(KlyphBundleExtras.PHOTO_LIST_URI, (ArrayList<String>) photoUris);
		startActivityForResult(intent, GALLERY);
	}

	@Override
	public void onPostPhotosDeleteClick()
	{
		photoUris = new ArrayList<String>();

		getFragmentManager().beginTransaction().hide(photosFragment).commit();
		// getFragmentManager().beginTransaction().hide(albumsFragment).commit();
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

		publishPhotos();
	}

	@Override
	public void onAlbumSpinnerSelectionChange()
	{
		privacyButton.setEnabled(albumsFragment.getSelectedAlbum().equals("me"));
		setPrivacy(privacy);
	}

	@Override
	public void onPermissionsChange()
	{
		if (pendingAnnounce == true)
		{
			handlePublishPost();
		}

	}

	@Override
	public void onCancelPermissions()
	{

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
		photosFragment = null;
		albumsFragment = null;
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
