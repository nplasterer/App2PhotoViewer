package com.mines.bossoplastererdiviness_photoviewer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFileSystem;

/**
 * Description: Displays slideshow of photos
 * 
 * @author Naomi Plasterer
 * @author Brandon Bosso
 * @author Austin Diviness
 */
public class SlideshowActivity extends Activity implements OnTaskCompleted {
	private DbxFileSystem filesystem = null;
	private DownloadImagesTask downloadTask;
	private ArrayList<Bitmap> images;
	private int imageIndex;
	private Timer timer;
	private int delay;
	private int period;
	private Handler handler;
	private Runnable slideShowRunnable;
	private boolean slideshowStarted;
	public static final int WIFI_SETTINGS_REQUEST = 1;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_slideshow);
		// set fullscreen window attributes TODO can this be moved to xml layout?
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set instance variables
		images = new ArrayList<Bitmap>();
		imageIndex = 0;
		delay = 0;
		period = 3000;
		timer = new Timer();
		slideshowStarted = false;
		handler = new Handler();
		slideShowRunnable = new Runnable() {
			public void run() {
				updateSlideshow();
			}
		};
		// get dropbox filesystem
		DbxAccountManager accManager = DbxAccountManager.getInstance(getApplicationContext(), MainActivity.APP_KEY, MainActivity.APP_SECRET);
		DbxAccount account = accManager.getLinkedAccount();
		try {
			filesystem = DbxFileSystem.forAccount(account);
		} catch (Unauthorized e) {
			e.printStackTrace();
		}	
		// check for enabled wifi
		wifiCheck();
	}
	
	/**
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 * Called on activity result. Currently used only to check returning from wifi 
	 * settings menu.
	 *
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == WIFI_SETTINGS_REQUEST) {
			download();
		}
	}
	
	/**
	 * Starts async task to download images from dropbox.
	 */
	public void download() {
		downloadTask = new DownloadImagesTask(this, this);
		downloadTask.execute(filesystem);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(fileList().length > 0 && slideshowStarted) {
			imageIndex--;
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					handler.post(slideShowRunnable);
				}
			}, delay, period); }
	}
	
	@Override
	public void onStop() {
		super.onStop();
		timer.cancel();
	}
	
	public void updateSlideshow() {
		int index = imageIndex % fileList().length;
		Log.d("mine", fileList()[index]);
		Bitmap myBitmap = BitmapFactory.decodeFile(getFilesDir() + "/" + fileList()[index]);
		ImageView container = (ImageView) findViewById(R.id.slideshow_container);
		container.setImageBitmap(myBitmap);
		imageIndex++;
	}
	
	/**
	 * Runs the slideshow.
	 */
	public void startSlideshow() {
		slideshowStarted = true;
		imageIndex = 0;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				handler.post(slideShowRunnable);
			}
		}, delay, period);
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

	/**
	 * Callback function used to start the slideshow if images to show exist.
	 */
	@Override
	public void onTaskCompleted() {
		if (fileList().length > 0) {
			startSlideshow();
		} else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_images), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * checks if wifi is enabled, creating a dialog to check if it's ok to 
	 * continue downloading if it is not.
	 */
	public void wifiCheck() {
		WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled()){
			download();
		}
		else {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
					Boolean shouldDownload = false;
			        switch (which) {
			        case DialogInterface.BUTTON_POSITIVE:
						shouldDownload = true;
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			            break;
			        
			        case DialogInterface.BUTTON_NEUTRAL:
			        	//Open wifi settings
			        	Intent wifiSettings = new Intent(Settings.ACTION_WIFI_SETTINGS);
			        	startActivityForResult(wifiSettings, WIFI_SETTINGS_REQUEST);
			        	break;
			        }
					dialog.dismiss();
					if (shouldDownload) {
						download();
					}
			    }
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.data_warning));
			builder.setPositiveButton(getResources().getString(R.string.yes), dialogClickListener);
			builder.setNegativeButton(getResources().getString(R.string.no), dialogClickListener);
			builder.setNeutralButton(getResources().getString(R.string.wifi), dialogClickListener);
			builder.show();
		}
	}

}
