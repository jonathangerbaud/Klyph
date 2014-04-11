/*
 * Copyright 2010 Facebook, Inc.
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

package com.abewy.android.apps.klyph.util.facebook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.abewy.android.apps.klyph.R;

public class FbDialog extends Dialog
{

	public static final String				REDIRECT_URI				= "fbconnect://success";
	public static final String				CANCEL_URI					= "fbconnect://cancel";
	public static String					DIALOG_BASE_URL				= "https://facebook.com/dialog/";
	public static final String				TOKEN						= "access_token";
	static final int						FB_BLUE						= 0xFF6D84B4;
	static final float[]					DIMENSIONS_DIFF_LANDSCAPE	= { 20, 60 };
	static final float[]					DIMENSIONS_DIFF_PORTRAIT	= { 40, 60 };
	static final FrameLayout.LayoutParams	FILL						= new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
																				ViewGroup.LayoutParams.FILL_PARENT);
	static final int						MARGIN						= 4;
	static final int						PADDING						= 2;
	static final String						DISPLAY_STRING				= "touch";
	static final String						FB_ICON						= "icon.png";

	private String							mUrl;
	private DialogListener					mListener;
	private ProgressDialog					mSpinner;
	private ImageView						mCrossImage;
	private WebView							mWebView;
	private FrameLayout						mContent;

	/**
	 * Callback interface for dialog requests.
	 * 
	 */
	public static interface DialogListener
	{

		/**
		 * Called when a dialog completes.
		 * 
		 * Executed by the thread that initiated the dialog.
		 * 
		 * @param values
		 *            Key-value string pairs extracted from the response.
		 */
		public void onComplete(Bundle values);

		/**
		 * Called when a Facebook responds to a dialog with an error.
		 * 
		 * Executed by the thread that initiated the dialog.
		 * 
		 */
		public void onFacebookError(FacebookError e);

		/**
		 * Called when a dialog has an error.
		 * 
		 * Executed by the thread that initiated the dialog.
		 * 
		 */
		public void onError(DialogError e);

		/**
		 * Called when a dialog is canceled by the user.
		 * 
		 * Executed by the thread that initiated the dialog.
		 * 
		 */
		public void onCancel();

	}

	public FbDialog(Context context, String url, DialogListener listener)
	{
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mUrl = url;
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mSpinner = new ProgressDialog(getContext());
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading...");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContent = new FrameLayout(getContext());

		/*
		 * Create the 'x' image, but don't add to the mContent layout yet
		 * at this point, we only need to know its drawable width and height
		 * to place the webview
		 */
		createCrossImage();

		/*
		 * Now we know 'x' drawable width and height,
		 * layout the webivew and add it the mContent layout
		 */
		int crossWidth = mCrossImage.getDrawable().getIntrinsicWidth();
		setUpWebView(crossWidth / 2);

		/*
		 * Finally add the 'x' image to the mContent layout and
		 * add mContent to the Dialog view
		 */
		mContent.addView(mCrossImage, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addContentView(mContent, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	private void createCrossImage()
	{
		mCrossImage = new ImageView(getContext());
		// Dismiss the dialog when user click on the 'x'
		mCrossImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				mListener.onCancel();
				FbDialog.this.dismiss();
			}
		});
		Drawable crossDrawable = getContext().getResources().getDrawable(R.drawable.close);
		mCrossImage.setImageDrawable(crossDrawable);
		/*
		 * 'x' should not be visible while webview is loading
		 * make it visible only after webview has fully loaded
		 */
		mCrossImage.setVisibility(View.INVISIBLE);
	}

	private void setUpWebView(int margin)
	{
		LinearLayout webViewContainer = new LinearLayout(getContext());
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new FbDialog.FbWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_7; en-us) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Safari/530.17");
		mWebView.loadUrl(mUrl);
		mWebView.setLayoutParams(FILL);
		mWebView.setVisibility(View.INVISIBLE);
		mWebView.getSettings().setSavePassword(false);

		webViewContainer.setPadding(margin, margin, margin, margin);
		webViewContainer.addView(mWebView);
		mContent.addView(webViewContainer);
	}

	private class FbWebViewClient extends WebViewClient
	{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			// Util.logd("Facebook-WebView", "Redirect URL: " + url);
			if (url.startsWith(REDIRECT_URI))
			{
				Bundle values = Util.parseUrl(url);

				String error = values.getString("error");
				if (error == null)
				{
					error = values.getString("error_type");
				}

				if (error == null)
				{
					mListener.onComplete(values);
				}
				else if (error.equals("access_denied") || error.equals("OAuthAccessDeniedException"))
				{
					mListener.onCancel();
				}
				else
				{
					mListener.onFacebookError(new FacebookError(error));
				}

				FbDialog.this.dismiss();
				return true;
			}
			else if (url.startsWith(CANCEL_URI))
			{
				mListener.onCancel();
				FbDialog.this.dismiss();
				return true;
			}
			else if (url.contains(DISPLAY_STRING))
			{
				return false;
			}
			return false;
			// launch non-dialog URLs in a full browser
			//getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			//return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
		{
			Log.d("FbDialog.FbWebViewClient", "onReceivedError: " + errorCode + failingUrl + description);
			super.onReceivedError(view, errorCode, description, failingUrl);
			mListener.onError(new DialogError(description, errorCode, failingUrl));
			FbDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			// Util.logd("Facebook-WebView", "Webview loading URL: " + url);
			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			mSpinner.dismiss();
			/*
			 * Once webview is fully loaded, set the mContent background to be transparent
			 * and make visible the 'x' image.
			 */
			mContent.setBackgroundColor(Color.TRANSPARENT);
			mWebView.setVisibility(View.VISIBLE);
			mCrossImage.setVisibility(View.VISIBLE);
		}
	}
}
