package com.geohaven.android;

import com.google.android.maps.GeoPoint;

public class MyGeoPoint {
	private double lat;
	private double lon;

	public MyGeoPoint(GeoPoint gp) {
		this.lat = gp.getLatitudeE6() / 1e6;
		this.lon = gp.getLongitudeE6() / 1e6;
	}

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
