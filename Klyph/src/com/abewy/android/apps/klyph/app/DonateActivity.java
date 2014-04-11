package com.abewy.android.apps.klyph.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import com.abewy.android.apps.klyph.R;
import com.abewy.android.apps.klyph.fragment.DonateFragment;

public class DonateActivity extends TitledFragmentActivity
{
	private static final String		GOOGLE_PUBKEY	= "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiw4aXYdQt4oP0dyMRw1VKp4b5DtKctkFNea9NSEex1Cw6KyFJ6ZkACf0XMOWJXzaNV8eVWobUh5Q1Yw0M3yfJSanRfF9RZQxtMfbRKNYbx6CysyNUNB5M+Hb3U1vcLqI5o74hTrvB+8Zfal/D4+c+9HOv6mrRrD0Mal4XkPqdjkUzrpXpHMyBtxt+FKvOHK6NEZ+9Ccm1+IvYSNtqskDPVN09nlj67kjCqIVfLPaZSNueIVZ9jQF0J4NfkhBzLzPCE6ySZLsV5fEDwcj1oqbXZntXVwbFYPbrIY62eJFIEtiQeRYm1ME6maPrkYyACmmLCD0GNFBwHEmK9tK5se+RwIDAQAB";
	private static final String[]	GOOGLE_CATALOG	= new String[] {
					"klyph.beta.donation.1",
					"klyph.beta.donation.2",
					"klyph.beta.donation.3",
					"klyph.beta.donation.4",
					"klyph.beta.donation.5",
					"klyph.beta.donation.6",
					"klyph.beta.donation.7",
					"klyph.beta.donation.8",
					"klyph.beta.donation.9",
					"klyph.beta.donation.10",
					"klyph.beta.donation.15",
					"klyph.beta.donation.20",
					"klyph.beta.donation.25",
					"klyph.beta.donation.30",
					"klyph.beta.donation.35",
					"klyph.beta.donation.40",
					"klyph.beta.donation.45",
					"klyph.beta.donation.50",
					"klyph.beta.donation.75",
					"klyph.beta.donation.100"		};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setTitle(R.string.donate_activity_title);

		FragmentTransaction ft = getFragmentManager().beginTransaction();

		DonateFragment donationsFragment = DonateFragment.newInstance(false, GOOGLE_PUBKEY, GOOGLE_CATALOG,
				getResources().getStringArray(R.array.donate_google_catalog_values));
		ft.replace(R.id.fragment_container, donationsFragment, "donationsFragment");

		ft.commit();
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_donate;
	}

	/**
	 * Needed for Google Play In-app Billing. It uses
	 * startIntentSenderForResult(). The result is not propagated to the
	 * Fragment like in startActivityForResult(). Thus we need to propagate
	 * manually to our Fragment.
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		FragmentManager fragmentManager = getFragmentManager();
		Fragment fragment = fragmentManager.findFragmentByTag("donationsFragment");
		if (fragment != null)
		{
			((DonateFragment) fragment).onActivityResult(requestCode, resultCode, data);
		}
	}
}
