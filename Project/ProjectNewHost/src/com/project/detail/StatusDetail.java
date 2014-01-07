package com.project.detail;



public class StatusDetail extends ProductDetail {
	
	private String JobID;
	private String CarID;
	private String Latitude;
	private String Longitude;
	private int statusJob;
	public int getStatusJob() {
	return statusJob;
}
public void setStatusJob(int statusJob) {
	this.statusJob = statusJob;
}
	public String getJobID() {
		return JobID;
	}
	public void setJobID(String jobID) {
		JobID = jobID;
	}
	public String getCarID() {
		return CarID;
	}
	public void setCarID(String carID) {
		CarID = carID;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
}
