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
	Integer zoomLevel[] = { 14, 13, 12, 11, 10, 9 }; /*
													 * assume zoom levels are 5,
													 * 10, 20, 40, 80, 160
													 */
	List<Overlay> mapOverlays;
	Drawable drawableGreen, drawableRed, drawableOrange;
	GeoOverlay itemizedOverlay;
	MapView mapview;
	int latSpan = 58908;
	int lonSpan = 51498;
	static final double LAT_STADIUM = 37.75 * 1E6;
	static final double LON_STADIUM = -122.20 * 1E6;
	List<MyGeoPoint> zones = new ArrayList<MyGeoPoint>();
	MyGeoPoint zoneObj0, zoneObj1, zoneObj2, zoneObj3, zoneObj4, zoneObj5, zoneObj6, zoneObj7, zoneObj8;

	private MyGeoPoint createGeoPoint(double lat, double lon) {
		MyGeoPoint zoneObj = new MyGeoPoint();
		zoneObj.Lat = lat;
		zoneObj.Lon = lon;
		return zoneObj;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		/* create zone objects */
		zoneObj0 = new MyGeoPoint();
		zoneObj0.Lat = LAT_STADIUM + latSpan / 3;
		zoneObj0.Lon = LON_STADIUM - lonSpan / 3; /* upper left */
		zones.add(zoneObj0);

		zoneObj1 = new MyGeoPoint();
		zoneObj1.Lat = LAT_STADIUM + latSpan / 3;
		zoneObj1.Lon = LON_STADIUM; /* upper middle */
		zones.add(zoneObj1);

		zoneObj2 = new MyGeoPoint();
		zoneObj2.Lat = LAT_STADIUM + latSpan / 3;
		zoneObj2.Lon = LON_STADIUM + lonSpan / 3; /* upper right */
		zones.add(zoneObj2);

		zoneObj3 = new MyGeoPoint();
		zoneObj3.Lat = LAT_STADIUM;
		zoneObj3.Lon = LON_STADIUM - lonSpan / 3; /* left middle */
		zones.add(zoneObj3);

		zoneObj4 = new MyGeoPoint();
		zoneObj4.Lat = LAT_STADIUM; /* middle */
		zoneObj4.Lon = LON_STADIUM;

		zones.add(zoneObj4);

		zoneObj5 = new MyGeoPoint();
		zoneObj5.Lat = LAT_STADIUM;
		zoneObj5.Lon = LON_STADIUM + lonSpan / 3; /* right middle */
		zones.add(zoneObj5);

		zoneObj6 = new MyGeoPoint();
		zoneObj6.Lat = LAT_STADIUM - latSpan / 3;
		zoneObj6.Lon = LON_STADIUM - lonSpan / 3; /* left bottom */
		zones.add(zoneObj6);

		zoneObj7 = new MyGeoPoint();
		zoneObj7.Lat = LAT_STADIUM - latSpan / 3;
		zoneObj7.Lon = LON_STADIUM; /* middle bottom */
		zones.add(zoneObj7);

		zoneObj8 = new MyGeoPoint();
		zoneObj8.Lat = LAT_STADIUM - latSpan / 3;
		zoneObj8.Lon = LON_STADIUM + lonSpan / 3; /* right bottom */
		zones.add(zoneObj8);

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
			latD = LAT_STADIUM;
			lonD = LON_STADIUM;
		}

		Log.d("MAPIT", "latD = " + latD + "longD =" + lonD);
		Integer lat = latD.intValue();
		Integer lon = lonD.intValue();
		ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		GeoOverlay itemizedOverlay = null;
		GeoPoint geopoint = new GeoPoint(lat, lon);

		mapview = (MapView) findViewById(R.id.mapv);

		mapOverlays = mapview.getOverlays();
		drawableGreen = this.getResources().getDrawable(R.drawable.green1);
		drawableRed = this.getResources().getDrawable(R.drawable.red1);
		drawableOrange = this.getResources().getDrawable(R.drawable.orange1);

		/*
		 * GeoPoint point = new GeoPoint(19240000,-99120000);
		 */

		Log.d("MAPIT", "Latitude = " + zoneObj8.Lat.intValue() + "Longitude = " + zoneObj8.Lon.intValue());
		GeoPoint point = new GeoPoint(zoneObj8.Lat.intValue(), zoneObj8.Lon.intValue());

		int[] safetyRatings = CrimeHelper.getQuadrantRatings(LAT_STADIUM / 1E6, LON_STADIUM / 1E6, lonSpan / 1E6, latSpan / 1E6);
		Log.d("MAPIT", "Safety ratings = " + safetyRatings[0] + safetyRatings[1] + safetyRatings[2] + safetyRatings[3]
				+ safetyRatings[4] + safetyRatings[5] + safetyRatings[6] + safetyRatings[7] + safetyRatings[8]);

		for (int i = 0; i <= 8; i++) {
			GeoPoint point1 = new GeoPoint(zones.get(i).Lat.intValue(), zones.get(i).Lon.intValue());

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
		Double Lat;
		Double Lon;

	}
}
