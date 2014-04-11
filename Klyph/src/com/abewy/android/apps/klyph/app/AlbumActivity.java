
package com.abewy.android.apps.klyph.app;

import java.util.ArrayList;
import java.util.List;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.fql.Photo;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.fragment.IToggleBarVisibility;
import com.abewy.android.apps.klyph.fragment.ImageFragment;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;
import com.abewy.android.apps.klyph.view.ListEmptyView;

public class AlbumActivity extends TitledViewPagerActivity implements IToggleBarVisibility
{
	private TabsAdapter	adapter;

	@Override
	protected int getCustomTheme()
	{
		return KlyphPreferences.getProfileTheme();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_background_transparent_gradient));
		getWindow().setBackgroundDrawableResource(R.drawable.image_background);
		
		setTitle("");

		setLoadingView(findViewById(R.id.progress_bar));

		ArrayList<Photo> photos = getIntent().getParcelableArrayListExtra(KlyphBundleExtras.ALBUM_PHOTOS);
		List<String> imageIds = getIntent().getStringArrayListExtra(KlyphBundleExtras.PHOTO_LIST_ID);

		if (photos != null)
		{
			List<GraphObject> list = new ArrayList<GraphObject>();
			for (Photo photo : photos)
			{
				list.add(photo);
			}
			((TabsAdapter) getPagerAdapter()).setPhotos(list);

			int startPosition = getIntent().getIntExtra(KlyphBundleExtras.START_POSITION, 0);
			getViewPager().setCurrentItem(startPosition);

			showViewPager();
			
			((TabsAdapter) getPagerAdapter()).show();
			setLoadingViewVisible(false);
		}
		else if (imageIds != null)
		{
			String id = "";
			int n = imageIds.size();
			for (int i = 0; i < n; i++)
			{
				if (i < n - 1)
				{
					id += "\"" + imageIds.get(i) + "\", "; 
				}
				else
				{
					id += "\"" + imageIds.get(i) + "\""; 
				}
			}

			new AsyncRequest(Query.PHOTO_LIST, id, "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(final Response response)
				{
					runOnUiThread(new Runnable() {

						@Override
						public void run()
						{
							if (response.getError() == null)
							{
								onPhotoListRequestSuccess(response.getGraphObjectList());
							}
							else
							{
								onPhotoListRequestError(response.getError());
							}
						}
					});
				}
			}).execute();
		}
		else
		{
			Log.d("AlbumActivity", "else");
			String id = getIntent().getStringExtra(KlyphBundleExtras.ALBUM_ID);
			// String name =
			// getIntent().getStringExtra(CkoobafeBundleExtras.ALBUM_NAME);

			// showPageIndicator();
			showViewPager();

			// ((TabsAdapter) getPagerAdapter()).show();

			new AsyncRequest(Query.ALBUM_PHOTOS, id, "", new AsyncRequest.Callback() {

				@Override
				public void onComplete(final Response response)
				{
					runOnUiThread(new Runnable() {

						@Override
						public void run()
						{
							if (response.getError() == null)
							{
								onAlbumPhotosRequestSuccess(response.getGraphObjectList());
							}
							else
							{
								onAlbumPhotosRequestError(response.getError());
							}
						}
					});
				}
			}).execute();
		}
	}
	
	private void onPhotoListRequestSuccess(List<GraphObject> photos)
	{
		final String firstVisibleId = getIntent().getStringExtra(KlyphBundleExtras.PHOTO_ID);
		
		int n = photos.size();
		int startPosition = 0;
		
		for (int i = 0; i < n; i++)
		{
			Photo photo = (Photo) photos.get(i);
			
			if (firstVisibleId.equals(photo.getPid()))
			{
				startPosition = i;
				break;
			}
		}
		
		((TabsAdapter) getPagerAdapter()).setPhotos(photos);
		getViewPager().setCurrentItem(startPosition);
		showViewPager();
		setLoadingViewVisible(false);
	}

	private void onPhotoListRequestError(RequestError error)
	{
		Log.d("AlbumActivity", "request error " + error);
		showError();
	}
	
	private void onAlbumPhotosRequestSuccess(List<GraphObject> photos)
	{
		((TabsAdapter) getPagerAdapter()).setPhotos(photos);
		showViewPager();
		setLoadingViewVisible(false);
	}
	
	private void onAlbumPhotosRequestError(RequestError error)
	{
		Log.d("AlbumActivity", "request error " + error);
		showError();
	}
	
	private void showError()
	{
		ListEmptyView lev = new ListEmptyView(this);
		lev.setText(R.string.request_error);
		addContentView(lev, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setLoadingViewVisible(false);
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_album;
	}
	
	@Override
	protected FragmentPagerAdapter getPagerAdapter()
	{
		if (adapter == null)
			adapter = new TabsAdapter(getFragmentManager(), getViewPager());

		return adapter;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (adapter != null)
		{
			adapter.destroy();
			adapter = null;
		}
	}

	private static class TabsAdapter extends FragmentPagerAdapter implements OnPageChangeListener
	{
		private List<GraphObject>	photos;
		private ImageFragment[]	fragments;

		public TabsAdapter(FragmentManager fm, ViewPager viewPager)
		{
			super(fm);
			fragments = new ImageFragment[0];
			photos = new ArrayList<GraphObject>();
			viewPager.setOnPageChangeListener(this);
		}

		public void show()
		{
			notifyDataSetChanged();
		}

		public void setPhotos(List<GraphObject> photos)
		{
			this.photos = photos;
			
			fragments = new ImageFragment[photos.size()];
			for (int i = 0; i < photos.size(); i++)
			{
				Photo photo = (Photo) photos.get(i);
				
				fragments[i] = new ImageFragment(i);
				fragments[i].setElementId(photo.getObject_id());
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount()
		{
			if (photos != null)
				return photos.size();
			
			return 0;
		}

		@Override
		public Fragment getItem(int position)
		{
			return fragments[position];
		}
		
		@Override
		public CharSequence getPageTitle(int position)
		{

			return "";
		}
		
		private boolean barVisibility = true;
		public void setBarVisibility(Fragment fragment, boolean visible)
		{
			barVisibility = visible;
			for (ImageFragment imageFragment : fragments)
			{
				if (imageFragment != fragment)
				{
					imageFragment.setBarVisibility(visible);
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
			
		}

		@Override
		public void onPageSelected(int position)
		{
			fragments[position].setBarVisibility(barVisibility);
		}
		
		public void destroy()
		{
			photos = null;
			fragments = null;
		}
	}

	private boolean barVisibility = true;

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
		
		((TabsAdapter) getPagerAdapter()).setBarVisibility(fragment, barVisibility);
		
		return barVisibility;
	}
	
	@Override
	public boolean isBarVisible()
	{
		return barVisibility;
	}
}
