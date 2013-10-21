/**
 * 
 */
package iiitb.hobbit.main;

import iiitb.hobbit.util.CriminalData;
import iiitb.hobbit.util.Measures;
import iiitb.hobbit.util.ParserDOM;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Rahul A R
 * @date 3:32:36 PM
 * @institute IIITB
 */
public class CriminalTrackerApp extends Application {

	ArrayList<CriminalData> criminalData;
	ArrayList<LatLng> latlngPoints;
	ArrayList<List<Integer>> similarTrajectories;
	Measures measures;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	void parseData() {
		ParserDOM pDOM = new ParserDOM();
		this.criminalData = pDOM.parse(this);
		this.latlngPoints = new ArrayList<LatLng>();

		for (CriminalData c : criminalData) {
			latlngPoints.add(new LatLng(c.getLatitude(), c.getLongitude()));
		}
	}
}
