package com.abewy.android.apps.klyph.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.abewy.android.apps.klyph.R;

public class VideoQualityDialog extends DialogFragment
{
	public interface VideoQualityDialogListener
	{
		public void onVideoHDClick(DialogFragment dialog);

		public void onVideoSDClick(DialogFragment dialog);
	}

	private VideoQualityDialogListener	listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_video_quality_message)
				.setTitle(R.string.dialog_video_quality_title)
				.setPositiveButton(R.string.dialog_video_quality_hd, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id)
					{
						listener.onVideoHDClick(VideoQualityDialog.this);
					}
				}).setNegativeButton(R.string.dialog_video_quality_sd, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id)
					{
						listener.onVideoSDClick(VideoQualityDialog.this);
					}
				});

		return builder.create();
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		try
		{
			listener = (VideoQualityDialogListener) activity;
		}
		catch (ClassCastException e)
		{
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString() + " must implement VideoQualityDialogListener");
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		listener = null;
	}
}
