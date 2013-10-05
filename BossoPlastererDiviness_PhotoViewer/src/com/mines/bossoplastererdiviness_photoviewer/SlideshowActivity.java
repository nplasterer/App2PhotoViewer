package com.mines.bossoplastererdiviness_photoviewer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
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
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		downloadTask = new DownloadImagesTask(this, this);
		downloadTask.execute(filesystem);
	}
	
	@Override
	public void onStart() {
		super.onStart();

	}
	
	public void updateSlideshow() {
		int index = imageIndex % images.size();
		ImageView container = (ImageView) findViewById(R.id.slideshow_container);
		container.setImageBitmap(images.get(index));
		imageIndex++;
	}
	
	public void startSlideshow() {
		imageIndex = 0;
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
