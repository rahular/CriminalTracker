package iiitb.hobbit.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Trajectory {

	public static HashMap<String, ArrayList<Integer>> getTrajectories(
			ArrayList<CriminalData> dataList) {
		Integer appLat;
		Integer appLon;
		String hashValue;

		HashMap<String, ArrayList<Integer>> hash = new HashMap<String, ArrayList<Integer>>();

		for (CriminalData criminal : dataList) {

			appLat = (int) (criminal.getLatitude() * 1000);
			appLon = (int) (criminal.getLongitude() * 1000);
			hashValue = appLat.toString() + appLon.toString();

			if (hash.get(hashValue) != null) {
				hash.get(hashValue).add(criminal.getNodeId());
			}

			else {
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(criminal.getNodeId());
				hash.put(hashValue, temp);
			}
		}
		// System.out.println(hash);
		return hash;
	}
}
