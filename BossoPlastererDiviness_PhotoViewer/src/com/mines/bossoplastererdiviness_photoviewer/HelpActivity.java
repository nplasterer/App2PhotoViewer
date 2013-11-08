package com.mines.bossoplastererdiviness_photoviewer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_about);
		TextView text = (TextView) findViewById(R.id.help_about_text);
		text.setText(R.string.help_text);
	}

}
