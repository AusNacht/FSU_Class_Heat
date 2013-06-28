package edu.fsu.cs.fsu_class_heat;


import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import android.view.LayoutInflater;
import android.view.View;

public class PopupAdapter implements InfoWindowAdapter{
	
	LayoutInflater inflater = null;

	PopupAdapter(LayoutInflater inflater)
	{
		this.inflater = inflater;
	}
	
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub
		View popup = inflater.inflate(R.layout.popup, null);
		
		return popup;
	}

	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

}
