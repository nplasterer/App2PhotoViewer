package com.mines.bossoplastererdiviness_photoviewer;

/*Emulator Options
 * device: Nexus 7
 * target: JellyBean 4.1.2 SDK version 16
 */

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;


/**
 * Description:
 * 
 * Documentation Statement:
 * 
 * @author Naomi Plasterer
 * @author Brandon Bosso
 * @author Austin Diviness
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

	public void startSlideshow(View view){
		Intent slideshow = new Intent(this, SlideshowActivity.class);
		startActivity(slideshow);	
	}
	
	public void viewPhotos(View view){
		Intent viewphotos = new Intent(this, ViewPhotosActivity.class);
		startActivity(viewphotos);	
	}
}
