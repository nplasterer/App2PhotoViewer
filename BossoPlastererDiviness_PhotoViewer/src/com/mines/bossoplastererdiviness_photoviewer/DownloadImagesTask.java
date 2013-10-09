package com.mines.bossoplastererdiviness_photoviewer;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

/**
 * Description: This class downloads images from dropbox on different threads.
 * 
 * @author Austin Diviness
 * @author Naomi Plasterer
 * @author Brandon Bosso
 */
public class DownloadImagesTask extends AsyncTask<DbxFileSystem, Void, Boolean> {

	private ProgressDialog loadingDialog;
	private Activity activity;
	private OnTaskCompleted listener;
	private List<DbxFileInfo> filesInfo;
	private int requestID;
	
	/**
	 * This is a constructor that sets the variables for this class.
	 * 
	 * @param activity - the calling activity
	 * @param listener - a listener to return content to the calling activity
	 */
	public DownloadImagesTask(Activity activity, OnTaskCompleted listener, int requestID) {
		this.activity = activity;
		this.listener = listener;
		this.requestID = requestID;
		filesInfo = new ArrayList<DbxFileInfo>();
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
		//Downloads images and saves them to a app specific folder on the device
		try {
			DbxFileSystem filesystem = params[0];
			filesInfo = filesystem.listFolder(DbxPath.ROOT);
			for (DbxFileInfo fileInfo: filesInfo) {
				String filename = fileInfo.path.getName();
				if (!Arrays.asList(activity.fileList()).contains(filename)) {
					DbxFile file = filesystem.open(fileInfo.path);
					System.gc();
					Bitmap image = BitmapFactory.decodeStream(file.getReadStream());
					saveFile(image, filename);
					file.close();
				}
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
	@Override 
	protected void onPostExecute(Boolean result) {
		listener.onTaskCompleted(requestID);	
	}
	
	/**
	 * This function saves a bitmap image to a file in the specified app specific folder.
	 * 
	 * @param image - the bitmap image from dropbox
	 * @param filename - the name of the file from dropbox to be saved
	 */
	protected void saveFile(Bitmap image, String filename) {
		try {
			FileOutputStream fileOutStream = activity.openFileOutput(filename, Context.MODE_PRIVATE);
			image.compress(Bitmap.CompressFormat.JPEG, 90, fileOutStream);
			fileOutStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
