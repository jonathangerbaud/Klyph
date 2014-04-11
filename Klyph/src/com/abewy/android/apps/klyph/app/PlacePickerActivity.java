package com.abewy.android.apps.klyph.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import com.abewy.android.apps.klyph.KlyphBundleExtras;
import com.abewy.android.apps.klyph.R;
import com.facebook.FacebookException;
import com.facebook.model.GraphPlace;
import com.facebook.widget.KlyphPlacePickerFragment;
import com.facebook.widget.PickerFragment;

public class PlacePickerActivity extends TitledFragmentActivity
{
	private KlyphPlacePickerFragment	placePickerFragment;
	private LocationListener			locationListener;
	private boolean destroyed = false;
	private static final Location		SAN_FRANCISCO_LOCATION		= new Location("") {
																		{
																			setLatitude(37.7750);
																			setLongitude(-122.4183);
																		}
																	};
	private static final int			SEARCH_RADIUS_METERS		= 30000;
	private static final int			SEARCH_RESULT_LIMIT			= 50;
	private static final String			SEARCH_TEXT					= "restaurant";
	private static final int			LOCATION_CHANGE_THRESHOLD	= 50;			// meters

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		destroyed = false;
		
		setTitle(R.string.choose_place);

		placePickerFragment = (KlyphPlacePickerFragment) getSupportFragmentManager().findFragmentById(
				R.id.place_picker_fragment);

		placePickerFragment.setOnSelectionChangedListener(new PickerFragment.OnSelectionChangedListener() {
			@Override
			public void onSelectionChanged(PickerFragment<?> fragment)
			{
				GraphPlace place = placePickerFragment.getSelection();
				
				if (place != null)
				{
					Intent intent = new Intent();
					intent.putExtra(KlyphBundleExtras.PLACE_ID, place.getId());
					intent.putExtra(KlyphBundleExtras.PLACE_NAME, place.getName());

					setResult(RESULT_OK, intent);

					finish();
				}
			}
		});
		placePickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
			@Override
			public void onError(PickerFragment<?> fragment, FacebookException error)
			{
				PlacePickerActivity.this.onError(error);
			}
		});

		placePickerFragment.setShowTitleBar(false);

	}

	@Override
	protected void onStart()
	{
		super.onStart();

		try
		{
			Location location = null;
			// Instantiate the default criteria for a location provider
			Criteria criteria = new Criteria();
			// Get a location manager from the system services
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// Get the location provider that best matches the criteria
			String bestProvider = locationManager.getBestProvider(criteria, false);

			if (bestProvider != null)
			{
				// Get the user's last known location
				location = locationManager.getLastKnownLocation(bestProvider);
				if (locationListener == null)
				{
					// Set up a location listener if one is not already set
					// up
					// and the selected provider is enabled
					locationListener = new LocationListener() {
						@Override
						public void onLocationChanged(Location location)
						{
							// On location updates, compare the current
							// location to the desired location set in the
							// place picker
							float distance = location.distanceTo(placePickerFragment.getLocation());
							if (distance >= LOCATION_CHANGE_THRESHOLD)
							{
								placePickerFragment.setLocation(location);
								placePickerFragment.setRadiusInMeters(SEARCH_RADIUS_METERS);
								placePickerFragment.setSearchText(SEARCH_TEXT);
								placePickerFragment.setResultsLimit(SEARCH_RESULT_LIMIT);
								placePickerFragment.loadData(true);
							}
						}

						@Override
						public void onStatusChanged(String s, int i, Bundle bundle)
						{}

						@Override
						public void onProviderEnabled(String s)
						{}

						@Override
						public void onProviderDisabled(String s)
						{}
					};

					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1,
							LOCATION_CHANGE_THRESHOLD, locationListener);
				}
			}
			if (location == null)
			{
				// Todo : set default location the last saved location
				location = SAN_FRANCISCO_LOCATION;
			}

			if (location != null)
			{
				// Configure the place picker: search center, radius,
				// query, and maximum results.
				placePickerFragment.setLocation(location);
				placePickerFragment.setRadiusInMeters(SEARCH_RADIUS_METERS);
				placePickerFragment.setSearchText(SEARCH_TEXT);
				placePickerFragment.setResultsLimit(SEARCH_RESULT_LIMIT);
				// Start the API call
				placePickerFragment.loadData(true);
			}
			/*
			 * else { // If no location found, show an error
			 * onError(getResources().getString(R.string.no_location_error),
			 * true); }
			 */
		}
		catch (Exception ex)
		{
			onError(ex);
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		if (locationListener != null)
		{
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.removeUpdates(locationListener);
			locationListener = null;
		}
	}

	@Override
	public boolean  onCreateOptionsMenu(Menu menu)
	{
		return false;
	}

	private void onError(Exception error)
	{
		onError(error.getLocalizedMessage(), false);
	}

	private void onError(String error, final boolean  finishActivity)
	{
		if (!destroyed)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.no_location_error).setMessage(error)
					.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i)
						{
							if (finishActivity)
							{
								setResult(RESULT_CANCELED);
								finish();
							}
						}
					});
			builder.show();	
		}
	}

	@Override
	protected int getLayout()
	{
		return R.layout.activity_place_picker;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		destroyed = true;
		placePickerFragment = null;
		locationListener = null;
	}
}
