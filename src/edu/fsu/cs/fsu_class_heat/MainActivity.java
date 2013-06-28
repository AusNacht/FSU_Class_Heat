package edu.fsu.cs.fsu_class_heat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends FragmentActivity {

	GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
        map = ( (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapView)).getMap();
        
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng loc = new LatLng(30.44388, -84.29806);
        CameraUpdate center = CameraUpdateFactory.newLatLng(loc);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        
        map.moveCamera(center);
        map.animateCamera(zoom);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
