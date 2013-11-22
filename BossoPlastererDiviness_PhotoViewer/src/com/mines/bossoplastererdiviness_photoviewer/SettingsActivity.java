package com.mines.bossoplastererdiviness_photoviewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;

/**
 * This class displays a description which describes who the authors are.
 * 
 * @author Brandon Bosso
 * @author Naomi Plasterer
 * @author Marcus Bermel
 * @author Austin Diviness
 */
public class SettingsActivity extends PreferenceActivity {
	
	private Activity mainActivity;
	
	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 * 
	 * We suppressed this warning because there are no new ways to create a preference activity.
	 * A preference activity fit our app best and there was no easy or comparable way to implement it other wise.
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.layout.settings);
		mainActivity = this;
		Preference unlinkAccount = (Preference) findPreference("unlink");
		unlinkAccount.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

					/* (non-Javadoc)
					 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
					 */
					//@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							MainActivity.getInstance().unlinkAccount();
							break;
						}
						dialog.dismiss();
					}
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
				builder.setMessage(getResources().getString(R.string.unlink_warning));
				builder.setPositiveButton(getResources().getString(R.string.yes), dialogClickListener);
				builder.setNegativeButton(getResources().getString(R.string.no), dialogClickListener);
				builder.show();
				return true;
			}
		});
	}

	/**
	 * @param view
	 */
	public void onAccountUnlinkClick(View view) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// TODO make sure to change buttons on main screen accordingly
					MainActivity.getInstance().unlinkAccount();
					break;
				}
				dialog.dismiss();
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.unlink_warning));
		builder.setPositiveButton(getResources().getString(R.string.yes), dialogClickListener);
		builder.setNegativeButton(getResources().getString(R.string.no), dialogClickListener);
		builder.show();
	}
}
