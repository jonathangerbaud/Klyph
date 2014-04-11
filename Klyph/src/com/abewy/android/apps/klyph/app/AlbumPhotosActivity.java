package com.abewy.android.apps.klyph.app;

import android.os.Bundle;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.fragment.AlbumPhotos;
import com.abewy.android.apps.klyph.R;

public class AlbumPhotosActivity extends TitledFragmentActivity
{
	private String	id;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		AlbumPhotos ep = (AlbumPhotos) getFragmentManager()
				.findFragmentById(R.id.photo_list_fragment);

		id = getIntent().getStringExtra(KlyphBundleExtras.ALBUM_ID);

		String name = getIntent().getStringExtra(
				KlyphBundleExtras.ALBUM_NAME);
		setTitle(name);

		ep.setElementId(id);
		
		if (getIntent().getBooleanExtra(KlyphBundleExtras.ALBUM_TAGGED, false) == true)
		{
			ep.setTaggedAlbum(true);
		}
		
		ep.load();
	}

	// Check if it works with klyph album generated
	/*@Override
	public boolean  onPrepareOptionsMenu(Menu menu)
	{
		if (menu.findItem(R.id.menu_delete) == null)
		{
			menu.add(Menu.NONE, R.id.menu_delete, Menu.NONE, "Delete")
			.setIcon(AttrUtil.getResourceId(this, R.attr.discardIcon))
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean  onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_delete)
		{
			askDelete();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void askDelete()
	{
		AlertUtil.showAlert(this, R.string.delete_post_confirmation_title,
				R.string.delete_post_confirmation_message, R.string.yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						delete();
					}

				}, R.string.no, null);
	}

	private void delete()
	{
		final AlertDialog dialog = AlertUtil.showAlert(this, R.string.delete, R.string.deleting);

		new AsyncRequest(Query.DELETE_OBJECT, id, "", new AsyncRequest.Callback() {

			@Override
			public void onComplete(Response response)
			{
				dialog.dismiss();

				if (response.getError() != null)
				{
					AlertUtil.showAlert(AlbumPhotosActivity.this, R.string.error, R.string.delete_post_error, R.string.ok);
				}
				else
				{
					Toast.makeText(AlbumPhotosActivity.this.getApplication(), R.string.post_deleted, Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		}).execute();
	}*/



	@Override
	protected int getLayout()
	{
		return R.layout.activity_album_photos;
	}
}
