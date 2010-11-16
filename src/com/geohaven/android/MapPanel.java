package com.geohaven.android;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Overlay;

public class MapPanel extends MapActivity {
	Double latD, lonD;
	Integer zoomLevel[] = { 14, 13, 12, 11, 10, 9 };
	/*
	 * assume zoom levels are 5, 10, 20, 40, 80, 160
	 */
	List<Overlay> mapOverlays;
	Drawable drawableGreen, drawableRed, drawableOrange;
	GeoOverlay itemizedOverlay;
	MapView mapview;
	int latSpan = 58908;
	int lonSpan = 51498;
	static final double LAT_STADIUM = 37.75;
	static final double LON_STADIUM = -122.20;
	List<MyGeoPoint> zones = new ArrayList<MyGeoPoint>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		/* create zone objects */
		/* upper left */
		zones.add(new MyGeoPoint(LAT_STADIUM + latSpan/1e6 / 3, LON_STADIUM - lonSpan/1e6 / 3));
		/* upper middle */
		zones.add(new MyGeoPoint(LAT_STADIUM + latSpan/1e6 / 3, LON_STADIUM));
		/* upper right */
		zones.add(new MyGeoPoint(LAT_STADIUM + latSpan/1e6 / 3, LON_STADIUM + lonSpan/1e6 / 3));
		/* left middle */
		zones.add(new MyGeoPoint(LAT_STADIUM, LON_STADIUM - lonSpan/1e6 / 3));
		/* middle */
		zones.add(new MyGeoPoint(LAT_STADIUM, LON_STADIUM));
		/* right middle */
		zones.add(new MyGeoPoint(LAT_STADIUM, LON_STADIUM + lonSpan/1e6 / 3));
		/* left bottom */
		zones.add(new MyGeoPoint(LAT_STADIUM - latSpan/1e6 / 3, LON_STADIUM - lonSpan/1e6 / 3));
		/* middle bottom */
		zones.add(new MyGeoPoint(LAT_STADIUM - latSpan/1e6 / 3, LON_STADIUM));
		/* right bottom */
		zones.add(new MyGeoPoint(LAT_STADIUM - latSpan/1e6 / 3, LON_STADIUM + lonSpan/1e6 / 3));

		displayLocation();
		fillZones();
	}

	@Override
	public void onResume() {
		super.onResume();
		displayLocation();

		Log.d("MAPIT", "Latitude Span = " + mapview.getLatitudeSpan() + "Longitude Span = " + mapview.getLongitudeSpan());
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void displayLocation() {

		{
			/*
			 * hardcode lat/long latD = Globals.lastGeoLat * 1E6; lonD =
			 * Globals.lastGeoLong * 1E6;
			 */
			latD = LAT_STADIUM * 1e6;
			lonD = LON_STADIUM * 1e6;
		}

		Log.d("MAPIT", "latD = " + latD + "longD =" + lonD);
		Integer lat = latD.intValue();
		Integer lon = lonD.intValue();
		GeoOverlay itemizedOverlay = null;
		GeoPoint geopoint = new GeoPoint(lat, lon);

		mapview = (MapView) findViewById(R.id.mapv);

		mapOverlays = mapview.getOverlays();
		drawableGreen = this.getResources().getDrawable(R.drawable.green1);
		drawableRed = this.getResources().getDrawable(R.drawable.red1);
		drawableOrange = this.getResources().getDrawable(R.drawable.orange1);

		int[] safetyRatings = CrimeHelper.getQuadrantRatings(LAT_STADIUM, LON_STADIUM, lonSpan / 1E6, latSpan / 1E6);
		Log.d("MAPIT", "Safety ratings = " + safetyRatings[0] + safetyRatings[1] + safetyRatings[2] + safetyRatings[3]
				+ safetyRatings[4] + safetyRatings[5] + safetyRatings[6] + safetyRatings[7] + safetyRatings[8]);

		for (int i = 0; i <= 8; i++) {
			GeoPoint point1 = new GeoPoint(zones.get(i).getLat(), zones.get(i).getLon());

			OverlayItem overlayitem = new OverlayItem(point1, "", "");

			if (safetyRatings[i] == 1) {
				itemizedOverlay = new GeoOverlay(drawableGreen, this);
			}

			if (safetyRatings[i] == 2) {
				itemizedOverlay = new GeoOverlay(drawableOrange, this);
			}

			if (safetyRatings[i] == 3) {
				itemizedOverlay = new GeoOverlay(drawableRed, this);
			}

			itemizedOverlay.addOverlay(overlayitem);
			mapOverlays.add((Overlay) itemizedOverlay);
		}

		Log.d("Mapit", "lat = " + lat + "long = " + lon);
		MapController mapController = mapview.getController();
		mapController.setCenter(geopoint);

		mapController.setZoom(14);
		mapview.setBuiltInZoomControls(true);
	}

	private void fillZones() {

	}

	private class MyGeoPoint {
		private int lat;
		private int lon;

		public MyGeoPoint(double lat, double lon) {
			this.lat = (int) (lat * 1e6);
			this.lon = (int) (lon * 1e6);
		}

		public int getLat() {
			return lat;
		}

		public int getLon() {
			return lon;
		}
	}
}
