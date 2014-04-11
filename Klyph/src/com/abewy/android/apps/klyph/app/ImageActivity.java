package com.abewy.android.apps.klyph.app;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import com.abewy.android.apps.klyph.KlyphApplication;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.fragment.IToggleBarVisibility;
import com.abewy.android.apps.klyph.fragment.ImageFragment;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class ImageActivity extends TitledFragmentActivity implements IToggleBarVisibility
{
	private boolean	isDestroyed	= false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		

		// Let the Application class know that the first launch is complete
		// If we come from a notification, then do not show the ads
		// When going back to main activity
		KlyphApplication.getInstance().launchComplete();

		isDestroyed = false;

		getWindow().setBackgroundDrawableResource(R.drawable.image_background);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_background_transparent_gradient));
		
		setTitle("");

		final ImageFragment image = (ImageFragment) getFragmentManager().findFragmentById(R.id.image_fragment);

		String photoId = getIntent().getStringExtra(KlyphBundleExtras.PHOTO_ID);
		Log.d("ImageActivity", "photo id " + photoId);
		String userId = getIntent().getStringExtra(KlyphBundleExtras.USER_ID);

		if (photoId != null)
		{
			image.setElementId(photoId);
			//image.load();
		}
		else if (userId != null)
		{
			AsyncRequest loadRequest = new AsyncRequest(Query.USER_PROFILE_PHOTO, userId, "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(Response response)
				{
					if (response.getError() == null)
					{
						if (response.getGraphObjectList().size() > 0 && isDestroyed == false)
						{
							Photo photo = (Photo) response.getGraphObjectList().get(0);
							image.setElementId(photo.getObject_id());
							image.load();
						}
					}
				}
			});

			loadRequest.execute();
		}
	}

	@Override
	protected int getCustomTheme()
	{
		return KlyphPreferences.getProfileTheme();
	}

	@Override
	public void onDestroy()
	{
		isDestroyed = true;
		super.onDestroy();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_image;
	}

	private boolean	barVisibility	= true;

	@Override
	public boolean toggleBarVisibility(Fragment fragment)
	{
		barVisibility = !barVisibility;

		if (barVisibility == true)
		{
			getActionBar().show();
		}
		else
		{
			getActionBar().hide();
		}

		return barVisibility;
	}

	@Override
	public boolean isBarVisible()
	{
		return barVisibility;
	}
}
