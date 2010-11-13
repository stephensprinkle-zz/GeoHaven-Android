package com.geohaven.android;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Home extends Activity {
	private Button preferencesButton;
	private Button mapButton;
	Intent mapintent;
	private LocationManager locationManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
        mapintent = new Intent (this, Mapit.class);
		
		initGPS();
		preferencesButton = (Button) findViewById(R.id.preferencesButton);
		preferencesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent preferencesActivity = new Intent(getBaseContext(), Preferences.class);
				startActivity(preferencesActivity);
			}
		});

		mapButton = (Button) findViewById(R.id.mapButton);
		mapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				startActivity(mapintent);
			}
		});

	}
	 /* GPS routines */
	public void runLocationUpdate(Location location) {
		
		Globals.lastGeoLat = location.getLatitude();
		Globals.lastGeoLong = location.getLongitude();	
		Log.d("HOME", "reached run location update, latitude = " +location.getLatitude() +"longitude = " +location.getLongitude());
        Double lastLat = location.getLatitude();
        Globals.lastLat = lastLat.intValue();
        Double lastLong = location.getLongitude();
		Globals.lastLong = lastLong.intValue();
		
		
	}

    private LocationListener lbounce = new LocationListener() {
		public void onProviderDisabled(String s) {
		}

		public void onProviderEnabled(String s) {
		}

		public void onStatusChanged(String s, int i, Bundle b) {
		}

		public void onLocationChanged(Location location) {
			
			runLocationUpdate(location);
			if (location.getAccuracy() < 100)

			{
				locationManager.removeUpdates(lbounce);
				locationManager.removeUpdates(lcoarse);

			}
		}
	};
	private LocationListener lcoarse = new LocationListener() {
		public void onProviderDisabled(String s) {
		}

		public void onProviderEnabled(String s) {
		}

		public void onStatusChanged(String s, int i, Bundle b) {
		}

		public void onLocationChanged(Location location) {
			
			runLocationUpdate(location);
			
			locationManager.removeUpdates(lcoarse);           /*make coarse update a one-shot for tweetbeat only */

		}
	};
	private LocationListener lbest = new LocationListener()

	{
		public void onProviderDisabled(String s) {
		}

		public void onProviderEnabled(String s) {
		}

		public void onStatusChanged(String s, int i, Bundle b) {
		}

		public void onLocationChanged(Location location) {

			runLocationUpdate(location);

			if (location.getAccuracy() < 10)

			{
				locationManager.removeUpdates(lbounce);
				locationManager.removeUpdates(lcoarse);

			}
		}
	};
	private void initGPS() {
		
		Criteria coarseCriteria = new Criteria();
		coarseCriteria.setAltitudeRequired(false);
		coarseCriteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		coarseCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
		coarseCriteria.setBearingRequired(false);
		coarseCriteria.setSpeedRequired(false);
		coarseCriteria.setCostAllowed(false);
		int freq = 1 * 60000;  /* 5 minute update frequency */
		int distance = 3000;   /* 3000 meter update frequency */

		this.locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
	
		
		String coarseProvider = locationManager.getBestProvider(coarseCriteria,
				true);
		
		Log.d("HOME", "coarse provider = " +coarseProvider);    
		if (coarseProvider != null)
		    locationManager.requestLocationUpdates(coarseProvider, 0, 0, lcoarse);
		Criteria bestCriteria = new Criteria();
		bestCriteria.setAltitudeRequired(false);
		bestCriteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		bestCriteria.setAccuracy(Criteria.ACCURACY_FINE);
		bestCriteria.setBearingRequired(false);
		bestCriteria.setSpeedRequired(false);
		bestCriteria.setCostAllowed(false);
		String bestProvider = locationManager.getBestProvider(bestCriteria,
				true);
		if (bestProvider != null)
		    {
		    locationManager.requestLocationUpdates(bestProvider, 0, 0, lbounce);
		    locationManager.requestLocationUpdates(bestProvider, freq, distance,
					lbest);
		    }
		/*
		runLocationUpdate(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        */
	}
}