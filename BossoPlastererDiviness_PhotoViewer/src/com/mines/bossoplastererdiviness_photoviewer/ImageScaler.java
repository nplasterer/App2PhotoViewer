package com.mines.bossoplastererdiviness_photoviewer;

import java.io.IOException;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;


/**
 * This class provides an object used to scale images for faster load speeds
 * 
 * @author Brandon Bosso
 * @author Naomi Plasterer
 * @author Marcus Bermel
 * @author Austin Diviness
 */
public class ImageScaler{

	private Activity activity;
	
	public ImageScaler(Activity activity){
		this.activity = activity;
	}
	
	/**
	 * Sets BitmapFactory options for the file.
	 *
	 * @param file The file to create options for
	 *
	 * @return BitmapFactory.Options object specific to the file
	 */
	public BitmapFactory.Options setBitmapOptions(DbxFile file) throws DbxException, IOException {
		Log.d("mine", "get window manager");
		BitmapFactory.Options options = new BitmapFactory.Options();
		BitmapFactory.Options testOptions = new BitmapFactory.Options();
		// get screen size		
		Display display = activity.getWindowManager().getDefaultDisplay();		
		Point size = new Point();
		display.getSize(size);
		// set options
		testOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(file.getReadStream(), null, testOptions);
		options.inSampleSize = calculateSampleSize(testOptions, size.x, size.y);
		return options;
	}

	/** 
	 * Calculates the sample size for a file.
	 *
	 * @param options The BitmapFactory.Options variable that has the tested dimensions of the file
	 * @param width Screen width
	 * @param height Screen height
	 */
	public int calculateSampleSize(BitmapFactory.Options options, int width, int height) {
		int sampleSize = 1;
		if (options.outHeight > height || options.outWidth > width) {
			int heightRatio = Math.round((float) options.outHeight / height);
			int widthRatio = Math.round((float) options.outWidth / width);
			sampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return sampleSize;
	}

}
