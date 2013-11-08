package com.mines.bossoplastererdiviness_photoviewer;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
	
	// TODO double check deprecation
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		addPreferencesFromResource(R.layout.settings);
	}

}
