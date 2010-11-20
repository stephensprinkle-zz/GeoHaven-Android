package com.geohaven.android;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapPanel extends MapActivity {
	private static final String LOG_TAG = "MAPPANEL";
	private static final double LAT_STADIUM = 37.75;
	private static final double LON_STADIUM = -122.20;

	private List<Overlay> mapOverlays;
	private EnhancedMapView mapview;
	private int latSpan = 58908;
	private int lonSpan = 51498;
	private MyGeoPoint mapCenter = new MyGeoPoint(LAT_STADIUM, LON_STADIUM);
	private MyGeoArea visibleArea = new MyGeoArea(LAT_STADIUM, LON_STADIUM, latSpan, lonSpan);

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
				visibleArea = new MyGeoArea(mapCenter, latSpan, lonSpan);
				calculateOverlays();
			}
		});
		mapview.setOnPanChangeListener(new EnhancedMapView.OnPanChangeListener() {
			public void onPanChange(MapView view, GeoPoint newCenter, GeoPoint oldCenter) {
				Log.d(LOG_TAG, "center changed from " + oldCenter.getLatitudeE6() + "," + oldCenter.getLongitudeE6() + " to "
						+ newCenter.getLatitudeE6() + "," + newCenter.getLongitudeE6());
				Log.d(LOG_TAG, "Latitude Span = " + mapview.getLatitudeSpan() + "Longitude Span = " + mapview.getLongitudeSpan());				
				mapCenter = new MyGeoPoint(newCenter.getLatitudeE6(), newCenter.getLongitudeE6());
				visibleArea = new MyGeoArea(mapCenter, latSpan, lonSpan);
				calculateOverlays();
			}
		});

		itemizedOverlaySafe = new GeoOverlay(drawableGreen, this);
		itemizedOverlayMedium = new GeoOverlay(drawableOrange, this);
		itemizedOverlayDangerous = new GeoOverlay(drawableRed, this);

		mapOverlays = mapview.getOverlays();
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
		
		Log.w(LOG_TAG, "calculateOverlays: " + visibleArea);
		AreaInfo[] ais = CrimeHelper.getAreaInfos(Crime.Category.WALKING, 3, visibleArea);

		for (AreaInfo ai : ais) {
			OverlayItem overlayitem = new OverlayItem(ai.getArea().getCenter().asGeoPoint(), "Area " + ai.getArea(), ai.toString());
			
			switch (ai.getNormalizedRating()) {
			case 1:
				itemizedOverlaySafe.addOverlay(overlayitem);				
				break;
			case 2:
				itemizedOverlayMedium.addOverlay(overlayitem);				
				break;
			case 3:
				itemizedOverlayDangerous.addOverlay(overlayitem);				
				break;
			default:
				Log.w(LOG_TAG, "Wha?  Got a normalized rating of: " + ai.getNormalizedRating() + " for item " + ai);
				break;
			}
		}
		
		mapOverlays.clear();
		if (itemizedOverlaySafe.size() > 0) mapOverlays.add(itemizedOverlaySafe);
		if (itemizedOverlayMedium.size() > 0) mapOverlays.add(itemizedOverlayMedium);
		if (itemizedOverlayDangerous.size() > 0) mapOverlays.add(itemizedOverlayDangerous);
	}

}
