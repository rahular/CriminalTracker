/**
 * 
 */
package iiitb.hobbit.main;

import iiitb.hobbit.util.Measures;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Rahul A R
 * @date 4:06:43 PM
 * @institute IIITB
 */
public class CriminalListActivity extends ListActivity {

	ListView list;
	Intent intent;
	String todo;
	int numOfCriminals = 30;
	Measures measures;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = getListView();
		intent = getIntent();

		todo = intent.getStringExtra("todo");
		new HeavyWorker().execute(null, null, null);

		ArrayList<String> values = new ArrayList<String>();
		for (int i = 1; i <= 30; i++)
			if (todo.equals("trajectory"))
				values.add("Criminal with ID " + i);
			else if (todo.equals("similarity"))
				values.add("Similarity for Trajectory " + i);
			else if (todo.equals("follower"))
				values.add("Followers of Trajectory " + i);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);

		setListAdapter(adapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		String todo = intent.getStringExtra("todo");
		if (todo.equals("trajectory"))
			startActivity(new Intent(this, TrajectoryActivity.class).putExtra(
					"ID", position + 1));
		else if (todo.equals("similarity")) {
			startActivity(new Intent(this, SimilarityActivity.class).putExtra(
					"ID", position + 1));
		} else if (todo.equals("follower")) {
			startActivity(new Intent(this, FollowerActivity.class).putExtra(
					"ID", position + 1));
		}
	}

	class HeavyWorker extends AsyncTask<Void, Void, Void> {
		ProgressDialog progress;

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress.dismiss();
		}

		@Override
		protected Void doInBackground(Void... params) {
			measures = new Measures(
					((CriminalTrackerApp) getApplication()).criminalData,
					getBaseContext());
			measures.buildBitVectors();
			((CriminalTrackerApp) getApplication()).measures = measures;
			((CriminalTrackerApp) getApplication()).similarTrajectories = measures
					.findSimilarTrajectories();

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(CriminalListActivity.this);
			progress.setTitle("Please wait");
			progress.setMessage("Doing some ground work...");
			progress.setCancelable(false);
			progress.show();
		}
	}
}
