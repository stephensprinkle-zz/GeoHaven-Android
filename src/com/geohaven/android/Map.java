package com.geohaven.android;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class Map extends MapActivity {
	
	public static final String EXTRA_LATITUDE = "latitude";
	public static final String EXTRA_LONGITUDE = "longitude";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
    

}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
