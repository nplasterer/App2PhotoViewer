package com.mines.bossoplastererdiviness_photoviewer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This class displays a description which describes who the authors are.
 * 
 * @author Brandon Bosso
 * @author Naomi Plasterer
 * @author Marcus Bermel
 * @author Austin Diviness
 */
public class AboutActivity extends Activity {
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_about);
	}

}
