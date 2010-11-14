package com.geohaven.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Home extends Activity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		TextView numCrimes = (TextView) findViewById(R.id.numCrimes);
		numCrimes.setText(""+CrimeHelper.countInRectangle(Crime.Category.WALKING, 37, -122, 2, 2));
		
//		TextView percievedRisk = (TextView) findViewById(R.id.percievedRisk);
//		percievedRisk.setText("Moderate");
//		
//		TextView dataRecency = (TextView) findViewById(R.id.dataRecency);

	}
}