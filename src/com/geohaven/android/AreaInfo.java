package com.geohaven.android;

import java.util.ArrayList;
import java.util.List;

public class AreaInfo {

	private MyGeoArea area;
	private List<Crime> incidents;
	private int normalizedRating = 0;
	private boolean isPopulated = false;
	private Crime.Category cat = Crime.Category.WALKING;
	private int sequenceNumber;

	public AreaInfo(MyGeoArea a) {
		area = a;
		incidents = new ArrayList<Crime>();
	}

	public int getCrimeCount() {
		return incidents.size();
	}

	public void populate(Crime.Category c) {
		if (isPopulated() && cat.equals(c)) {
			return;
		}
		
		cat = c;
		incidents = CrimeHelper.getByRectangle(cat, area);

		setPopulated(true);
	}

	private void setPopulated(boolean isPopulated) {
		this.isPopulated = isPopulated;
	}

	public boolean isPopulated() {
		return isPopulated;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	public MyGeoArea getArea() {
		return area;
	}
	
	public String toString() {
		return getSequenceNumber() + ": " + (isPopulated() ? "X" : getCrimeCount()) + " -- " + area;
	}

	public void setNormalizedRating(int normalizedRating) {
		this.normalizedRating = normalizedRating;
	}

	public int getNormalizedRating() {
		return normalizedRating;
	}
}
