package com.mines.bossoplastererdiviness_photoviewer;

import com.dropbox.sync.android.DbxFileSystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Description: Displays slideshow of photos
 * 
 * @author Naomi Plasterer
 * @author Brandon Bosso
 * @author Austin Diviness
 */
public class SlideshowActivity extends Activity {
	private DbxFileSystem dbxFs = null;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_slideshow);
		dbxFs = (DbxFileSystem)getIntent().getExtras().getSerializable("dbxFs");
	}
	
	@Override
	public void onStart(){
		
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
