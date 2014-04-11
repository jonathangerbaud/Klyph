package com.abewy.android.apps.klyph.app;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.fql.Video;
import com.abewy.android.apps.klyph.fragment.AlbumVideos;
import com.abewy.android.apps.klyph.fragment.AlbumVideos.ElementVideosListener;
import com.abewy.android.apps.klyph.fragment.VideoQualityDialog;
import com.abewy.android.apps.klyph.fragment.VideoQualityDialog.VideoQualityDialogListener;
import com.abewy.util.PhoneUtil;

public class AlbumVideosActivity extends TitledFragmentActivity implements ElementVideosListener, VideoQualityDialogListener
{
	private String	id;
	private Video videoToDisplay;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		AlbumVideos fragment = (AlbumVideos) getFragmentManager().findFragmentById(R.id.video_list_fragment);

		id = getIntent().getStringExtra(KlyphBundleExtras.ELEMENT_ID);

		String name = getIntent().getStringExtra(KlyphBundleExtras.NAME);
		setTitle(name);

		fragment.setElementId(id);
		fragment.load();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_album_videos;
	}

	@Override
	public void onVideoSelected(Fragment fragment, Video video)
	{
		videoToDisplay = video;
		
		DialogFragment dialog = new VideoQualityDialog();
        dialog.show(getFragmentManager(), "VideoQualityDialog");
	}
	
	@Override
	public void onVideoHDClick(DialogFragment dialog)
	{
		Uri intentUri = Uri.parse(videoToDisplay.getSrc_hq());
		
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(intentUri, "video/" + videoToDisplay.getVideoFormat());
		
		try
		{
			startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			PhoneUtil.openURL(this, videoToDisplay.getSrc_hq());
		}
		
		videoToDisplay = null;
	}

	@Override
	public void onVideoSDClick(DialogFragment dialog)
	{
		Uri intentUri = Uri.parse(videoToDisplay.getSrc());
		
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(intentUri, "video/" + videoToDisplay.getVideoFormat());
		
		videoToDisplay = null;
		
		try
		{
			startActivity(intent);
		}
		catch(ActivityNotFoundException e)
		{
			intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(intentUri);
			startActivity(intent);
		}
	}
}
