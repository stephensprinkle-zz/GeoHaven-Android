package com.geohaven.android;

import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class Mapit extends MapActivity {
						
		Double latD,lonD;
		 Integer zoomLevel[] = {14,13,12,11,10,9};   /* assume zoom levels are 5, 10, 20, 40, 80, 160 */
		
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mapview);
	        displayLocation();	        	        
	}
		@Override
	    public void onResume()
	    {
			super.onResume();
	    	displayLocation();
	        
	    	
	    }
		@Override
		protected boolean isRouteDisplayed() {
			// TODO Auto-generated method stub
			return false;
		}
		private void displayLocation()
		{
			
	        {	
	          latD = Globals.lastGeoLat * 1E6;
	          lonD = Globals.lastGeoLong * 1E6;
	          
	        }
	        Log.d("MAPIT", "latD = " +latD +"longD =" +lonD);	
	        Integer lat = latD.intValue();
	        Integer lon =  lonD.intValue();
	        
	        
	        GeoPoint geopoint = new GeoPoint(lat,lon); 
	        
	    	MapView mapview = (MapView) findViewById(R.id.mapv);
	    	
	    
            Log.d("Mapit", "lat = " +lat +"long = " +lon);
	    	MapController mapController = mapview.getController();
	    	mapController.setCenter(geopoint);
	    	
	    	
	    	mapController.setZoom(zoomLevel[Globals.rangePosition]);
	    	mapview.setBuiltInZoomControls(true);	
		}
}


