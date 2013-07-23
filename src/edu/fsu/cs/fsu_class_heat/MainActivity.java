package edu.fsu.cs.fsu_class_heat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

	Cursor mCursor;
	GoogleMap map;
	MarkerOptions mo_lov, mo_hcb;
	final int loveCap = 15, hcbCap = 10;

	// Integers to keep count of the number of class
	int HCB = 0;
	int LOV = 0;

	final int MONDAY = 0;
	final int TUESDAY = 1;
	final int WEDNESDAY = 2;
	final int THURSDAY = 3;
	final int FRIDAY = 4;
	final int SPRING = 0;
	final int SUMMER = 1;
	final int FALL = 2;

	int daySelection = MONDAY;
	int semesterSelection = SPRING;
	int hourSelection = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().setDisplayShowTitleEnabled(false);

		map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.mapView)).getMap();

		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		LatLng loc = new LatLng(30.44388, -84.29806);
		CameraUpdate center = CameraUpdateFactory.newLatLng(loc);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

		map.moveCamera(center);
		map.animateCamera(zoom);

		// ***** Database Portion *****
		mCursor = getContentResolver().query(class_database.CONTENT_URI, null,
				null, null, null);

		if (mCursor != null) {

			// if contentprovider is empty, load the dataset into content
			// provider
			// if contentprovider is not empty, there is no need to import the
			// dataset
			if (mCursor.getCount() <= 0) {
				Uri mNewUri;
				ContentValues mNewValues = new ContentValues();

				// Retrieve dataset textfile in res/raw/class_dataset.txt
				InputStream inputStream = this.getResources().openRawResource(
						R.raw.class_dataset);
				InputStreamReader inputreader = new InputStreamReader(
						inputStream);
				BufferedReader buffreader = new BufferedReader(inputreader);

				String line;

				// Iterate each line of the dataset file
				// Split each line into tokens and load into the database
				try {
					while ((line = buffreader.readLine()) != null) {

						StringTokenizer line_tokens = new StringTokenizer(line,
								"\t");

						mNewValues.put(class_database.COLUMN_BUILDING,
								line_tokens.nextToken());
						mNewValues.put(class_database.COLUMN_DAYS,
								line_tokens.nextToken());
						mNewValues.put(class_database.COLUMN_BEGIN,
								line_tokens.nextToken());
						mNewValues.put(class_database.COLUMN_END,
								line_tokens.nextToken());
						mNewUri = getContentResolver().insert(
								class_database.CONTENT_URI, mNewValues);
					}
				} catch (IOException e) {

				}

				// Reinitialize database cursor
				mCursor = getContentResolver().query(
						class_database.CONTENT_URI, null, null, null, null);
			}
		}
		mCursor.close();

		// ***** End of Database Portion *****

		LatLng love = new LatLng(30.446112, -84.299566);
		LatLng hcb = new LatLng(30.443291, -84.297034);

		// count_classes(getDay(), getTime());
		count_classes('M', 1303);

		Log.i("marker", String.valueOf(LOV));
		Log.i("marker", String.valueOf(HCB));

		float classpercent_lov, classpercent_hcb;

		classpercent_lov = (float) LOV / loveCap;
		classpercent_hcb = (float) HCB / hcbCap;
		if (classpercent_lov == 0) {
			mo_lov = new MarkerOptions()
					.title("Love Building")
					.position(love)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED))
					.visible(false).draggable(false);
		}

		else if (classpercent_lov <= .5 && classpercent_lov > 0) {
			mo_lov = new MarkerOptions()
					.title("Love Building")
					.position(love)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
					.visible(true).draggable(false);
		}

		else if (classpercent_lov > .5 && classpercent_lov <= .75) {
			mo_lov = new MarkerOptions()
					.title("Love Building")
					.position(love)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
					.visible(true).draggable(false);
		}

		else if (classpercent_lov > .75) {
			mo_lov = new MarkerOptions()
					.title("Love Building")
					.position(love)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED))
					.visible(true).draggable(false);
		}

		if (classpercent_hcb == 0) {
			mo_hcb = new MarkerOptions()
					.title("HCB")
					.position(hcb)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED))
					.visible(false).draggable(false);
		}

		else if (classpercent_hcb <= .5 && classpercent_hcb > 0) {
			mo_hcb = new MarkerOptions()
					.title("HCB")
					.position(hcb)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
					.visible(true).draggable(false);
		}

		else if (classpercent_hcb > .5 && classpercent_hcb <= .75) {
			mo_hcb = new MarkerOptions()
					.title("HCB")
					.position(hcb)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
					.visible(true).draggable(false);
		}

		else if (classpercent_hcb > .75) {
			mo_hcb = new MarkerOptions()
					.title("HCB")
					.position(hcb)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED))
					.visible(true).draggable(false);
		}

		map.addMarker(mo_lov);
		map.addMarker(mo_hcb);

		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				Log.i("marker", "here");
				if (marker.getTitle().toString()
						.equals(mo_hcb.getTitle().toString())) {
					marker.setSnippet("Capacity: " + String.valueOf(hcbCap)
							+ "\nIn-Use: " + String.valueOf(HCB) + "\nEmpty: "
							+ String.valueOf(hcbCap - HCB));
				}

				if (marker.getTitle().toString()
						.equals(mo_lov.getTitle().toString())) {
					Log.i("marker", "here");
					marker.setSnippet("Capacity: " + String.valueOf(loveCap)
							+ "\nIn-Use: " + String.valueOf(LOV) + "\nEmpty: "
							+ String.valueOf(loveCap - LOV));
				}

				return false;
			}
		});
	}

	private MenuItem mSpinnerItem1 = null;
	private MenuItem mSpinnerItem2 = null;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater mi=getMenuInflater();
	    mi.inflate(R.menu.main, menu);
	    mSpinnerItem1 = menu.findItem( R.id.menuSem);
	    View view1 = mSpinnerItem1.getActionView();
	    if (view1 instanceof Spinner)
	    {
	        final Spinner spinner1 = (Spinner) view1;
	        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(new ContextThemeWrapper(this, android.R.style.Theme_Holo), R.array.sem_options, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner1.setAdapter(adapter);


	        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

	            @Override
	            public void onItemSelected(AdapterView<?> arg0, View arg1,
	                    int arg2, long arg3) {
	                // TODO Auto-generated method stub
	            	
	            	switch(arg2)
	            	{
	            	case SPRING:
	            		semesterSelection = SPRING;
	            		break;
	            	case SUMMER:
	            		semesterSelection = SUMMER;
	            		break;
	            	case FALL:
	            		semesterSelection = FALL;
	            		break;
	            	}
	            		

	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> arg0) {
	                // TODO Auto-generated method stub
	            }
	        });

	    }
	    
	    mSpinnerItem2 = menu.findItem( R.id.menuDay);
	    View view2 = mSpinnerItem2.getActionView();
	    if (view2 instanceof Spinner)
	    {
	        final Spinner spinner2 = (Spinner) view2;
	        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(new ContextThemeWrapper(this, android.R.style.Theme_Holo), R.array.day_options, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner2.setAdapter(adapter);


	        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

	            @Override
	            public void onItemSelected(AdapterView<?> arg0, View arg1,
	                    int arg2, long arg3) {
	                // TODO Auto-generated method stub
	            	
	            	switch(arg2)
	            	{
	            	case MONDAY:
	            		daySelection = MONDAY;
	            		break;
	            	case TUESDAY:
	            		daySelection = TUESDAY;
	            		break;
	            	case WEDNESDAY:
	            		daySelection = WEDNESDAY;
	            		break;
	            	case THURSDAY:
	            		daySelection = THURSDAY;
	            		break;
	            	case FRIDAY:
	            		daySelection = FRIDAY;
	            		break;
	            	}
	            		

	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> arg0) {
	                // TODO Auto-generated method stub
	            }
	        });

	    }
		return true;
	}

	// returns integer time hhmm
	public int getTime() {
		Time now = new Time();
		now.setToNow();
		String timestr = now.format("%H%M");
		int timeint = Integer.valueOf(timestr);

		// or we could Query(timeint);

		return timeint;
	}

	public char getDay() {

		char daychar = ' ';
		Time day = new Time();

		day.setToNow();

		int dayint = day.weekDay;

		if (dayint == 1) {
			daychar = 'M';
		} else if (dayint == 2) {
			daychar = 'T';
		} else if (dayint == 3) {
			daychar = 'W';
		} else if (dayint == 4) {
			daychar = 'R';
		} else if (dayint == 5) {
			daychar = 'F';
		}

		return daychar;
	}

	// Count the number of classes going on at a given time
	// Call example: Monday 3:00 PM will be count_classes('M', 1500);
	public void count_classes(char CURRENT_DAY, int CURRENT_TIME) {

		// Main loop: iterate through contentprovider and take count of classes
		mCursor = getContentResolver().query(class_database.CONTENT_URI, null,
				null, null, null);
		mCursor.moveToFirst();

		// Reset counts
		HCB = 0;
		LOV = 0;

		while (mCursor.isAfterLast() == false) {

			// Get data from database
			String CLASS_DAYS = mCursor.getString(2).trim();
			int CLASS_BEGIN = Integer.valueOf(mCursor.getString(3).trim());
			int CLASS_END = Integer.valueOf(mCursor.getString(4).trim());

			if (mCursor.getString(1).equals("HCB")) {
				if (CLASS_DAYS.contains(Character.toString(CURRENT_DAY))) {
					if (CLASS_BEGIN <= CURRENT_TIME
							&& CURRENT_TIME <= CLASS_END) {
						HCB++;
					}
				}
			} else if (mCursor.getString(1).equals("LOV")) {
				if (CLASS_DAYS.contains(Character.toString(CURRENT_DAY))) {
					if (CLASS_BEGIN <= CURRENT_TIME
							&& CURRENT_TIME <= CLASS_END) {
						LOV++;
					}
				}
			}
			mCursor.moveToNext();
		}
		mCursor.close();
	}
}
