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
	private static final String LOG_TAG = "MAPPANEL";
	private static final double LAT_STADIUM = 37.75;
	private static final double LON_STADIUM = -122.20;

	private List<Overlay> mapOverlays;
	private EnhancedMapView mapview;
	private int latSpan = 58908;
	private int lonSpan = 51498;
	private MyGeoPoint mapCenter = new MyGeoPoint(LAT_STADIUM, LON_STADIUM);
	private List<MyGeoPoint> zones = new ArrayList<MyGeoPoint>();

	private Drawable drawableGreen, drawableOrange, drawableRed;
	private GeoOverlay itemizedOverlaySafe, itemizedOverlayMedium, itemizedOverlayDangerous;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		drawableGreen = this.getResources().getDrawable(R.drawable.green1);
		drawableOrange = this.getResources().getDrawable(R.drawable.orange1);
		drawableRed = this.getResources().getDrawable(R.drawable.red1);

		mapview = (EnhancedMapView) findViewById(R.id.mapv);
		mapview.setBuiltInZoomControls(true);

		mapview.setOnZoomChangeListener(new EnhancedMapView.OnZoomChangeListener() {
			@Override
			public void onZoomChange(MapView view, int newZoom, int oldZoom) {
				Log.d(LOG_TAG, "zoom changed from " + oldZoom + " to " + newZoom);
				Log.d(LOG_TAG, "Latitude Span = " + mapview.getLatitudeSpan() + "Longitude Span = " + mapview.getLongitudeSpan());
				latSpan = mapview.getLatitudeSpan();
				lonSpan = mapview.getLongitudeSpan();
				populateZones(mapCenter, latSpan, lonSpan);
				calculateOverlays();
			}
		});
		mapview.setOnPanChangeListener(new EnhancedMapView.OnPanChangeListener() {
			public void onPanChange(MapView view, GeoPoint newCenter, GeoPoint oldCenter) {
				Log.d(LOG_TAG, "center changed from " + oldCenter.getLatitudeE6() + "," + oldCenter.getLongitudeE6() + " to "
						+ newCenter.getLatitudeE6() + "," + newCenter.getLongitudeE6());
				Log.d(LOG_TAG, "Latitude Span = " + mapview.getLatitudeSpan() + "Longitude Span = " + mapview.getLongitudeSpan());				
				mapCenter = new MyGeoPoint(newCenter.getLatitudeE6(), newCenter.getLongitudeE6());
				populateZones(mapCenter, latSpan, lonSpan);
				calculateOverlays();
			}
		});

		itemizedOverlaySafe = new GeoOverlay(drawableGreen, this);
		itemizedOverlayMedium = new GeoOverlay(drawableOrange, this);
		itemizedOverlayDangerous = new GeoOverlay(drawableRed, this);

		mapOverlays = mapview.getOverlays();
		mapOverlays.add(itemizedOverlaySafe);
		mapOverlays.add(itemizedOverlayMedium);
		mapOverlays.add(itemizedOverlayDangerous);
	}

	private void populateZones(MyGeoPoint center, int latSpan, int lonSpan) {
		Log.d(LOG_TAG, "LatSpan = " + latSpan + "LonSpan = " + lonSpan);

		double cLat = center.getLat();
		double cLon = center.getLon();
		double boxLat = latSpan / 1e6 / 3;
		double boxLon = lonSpan / 1e6 / 3;

		zones.clear();
		zones.add(new MyGeoPoint(cLat + boxLat, cLon - boxLon)); // upper left
		zones.add(new MyGeoPoint(cLat + boxLat, cLon)); // upper middle
		zones.add(new MyGeoPoint(cLat + boxLat, cLon + boxLon)); // upper right
		zones.add(new MyGeoPoint(cLat, cLon - boxLon)); // left middle
		zones.add(new MyGeoPoint(cLat, cLon)); // middle
		zones.add(new MyGeoPoint(cLat, cLon + boxLon)); // right middle
		zones.add(new MyGeoPoint(cLat - boxLat, cLon - boxLon)); // left bottom
		zones.add(new MyGeoPoint(cLat - boxLat, cLon)); // middle bottom
		zones.add(new MyGeoPoint(cLat - boxLat, cLon + boxLon)); // right bottom
	}

	@Override
	public void onResume() {
		super.onResume();

		MapController mapController = mapview.getController();
		mapController.setCenter(mapCenter.asGeoPoint());

		mapController.setZoom(14);

		Log.d(LOG_TAG, "Latitude Span = " + mapview.getLatitudeSpan() + "Longitude Span = " + mapview.getLongitudeSpan());
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void calculateOverlays() {
		itemizedOverlaySafe.clear();
		itemizedOverlayMedium.clear();
		itemizedOverlayDangerous.clear();
		
		int[] safetyRatings = CrimeHelper.getAreaRatings(mapCenter.getLat(), mapCenter.getLon(), lonSpan / 1E6, latSpan / 1E6);
		Log.d(LOG_TAG, "Safety ratings = " + safetyRatings[0] + safetyRatings[1] + safetyRatings[2] + safetyRatings[3]
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
			return (int) (lat * 1e6);
		}

		public int getGLon() {
			return (int) (lon * 1e6);
		}

		public GeoPoint asGeoPoint() {
			return new GeoPoint(getGLat(), getGLon());
		}
		
		public String toString() {
			return "LAT: " + getGLat() + " LON: " + getGLon();
		}
	}
}
