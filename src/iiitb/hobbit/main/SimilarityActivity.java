/**
 * 
 */
package iiitb.hobbit.main;

import iiitb.hobbit.util.CriminalData;
import iiitb.hobbit.util.Measures;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * @author Rahul A R
 * @date 6:18:22 PM
 * @institute IIITB
 */
public class SimilarityActivity extends Activity {
	// private static final String TAG = "SimilarityActivity";
	private GoogleMap map;
	ArrayList<CriminalData> criminalData;
	ArrayList<List<Integer>> similarTrajectories;
	ArrayList<LatLng> points;

	Measures measures;
	int[] colors = { Color.RED, Color.BLACK, Color.BLUE, Color.GREEN,
			Color.CYAN, Color.DKGRAY, Color.GRAY, Color.LTGRAY };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trajectory);

		similarTrajectories = ((CriminalTrackerApp) getApplication()).similarTrajectories;
		measures = ((CriminalTrackerApp) getApplication()).measures;
		measures.setContext(getBaseContext());
		criminalData = ((CriminalTrackerApp) getApplication()).criminalData;
		
		if (similarTrajectories == null) {
			// get latLngPoints from app file
			measures = new Measures(criminalData, getBaseContext());
			((CriminalTrackerApp) getApplication()).measures = measures;
			measures.buildBitVectors();
			similarTrajectories = measures.findSimilarTrajectories();
		}

		int id = getIntent().getIntExtra("ID", -1);
		List<Integer> trajectories = similarTrajectories.get(id - 1);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		ArrayList<Integer> tempTrajectories = new ArrayList<Integer>();
		for (int i : trajectories) {
			tempTrajectories.add(i + 1); // CHANGE
		}

		String msg;
		if (trajectories.isEmpty())
			msg = "No similar paths found!";
		else
			msg = "The trajectories with IDs " + tempTrajectories
					+ " are similar!";

		trajectories.add(0, id - 1); // CHANGE
		int colorId = 0;
		for (int i : trajectories) {
			points = pruneData(i + 1);
			map.addPolyline(
					new PolylineOptions().width(5).color(
							colors[colorId++ % colors.length])).setPoints(points);
		}
		trajectories.remove(0);

		// Move the camera instantly with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(criminalData.get(id)
				.getPosition(), 15));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		showDialog("Alert", msg);
	}

	void showDialog(String title, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, null);
		builder.show();
	}

	ArrayList<LatLng> pruneData(int i) {
		ArrayList<LatLng> points = new ArrayList<LatLng>();
		for (CriminalData c : criminalData) {
			if (c.getCriminalId() == i)
				points.add(new LatLng(c.getLatitude(), c.getLongitude()));
		}
		return points;
	}
}
