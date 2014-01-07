package com.project;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.R;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class LocalCarPage extends FragmentActivity {

	Double Longitude;
	Double Latitude;
	private GoogleMap mMap;
	ArrayList<LatLng> listSYDNEY;
	ArrayList<String> listJobID;
	String jobID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_manager_status_local_car);
		listJobID = getIntent().getStringArrayListExtra(
				"JobID");
		listSYDNEY = new ArrayList<LatLng>();
		ArrayList<String> listSYDNEYTemp = getIntent().getStringArrayListExtra(
				"SYDNEY");
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		for (int i = 0; i < listSYDNEYTemp.size(); i++) {
			LatLng SYDNEY;
			String[] temp = listSYDNEYTemp.get(i).split(",");
			Longitude = Double.parseDouble(temp[1]);
			Latitude = Double.parseDouble(temp[0]);
			SYDNEY = new LatLng(Latitude, Longitude);
			listSYDNEY.add(SYDNEY);
			 

		}
		/*
		 * String temp = getIntent().getExtras().getString("Longitude");
		 * Longitude = Double.parseDouble(temp); temp =
		 * getIntent().getExtras().getString("Latitude"); Latitude =
		 * Double.parseDouble(temp); jobID =
		 * getIntent().getExtras().getString("JobID"); SYDNEY = new
		 * LatLng(Latitude, Longitude);
		 */

		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map_canvas)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		int numMarkersInRainbow = 12;
		mMap.getUiSettings().setZoomControlsEnabled(false);
		for (int i = 0; i < listSYDNEY.size(); i++) {
			mMap.addMarker(new MarkerOptions()
					.position(listSYDNEY.get(i))
					.title(listJobID.get(i))
					.icon(BitmapDescriptorFactory
							.defaultMarker(i * 360 / numMarkersInRainbow)));
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.7511762, 101.0807192), 6.5f));
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
