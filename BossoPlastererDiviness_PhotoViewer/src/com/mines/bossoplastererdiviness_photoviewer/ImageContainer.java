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

public class ImageContainer extends Activity {
	
	private DbxFileSystem fileSystem;
	private DbxAccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("mine", "receive intent");
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_container);
        ImageView iView = (ImageView) findViewById(R.id.image_container);
        
        accountManager = DbxAccountManager.getInstance(getApplicationContext(), MainActivity.APP_KEY, MainActivity.APP_SECRET);
        try {
			fileSystem = DbxFileSystem.forAccount(accountManager.getLinkedAccount());
		} catch (Unauthorized e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Intent i = getIntent();
        String filename = i.getStringExtra("name");
        DbxPath path = new DbxPath(filename);
        DbxFile file = null;
        Bitmap image = null;
        try {
			file = fileSystem.open(path);
			image = BitmapFactory.decodeStream(file.getReadStream());
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        iView.setImageBitmap(image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_image_container, menu);
        return true;
    }
}
