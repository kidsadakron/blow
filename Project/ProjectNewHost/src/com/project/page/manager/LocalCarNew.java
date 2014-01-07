package com.project.page.manager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocalCarNew extends Fragment {
	LatLng SYDNEY;
	GoogleMap mMap;
	String jobID;
	Double longitude;
	Double latitude;
	int statusJob;
	private Bundle mBundle;
	private MapView mMapView;

	public static LocalCarNew newInstance(String jobId, Double longitude,
			Double latitude, int statusJob) {
		LocalCarNew f = new LocalCarNew();
		Bundle b = new Bundle();
		b.putString("JobID", jobId);
		b.putDouble("Longitude", longitude);
		b.putDouble("Latitude", latitude);
		b.putInt("StatusJob", statusJob);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;
		longitude = getArguments().getDouble("Longitude");
		Log.v("ss", longitude + "");
		latitude = getArguments().getDouble("Latitude");
		jobID = getArguments().getString("JobID");
		statusJob = getArguments().getInt("StatusJob");
		SYDNEY = new LatLng(latitude, longitude);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.new_detail_manager_status_local_car,
				container, false);
		try {
			MapsInitializer.initialize(getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO handle this situation
		}

		mMapView = (MapView) v.findViewById(R.id.map);
		mMapView.onCreate(mBundle);
		setUpMapIfNeeded(v);

		return v;
	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (mMap == null) {
			mMap = ((MapView) inflatedView.findViewById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {

		mMap.getUiSettings().setZoomControlsEnabled(false);

		mMap.addMarker(new MarkerOptions()
				.position(SYDNEY)
				.title(jobID)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 15.0f));

	}
	
	 @Override
	    public void onResume() {
	        super.onResume();
	        mMapView.onResume();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        mMapView.onPause();
	    }

	    @Override
	    public void onDestroy() {
	        mMapView.onDestroy();
	        super.onDestroy();
	    }
}