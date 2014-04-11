package com.abewy.android.apps.klyph.app;

import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import com.abewy.android.apps.klyph.R;
import com.inscription.ChangeLogDialog;

public class ChangeLogActivity extends TitledActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setTitle(R.string.changelog_activity_title);
		
		WebView webView = (WebView) findViewById(R.id.webView);
		
		ChangeLogDialog cld = new ChangeLogDialog(this);
		webView.loadDataWithBaseURL(null, cld.getHTML(), "text/html", "utf-8", null);
	}

	@Override
	public boolean  onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_changelog;
	}
}
