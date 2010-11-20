package com.geohaven.android;

import com.google.android.maps.GeoPoint;

public class MyGeoArea {
	private MyGeoPoint center;
	private double latSpan, lonSpan;

	public MyGeoArea(MyGeoPoint c, int latS, int lonS) {
		center = c;
		latSpan = latS / 1e6;
		lonSpan = lonS / 1e6;
	}

	public MyGeoArea(GeoPoint c, int latS, int lonS) {
		center = new MyGeoPoint(c);
		latSpan = latS / 1e6;
		lonSpan = lonS / 1e6;
	}

	public MyGeoArea(MyGeoPoint c, double latS, double lonS) {
		center = c;
		latSpan = latS;
		lonSpan = lonS;
	}
	public MyGeoArea(GeoPoint c, double latS, double lonS) {
		center = new MyGeoPoint(c);
		latSpan = latS;
		lonSpan = lonS;
	}

	public MyGeoArea(double lat, double lon, double latS, double lonS) {
		center = new MyGeoPoint(lat, lon);
		latSpan = latS;
		lonSpan = lonS;
	}

	public MyGeoArea(int lat, int lon, double latS, double lonS) {
		center = new MyGeoPoint(lat, lon);
		latSpan = latS;
		lonSpan = lonS;
	}

	public MyGeoPoint getCenter() {
		return center;
	}

	public double getLatSpan() {
		return latSpan;
	}

	public double getLonSpan() {
		return lonSpan;
	}
	
	public String toString() {
		return "C:[" + center + "] LATs: " + getLatSpan() + " LONs: " + getLonSpan();
	}

	public MyGeoPoint[] getCentersOfSubAreas(int perSide) {
		MyGeoPoint[] results = new MyGeoPoint[perSide * perSide];

		double subLatSpan = getLatSpan() / perSide;
		double subLonSpan = getLonSpan() / perSide;

		double latOfTopCenters = getCenter().getLat() - getLatSpan() / 2 + subLatSpan / 2;
		double lonOfLeftCenters = getCenter().getLon() - getLonSpan() / 2 + subLonSpan / 2;

		// TODO: make sure I have my x/y and lat/long and ordering
		// (across-then-down) right
		for (int lonIncrement = 0; lonIncrement < perSide; lonIncrement++) {
			for (int latIncrement = 0; latIncrement < perSide; latIncrement++) {
				MyGeoPoint p = new MyGeoPoint(latOfTopCenters + subLatSpan * latIncrement, lonOfLeftCenters + subLonSpan * lonIncrement);
				results[lonIncrement * perSide + latIncrement] = p;
			}
		}

		return results;
	}


}
