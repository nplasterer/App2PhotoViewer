package com.mines.bossoplastererdiviness_photoviewer;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;

public class LoadImageTask extends AsyncTask<DbxFile, Void, Bitmap> {

	@Override
	protected Bitmap doInBackground(DbxFile... params) {
		DbxFile file = params[0];		
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPurgeable = true;
		bitmapOptions.inSampleSize = 2;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(file.getReadStream(), null, bitmapOptions);
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			file.close();
		}
		return bitmap;

	}

}
