package com.mines.bossoplastererdiviness_photoviewer;

/**
 * Emulator Options
 * device: Nexus 7
 * target: JellyBean 4.1.2 SDK version 16
 * 
 * Dropbox Account Information
 * login:
 * password: 
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.dropbox.sync.android.DbxAccountManager;


/**
 * Description: This application connects with dropbox and an application specific folder to give
 * the user the ability to browse a set folder of images either by thumbnail or slideshow.
 * 
 * Documentation Statement: We worked on this as a team. All of this code is original. When we got
 * stuck we used stackoverflow.com for hints and the Dropbox API as documentation.
 * Point Distribution: We would like to distribute the points evenly among all four of us.
 * 
 * @author Marcus Bermel
 * @author Naomi Plasterer
 * @author Brandon Bosso
 * @author Austin Diviness
 */
public class MainActivity extends Activity {
	private static DbxAccountManager accountManager;
	//app specific key and secret provided from dropbox
	public static final String APP_KEY = "ebig093cmc8g6go";
	public static final String APP_SECRET = "nx0ryugdsn42cut";
	public static final int DROPBOX_REQUEST_LINK = 0;
	private static MainActivity instance;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		instance = this;
		//get account manager
		accountManager = DbxAccountManager.getInstance(getApplicationContext(), APP_KEY, APP_SECRET);
		//check to see if an account is already linked
		if(!accountManager.hasLinkedAccount()) {
			View slideshow = findViewById(R.id.start_slideshow);
			slideshow.setVisibility(View.GONE);
			View photos = findViewById(R.id.view_photos);
			photos.setVisibility(View.GONE);
		} else {
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_settings:
			Intent settings = new Intent(this, SettingsActivity.class);
			startActivity(settings);	
			return true;
		case R.id.help:
			Intent help = new Intent(this, HelpActivity.class);
			startActivity(help);
			return true;
		case R.id.about:
			Intent about = new Intent(this, AboutActivity.class);
			startActivity(about);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * This creates an intent when clicked and sends it to the slideshow activity.
	 * 
	 * @param view -the view that was clicked
	 */
	public void startSlideshow(View view) {
		Intent slideshow = new Intent(this, SlideshowActivity.class);
		startActivity(slideshow);	
	}
	
	/**
	 * This creates an intent when clicked and sends it to the view photos activity.
	 * 
	 * @param view -the view that was clicked
	 */
	public void viewPhotos(View view) {
		Intent viewphotos = new Intent(this, ViewPhotosActivity.class);
		startActivity(viewphotos);	
	}
	
	/**
	 * This sends a user to the external Dropbox website to add there account.
	 * 
	 * @param view -the view that was clicked
	 */
	public void addAccount(View view) {
		try {
			accountManager.startLink((Activity)this, DROPBOX_REQUEST_LINK);
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DROPBOX_REQUEST_LINK) {
			if (resultCode == Activity.RESULT_OK) {
				// set visibility of buttons
				View slideshow = findViewById(R.id.start_slideshow);
				slideshow.setVisibility(View.VISIBLE);
				View photos = findViewById(R.id.view_photos);
				photos.setVisibility(View.VISIBLE);
				View add = findViewById(R.id.add_account);
				add.setVisibility(View.GONE);
				
				// Creates a dialog warning the user about data usage
				DialogInterface.OnClickListener dialogClickListener =  new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							dialog.dismiss();
							break;
						
						case DialogInterface.BUTTON_NEGATIVE:
							Intent wifiSettings = new Intent(Settings.ACTION_WIFI_SETTINGS);
							startActivity(wifiSettings);
							dialog.dismiss();
							break;
						}
					}
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				TextView alert = new TextView(this);
				alert.setPadding(2, 5, 2, 5);
				alert.setText(getResources().getString(R.string.data_warning));
				alert.setTextSize(20);
				alert.setGravity(Gravity.CENTER_HORIZONTAL);
				builder.setView(alert)
					.setPositiveButton(getResources().getString(R.string.ok), dialogClickListener)
					.setNegativeButton(getResources().getString(R.string.wifi), dialogClickListener).show();
			}
		}
	}
	
	/**
	 * This method is referenced by the settings activity to unlink the account. 
	 */
	public void unlinkAccount() {
		View slideshow = findViewById(R.id.start_slideshow);
		slideshow.setVisibility(View.GONE);
		View photos = findViewById(R.id.view_photos);
		photos.setVisibility(View.GONE);
		View add = findViewById(R.id.add_account);
		add.setVisibility(View.VISIBLE);
		accountManager.unlink();
	}
	
	/**
	 * returns the instance of the main activity class.
	 */
	public static MainActivity getInstance() {
		return instance;
	}
}
