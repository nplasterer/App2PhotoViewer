package com.mines.bossoplastererdiviness_photoviewer;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxException.Unauthorized;

/**
 * This class handles the fullscreen display of an image from the thumbnail view
 * 
 * @author Brandon Bosso
 * @author Naomi Plasterer
 * @author Marcus Bermel
 * @author Austin Diviness
 */
public class ImageContainer extends Activity {
	
	private DbxFileSystem fileSystem;
	private DbxAccountManager accountManager;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_container);
        ImageView iView = (ImageView) findViewById(R.id.image_container);
        
        //Opens the image contained in the grid element that was clicked by the user
        accountManager = DbxAccountManager.getInstance(getApplicationContext(), MainActivity.APP_KEY, MainActivity.APP_SECRET);
        try {
			fileSystem = DbxFileSystem.forAccount(accountManager.getLinkedAccount());
		} catch (Unauthorized e) {
			e.printStackTrace();
		}
        Intent i = getIntent();
        String filename = i.getStringExtra("name");
        DbxPath path = new DbxPath(filename);
        DbxFile file = null;
        Bitmap image = null;
        try {
			file = fileSystem.open(path);
			ImageScaler imageScaler = new ImageScaler(this);
			
			image = BitmapFactory.decodeStream(file.getReadStream(), null, imageScaler.setBitmapOptions(file));
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        //Populates the ImageView with the opened bitmap
        iView.setImageBitmap(image);
    }

}
