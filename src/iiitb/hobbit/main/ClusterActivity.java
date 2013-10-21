package iiitb.hobbit.main;

import iiitb.hobbit.util.CriminalData;
import iiitb.hobbit.util.cluster.Cluster;
import iiitb.hobbit.util.cluster.Clusterer;
import iiitb.hobbit.util.cluster.Clusterer.OnPaintingClusterListener;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ClusterActivity extends Activity {

	private GoogleMap map;
	ArrayList<CriminalData> criminalData;
	private ArrayList<LatLng> latlngPoints;
	private Clusterer<CriminalData> clusterer;
	private HashMap<Marker, Cluster> clusters = new HashMap<Marker, Cluster>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cluster);

		// get latLngPoints from app file
		latlngPoints = ((CriminalTrackerApp) getApplication()).latlngPoints;
		criminalData = ((CriminalTrackerApp) getApplication()).criminalData;

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// Move the camera instantly to p1 with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngPoints.get(0),
				15));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

		// add clusters
		initClusterer();
	}

	private void initClusterer() {
		clusterer = new Clusterer<CriminalData>(this, map);
		clusterer.setOnPaintingClusterListener(new OnPaintingClusterListener() {

			@Override
			public void onMarkerCreated(Marker marker, Cluster cluster) {
				clusters.put(marker, cluster);
			}

			@Override
			public MarkerOptions onCreateClusterMarkerOptions(Cluster cluster) {
				return new MarkerOptions()
						.title("Clustering " + cluster.getWeight() + " items")
						.position(cluster.getCenter())
						.icon(BitmapDescriptorFactory
								.fromBitmap(getClusteredLabel(
										Integer.valueOf(cluster.getWeight())
												.toString(),
										ClusterActivity.this)));
			}
		});
		clusterer.addAll(criminalData);
	}

	private Bitmap getClusteredLabel(String cnt, Context ctx) {
		Resources r = ctx.getResources();
		Bitmap res = BitmapFactory.decodeResource(r, R.drawable.circle_red);
		res = res.copy(Bitmap.Config.ARGB_8888, true);
		Canvas c = new Canvas(res);

		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(30);

		c.drawText(String.valueOf(cnt), res.getWidth() / 2, res.getHeight() / 2
				+ textPaint.getTextSize() / 3, textPaint);

		return res;
	}
}