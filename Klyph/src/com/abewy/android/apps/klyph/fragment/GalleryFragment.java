package com.abewy.android.apps.klyph.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.abewy.android.adapter.TypeAdapter;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.adapter.BaseAdapterSelector;
import com.abewy.android.apps.klyph.adapter.KlyphAdapter;
import com.abewy.android.apps.klyph.adapter.MultiObjectAdapter;
import com.abewy.android.apps.klyph.app.GalleryActivity;
import com.abewy.android.apps.klyph.core.KlyphDevice;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.imageloader.ImageLoader;
import com.abewy.android.apps.klyph.core.util.AttrUtil;
import com.abewy.android.apps.klyph.widget.CheckableGalleryLayout;
import com.abewy.util.Android;

public class GalleryFragment extends KlyphFragment2
{
	private static final int	CAMERA_GALLERY_CODE	= 1368;
	private static final int	CAMERA_CAPTURE_CODE	= 2475;

	private List<String>		initUris;
	private List<String>		imageUris;

	private ProgressBar			loadingView;

	// Prevent cursor to reload
	private boolean				cursorLoaded		= false;
	private boolean				showCamera			= false;
	private boolean				hasCamera			= false;

	private Uri					cameraFileUri;

	private ActionMode			actionMode;
	private ActionMode.Callback	mActionModeCallback	= new ActionMode.Callback() {

														@Override
														public boolean  onCreateActionMode(ActionMode mode, Menu menu)
														{

															return true;
														}

														@Override
														public boolean  onPrepareActionMode(ActionMode mode, Menu menu)
														{
															return false;
														}

														@Override
														public boolean  onActionItemClicked(ActionMode mode, MenuItem item)
														{
															switch (item.getItemId())
															{
																default:
																	return false;
															}
														}

														@Override
														public void onDestroyActionMode(ActionMode mode)
														{
															actionMode = null;
															goBack();
														}
													};

	public GalleryFragment()
	{

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAMERA_GALLERY_CODE && resultCode == Activity.RESULT_OK)
		{
			ArrayList<String> cameraUris = data.getStringArrayListExtra(KlyphBundleExtras.PHOTO_LIST_URI);
			goBack(cameraUris);
		}
		else if (requestCode == CAMERA_CAPTURE_CODE && resultCode == Activity.RESULT_OK)
		{
			String url = "";

			try
			{
				url = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), cameraFileUri.getPath(), "", "");
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			String uri = getRealPathFromURI(Uri.parse(url));

			ArrayList<String> uris = new ArrayList<String>();
			uris.add(uri);

