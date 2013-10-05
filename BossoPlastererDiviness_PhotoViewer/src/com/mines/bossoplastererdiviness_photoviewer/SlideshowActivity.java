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
	private boolean wifiConnected;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.start_slideshow);
		DbxAccountManager accManager = DbxAccountManager.getInstance(getApplicationContext(), MainActivity.APP_KEY, MainActivity.APP_SECRET);
		DbxAccount account = accManager.getLinkedAccount();
		try {
			filesystem = DbxFileSystem.forAccount(account);
		} catch (Unauthorized e) {
			e.printStackTrace();
		}	
		handler = new Handler();
		slideShowRunnable = new Runnable() {
			public void run() {
				updateSlideshow();
			}
		};
		images = new ArrayList<Bitmap>();
		imageIndex = 0;
		delay = 0;
		period = 3000;
		timer = new Timer();
		wifiConnected = false;
		
		WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		Log.d("mine", wifi.toString());
		if (wifi.isWifiEnabled()){
			wifiConnected = true;
			download();
		}
		else {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			        	wifiConnected = true;
			        	dialog.dismiss();
			        	download();
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			        	dialog.dismiss();
			            break;
			        
			        case DialogInterface.BUTTON_NEUTRAL:
			        	//Open wifi settings
			        	Intent wifiSettings = new Intent(Settings.ACTION_WIFI_SETTINGS);
			        	startActivityForResult(wifiSettings, 1);
			        	wifiConnected = true;
			        	dialog.dismiss();
			        	break;
			        }
			    }
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.data_warning)).setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
				.setNegativeButton(getResources().getString(R.string.no), dialogClickListener).setNeutralButton(getResources().getString(R.string.wifi), dialogClickListener).show();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1) {
			wifiConnected = true;
			download();
		}
	}
	
	public void download() {
		if(wifiConnected) {
			downloadTask = new DownloadImagesTask(this, this);
			downloadTask.execute(filesystem);
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(images.size() > 0) {
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
		int index = imageIndex % images.size();
		ImageView container = (ImageView) findViewById(R.id.slideshow_container);
		container.setImageBitmap(images.get(index));
		imageIndex++;
	}
	
	public void startSlideshow() {
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

	@Override
	public void onTaskCompleted(ArrayList<Bitmap> image) {
		images = image;
		if (images.size() > 0) {
			startSlideshow();
		} else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_images), Toast.LENGTH_LONG).show();
		}
	}

}
