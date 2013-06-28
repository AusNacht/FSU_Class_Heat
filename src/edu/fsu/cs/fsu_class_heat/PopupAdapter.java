package edu.fsu.cs.fsu_class_heat;


import com.google.android.gms.maps.model.Marker;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PopupAdapter {
	
	LayoutInflater inflater = null;

	PopupAdapter(LayoutInflater inflater)
	{
		this.inflater = inflater;
	}
	
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub
		View popup = inflater.inflate(R.layout.popup, null);
		
		//TextView tv = (TextView) popup.findViewById(R.id.title);
		//tv.setText(marker.getTitle());
		//tv = (TextView) popup.findViewById(R.id.snippet);
		//tv.setText(marker.getSnippet());
		return popup;
	}

	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

}