			goBack(uris);
		}
	}

	private String getRealPathFromURI(Uri contentUri)
	{

		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, // Which columns to return
				null,       // WHERE clause; which rows to return (all rows)
				null,       // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	public void setSelectedPhotos(List<String> imageUrls)
	{
		this.initUris = imageUrls;

		if (this.initUris == null)
			this.initUris = new ArrayList<String>();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		loadingView = (ProgressBar) view.findViewById(android.R.id.progress);

		setListVisible(false);

		super.onViewCreated(view, savedInstanceState);

		imageUris = new ArrayList<String>();

		getGridView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		getGridView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> gridView, View view, int position, long id)
			{
				GraphObject o = getAdapter().getItem(position);
				
				if (o instanceof CameraObject)
				{
					Intent intent = new Intent(getActivity(), GalleryActivity.class);
					intent.putExtra(KlyphBundleExtras.CAMERA_PICTURES, true);
					intent.putStringArrayListExtra(KlyphBundleExtras.PHOTO_LIST_URI, (ArrayList<String>) initUris);
					startActivityForResult(intent, CAMERA_GALLERY_CODE);
				}
				else
				{
					if (!Android.isMinAPI(11))
					{
						CheckableGalleryLayout cgl = (CheckableGalleryLayout) view;
						cgl.toggle();
						Picture image = (Picture) o;
						image.setSelected(cgl.isChecked());
					}

					if (actionMode == null)
						actionMode = getActivity().startActionMode(mActionModeCallback);

					refreshActionModeTitle();
				}
			}
		});

		showCamera = !getActivity().getIntent().getBooleanExtra(KlyphBundleExtras.CAMERA_PICTURES, false);
		hasCamera = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
					|| getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

		defineEmptyText(R.string.empty_list_no_photo);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);

		if (hasCamera && menu.findItem(R.id.menu_take_photo) == null)
		{
			menu.add(Menu.NONE, R.id.menu_take_photo, Menu.NONE, "Take a photo").setIcon(AttrUtil.getResourceId(getActivity(), R.attr.takePhotoIcon))
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
	}

	@Override
	public boolean  onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_take_photo)
		{
			// create Intent to take a picture and return control to the calling application
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			cameraFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
			intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri); // set the image file name
			
			// start the image capture Intent
			startActivityForResult(intent, CAMERA_CAPTURE_CODE);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public static final int	MEDIA_TYPE_IMAGE	= 1;
	public static final int	MEDIA_TYPE_VIDEO	= 2;

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type)
	{
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type)
	{
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Klyph");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists())
		{
			if (!mediaStorageDir.mkdirs())
			{
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		}
		else if (type == MEDIA_TYPE_VIDEO)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		}
		else
		{
			return null;
		}

		return mediaFile;
	}

	@Override
	protected int getNumColumn()
	{
		return getResources().getInteger(R.integer.klyph_gallery_num_column);
	}

	@Override
	protected int getCustomLayout()
	{
		return R.layout.grid_simple;
	}

	private void refreshActionModeTitle()
	{
		int n = getGridView().getCheckedItemCount();

		if (n == 0)
		{
			actionMode.setTitle(R.string.no_photo_selected);
		}
		else if (n == 1)
		{
			actionMode.setTitle(R.string.one_photo_selected);
		}
		else
		{
			actionMode.setTitle(getString(R.string.several_photos_selected, n));
		}
	}

	public void goBack()
	{
		goBack(new ArrayList<String>());
	}

	public void goBack(ArrayList<String> uris)
	{
		Intent intent = new Intent();

		SparseBooleanArray positions = getGridView().getCheckedItemPositions();
		int n = getGridView().getCheckedItemCount();

		if (uris == null)
			uris = new ArrayList<String>();

		if (n > 0)
		{
			MultiObjectAdapter adapter = getAdapter();

			for (int i = 0; i < n; i++)
			{
				if (adapter.getItem(positions.keyAt(i)) instanceof Picture)
				{
					Picture image = (Picture) adapter.getItem(positions.keyAt(i));
					uris.add(image.getUri());
				}
			}
		}

		if (uris.size() > 0)
		{
			intent.putStringArrayListExtra(KlyphBundleExtras.PHOTO_LIST_URI, uris);
			getActivity().setResult(Activity.RESULT_OK, intent);
		}
		else
		{
			getActivity().setResult(Activity.RESULT_CANCELED, null);
		}

		getActivity().finish();
	}

	public void setCursor(Cursor cursor)
	{
		if (cursorLoaded == false)
		{
			MultiObjectAdapter adapter = new MultiObjectAdapter(getListView())
			{

				@Override
				protected TypeAdapter<GraphObject> getAdapter(GraphObject object, int layoutType)
				{
					TypeAdapter<GraphObject> adapter = BaseAdapterSelector.getAdapter(object, layoutType);
					
					if (adapter != null)
						return adapter;
					
					if (object instanceof Picture)
						return new GalleryAdapter();
					
					if (object instanceof CameraObject)
						return new CameraObjectAdapter();
					
					return null;
				}
			};

			if (cursor != null && cursor.isClosed() == false)
			{
				int n = cursor.getCount();
				int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

				for (int i = 0; i < n; i++)
				{
					cursor.moveToPosition(i);

					imageUris.add(cursor.getString(columnIndex));
					adapter.add(new Picture(cursor.getString(columnIndex)));
				}

				if (showCamera && hasCamera)
				{
					adapter.insert(new CameraObject(), 0);
				}

				getGridView().setAdapter(adapter);
				getGridView().setVisibility(View.VISIBLE);

				n = adapter.getCount();

				int nSelected = 0;
				for (int i = 0; i < n; i++)
				{
					if (adapter.getItem(i) instanceof Picture)
					{
						Picture image = (Picture) adapter.getItem(i);
						// Log.d("GalleryFragment", "uri = " + image.getUri());
						for (String uri : initUris)
						{
							if (image.getUri().equals(uri))
							{
								getGridView().setItemChecked(i, true);
								nSelected++;
								break;
							}
						}
					}
				}

				adapter.notifyDataSetChanged();

				if (nSelected > 0)
				{
					if (actionMode == null)
						actionMode = getActivity().startActionMode(mActionModeCallback);

					refreshActionModeTitle();
				}

				cursor.close();
			}
			else
			{
				if (showCamera && hasCamera)
				{
					adapter.insert(new CameraObject(), 0);
				}

				getGridView().setAdapter(adapter);
			}

			cursorLoaded = true;
		}

		loadingView.setVisibility(View.GONE);
		getGridView().setVisibility(View.VISIBLE);
		((View) getGridView().getParent()).setVisibility(View.VISIBLE);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		initUris = null;
		imageUris = null;
		loadingView = null;
		cameraFileUri = null;
		actionMode = null;
		mActionModeCallback = null;
	}

	private static class CameraObject extends GraphObject
	{
		private static final int VIEW_TYPE = 951478;
		
		public CameraObject()
		{

		}

		@Override
		public int getItemViewType()
		{
			return VIEW_TYPE;
		}		
	}

	private static class CameraObjectAdapter extends KlyphAdapter
	{
		@Override
		protected int getLayout()
		{
			return R.layout.item_gallery_camera_folder;
		}

		@Override
		protected void mergeViewWithData(View view, GraphObject data)
		{
			setHolder(view, new CameraObjectHolder());
		}

		@Override
		public void setLayoutParams(View view)
		{
			LayoutParams lp = view.getLayoutParams();
			int numColumn = view.getContext().getResources().getInteger(R.integer.klyph_gallery_num_column);
			lp.height = lp.width = (KlyphDevice.getDeviceWidth() - (numColumn - 1) * 2) / numColumn;
			view.setLayoutParams(lp);
		}

		private class CameraObjectHolder
		{
			public CameraObjectHolder()
			{

			}
		}
	}

	private static class Picture extends GraphObject
	{
		private static final int VIEW_TYPE = 12896475;
		
		private String					uri;

		public Picture(String uri)
		{
			this.uri = uri;
		}

		public String getUri()
		{
			return uri;
		}
		
		@Override
		public int getItemViewType()
		{
			return VIEW_TYPE;
		}	
	}

	private static class GalleryAdapter extends KlyphAdapter
	{
		private int	placeHolder	= -1;

		@Override
		protected void attachHolder(View view)
		{
			ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

			setHolder(view, new GalleryHolder(imageView));
		}

		@Override
		protected int getLayout()
		{
			return R.layout.item_gallery;
		}

		@Override
		protected void mergeViewWithData(View view, GraphObject data)
		{
			if (!Android.isMinAPI(11))
			{
				((CheckableGalleryLayout) view).setChecked(data.isSelected());
			}

			GalleryHolder holder = (GalleryHolder) getHolder(view);

			Picture mi = (Picture) data;

			holder.getImageView().setImageDrawable(null);

			if (placeHolder == -1)
				placeHolder = AttrUtil.getResourceId(holder.getImageView().getContext(), R.attr.squarePlaceHolderIcon);

			String uri = "file://" + mi.getUri();

			ImageLoader.display(holder.getImageView(), uri, placeHolder);
		}
		
		private class GalleryHolder
		{
			private ImageView	imageView;

			public GalleryHolder(ImageView imageView)
			{
				this.imageView = imageView;
			}

			public ImageView getImageView()
			{
				return imageView;
			}
		}
	}
}