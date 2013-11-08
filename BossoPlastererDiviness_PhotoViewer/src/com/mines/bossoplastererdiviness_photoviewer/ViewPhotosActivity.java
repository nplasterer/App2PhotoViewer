package com.mines.bossoplastererdiviness_photoviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
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

public class ViewPhotosActivity extends Activity {

	GridView gridView;
	private List<DbxFileInfo> filesInfo;
	private DbxFileSystem fileSystem;
	private DbxAccountManager accountManager;
	private ArrayList<Bitmap> pix;



	public ViewPhotosActivity(){
		filesInfo = new ArrayList<DbxFileInfo>();
		pix = new ArrayList<Bitmap>();


	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		accountManager = DbxAccountManager.getInstance(getApplicationContext(), MainActivity.APP_KEY, MainActivity.APP_SECRET);
		//DbxAccountManager accManager = DbxAccountManager.getInstance(getApplicationContext(), MainActivity.APP_KEY, MainActivity.APP_SECRET);
		try {
			fileSystem = DbxFileSystem.forAccount(accountManager.getLinkedAccount());
		} catch (Unauthorized e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			filesInfo = fileSystem.listFolder(DbxPath.ROOT);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (DbxFileInfo fileInfo: filesInfo) {
			String filename = fileInfo.path.getName();
			DbxFile file;
			try{

				//file = fileSystem.open(fileInfo.path);
				file = fileSystem.openThumbnail(fileInfo.path, ThumbSize.M, ThumbFormat.PNG);
				Bitmap image = BitmapFactory.decodeStream(file.getReadStream());
				pix.add(image);
				file.close();
			}catch (DbxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.gc();

		}


		


		setContentView(R.layout.view_photos);

		gridView = (GridView) findViewById(R.id.gridView1);

		ImageAdapter adapter = new ImageAdapter(this, pix);
		gridView.setAdapter(adapter);
	}

}