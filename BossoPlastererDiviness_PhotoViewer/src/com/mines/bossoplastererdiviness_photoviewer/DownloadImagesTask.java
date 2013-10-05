package com.mines.bossoplastererdiviness_photoviewer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public class DownloadImagesTask extends AsyncTask<DbxFileSystem, Void, Boolean> {

	private ProgressDialog loadingDialog;
	private Activity activity;
	private OnTaskCompleted listener;
	private ArrayList<?> image;
	private List<DbxFileInfo> filesInfo;
	public static final String PATH = "/";
	
	public DownloadImagesTask(Activity activity) {
		this.activity = activity;
		//this.listener=listener;
		filesInfo = new ArrayList<DbxFileInfo>();
	}
	@Override
	protected void onPreExecute() {
		loadingDialog = new ProgressDialog(activity);
		loadingDialog.setTitle("Downloading Images");
		loadingDialog.setMessage("Please wait...");
		loadingDialog.show();
	}

	@Override
	protected Boolean doInBackground(DbxFileSystem... params) {
		try{
			DbxFileSystem filesystem = params[0];
			if (filesystem == null){
				Log.d("mine", "OMG ITS NULL");
			}
			DbxPath path = new DbxPath(PATH);
			Log.d("mine", "In background task2" + params.length);
			filesInfo = filesystem.listFolder(path);
			Log.d("mine", "filesInfo size: " + filesInfo.size());
			for (DbxFileInfo fileInfo: filesInfo) {
				Log.d("mine", fileInfo.path.getName());
			}
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("mine", "We got an error");
		} finally {
			loadingDialog.dismiss();
		}
		return true;
	}

	


	@Override 
	protected void onPostExecute(Boolean result) {
		//listener.onTaskCompleted(image);
		//loadingDialog.dismiss();
		Log.d("mine", "Post execute");
		
	}

	

}
