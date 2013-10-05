package com.mines.bossoplastererdiviness_photoviewer;

import java.io.File;
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

public class DownloadImagesTask extends AsyncTask<DbxFileSystem, Void, Boolean> {

	private ProgressDialog loadingDialog;
	private Activity activity;
	private OnTaskCompleted listener;
	private ArrayList<Bitmap> images;
	private List<DbxFileInfo> filesInfo;
	private File storageDir;
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
		try {
			DbxFileSystem filesystem = params[0];
			DbxPath path = new DbxPath(PATH);
			filesInfo = filesystem.listFolder(path);
			for (DbxFileInfo fileInfo: filesInfo) {
				String filename = fileInfo.path.getName();
				if (!Arrays.asList(activity.fileList()).contains(filename)) {
					DbxFile file = filesystem.open(fileInfo.path);
					Bitmap image = bitmapFactory.decodeStream(file.getReadStream());
					saveFile(image, filename);
					file.close();
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(activity.getBaseContext(), "Download stopping", Toast.LENGTH_SHORT).show();
			return false;
		} finally {
			loadingDialog.dismiss();
		}
		return true;
	}

	


	@Override 
	protected void onPostExecute(Boolean result) {
		listener.onTaskCompleted();	
	}
	
	protected void saveFile(Bitmap image, String filename) {
		//filename += ".png";
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
