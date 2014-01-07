package com.project.detail;

import com.google.android.gms.maps.model.LatLng;

public class ManagerDetail {
	private String jobID;
	private LatLng sydney;

	public ManagerDetail() {

	}

	public ManagerDetail(String jobID, LatLng sydney) {
		this.jobID = jobID;
		this.sydney = sydney;
	}

	public LatLng getSydney() {
		return sydney;
	}

	public void setSydney(LatLng sydney) {
		this.sydney = sydney;
	}

	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

}
