package com.abewy.android.apps.klyph.app;

import java.util.ArrayList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.fragment.GalleryFragment;

public class GalleryActivity extends TitledFragmentActivity implements LoaderCallbacks<Cursor>
{
	private static final Uri	SOURCE_URI			= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	private static final int	CURSOR_CODE			= 123456789;
	private ArrayList<String>	imageUris;
	private GalleryFragment		fragment;
	private boolean 				showCameraPictures	= false;

	// Prevent cursor to reload
	private boolean				cursorLoaded		= false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.gallery_activity_title));

		imageUris = getIntent().getStringArrayListExtra(KlyphBundleExtras.PHOTO_LIST_URI);

		fragment = (GalleryFragment) getFragmentManager().findFragmentById(R.id.gallery_fragment);
		fragment.setSelectedPhotos(imageUris);

		showCameraPictures = getIntent().getBooleanExtra(KlyphBundleExtras.CAMERA_PICTURES, false);

		getSupportLoaderManager().initLoader(CURSOR_CODE, null, this);
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_gallery;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		if (cursorLoaded == false)
		{
			final String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
			String comparator = showCameraPictures == true ? " = ?" : " != ?";
			String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + comparator;
			String[] selectionArgs = new String[] { "Camera" };
			final String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";
			return new CursorLoader(this, SOURCE_URI, projection, selection, selectionArgs, orderBy);
		}

		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		if (cursorLoaded == false)
		{
			fragment.setCursor(cursor);

			cursorLoaded = true;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		fragment = null;
		imageUris = null;
		getSupportLoaderManager().destroyLoader(CURSOR_CODE);
	}
}