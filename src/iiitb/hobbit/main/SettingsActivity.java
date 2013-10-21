/**
 * 
 */
package iiitb.hobbit.main;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

/**
 * @author Rahul A R
 * @date 7:04:01 PM
 * @institute IIITB
 */
public class SettingsActivity extends PreferenceActivity {

	EditTextPreference similarityPref, followerPref;
	Context contextForDialogs;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		contextForDialogs = this;

		similarityPref = (EditTextPreference) getPreferenceScreen()
				.findPreference("similarity_degree");
		followerPref = (EditTextPreference) getPreferenceScreen()
				.findPreference("follower_degree");

		similarityPref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						try {
							double measure = Double.parseDouble(newValue
									.toString());
							if (measure < 0 || measure > 100) {
								showFailureDialog("Invalid input",
										"Enter a percentage between 0 and 100");
								return false;
							}
						} catch (Exception e) {
							showFailureDialog("Invalid input",
									"Enter a percentage between 0 and 100");
							return false;
						}
						return true;
					}
				});
		followerPref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						try {
							double measure = Double.parseDouble(newValue
									.toString());
							if (measure < 0 || measure > 100) {
								showFailureDialog("Invalid input",
										"Enter a percentage between 0 and 100");
								return false;
							}
						} catch (Exception e) {
							showFailureDialog("Invalid input",
									"Enter a percentage between 0 and 100");
							return false;
						}
						return true;
					}
				});
	}

	void showFailureDialog(String title, String message) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				contextForDialogs);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok, null);
		builder.show();
	}
}
