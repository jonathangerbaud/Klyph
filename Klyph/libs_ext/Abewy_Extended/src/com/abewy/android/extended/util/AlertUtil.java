package com.abewy.android.extended.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

public class AlertUtil
{
	private static final int	NO_RESOURCE	= -1;
	public static final int		NONE		= -1;

	public static AlertDialog showAlert(Context context, int title, int message)
	{
		return showAlert(context, title, message, null, NO_RESOURCE, null, NO_RESOURCE, null);
	}

	public static AlertDialog showAlert(Context context, int title, int message, int positiveButton)
	{
		return showAlert(context, title, message, null, positiveButton, null, NO_RESOURCE, null);
	}

	public static AlertDialog showAlert(Context context, int title, int message, int positiveButton, OnClickListener listener)
	{
		return showAlert(context, title, message, null, positiveButton, listener, NO_RESOURCE, null);
	}

	public static AlertDialog showAlert(Context context, int title, int message, int positiveButton, int negativeButton)
	{
		return showAlert(context, title, message, null, positiveButton, null, negativeButton, null);
	}

	public static AlertDialog showAlert(Context context, int title, int message, View contentView, int positiveButton, int negativeButton)
	{
		return showAlert(context, title, message, contentView, positiveButton, null, negativeButton, null);
	}

	public static AlertDialog showAlert(Context context, int title, int message, int positiveButton, OnClickListener positiveListener,
			int negativeButton, OnClickListener negativeListener)
	{
		return showAlert(context, title, message, null, positiveButton, positiveListener, negativeButton, negativeListener);
	}

	public static AlertDialog showAlert(Context context, int title, int message, View contentView, int positiveButton,
			OnClickListener positiveListener, int negativeButton, OnClickListener negativeListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (title != NONE)
		{
			builder.setTitle(title);
		}

		if (message != NONE)
		{
			builder.setMessage(message);
		}

		if (contentView != null)
		{
			builder.setView(contentView);
		}

		if (positiveButton != NO_RESOURCE)
		{
			builder.setPositiveButton(positiveButton, positiveListener);
		}

		if (negativeButton != NO_RESOURCE)
		{
			builder.setNegativeButton(negativeButton, negativeListener);
		}

		return builder.show();
	}
	
	public static AlertDialog showAlert(Context context, int title, String[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener itemsListener, int positiveButton, 
			OnClickListener positiveListener, int negativeButton, OnClickListener negativeListener)
	{
		return showAlert(context, title, NONE, items, checkedItems, itemsListener, positiveButton, positiveListener, negativeButton, negativeListener);
	}
	
	public static AlertDialog showAlert(Context context, int title, int message, String[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener itemsListener,
			int positiveButton, OnClickListener positiveListener, int negativeButton, OnClickListener negativeListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (title != NONE)
		{
			builder.setTitle(title);
		}

		if (message != NONE)
		{
			builder.setMessage(message);
		}
		
		builder.setMultiChoiceItems(items, checkedItems, itemsListener);

		if (positiveButton != NO_RESOURCE)
		{
			builder.setPositiveButton(positiveButton, positiveListener);
		}

		if (negativeButton != NO_RESOURCE)
		{
			builder.setNegativeButton(negativeButton, negativeListener);
		}

		return builder.show();
	}
}
