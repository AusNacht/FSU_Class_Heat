package edu.fsu.cs.fsu_class_heat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity {

	GoogleMap map;
	final int loveCap = 15, hcbCap = 10;
	
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
        
        LatLng love = new LatLng(30.446112,-84.299566);
        
        MarkerOptions mol = new MarkerOptions().position(love).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).visible(true).draggable(false);
        
        map.addMarker(mol);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//returns integer time hhmm
	public int getTime()
	{
		Time now = new Time();
		now.setToNow();
		String timestr = now.format("%H%M");
		int timeint = Integer.valueOf(timestr);

		//or we could Query(timeint);

		return timeint;
	}
	
	public char getDay()
	{

		char daychar = ' ';
		Time day = new Time();

		day.setToNow();

		int dayint = day.weekDay;

		if(dayint == 1)
		{
			daychar = 'm';
		}
		else if(dayint == 2)
		{
			daychar = 't';
		}
		else if(dayint == 3)
		{
			daychar = 'w';
		}
		else if(dayint == 4)
		{
			daychar = 'r';
		}
		else if(dayint == 5)
		{
			daychar = 'f';
		}

		return daychar;
	}

}
