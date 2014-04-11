package com.abewy.android.apps.klyph.app;

import java.util.List;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.graph.Comment;
import com.abewy.android.apps.klyph.core.graph.GraphObject;
import com.abewy.android.apps.klyph.core.request.BaseAsyncRequest.Callback;
import com.abewy.android.apps.klyph.core.request.RequestError;
import com.abewy.android.apps.klyph.core.request.Response;
import com.abewy.android.apps.klyph.core.util.AlertUtil;
import com.abewy.android.apps.klyph.request.AsyncRequest;
import com.abewy.android.apps.klyph.request.AsyncRequest.Query;

public class EditCommentDialog extends DialogFragment
{
	private Comment						comment;
	private EditText					editText;
	private AlertDialog					dialog;
	private EditCommentDialogListener	listener;
	private int							numTry	= 0;

	public static interface EditCommentDialogListener
	{
		public void onCommentEdited();
	}

	public EditCommentDialog(Comment comment, EditCommentDialogListener listener)
	{
		this.comment = comment;
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		View view = inflater.inflate(R.layout.dialog_edit_comment, null);
		editText = (EditText) view.findViewById(R.id.edit_text);
		editText.setText(comment.getMessage());

		// Pass null as the parent view because its going in the dialog layout
		builder.setView(view)
		// Add action buttons
				.setPositiveButton(R.string.ok, null).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id)
					{
						EditCommentDialog.this.getDialog().cancel();
					}
				});

		builder.setTitle(R.string.edit_comment);

		final AlertDialog dialog = builder.create();

		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface d)
			{

				Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view)
					{
						editComment();
					}
				});
			}
		});

		return dialog;
	}

	private void editComment()
	{
		if (!comment.getMessage().equals(editText.getText().toString()))
		{
			//If user have edited its comments, then push the update
			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.dialog_progress_text, null, false);
			dialog = new AlertDialog.Builder(getActivity()).setView(view).setCancelable(false).create();
			dialog.show();

			launchRequest();
		}
		else
		{
			// No modification, just leave
			this.dismiss();
		}
	}

	private void launchRequest()
	{
		new AsyncRequest(Query.EDIT_COMMENT, comment.getId(), editText.getText().toString(), new Callback() {

			@Override
			public void onComplete(Response response)
			{
				onRequestComplete(response);
			}
		}).execute();
	}

	private void onRequestComplete(final Response response)
	{
		Log.d("EditCommentDialog", "onRequestComplete: " + response);
		if (getActivity() != null)
		{
			getActivity().runOnUiThread(new Runnable() {

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
	}

	private void onRequestError(RequestError error)
	{
		Log.d("EditCommentDialog", "onRequestError: " + error);
		numTry++;

		if (numTry < 3)
		{
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run()
				{
					launchRequest();
				}
			}, 1500);
		}
		else
		{
			numTry = 0;

			if (dialog != null)
			{
				dialog.dismiss();
				dialog = null;
			}

			if (getActivity() != null)
				AlertUtil.showAlert(getActivity(), R.string.error, R.string.publish_comment_error, R.string.ok);
		}
	}

	private void onRequestSuccess(List<GraphObject> graphObjectList)
	{
		Log.d("EditCommentDialog", "onRequestSuccess: ");
		comment.setMessage(editText.getText().toString());

		if (listener != null)
			listener.onCommentEdited();

		dialog.dismiss();
		this.dismiss();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		listener = null;
		dialog = null;
		editText = null;
		comment = null;
	}
}