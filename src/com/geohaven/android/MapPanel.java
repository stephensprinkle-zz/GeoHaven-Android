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
	GeoOverlay itemizedOverlay;
	MapView mapview;
	int latSpan = 58908;
	int lonSpan = 51498;
	static final double LAT_STADIUM = 37.75;
	static final double LON_STADIUM = -122.20;
	List<MyGeoPoint> zones = new ArrayList<MyGeoPoint>();
	
	Drawable drawableGreen, drawableOrange, drawableRed;
	GeoOverlay itemizedOverlaySafe, itemizedOverlayMedium, itemizedOverlayDangerous;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		drawableGreen = this.getResources().getDrawable(R.drawable.green1);
		drawableOrange = this.getResources().getDrawable(R.drawable.orange1);
		drawableRed = this.getResources().getDrawable(R.drawable.red1);
		
		mapview = (MapView) findViewById(R.id.mapv);
		mapOverlays = mapview.getOverlays();

		itemizedOverlaySafe = new GeoOverlay(drawableGreen, this);
		itemizedOverlayMedium = new GeoOverlay(drawableOrange, this);
		itemizedOverlayDangerous = new GeoOverlay(drawableRed, this);
		mapOverlays.add(itemizedOverlaySafe);
		mapOverlays.add(itemizedOverlayMedium);
		mapOverlays.add(itemizedOverlayDangerous);

		populateZones(new MyGeoPoint(LAT_STADIUM, LON_STADIUM), latSpan, lonSpan);

	}

	private void populateZones(MyGeoPoint center, int latSpan, int lonSpan) {
		double cLat = center.getLat();
		double cLon = center.getLon();
		double boxLat = latSpan/1e6 / 3;
		double boxLon = lonSpan/1e6 / 3;
		
		zones.clear();
		zones.add(new MyGeoPoint(cLat + boxLat, cLon - boxLon));	// upper left
		zones.add(new MyGeoPoint(cLat + boxLat, cLon));				// upper middle
		zones.add(new MyGeoPoint(cLat + boxLat, cLon + boxLon));	// upper right
		zones.add(new MyGeoPoint(cLat, cLon - boxLon));				// left middle
		zones.add(new MyGeoPoint(cLat, cLon));						// middle
		zones.add(new MyGeoPoint(cLat, cLon + boxLon));				// right middle
		zones.add(new MyGeoPoint(cLat - boxLat, cLon - boxLon));	// left bottom
		zones.add(new MyGeoPoint(cLat - boxLat, cLon));				// middle bottom
		zones.add(new MyGeoPoint(cLat - boxLat, cLon + boxLon));	// right bottom
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

		GeoPoint geopoint = new GeoPoint(lat, lon);

		int[] safetyRatings = CrimeHelper.getQuadrantRatings(LAT_STADIUM, LON_STADIUM, lonSpan / 1E6, latSpan / 1E6);
		Log.d("MAPIT", "Safety ratings = " + safetyRatings[0] + safetyRatings[1] + safetyRatings[2] + safetyRatings[3]
				+ safetyRatings[4] + safetyRatings[5] + safetyRatings[6] + safetyRatings[7] + safetyRatings[8]);

		for (int i = 0; i <= 8; i++) {
			GeoPoint point1 = new GeoPoint(zones.get(i).getGLat(), zones.get(i).getGLon());

			OverlayItem overlayitem = new OverlayItem(point1, "Area " + i, "Yayaya");

			if (safetyRatings[i] == 1) {
				itemizedOverlaySafe.addOverlay(overlayitem);
			}

			if (safetyRatings[i] == 2) {
				itemizedOverlayMedium.addOverlay(overlayitem);
			}

			if (safetyRatings[i] == 3) {
				itemizedOverlayDangerous.addOverlay(overlayitem);
			}
		}

		Log.d("Mapit", "lat = " + lat + "long = " + lon);
		MapController mapController = mapview.getController();
		mapController.setCenter(geopoint);

		mapController.setZoom(14);
		mapview.setBuiltInZoomControls(true);
	}

	private class MyGeoPoint {
		private double lat;
		private double lon;

		public MyGeoPoint(double lat, double lon) {
			this.lat = lat;
			this.lon = lon;
		}

		public MyGeoPoint(int lat, int lon) {
			this.lat = lat / 1e6;
			this.lon = lon / 1e6;
		}

		public double getLat() {
			return lat;
		}

		public double getLon() {
			return lon;
		}
		
		public int getGLat() {
			return (int)(lat * 1e6);
		}

		public int getGLon() {
			return (int)(lon * 1e6);
		}
	}
}
