package com.mines.bossoplastererdiviness_photoviewer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadImagesTask extends AsyncTask<Void, Void, Boolean> {

	private ProgressDialog loadingDialog;
	private Activity context;
	
	public DownloadImagesTask(Activity activity) {
		this.context = activity;
		Log.d("mine", "" + context);
	}
	@Override
	protected void onPreExecute() {
		loadingDialog = new ProgressDialog(context);
		loadingDialog.setTitle("Downloading Images");
		loadingDialog.setMessage("Please wait...");
		loadingDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	


	@Override 
	protected void onPostExecute(Boolean result) {
		loadingDialog.dismiss();
		
	}

	

}
