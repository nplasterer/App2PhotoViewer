package com.mines.bossoplastererdiviness_photoviewer;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

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
		if (filesystem == null){
			Log.d("mine", "OMG ITS NULL");
		}
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("mine", "In on start");
		downloadTask = new DownloadImagesTask(this);
		downloadTask.execute(filesystem);
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
	public void onTaskCompleted(ArrayList<?> image) {
		
	}

}
