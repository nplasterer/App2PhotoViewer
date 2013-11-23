package com.mines.bossoplastererdiviness_photoviewer;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Display;
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
 * @author Marcus Bermel
 * @author Naomi Plasterer
 * @author Brandon Bosso
 * @author Austin Diviness
 */
public class SlideshowActivity extends Activity {
	private DbxFileSystem filesystem = null;
	private List<DbxFileInfo> files;
	private int imageIndex;
	private Timer timer;
	private int delay;
	private double period;
	private Handler handler;
	private Runnable slideShowRunnable;
	private boolean slideshowStarted;
	private Bitmap currentBitmap;
	private Bitmap nextBitmap;
	public static final int WIFI_SETTINGS_REQUEST = 1;
	public static final int DOWNLOAD_IMAGES_TASK_REQUEST = 2;
	private static final int DEFAULT_SPEED = 3000;
	private static final int MILLISECONDS = 1000;
	private ImageScaler imageScaler;
	
	
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
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// set instance variables
		imageIndex = 0;
		delay = 0;
		imageScaler = new ImageScaler(this);
		String slideshowSpeedPref = prefs.getString("slideshow_speed", "none");
		if (slideshowSpeedPref.equals("none") || slideshowSpeedPref.equals("")) {
			period = DEFAULT_SPEED;
		}
		else {
			period = Float.valueOf(slideshowSpeedPref) * MILLISECONDS;
		}
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
			files = filesystem.listFolder(DbxPath.ROOT);
		} catch (Unauthorized e) {
			e.printStackTrace();
		} catch (InvalidPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		if(slideshowStarted) {
			imageIndex--;
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					handler.post(slideShowRunnable);
				}
			}, delay, (long) period); }
		else {
			startSlideshow();
		}
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
		currentBitmap = nextBitmap;
		ImageView container = (ImageView) findViewById(R.id.slideshow_container);
		container.setImageBitmap(currentBitmap);
		loadBitmap(BitmapSelect.NEXT);
		setIndexNextFile();
	}
	
	/**
	 * Runs the slideshow.
	 */
	public void startSlideshow() {
		slideshowStarted = true;
		imageIndex = 0;
		timer = new Timer();
		initialBitmapLoad();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				handler.post(slideShowRunnable);
			}
		}, delay, (long) period);
	}

    /**
     * initial set up for loading bitmaps for slideshow to use.
     */
	public void initialBitmapLoad() {
		ImageView container = (ImageView) findViewById(R.id.slideshow_container);
		currentBitmap = nextBitmap;
		loadBitmap(BitmapSelect.CURRENT);
		container.setImageBitmap(currentBitmap);
		setIndexNextFile();
		loadBitmap(BitmapSelect.NEXT);
		setIndexNextFile();
		
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
     * increments index so that the index relates to a file.
     */
	private void setIndexNextFile() {
		++imageIndex;
		imageIndex = imageIndex % files.size();
		while (files.get(imageIndex).isFolder) {
			imageIndex = (1 + imageIndex) % files.size();
		}
	}
	
    /**
     * loads a bitmap.
     *
     * @param which The variable to load the bitmap to
     */
	private void loadBitmap(BitmapSelect which) {
		DbxFile file = null;
		Bitmap bitmap = null;
		try {

			file = filesystem.open(files.get(imageIndex).path);
			// set up bitmap options
			bitmap = BitmapFactory.decodeStream(file.getReadStream(), null, imageScaler.setBitmapOptions(file));
			if (which == BitmapSelect.CURRENT) {
				currentBitmap = bitmap;
			} else {
				nextBitmap = bitmap;
			}
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			file.close();
		}
	}
}
