package com.abewy.android.apps.klyph.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.app.AllPhotosActivity;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.core.util.AttrUtil;

public class PostLink extends Fragment
{
	private static final int	REQUEST_CODE	= 9658;
	private static final String	HTTP			= "http://";
	private static final String	HTTPS			= "https://";

	private PostLinkListener	listener;
	private EditText			linkURL;
	private EditText			linkImageURL;
	private EditText			linkName;
	private EditText			linkDesc;
	private ImageButton			addButton;
	private ImageButton			deleteButton;
	private ImageView			linkImage;

	public interface PostLinkListener
	{
		public void onPostLinkDeleteClick();
	}

	public PostLink()
	{

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_post_link, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		view.setBackgroundResource(AttrUtil.getResourceId(getActivity(), R.attr.actionBarStackedBackground));

		deleteButton = (ImageButton) view.findViewById(R.id.delete_button);
		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (listener != null)
					listener.onPostLinkDeleteClick();
			}
		});

		addButton = (ImageButton) view.findViewById(R.id.add_button);
		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), AllPhotosActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});

		linkURL = (EditText) view.findViewById(R.id.link);
		linkName = (EditText) view.findViewById(R.id.name);
		linkDesc = (EditText) view.findViewById(R.id.desc);
		linkImage = (ImageView) view.findViewById(R.id.link_image);
		linkImageURL = (EditText) view.findViewById(R.id.link_image_url);
		

		reset();
	}
	
	public void setUrl(String url)
	{
		if (url != null)
			linkURL.setText(url);
	}
	
	public void setTitle(String title)
	{
		if (title != null)
			linkName.setText(title);
	}
	
	public void initWithIntent(Intent intent)
	{
		String url = intent.getStringExtra(KlyphBundleExtras.SHARE_LINK_URL);
		String imageUrl = intent.getStringExtra(KlyphBundleExtras.SHARE_LINK_IMAGE_URL);
		String name = intent.getStringExtra(KlyphBundleExtras.SHARE_LINK_NAME);
		String desc = intent.getStringExtra(KlyphBundleExtras.SHARE_LINK_DESC);
		linkURL.setText(url);
		linkName.setText(name);
		linkDesc.setText(desc);
		linkImageURL.setText(imageUrl);
		
		ImageLoader.display(linkImage, imageUrl, true, AttrUtil.getResourceId(getActivity(), R.attr.squarePlaceHolderIcon));
		linkImage.setVisibility(View.VISIBLE);
		
		linkURL.setEnabled(false);
		linkName.setEnabled(false);
		linkDesc.setEnabled(false);
		linkImageURL.setEnabled(false);
		
		addButton.setVisibility(View.GONE);
		deleteButton.setVisibility(View.GONE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
		{
			String url = data.getStringExtra(KlyphBundleExtras.URL);
			String thumbUrl = data.getStringExtra(KlyphBundleExtras.THUMB_URL);
			
			linkImageURL.setText(url);
			
			ImageLoader.display(linkImage, thumbUrl, true, AttrUtil.getResourceId(getActivity(), R.attr.squarePlaceHolderIcon));
			linkImage.setVisibility(View.VISIBLE);
		}
	}

	public void reset()
	{
		linkURL.setText(HTTP);
		linkImageURL.setText(HTTP);
		linkName.setText("");
		linkDesc.setText("");
		addButton.setVisibility(View.VISIBLE);
		linkImage.setVisibility(View.GONE);
		linkImage.setImageDrawable(null);
	}

	public Bundle getParams()
	{
		String url = linkURL.getText().toString();
		String imageUrl = linkImageURL.getText().toString();
		if (!url.contains(HTTP) && !url.contains(HTTPS))
		{
			url = HTTP + url;
		}

		/*
		 * if (!imageUrl.contains(HTTP) && !imageUrl.contains(HTTPS))
		 * {
		 * url = HTTP + url;
		 * }
		 */

		Bundle bundle = new Bundle();
		bundle.putString("link", url);

		if (!imageUrl.equals(HTTP) && imageUrl.length() > 0)
		{
			bundle.putString("picture", imageUrl);
		}
		bundle.putString("name", linkName.getText().toString());
		bundle.putString("description", linkDesc.getText().toString());

		return bundle;
	}

	public boolean  checkEntries()
	{
		String linkUrl = linkURL.getText().toString();
		String linkImageUrl = linkImageURL.getText().toString();
		String name = linkName.getText().toString();

		if (linkUrl.equals(HTTP) || linkURL.equals(HTTPS) || !URLUtil.isValidUrl(linkUrl))
		{
			AlertUtil.showAlert(getActivity(), R.string.error, R.string.post_link_malformated_url, R.string.ok);
			return false;
		}

		if (!linkUrl.contains(HTTP) && !linkUrl.contains(HTTPS))
		{
			linkUrl = HTTP + linkUrl;
		}

		if (!linkImageUrl.contains(HTTP) && !linkImageUrl.contains(HTTPS))
		{
			linkImageUrl = HTTP + linkImageUrl;
		}

		if (!linkImageUrl.equals(HTTP) && !URLUtil.isValidUrl(linkUrl))
		{
			AlertUtil.showAlert(getActivity(), R.string.error, R.string.post_link_malformated_image_url, R.string.ok);
			return false;
		}

		if (linkURL.isEnabled() && name.length() == 0)
		{
			AlertUtil.showAlert(getActivity(), R.string.error, R.string.post_link_empty_name, R.string.ok);
			return false;
		}

		return true;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (activity instanceof PostLinkListener)
		{
			listener = (PostLinkListener) activity;
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		listener = null;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		listener = null;
		linkURL = null;
		linkImageURL = null;
		linkName = null;
		linkDesc = null;
		addButton = null;
		deleteButton = null;
		linkImage = null;
	}
}
