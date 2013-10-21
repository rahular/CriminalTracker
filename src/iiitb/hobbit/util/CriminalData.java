package iiitb.hobbit.util;

import iiitb.hobbit.util.cluster.Clusterable;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

public class CriminalData implements Clusterable {
	private double latitude;
	private double longitude;
	private Date time;
	private int criminalId;
	private int nodeId;

	public CriminalData() {
		this.latitude = 0.0;
		this.longitude = 0.0;
		this.criminalId = 0;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getCriminalId() {
		return criminalId;
	}

	public void setCriminalId(int criminalId) {
		this.criminalId = criminalId;
	}

	public String toString() {

		return this.criminalId + " " + this.latitude + " " + this.longitude + " "
				+ this.time;

	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public LatLng getPosition() {
		return new LatLng(latitude, longitude);
	}

}