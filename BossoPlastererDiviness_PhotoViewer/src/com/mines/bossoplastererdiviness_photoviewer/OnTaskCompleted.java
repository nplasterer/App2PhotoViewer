package com.mines.bossoplastererdiviness_photoviewer;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.dropbox.sync.android.DbxPath;

/**
* Description: The interface implemented by the AsyncTask LoadThumbnails
*
* @author Austin Diviness
* @author Naomi Plasterer
* @author Brandon Bosso
* @author Marcus Bermel
*/
public interface OnTaskCompleted {
        void onTaskCompleted(int requestID, ArrayList<Bitmap> pix, ArrayList<DbxPath> paths);
}