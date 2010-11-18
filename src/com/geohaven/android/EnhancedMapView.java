package com.geohaven.android;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class EnhancedMapView extends MapView {

//	private static final String LOG_TAG = "MAPVIEW";

	public interface OnZoomChangeListener {
		public void onZoomChange(MapView view, int newZoom, int oldZoom);
	}

	public interface OnPanChangeListener {
		public void onPanChange(MapView view, GeoPoint newCenter, GeoPoint oldCenter);
	}

	private EnhancedMapView _this;

	// Set this variable to your preferred timeout
	private long events_timeout = 500L;
	private boolean touchIsActive = false;
	private GeoPoint last_center_pos;
	private int last_zoom;
	private Timer zoom_event_delay_timer = new Timer();
	private Timer pan_event_delay_timer = new Timer();

	private EnhancedMapView.OnZoomChangeListener zoom_change_listener;
	private EnhancedMapView.OnPanChangeListener pan_change_listener;

	private void init() {
		_this = this;
		last_center_pos = this.getMapCenter();
		last_zoom = this.getZoomLevel();
	}
	
	public EnhancedMapView(Context context, String apiKey) {
		super(context, apiKey);
		init();
	}

	public EnhancedMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EnhancedMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setOnZoomChangeListener(EnhancedMapView.OnZoomChangeListener l) {
		zoom_change_listener = l;
	}

	public void setOnPanChangeListener(EnhancedMapView.OnPanChangeListener l) {
		pan_change_listener = l;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		touchIsActive = ev.getAction() != 1;

		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		super.computeScroll();

		if (getZoomLevel() != last_zoom) {
			// if computeScroll called before timer counts down we should drop
			// it and start it over again
			zoom_event_delay_timer.cancel();
			zoom_event_delay_timer = new Timer();
			zoom_event_delay_timer.schedule(new TimerTask() {
				@Override
				public void run() {
					zoom_change_listener.onZoomChange(_this, getZoomLevel(), last_zoom);
					last_zoom = getZoomLevel();
				}
			}, events_timeout);
		}

		// Send event only when map's center has changed and user stopped
		// touching the screen
		if (!last_center_pos.equals(getMapCenter()) && !touchIsActive) {
			pan_event_delay_timer.cancel();
			pan_event_delay_timer = new Timer();
			pan_event_delay_timer.schedule(new TimerTask() {
				@Override
				public void run() {
					pan_change_listener.onPanChange(_this, getMapCenter(), last_center_pos);
					last_center_pos = getMapCenter();
				}
			}, events_timeout);
		}
	}

}
