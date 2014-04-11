package com.abewy.android.apps.klyph.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.abewy.android.apps.klyph.R;

public class FaqActivity extends TitledActivity
{
	private static final String	FAQ_URL	= "http://www.abewy.com/apps/klyph/klyph_faq.html";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);

		setTitle(R.string.menu_faq);

		WebView webView = (WebView) findViewById(R.id.webview);

		webView.getSettings().setJavaScriptEnabled(true);

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress)
			{
				// Activities and WebViews measure progress with different scales.
				// The progress meter will automatically disappear when we reach 100%
				setProgress(progress * 1000);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
			{
				Toast.makeText(FaqActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
		});

		webView.loadUrl(FAQ_URL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_faq;
	}
}
