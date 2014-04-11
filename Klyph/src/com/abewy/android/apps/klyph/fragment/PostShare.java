package com.abewy.android.apps.klyph.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.app.FriendPickerActivity;
import com.abewy.android.apps.klyph.core.fql.Album;
import com.abewy.android.apps.klyph.core.fql.Attachment;
import com.abewy.android.apps.klyph.core.fql.Friend;
import com.abewy.android.apps.klyph.core.fql.Media;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.fql.Stream;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class PostShare extends Fragment implements OnItemSelectedListener
{
	private static final int	SELECT_FRIEND_CODE	= 48563;

	private static final String	ALBUM				= "http://www.facebook.com/album.php?fbid=";
	private static final String	PHOTO				= "http://www.facebook.com/photo.php?fbid=";
	private static final String	VIDEO				= "http://www.facebook.com/photo.php?v=";

	private TextView			linkName;
	private TextView			linkDesc;
	private ImageView			linkImage;
	private ProgressBar			progress;
	private Spinner				spinner;

	private String				url;
	private String				imageUrl;
	private boolean 				isLink				= false;
	private int					previousPosition;

	public PostShare()
	{

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_post_share, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		view.setBackgroundResource(AttrUtil.getResourceId(getActivity(), R.attr.actionBarStackedBackground));

		linkName = (TextView) view.findViewById(R.id.name);
		linkDesc = (TextView) view.findViewById(R.id.desc);
		linkImage = (ImageView) view.findViewById(R.id.image);
		progress = (ProgressBar) view.findViewById(R.id.progress);
		spinner = (Spinner) view.findViewById(R.id.spinner);
	}

	public void initWithIntent(Intent intent)
	{
		String photoId = intent.getStringExtra(KlyphBundleExtras.SHARE_PHOTO_ID);
		String videoId = intent.getStringExtra(KlyphBundleExtras.SHARE_VIDEO_ID);
		String albumId = intent.getStringExtra(KlyphBundleExtras.SHARE_ALBUM_ID);
		Stream post = intent.getParcelableExtra(KlyphBundleExtras.SHARE_POST_ID);
		String linkUrl = intent.getStringExtra(KlyphBundleExtras.SHARE_LINK_URL);

		if (photoId != null)
		{
			url = PHOTO + photoId;

			new AsyncRequest(Query.PHOTO, photoId, "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					onPhotoRequestComplete(response);
				}
			}).execute();
		}
		else if (videoId != null)
		{
			url = VIDEO + videoId;

			new AsyncRequest(Query.VIDEO, videoId, "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					onVideoRequestComplete(response);
				}
			}).execute();
		}
		else if (albumId != null)
		{
			url = ALBUM + albumId;

			new AsyncRequest(Query.ALBUM, albumId, "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					onAlbumRequestComplete(response);
				}
			}).execute();
		}
		else if (post != null)
		{
			url = post.getPermalink();

			if (post.isStatus())
			{
				fillViews(post.getActor_name(), post.getMessage(), post.getActor_pic());
				getActivity().setTitle(R.string.post_share_status);
			}
			else
			{
				Attachment att = post.getAttachment();

				if (att.isPhoto() || att.isVideo() || att.isAlbum())
				{
					Media media = att.getMedia().get(0);

					if (att.isAlbum())
					{
						fillViews(post.getActor_name(), att.getName(), media.getSrc());
					}
					else
					{
						fillViews(post.getActor_name(), media.getAlt(), media.getSrc());
					}

					if (att.isPhoto())
						getActivity().setTitle(R.string.post_share_photo);
					else if (att.isVideo())
						getActivity().setTitle(R.string.post_share_video);
					else
						getActivity().setTitle(R.string.post_share_album);
				}
				else
				{
					getActivity().setTitle(R.string.post_share_status);
					fillViews(post.getActor_name(), post.getMessage(), post.getActor_pic());
				}
			}
		}
		else if (linkUrl != null)
		{
			isLink = true;
			url = linkUrl;
			imageUrl = intent.getStringExtra(KlyphBundleExtras.SHARE_LINK_IMAGE_URL);
			String linkName = intent.getStringExtra(KlyphBundleExtras.SHARE_LINK_NAME);
			String linkDesc = intent.getStringExtra(KlyphBundleExtras.SHARE_LINK_DESC);

			fillViews(linkName, linkDesc, imageUrl);
			getActivity().setTitle(R.string.post_share_link);
		}
		else
		{
			Log.d("PostShare", "All null");
		}

		if (isLink == false)
		{
			((ViewGroup) spinner.getParent()).setVisibility(View.GONE);
		}
		else
		{
			SpinnerAdapter adapter = new SpinnerAdapter(getActivity());
			spinner.setAdapter(adapter);

			Friend friend = new Friend();
			friend.setName(getString(R.string.post_share_on_my_wall));
			adapter.add(friend);

			friend = new Friend();
			friend.setName(getString(R.string.post_share_on_friend_wall));
			adapter.add(friend);
			adapter.notifyDataSetChanged();

			spinner.setSelection(0);
			spinner.setOnItemSelectedListener(this);

			previousPosition = 0;
		}
	}
	
	private void onPhotoRequestComplete(final Response response)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onPhotoRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						onPhotoRequestError(response.getError());
					}
				}
			});
		}
	}

	private void onPhotoRequestSuccess(List<GraphObject> result)
	{
		if (result.size() > 0)
		{
			Photo photo = (Photo) result.get(0);
			fillViews(photo.getAlbum_name(), photo.getOwner_name(), photo.getSrc());
		}
		else
		{
			displayError("Photo list size = 0");
		}
	}
	
	private void onPhotoRequestError(RequestError error)
	{
		displayError(error.getMessage());
	}
	
	private void onVideoRequestComplete(final Response response)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onVideoRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						onVideoRequestError(response.getError());
					}
				}
			});
		}
	}

	private void onVideoRequestSuccess(List<GraphObject> result)
	{
		if (result.size() > 0)
		{
			Video video = (Video) result.get(0);
			fillViews(video.getAlbum_name(), video.getOwner_name(), video.getThumbnail_link());
		}
		else
		{
			displayError("Video list size = 0");
		}
	}
	
	private void onVideoRequestError(RequestError error)
	{
		displayError(error.getMessage());
	}
	
	private void onAlbumRequestComplete(final Response response)
	{
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run()
				{
					if (response.getError() == null)
					{
						onAlbumRequestSuccess(response.getGraphObjectList());
					}
					else
					{
						onAlbumRequestError(response.getError());
					}
				}
			});
		}
	}

	private void onAlbumRequestSuccess(List<GraphObject> result)
	{
		if (result.size() > 0)
		{
			Album album = (Album) result.get(0);
			fillViews(album.getName(), album.getOwner_name(), album.getCover_images().get(0).getSource());
		}
		else
		{
			displayError("Album list size = 0");
		}
	}
	
	private void onAlbumRequestError(RequestError error)
	{
		displayError(error.getMessage());
	}

	private void displayError(String error)
	{
		Log.d("PostShare", "Error " + error);
	}

	private void fillViews(String name, String desc, String imageUrl)
	{
		if (getView() != null && getActivity() != null)
		{
			linkName.setText(name);
			linkDesc.setText(desc);
			ImageLoader.display(linkImage, imageUrl, true, AttrUtil.getResourceId(getActivity(), R.attr.squarePlaceHolderIcon));

			linkName.setVisibility(View.VISIBLE);
			linkDesc.setVisibility(View.VISIBLE);
			linkImage.setVisibility(View.VISIBLE);

			progress.setVisibility(View.GONE);
		}
	}

	public Bundle getParams()
	{
		Bundle bundle = new Bundle();
		bundle.putString("link", url);

		if (isLink == true)
		{
			bundle.putString("name", linkName.getText().toString());
			bundle.putString("description", linkDesc.getText().toString());

			if (spinner.getSelectedItemPosition() > 1)
			{
				bundle.putString("to", ((Friend) spinner.getSelectedItem()).getUid());
			}
			else
			{
				bundle.putString("picture", imageUrl);
			}
		}

		return bundle;
	}

	public boolean  isFriendShare()
	{
		return spinner.getSelectedItemPosition() > 1;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SELECT_FRIEND_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				Friend friend = new Friend();
				friend.setUid(data.getStringExtra(KlyphBundleExtras.USER_ID));
				friend.setName(data.getStringExtra(KlyphBundleExtras.USER_NAME));

				SpinnerAdapter adapter = (SpinnerAdapter) spinner.getAdapter();
				int position = -1;
				for (int i = 2; i < adapter.getCount(); i++)
				{
					if (adapter.getItem(i).getUid().equals(friend.getUid()))
					{
						position = i;
						break;
					}
				}

				if (position == -1)
				{
					adapter.add(friend);
					adapter.notifyDataSetChanged();
					position = adapter.getCount() - 1;
				}

				spinner.setSelection(position);
			}
			else
			{
				spinner.setSelection(previousPosition);
			}
		}
	}

	private static class SpinnerAdapter extends BaseAdapter
	{
		private LayoutInflater	inflater;
		private List<Friend>	friends;

		public SpinnerAdapter(Context context)
		{
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			friends = new ArrayList<Friend>();
		}

		public void add(Friend friend)
		{
			friends.add(friend);
		}

		@Override
		public int getCount()
		{
			return friends.size();
		}

		@Override
		public Friend getItem(int index)
		{
			return friends.get(index);
		}

		@Override
		public long getItemId(int index)
		{
			return index;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
			}

			CheckedTextView ct = (CheckedTextView) convertView.findViewById(android.R.id.text1);
			ct.setText(getItem(position).getName());

			return convertView;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> Spinner, View arg1, int position, long arg3)
	{
		if (position == 1)
		{
			Intent intent = new Intent(getActivity(), FriendPickerActivity.class);
			intent.putExtra(KlyphBundleExtras.SINGLE_CHOICE, true);
			startActivityForResult(intent, SELECT_FRIEND_CODE);
		}
		else
		{
			previousPosition = position;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		linkName = null;
		linkDesc = null;
		linkImage = null;
		progress = null;
		spinner = null;
	}
}