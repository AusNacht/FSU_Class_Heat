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

	//building codes
	static final int lovebc = 116;
	static final int carothbc = 55;
	static final int carabc = 113;
	static final int fishbc = 37;
	static final int rogbc = 36;
	static final int belbc = 8;
	static final int hbcbc = 4009;
	static final int welbc = 9999;
	static final int shorbc = 19;
	static final int rosabc = 23;
	static final int rosbbc = 52;

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
								",");
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
		
		LatLng dot = dotlocation(lovebc);
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

	////place the static final variables up top



	LatLng dotlocation(int buildingcode)
	{	


		//All SW and NE points
		//Love
		double lovelathigh = 30.446112;
		double lovelonhigh = -84.299222;
		double lovelatlow = 30.446084;
		double lovelonlow = -84.299877;

		//Carothers
		//LatLng carothSW = new LatLng(30.445612,-84.300488);
		double carothlathigh = 30.445612;
		double carothlonhigh = -84.299555;
		double carothlatlow = 30.445473;
		double carothlonlow = -84.300488;
		//LatLng carothNE = new LatLng(30.445473,-84.299555);

		//Caraway
		//LatLng caraSW = new LatLng(30.44514,-84.29889);
		//LatLng caraNE = new LatLng(30.445131,-84.298439);
		double caralathigh = 30.44514;
		double caralonhigh = -84.298439;
		double caralatlow = 30.445131;
		double caralonlow = -84.29889;


		//Fischer lecture hall
		//LatLng fishSW = new LatLng(30.444095,-84.300735);
		//LatLng fishNE = new LatLng(30.444308,-84.300402);
		double fishlathigh = 30.444308;
		double fishlonhigh = -84.300402;
		double fishlatlow = 30.444095;
		double fishlonlow = -84.300735;

		//Rogers
		//LatLng rogSW = new LatLng(30.443984,-84.300284);
		//LatLng rogNE = new LatLng(30.444114,-84.299909);
		double roglathigh = 30.444114;
		double roglonhigh = -84.299909;
		double roglatlow = 30.443984;
		double roglonlow = -84.300284;

		//Bellamy
		//LatLng belSW = new LatLng(30.44293,-84.296025);
		//LatLng belNE = new LatLng(30.443559,-84.29566);
		double bellathigh = 30.443559;
		double bellonhigh = -84.29566;
		double bellatlow = 30.44293;
		double bellonlow = -84.296025;

		//HCB
		//LatLng hcbSW = new LatLng(30.443041,-84.297634);
		//LatLng hcbNE = new LatLng(30.443346,-84.296669);
		double hcblathigh = 30.443346;
		double hcblonhigh = -84.296669;
		double hcblatlow = 30.443041;
		double hcblonlow = -84.297634;

		//Wellness center
		//LatLng wellSW = new LatLng(30.441468,-84.299608);
		//LatLng wellNE = new LatLng(30.441875,-84.298911);
		double wellathigh = 30.441875;
		double wellonhigh = -84.298911;
		double wellatlow = 30.441468;
		double wellonlow = -84.299608;

		//Shores
		//LatLng shorSW = new LatLng(30.44096,-84.296154);
		//LatLng shorNE = new LatLng(30.441376,-84.295853);
		double shorlathigh = 30.441376;
		double shorlonhigh = -84.295853;
		double shorlatlow = 30.44096;
		double shorlonlow = -84.296154;

		//Rovetta business building A
		//LatLng rovaSW = new LatLng(30.444336,-84.296336);
		//LatLng rovaNE = new LatLng(30.444206,-84.295413);
		double rovalathigh = 30.444336;
		double rovalonhigh = -84.295413;
		double rovalatlow = 30.444206;
		double rovalonlow = -84.296336;

		//Rovetta business building B
		//LatLng rovbSW = new LatLng(30.443725,-84.295521);
		//LatLng rovbNE = new LatLng(30.443966,-84.295092);
		double rovblathigh = 30.443966;
		double rovblonhigh = -84.295092;
		double rovblatlow = 30.443725;
		double rovblonlow = -84.295521;

		switch(buildingcode)
		{
		case lovebc:
			double lovegenlat = (double)(Math.random() * (lovelathigh - lovelatlow) + lovelatlow);
			double lovegenlon = (double)(Math.random() * (lovelonhigh - lovelonlow) + lovelonlow);
			Log.d("maps", "Love lat: " + lovegenlat);
			Log.d("maps", "Love lon: " + lovegenlon);
			LatLng lovegen = new LatLng(lovegenlat, lovegenlon);
			return lovegen;
			//break;			
		case carothbc:
			double carothgenlat = (double)(Math.random() * (carothlathigh - carothlatlow) + carothlatlow);
			double carothgenlon = (double)(Math.random() * (carothlonhigh - carothlonlow) + carothlonlow);
			LatLng carothgen = new LatLng(carothgenlat, carothgenlon);
			return carothgen;
			//break;
		case carabc:
			double caragenlat = (double)(Math.random() * (caralathigh - caralatlow) + caralatlow);
			double caragenlon = (double)(Math.random() * (caralonhigh - caralonlow) + caralonlow);
			LatLng caragen = new LatLng(caragenlat, caragenlon);
			return caragen;
			//break;
		case fishbc:
			double fishgenlat = (double)(Math.random() * (fishlathigh - fishlatlow) + fishlatlow);
			double fishgenlon = (double)(Math.random() * (fishlonhigh - fishlonlow) + fishlonlow);
			LatLng fishgen = new LatLng(fishgenlat, fishgenlon);
			return fishgen;
			//break;
		case rogbc:
			double roggenlat = (double)(Math.random() * (roglathigh - roglatlow) + roglatlow);
			double roggenlon = (double)(Math.random() * (roglonhigh - roglonlow) + roglonlow);
			LatLng roggen = new LatLng(roggenlat, roggenlon);
			return roggen;
			//break;
		case belbc:
			double belgenlat = (double)(Math.random() * (bellathigh - bellatlow) + bellatlow);
			double belgenlon = (double)(Math.random() * (bellonhigh - bellonlow) + bellonlow);
			LatLng belgen = new LatLng(belgenlat, belgenlon);
			return belgen;
			//break;
		case hbcbc:
			double hcbgenlat = (double)(Math.random() * (hcblathigh - hcblatlow) + hcblatlow);
			double hcbgenlon = (double)(Math.random() * (hcblonhigh - hcblonlow) + hcblonlow);
			LatLng hcbgen = new LatLng(hcbgenlat, hcbgenlon);
			return hcbgen;
			//break;
		case welbc:
			double welgenlat = (double)(Math.random() * (wellathigh - wellatlow) + wellatlow);
			double welgenlon = (double)(Math.random() * (wellonhigh - wellonlow) + wellonlow);
			LatLng welgen = new LatLng(welgenlat, welgenlon);
			return welgen;
			//break;
		case shorbc:
			double shorgenlat = (double)(Math.random() * (shorlathigh - shorlatlow) + shorlatlow);
			double shorgenlon = (double)(Math.random() * (shorlonhigh - shorlonlow) + shorlonlow);
			LatLng shorgen = new LatLng(shorgenlat, shorgenlon);
			return shorgen;
			//break;
		case rosabc:
			double rovagenlat = (double)(Math.random() * (rovalathigh - rovalatlow) + rovalatlow);
			double rovagenlon = (double)(Math.random() * (rovalonhigh - rovalonlow) + rovalonlow);
			LatLng rovagen = new LatLng(rovagenlat, rovagenlon);
			return rovagen;
			//break;
		case rosbbc:
			double rovbgenlat = (double)(Math.random() * (rovblathigh - rovblatlow) + rovblatlow);
			double rovbgenlon = (double)(Math.random() * (rovblonhigh - rovblonlow) + rovblonlow);
			LatLng rovbgen = new LatLng(rovbgenlat, rovbgenlon);
			return rovbgen;
			//break;

		default:
			LatLng def = new LatLng(0.0, 0.0);
			return def;
			//break;
		}


	}

}
