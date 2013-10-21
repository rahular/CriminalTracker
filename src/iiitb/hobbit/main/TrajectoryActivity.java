/**
 * 
 */
package iiitb.hobbit.main;

import iiitb.hobbit.util.CriminalData;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * @author Rahul A R
 * @date 4:27:04 PM
 * @institute IIITB
 */
public class TrajectoryActivity extends Activity {
	private GoogleMap map;
	ArrayList<CriminalData> criminalData;
	private ArrayList<LatLng> criminalPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trajectory);

		// get latLngPoints from app file
		criminalData = ((CriminalTrackerApp) getApplication()).criminalData;
		pruneData(getIntent().getExtras().getInt("ID"));

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		map.addPolyline(new PolylineOptions().width(5).color(Color.RED))
				.setPoints(criminalPoints);

		// Move the camera instantly to p1 with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(criminalPoints.get(0),
				15));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	}

	void pruneData(int id) {
		criminalPoints = new ArrayList<LatLng>();
		for (CriminalData c : criminalData) {
			if (c.getCriminalId() == id) {
				criminalPoints
						.add(new LatLng(c.getLatitude(), c.getLongitude()));
			}
		}
	}
}
