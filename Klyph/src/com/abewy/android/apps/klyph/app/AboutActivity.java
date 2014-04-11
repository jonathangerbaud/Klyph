package com.abewy.android.apps.klyph.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.abewy.android.apps.klyph.KlyphPreferences;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.core.KlyphFlags;
import com.abewy.util.ApplicationUtil;
import com.abewy.util.PhoneUtil;

public class AboutActivity extends TitledActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);

		setTitle(R.string.about_activity_title);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_background_transparent_gradient));

		ImageView appLogo = (ImageView) findViewById(R.id.app_logo);
		appLogo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(AboutActivity.this);
				alert.setTitle(R.string.donate_unlock);
				alert.setMessage(R.string.donate_code);

				final EditText editText = new EditText(AboutActivity.this);
				alert.setView(editText);

				alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						if (AboutActivity.this.getString(R.string.donation_validation_code).equals(editText.getText().toString()))
						{
							handleResult();
						}
					}
				});

				alert.show();

			}
		});

		ImageView companyLogo = (ImageView) findViewById(R.id.company_logo);

		companyLogo.setClickable(true);
		companyLogo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				PhoneUtil.openURL(AboutActivity.this, getString(R.string.company_url));
			}
		});

		TextView version = (TextView) findViewById(R.id.version);
		TextView appName = (TextView) findViewById(R.id.app_name);

		appName.setText(KlyphFlags.IS_PRO_VERSION == true ? R.string.app_pro_large_name : R.string.app_large_name);
		version.setText(getString(R.string.about_version, ApplicationUtil.getAppVersion(this)));
	}

	private void handleResult()
	{
		KlyphPreferences.setUserDonated(true);

		// show thanks openDialog
		openDialog(android.R.drawable.ic_dialog_info, R.string.donations__thanks_dialog_title, getString(R.string.donations__thanks_dialog));
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
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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

	@Override
	public boolean  onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_about;
	}
	
	@Override
	protected int getCustomTheme()
	{
		return KlyphPreferences.getProfileTheme();
	}
}
