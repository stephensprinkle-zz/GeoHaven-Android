package com.geohaven.android;

import java.io.IOException;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class GeoHavenMain extends TabActivity{	
	
	private LocationManager locationManager;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	
	    try {
			CrimeHelper.init(getAssets().open("crimedata.tsv"));
		} catch (IOException e) {
			Log.e("DATA_OPEN", "couldn't open it", e);
		}
		initGPS();
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
		
		//Log.d("HOME", "coarse provider = " +coarseProvider);    
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
	    
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, Home.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Home").setIndicator("Home",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, Map.class);
	    spec = tabHost.newTabSpec("Map").setIndicator("Map",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, Preferences.class);
	    spec = tabHost.newTabSpec("Settings").setIndicator("Settings",
	                      res.getDrawable(R.drawable.ic_tab_artists))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}
	
}
