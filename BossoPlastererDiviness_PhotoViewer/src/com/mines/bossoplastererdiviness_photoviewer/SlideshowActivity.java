package com.mines.bossoplastererdiviness_photoviewer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import com.dropbox.sync.android.DbxFileSystem;

/**
 * Description: Displays slideshow of photos
 * 
 * @author Naomi Plasterer
 * @author Brandon Bosso
 * @author Austin Diviness
 */
public class SlideshowActivity extends Activity {
	private DbxFileSystem dbxFs = null;
	private DownloadImagesTask downloadTask;
	private ArrayList<Bitmap> images;
	private int imageIndex;
	private Timer timer;
	private int delay;
	private int period;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_slideshow);
		dbxFs = (DbxFileSystem) getIntent().getExtras().getSerializable("dbxFs");
		images = new ArrayList<Bitmap>();
		imageIndex = 0;
		delay = 0;
		period = 3000;
		timer = new Timer();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		downloadTask = new DownloadImagesTask(this);
		downloadTask.execute();
	}
	
	public void startSlideshow() {
		imageIndex = 0;
		slideshow();
	}
	
	public void slideshow() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				int index = imageIndex % images.size();
				ImageView container = (ImageView) findViewById(R.id.slideshow_container);
				container.setImageBitmap(images.get(index));
				imageIndex++;
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

}
