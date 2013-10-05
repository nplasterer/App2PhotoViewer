package com.mines.bossoplastererdiviness_photoviewer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public class DownloadImagesTask extends AsyncTask<DbxFileSystem, Void, Boolean> {

	private ProgressDialog loadingDialog;
	private Activity activity;
	private OnTaskCompleted listener;
	private ArrayList<Bitmap> images;
	private List<DbxFileInfo> filesInfo;
	public static final String PATH = "/";
	
	public DownloadImagesTask(Activity activity, OnTaskCompleted listener) {
		this.activity = activity;
		this.listener=listener;
		filesInfo = new ArrayList<DbxFileInfo>();
		images = new ArrayList<Bitmap>();
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
		BitmapFactory bitmapFactory = new BitmapFactory();
		try{
			DbxFileSystem filesystem = params[0];
			DbxPath path = new DbxPath(PATH);
			filesInfo = filesystem.listFolder(path);
			for (DbxFileInfo fileInfo: filesInfo) {
				DbxFile file = filesystem.open(fileInfo.path);
				Bitmap image = bitmapFactory.decodeStream(file.getReadStream());
				images.add(image);
				file.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			loadingDialog.dismiss();
		}
		return true;
	}

	


	@Override 
	protected void onPostExecute(Boolean result) {
		listener.onTaskCompleted(images);	
	}

	

}