/*
 * Original code by Dominik Schuermann
 * 
 * Copyright (C) 2011-2013 Dominik Schürmann <dominik@dominikschuermann.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abewy.android.apps.klyph.fragment;

import org.sufficientlysecure.donations.google.util.IabHelper;
import org.sufficientlysecure.donations.google.util.IabResult;
import org.sufficientlysecure.donations.google.util.Purchase;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;

public class DonateFragment extends Fragment
{
	public static final String		ARG_DEBUG					= "debug";

	public static final String		ARG_GOOGLE_PUBKEY			= "googlePubkey";
	public static final String		ARG_GOOGLE_CATALOG			= "googleCatalog";
	public static final String		ARG_GOOGLE_CATALOG_VALUES	= "googleCatalogValues";

	private static final String		TAG							= "DonateFragment";

	// http://developer.android.com/google/play/billing/billing_testing.html
	private static final String[]	CATALOG_DEBUG				= new String[] {
					"android.test.purchased",
					"android.test.canceled",
					"android.test.refunded",
					"android.test.item_unavailable"			};

	private Spinner					mGoogleSpinner;

	// Google Play helper object
	private IabHelper				mHelper;

	protected boolean 				mDebug						= false;

	protected String				mGooglePubkey				= "";
	protected String[]				mGgoogleCatalog				= new String[] {};
	protected String[]				mGoogleCatalogValues		= new String[] {};

	private ViewStub				googleViewStub;
	private TextView				thankYou;

	public static DonateFragment newInstance(boolean  debug, String googlePubkey, String[] googleCatalog,
			String[] googleCatalogValues)
	{
		DonateFragment donateFragment = new DonateFragment();
		Bundle args = new Bundle();

		args.putBoolean(ARG_DEBUG, debug);

		args.putString(ARG_GOOGLE_PUBKEY, googlePubkey);
		args.putStringArray(ARG_GOOGLE_CATALOG, googleCatalog);
		args.putStringArray(ARG_GOOGLE_CATALOG_VALUES, googleCatalogValues);

		donateFragment.setArguments(args);
		return donateFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mDebug = getArguments().getBoolean(ARG_DEBUG);

		mGooglePubkey = getArguments().getString(ARG_GOOGLE_PUBKEY);
		mGgoogleCatalog = getArguments().getStringArray(ARG_GOOGLE_CATALOG);
		mGoogleCatalogValues = getArguments().getStringArray(ARG_GOOGLE_CATALOG_VALUES);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_donate, container, false);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		thankYou = (TextView) getView().findViewById(R.id.thank_you);

		if (KlyphPreferences.hasUserDonated())
		{
			thankYou.setVisibility(View.VISIBLE);
		}
		else
		{
			ImageView logo = (ImageView) getActivity().findViewById(R.id.app_logo);
			logo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
					alert.setTitle(R.string.donate_unlock);
					alert.setMessage(R.string.donate_code);

					final EditText editText = new EditText(getActivity());
					alert.setView(editText);

					alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							if (getActivity().getString(R.string.donation_validation_code).equals(editText.getText().toString()))
							{
								handleResult(0, null);
							}
						}
					});

					alert.show();

				}
			});

			// inflate google view into stub
			googleViewStub = (ViewStub) getActivity().findViewById(R.id.donate_google_stub);
			googleViewStub.inflate();

			// choose donation amount
			mGoogleSpinner = (Spinner) getActivity().findViewById(R.id.donations__google_android_market_spinner);
			ArrayAdapter<CharSequence> adapter;
			if (mDebug)
			{
				adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item,
						CATALOG_DEBUG);
			}
			else
			{
				adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item,
						mGoogleCatalogValues);
			}
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mGoogleSpinner.setAdapter(adapter);

			Button btGoogle = (Button) getActivity().findViewById(R.id.donations__google_android_market_donate_button);
			btGoogle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					donateGoogleOnClick(v);
				}
			});

			// Create the helper, passing it our context and the public key to
			// verify signatures with
			if (mDebug)
				Log.d(TAG, "Creating IAB helper.");
			mHelper = new IabHelper(getActivity(), mGooglePubkey);

			// enable debug logging (for a production application, you should
			// set this to false).
			mHelper.enableDebugLogging(mDebug);

			// Start setup. This is asynchronous and the specified listener
			// will be called once setup completes.
			if (mDebug)
				Log.d(TAG, "Starting setup.");
			mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
				public void onIabSetupFinished(IabResult result)
				{
					if (mDebug)
						Log.d(TAG, "Setup finished.");

					if (!result.isSuccess())
					{
						// Oh noes, there was a problem.
						openDialog(android.R.drawable.ic_dialog_alert,
								R.string.donations__google_android_market_not_supported_title,
								getString(R.string.donations__google_android_market_not_supported));
						return;
					}

					// Have we been disposed of in the meantime? If so, quit.
					if (mHelper == null)
						return;
				}
			});
		}
	}

	/**
	 * Open dialog
	 * 
	 * @param icon
	 * @param title
	 * @param message
	 */
	private void openDialog(int icon, int title, String message)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setIcon(icon);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(true);
		dialog.setNeutralButton(R.string.donations__button_close, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * Donate button executes donations based on selection in spinner
	 * 
	 * @param view
	 */
	public void donateGoogleOnClick(View view)
	{
		final int index;
		index = mGoogleSpinner.getSelectedItemPosition();
		if (mDebug)
			Log.d(TAG, "selected item in spinner: " + index);

		if (mDebug)
		{
			// when debugging, choose android.test.x item
			mHelper.launchPurchaseFlow(getActivity(), CATALOG_DEBUG[index], IabHelper.ITEM_TYPE_INAPP, 0,
					mPurchaseFinishedListener, null);
		}
		else
		{
			mHelper.launchPurchaseFlow(getActivity(), mGgoogleCatalog[index], IabHelper.ITEM_TYPE_INAPP, 0,
					mPurchaseFinishedListener, null);
		}
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener	mPurchaseFinishedListener	= new IabHelper.OnIabPurchaseFinishedListener() {
																			public void onIabPurchaseFinished(
																					IabResult result, Purchase purchase)
																			{
																				handleResult(result.getResponse(),
																						purchase);
																			}
																		};

	private void handleResult(int resultCode, Purchase purchase)
	{
		if (mDebug)
			Log.d(TAG, "Purchase finished: " + resultCode + ", purchase: " + purchase);

		if (resultCode == 0 || resultCode == 7)
		{
			KlyphPreferences.setUserDonated(true);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			googleViewStub.setVisibility(View.GONE);
			thankYou.setVisibility(View.VISIBLE);

			if (mDebug)
				Log.d(TAG, "Purchase successful.");

			// directly consume in-app purchase, so that people can
			// donate multiple times
			/*
			 * if (mDebug)
			 * {
			 * mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			 * }
			 */

			// show thanks openDialog
			openDialog(android.R.drawable.ic_dialog_info, R.string.donations__thanks_dialog_title,
					getString(R.string.donations__thanks_dialog));
		}
	}

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener	mConsumeFinishedListener	= new IabHelper.OnConsumeFinishedListener() {
																		public void onConsumeFinished(
																				Purchase purchase, IabResult result)
																		{
																			if (mDebug)
																				Log.d(TAG,
																						"Consumption finished. Purchase: "
																								+ purchase
																								+ ", result: " + result);

																			// if
																			// we
																			// were
																			// disposed
																			// of
																			// in
																			// the
																			// meantime,
																			// quit.
																			if (mHelper == null)
																				return;

																			if (result.isSuccess())
																			{
																				if (mDebug)
																					Log.d(TAG,
																							"Consumption successful. Provisioning.");
																			}
																			if (mDebug)
																				Log.d(TAG, "End consumption flow.");
																		}
																	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (mDebug)
			Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
		if (mHelper == null)
			return;

		// Pass on the fragment result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data))
		{
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		}
		else
		{
			if (mDebug)
				Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

}
