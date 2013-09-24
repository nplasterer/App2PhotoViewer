package com.mines.bossoplastererdiviness_photoviewer;

/*Emulator Options
 * device: Nexus 7
 * target: JellyBean 4.1.2 SDK version 16
 */
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


/**
 * Description:
 * 
 * Documentation Statement:
 * 
 * @author Naomi Plasterer Brandon Bosso Austin Diviness
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
