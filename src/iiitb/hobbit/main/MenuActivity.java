/**
 * 
 */
package iiitb.hobbit.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author Rahul A R
 * @date 2:52:25 PM
 * @institute IIITB
 */
public class MenuActivity extends Activity implements OnClickListener {

	Button crimeProne, trackCriminals, similarity, follower;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		new HeavyWorker().execute(null, null, null);

		crimeProne = (Button) findViewById(R.id.btn_crime_prone);
		trackCriminals = (Button) findViewById(R.id.btn_track_criminal);
		similarity = (Button) findViewById(R.id.btn_similarity);
		follower = (Button) findViewById(R.id.btn_followers);

		crimeProne.setOnClickListener(this);
		trackCriminals.setOnClickListener(this);
		similarity.setOnClickListener(this);
		follower.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_crime_prone) {
			Intent intent = new Intent(this, ClusterActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.btn_track_criminal) {
			Intent intent = new Intent(this, CriminalListActivity.class);
			intent.putExtra("todo", "trajectory");
			startActivity(intent);
		} else if (v.getId() == R.id.btn_similarity) {
			Intent intent = new Intent(this, CriminalListActivity.class);
			intent.putExtra("todo", "similarity");
			startActivity(intent);
		} else if (v.getId() == R.id.btn_followers) {
			Intent intent = new Intent(this, CriminalListActivity.class);
			intent.putExtra("todo", "follower");
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(getBaseContext(), SettingsActivity.class));
			return false;
		default:
			return false;
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
			((CriminalTrackerApp) getApplication()).parseData();
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(MenuActivity.this);
			progress.setTitle("Please wait");
			progress.setMessage("Parsing GPS data from assets...");
			progress.setCancelable(false);
			progress.show();
		}
	}
}
