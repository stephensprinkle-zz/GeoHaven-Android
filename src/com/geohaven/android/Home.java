package com.geohaven.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends Activity {
	private Button preferencesButton;
	private Button mapButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		preferencesButton = (Button) findViewById(R.id.preferencesButton);
		preferencesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent preferencesActivity = new Intent(getBaseContext(), Preferences.class);
				startActivity(preferencesActivity);
			}
		});

		mapButton = (Button) findViewById(R.id.mapButton);
		mapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mapActivity = new Intent(getBaseContext(), Map.class);
				mapActivity.putExtra(Map.EXTRA_LATITUDE, 42.361589);
				mapActivity.putExtra(Map.EXTRA_LONGITUDE, -71.081345);
				startActivity(mapActivity);
			}
		});

	}
}