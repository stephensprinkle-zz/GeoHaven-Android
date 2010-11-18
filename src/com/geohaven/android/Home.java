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
		numCrimes.setText(""+CrimeHelper.countInRectangle(Crime.Category.WALKING, 37.75, -122.20, 0.058908, 0.051498));
		
		int[] qr = CrimeHelper.getAreaRatings(37.75, -122.20, 0.058908, 0.051498);
		TextView perceivedRisk = (TextView) findViewById(R.id.perceivedRisk);
		perceivedRisk.setText("Moderate");
		perceivedRisk.setText(""+qr[0] + qr[1] + qr[2] + qr[3] + qr[4] + qr[5] + qr[6] + qr[7] + qr[8]);
//		
//		TextView dataRecency = (TextView) findViewById(R.id.dataRecency);

	}
}