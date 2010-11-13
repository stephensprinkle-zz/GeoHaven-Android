package com.geohaven.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Map extends Activity {
	
	public static final String EXTRA_LATITUDE = "latitude";
	public static final String EXTRA_LONGITUDE = "longitude";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.map);
        TextView textview = new TextView(this);
        textview.setText("This is the Map tab");
        setContentView(textview);
    

}

}
