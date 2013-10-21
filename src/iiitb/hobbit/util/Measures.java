/**
 * 
 */
package iiitb.hobbit.util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Rahul A R
 * @date 5:59:37 PM
 * @institute IIITB
 */
public class Measures {

	ArrayList<CriminalData> dataPoints; // Nodes : Latitude, Longitude, etc.
	ArrayList<BitSet> bTrac; // Bit vectors for trajectories
	HashMap<String, ArrayList<Integer>> hash; // Clustering of nodes
	HashMap<String, Integer> hashIndices; // Mapping from 'hash' keys to indices
											// (0..n)
	ArrayList<List<Integer>> similarTrajectories; // A list of similar
													// trajectories for each
													// trajectory
	ArrayList<ArrayList<ClusterDate>> timeVectors; // To keep track of time of
													// each
	ArrayList<Integer> followerTrajectories; // A list of all
	// follower
	// trajectories to a particular followee
	int numOfCriminals = 30;
	int thirtyMin = 30 * 1000 * 60;
	private Context context;

	public Measures(ArrayList<CriminalData> criminalData, Context context) {
		this.setContext(context);
		dataPoints = criminalData;
		bTrac = new ArrayList<BitSet>();
		timeVectors = new ArrayList<ArrayList<ClusterDate>>();

		// Create Hash Map
		hash = Trajectory.getTrajectories(dataPoints);

		// Map the hash keys to 0..n for indexing
		int value = 0;
		hashIndices = new HashMap<String, Integer>();
		Set<String> keys = hash.keySet();
		for (String key : keys) {
			hashIndices.put(key, value++);
		}

		// Create bit vectors
		for (int i = 0; i < numOfCriminals; i++)
			bTrac.add(new BitSet());

		// Create time vectors
		for (int i = 0; i < numOfCriminals; i++)
			timeVectors.add(new ArrayList<ClusterDate>());
	}

	// Inner class utilized for finding follower patterns
	class ClusterDate {
		Date time;
		int clusterId;

		public ClusterDate(int clusterId, Date time) {
			this.clusterId = clusterId;
			this.time = time;
		}
	}

	public void buildBitVectors() {
		int hashIndex, criminalId;
		for (CriminalData c : dataPoints) {
			String getter = (String.valueOf((int) (c.getLatitude() * 1000)) + (String
					.valueOf((int) (c.getLongitude() * 1000))));

			criminalId = c.getCriminalId() - 1;
			hashIndex = hashIndices.get(getter);

			bTrac.get(criminalId).set(hashIndex);
			timeVectors.get(criminalId).add(
					new ClusterDate(hashIndex, c.getTime()));
		}
	}

	void showBitVectors() {
		int i = 0;
		for (BitSet b : bTrac) {
			System.out.print("Bit vector " + i + " : ");
			System.out.println(b);
			i++;
		}
		System.out.println("Number of bit vectors : " + i);
	}

	void showHashIndices() {
		Set<String> keys = hashIndices.keySet();
		for (String key : keys) {
			Integer temp = hashIndices.get(key);
			System.out.println(key + " : " + temp);
		}
		System.out.println("Number of keys : " + keys.size());
	}

	void showHash() {
		Set<String> keys = hash.keySet();
		for (String key : keys) {
			ArrayList<Integer> temp = hash.get(key);
			System.out.println("For key " + key + " : ");
			for (int i : temp) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
		System.out.println("Number of keys : " + keys.size());
	}

	void showSimilarTrajectories() {
		for (int i = 0; i < similarTrajectories.size(); i++)
			System.out.println(similarTrajectories.get(i));
	}

	public ArrayList<List<Integer>> findSimilarTrajectories() {
		similarTrajectories = new ArrayList<List<Integer>>();
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getContext());
		double similarityMeasure = Double.parseDouble(sharedPrefs.getString(
				"similarity_degree", 70 + ""));
		similarityMeasure /= 100;

		int[] lengths = new int[numOfCriminals];
		for (int i = 0; i < numOfCriminals; i++) {
			lengths[i] = bTrac.get(i).cardinality();
		}

		int normalizer;
		BitSet temp;
		for (int i = 0; i < numOfCriminals; i++) {
			similarTrajectories.add(new ArrayList<Integer>());
			for (int j = 0; j < numOfCriminals; j++) {
				if (i == j)
					continue;
				normalizer = lengths[i] > lengths[j] ? lengths[i] : lengths[j];
				temp = (BitSet) bTrac.get(i).clone();
				// temp.flip(0, temp.length());
				// temp.xor(bTrac.get(j));
				temp.and(bTrac.get(j));
				System.out.println("i : " + i + "\tj : " + j
						+ "\tCardinality : " + temp.cardinality()
						+ "\tNormalizer : " + normalizer + "\tSimilarity : "
						+ (double) temp.cardinality() / normalizer + " ");
				if ((double) temp.cardinality() / normalizer >= similarityMeasure) {
					similarTrajectories.get(i).add(j);
				}
			}
		}
		return similarTrajectories;
	}

	public ArrayList<Integer> findFollowerTrajectories(int followeeIndex) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getContext());
		double followerMeasure = Double.parseDouble(sharedPrefs.getString(
				"follower_degree", 70 + ""));
		followerMeasure /= 100;
		followerTrajectories = new ArrayList<Integer>();

		ClusterDate[] followee = timeVectors.get(followeeIndex).toArray(
				new ClusterDate[timeVectors.get(followeeIndex).size()]);
		for (int j : similarTrajectories.get(followeeIndex)) {
			ClusterDate[] follower = timeVectors.get(j).toArray(
					new ClusterDate[timeVectors.get(j).size()]);

			int normalizer = followee.length;
			int counter = hirschberg(followee, follower);
			System.out
					.println("i : " + followeeIndex + "\tj : " + j
							+ "\tCounter : " + counter + "\tNormalizer : "
							+ normalizer + "\tFollowerSimilarity : "
							+ (double) counter / normalizer);
			if ((double) counter / normalizer >= followerMeasure)
				followerTrajectories.add(j);
		}
		return followerTrajectories;
	}

	public int hirschberg(ClusterDate[] a, ClusterDate[] b) {
		int m = a.length;
		int n = b.length;
		int[][] k = new int[2][n + 1];
		for (int j = 0; j <= n; j++) {
			k[1][j] = 0;
		}

		for (int i = 1; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				k[0][j] = k[1][j];
			}

			for (int j = 1; j <= n; j++) {
				if (a[i - 1].clusterId == b[j - 1].clusterId
						&& a[i - 1].time.getTime() - b[j - 1].time.getTime() < thirtyMin
						&& a[i - 1].time.getTime() - b[j - 1].time.getTime() >= 0) {
					k[1][j] = k[0][j - 1] + 1;
				} else {
					k[1][j] = Math.max(k[1][j - 1], k[0][j]);
				}
			}
		}

		return k[1][n];
	}

	void showFollowerTrajectories() {
		for (int i = 0; i < followerTrajectories.size(); i++)
			System.out.println(followerTrajectories.get(i));
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
