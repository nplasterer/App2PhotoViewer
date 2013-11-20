package com.mines.bossoplastererdiviness_photoviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxFileSystem.ThumbFormat;
import com.dropbox.sync.android.DbxFileSystem.ThumbSize;
import com.dropbox.sync.android.DbxPath;
/**
 * This class displays the user's photos in a gridView. When a thumbnail is clicked, the image is 
 * displayed fullscreen via ImageContainer.
 * 
 * @author Brandon Bosso
 * @author Naomi Plasterer
 * @author Marcus Bermel
 * @author Austin Diviness
 */
public class ViewPhotosActivity extends Activity {

	GridView gridView;
	private List<DbxFileInfo> filesInfo;
	private DbxFileSystem fileSystem;
	private DbxAccountManager accountManager;
	private ArrayList<Bitmap> pix;
	private ArrayList<DbxPath> paths;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		filesInfo = new ArrayList<DbxFileInfo>();
		pix = new ArrayList<Bitmap>();
		paths = new ArrayList<DbxPath>();
		accountManager = DbxAccountManager.getInstance(getApplicationContext(), MainActivity.APP_KEY, MainActivity.APP_SECRET);
		try {
			fileSystem = DbxFileSystem.forAccount(accountManager.getLinkedAccount());
		} catch (Unauthorized e) {
			e.printStackTrace();
		}

		try {
			filesInfo = fileSystem.listFolder(DbxPath.ROOT);
		} catch (DbxException e) {
			e.printStackTrace();
		}

		// Adds thumbnail of each image from Dropbox to an ArrayList
		for (DbxFileInfo fileInfo: filesInfo) {
			String filename = fileInfo.path.getName();
			DbxFile file;
			try{
				file = fileSystem.openThumbnail(fileInfo.path, ThumbSize.M, ThumbFormat.PNG);
				Bitmap image = BitmapFactory.decodeStream(file.getReadStream());
				pix.add(image);
				paths.add(fileInfo.path);
				file.close();
			}catch (DbxException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.gc();
		}

		setContentView(R.layout.view_photos);

		gridView = (GridView) findViewById(R.id.gridView1);
		
		// Populates the gridView with the thumbnails opened earlier
		ImageAdapter adapter = new ImageAdapter(this, pix);
		// Attaches imageAdapter to gridView
		gridView.setAdapter(adapter);
		
		// sets the onClick listener for each element of the gridView
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long row) {
				DbxPath path = paths.get(position);
				String name = path.getName();
				Intent i = new Intent(getApplicationContext(), ImageContainer.class);
				i.putExtra("name", name);
				startActivity(i);
			}
		});
	}

}