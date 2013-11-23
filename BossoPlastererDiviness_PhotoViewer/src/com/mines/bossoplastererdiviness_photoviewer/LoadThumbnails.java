package com.mines.bossoplastererdiviness_photoviewer;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxFileSystem.ThumbFormat;
import com.dropbox.sync.android.DbxFileSystem.ThumbSize;
import com.dropbox.sync.android.DbxPath;
/**
 * This class loads the thumbnails for the gridView on a separate thread to free the UI
 * 
 * @author Brandon Bosso
 * @author Naomi Plasterer
 * @author Marcus Bermel
 * @author Austin Diviness
 */
public class LoadThumbnails extends AsyncTask<DbxFileSystem, Void, Boolean> {

	private ProgressDialog loadingDialog;
	private Activity activity;
	private OnTaskCompleted listener;
	private int requestID;
	private ArrayList<Bitmap> pix;
	private ArrayList<DbxPath> paths;
	

	public LoadThumbnails(Activity activity, OnTaskCompleted listener, int requestID) {
		this.activity = activity;
		this.listener = listener;
		this.requestID = requestID;
		pix = new ArrayList<Bitmap>();
		paths = new ArrayList<DbxPath>();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		//Loading bar to show that images are downloading
		loadingDialog = new ProgressDialog(activity);
		loadingDialog.setTitle(activity.getResources().getString(R.string.downloading_images));
		loadingDialog.setMessage(activity.getResources().getString(R.string.please_wait));
		loadingDialog.show();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Boolean doInBackground(DbxFileSystem... params) {
		//Opens thumbnails for each image contained in the dropbox folder
		try {
			DbxFileSystem fileSystem = params[0];
			for (DbxFileInfo fileInfo: fileSystem.listFolder(DbxPath.ROOT)) {
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
		}
		catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(activity.getBaseContext(), activity.getResources().getString(R.string.download_stopping), Toast.LENGTH_SHORT).show();
			return false;
		} finally {
			loadingDialog.dismiss();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	//@Override
	protected void onPostExecute(Boolean result) {
		listener.onTaskCompleted(requestID, pix, paths);        
	}

}
