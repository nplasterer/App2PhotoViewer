package com.mines.bossoplastererdiviness_photoviewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;

public class SettingsActivity extends PreferenceActivity {
	
	private Activity mainActivity;
	
	// TODO double check deprecation
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		addPreferencesFromResource(R.layout.settings);
		mainActivity = this;
		Preference unlinkAccount = (Preference) findPreference("unlink");
		unlinkAccount.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

					@Override
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
				AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
				builder.setMessage(getResources().getString(R.string.unlink_warning));
				builder.setPositiveButton(getResources().getString(R.string.yes), dialogClickListener);
				builder.setNegativeButton(getResources().getString(R.string.no), dialogClickListener);
				builder.show();
				return true;
			}
		});
	}

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
