package com.mines.bossoplastererdiviness_photoviewer;

/*Emulator Options
 * device: Nexus 7
 * target: JellyBean 4.1.2 SDK version 16
 */

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFileSystem;


/**
 * Description: This application connects with dropbox and an application specific folder to give
 * the user the ability to browse a set folder of images either by thumbnail or slideshow.
 * 
 * Documentation Statement: We worked on this as a team. All of this code is original. When we got
 * stuck we used stackoverflow.com for hints and the Dropbox API as documentation.
 * 
 * @author Naomi Plasterer
 * @author Brandon Bosso
 * @author Austin Diviness
 */
public class MainActivity extends Activity {
	private DbxAccountManager accountManager;
	private static final String APP_KEY = "";
	private static final String APP_SECRET = "";
	public static final int DROPBOX_REQUEST_LINK = 0;
	private DbxFileSystem dbxFs = null;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		accountManager = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
		if(!accountManager.hasLinkedAccount()) {
			View slideshow = findViewById(R.id.start_slideshow);
			slideshow.setVisibility(View.GONE);
			View photos = findViewById(R.id.view_photos);
			photos.setVisibility(View.GONE);
		}
		else {
			View add = findViewById(R.id.add_account);
			add.setVisibility(View.GONE);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.remove_account:
			accountManager.unlink();
			dbxFs = null;
			View slideshow = findViewById(R.id.start_slideshow);
			slideshow.setVisibility(View.GONE);
			View photos = findViewById(R.id.view_photos);
			photos.setVisibility(View.GONE);
			View add = findViewById(R.id.add_account);
			add.setVisibility(View.VISIBLE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		//accountManager.unlink();
	}
	/**
	 * This creates an intent when clicked and sends it to the slideshow activity.
	 * 
	 * @param view -the view that was clicked
	 */
	public void startSlideshow(View view){
		Intent slideshow = new Intent(this, SlideshowActivity.class);
		slideshow.putExtra("dbxFs", (Serializable)dbxFs);
		startActivity(slideshow);	
	}
	
	/**
	 * This creates an intent when clicked and sends it to the view photos activity.
	 * 
	 * @param view -the view that was clicked
	 */
	public void viewPhotos(View view){
		Intent viewphotos = new Intent(this, ViewPhotosActivity.class);
		startActivity(viewphotos);	
	}
	
	/**
	 * This sends a user to the external Dropbox website to add there account.
	 * 
	 * @param view -the view that was clicked
	 */
	public void addAccount(View view) {
		try{
			accountManager.startLink((Activity)this, DROPBOX_REQUEST_LINK);
		}catch(Exception e){
			Log.d("log", e.getMessage());
		}
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (accountManager.hasLinkedAccount()){
			Log.d("log", "has linked account");
		}
		if (requestCode == DROPBOX_REQUEST_LINK) {
			if (resultCode == Activity.RESULT_OK) {
				//lets the user know that there account has been linked
				Toast.makeText(getApplicationContext(), "linked", Toast.LENGTH_LONG).show();
				View slideshow = findViewById(R.id.start_slideshow);
				slideshow.setVisibility(View.VISIBLE);
				View photos = findViewById(R.id.view_photos);
				photos.setVisibility(View.VISIBLE);
				View add = findViewById(R.id.add_account);
				add.setVisibility(View.GONE);
				try {
					dbxFs = DbxFileSystem.forAccount(accountManager.getLinkedAccount());
				} catch (Unauthorized e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
