package com.mines.bossoplastererdiviness_photoviewer;

import java.io.IOException;
import java.util.List;
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
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxPath.InvalidPathException;

/**
 * Description: Displays slideshow of photos
 * 
 * @author Naomi Plasterer
 * @author Brandon Bosso
 * @author Austin Diviness
 */
public class SlideshowActivity extends Activity implements OnTaskCompleted {
	private DbxFileSystem filesystem = null;
	private List<DbxFileInfo> files;
	private int imageIndex;
	private Timer timer;
	private int delay;
	private int period;
	private Handler handler;
	private Runnable slideShowRunnable;
	private boolean slideshowStarted;
	public static final int WIFI_SETTINGS_REQUEST = 1;
	public static final String PATH = "/";
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set fullscreen window attributes TODO can this be moved to xml layout?
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.start_slideshow);
		// set instance variables
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
			files = filesystem.listFolder(new DbxPath(PATH));
		} catch (Unauthorized e) {
			e.printStackTrace();
		} catch (InvalidPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		// check for enabled wifi
		wifiCheck();
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		timer.cancel();
	}
	
	/**
	 * A function that increments the counter and makes the new image appear on screen.
	 */
	public void updateSlideshow() {
		System.gc();
		imageIndex = imageIndex % files.size();
		while (files.get(imageIndex).isFolder) {
			imageIndex = (1 + imageIndex) % files.size();
		}
		Bitmap bitmap = null;
		DbxFile file = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inSampleSize = 2;
		try {
			file = filesystem.open(files.get(imageIndex).path);
			bitmap = BitmapFactory.decodeStream(file.getReadStream(), null, options);
			ImageView container = (ImageView) findViewById(R.id.slideshow_container);
			container.setImageBitmap(bitmap);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			file.close();
		}
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
			startSlideshow();
		}
		else {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
					Boolean shouldDownload = false;
					Boolean startSlideshow = false;
			        switch (which) {
			        case DialogInterface.BUTTON_POSITIVE:
						startSlideshow = true;
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			        	startSlideshow = true;
			            break;
			        
			        case DialogInterface.BUTTON_NEUTRAL:
			        	//Open wifi settings
			        	Intent wifiSettings = new Intent(Settings.ACTION_WIFI_SETTINGS);
			        	startActivityForResult(wifiSettings, WIFI_SETTINGS_REQUEST);
			        	break;
			        }
					dialog.dismiss();
					if (startSlideshow) {
						startSlideshow();
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
