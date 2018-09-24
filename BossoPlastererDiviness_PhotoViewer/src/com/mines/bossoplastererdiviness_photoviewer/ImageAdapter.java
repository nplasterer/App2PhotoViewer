package com.mines.bossoplastererdiviness_photoviewer;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
/**
 * This class allows the gridView to display a list of photo thumbnails.
 * 
 * @author Brandon Bosso
 * @author Naomi Plasterer
 * @author Marcus Bermel
 * @author Austin Diviness
 */
public class ImageAdapter extends BaseAdapter {
	Context context;
	ArrayList<Bitmap> images;
	
	/**
	 * Sets the context and images to the given parameters
	 * 
	 * @param c
	 * @param images
	 */
	public ImageAdapter(Context c, ArrayList<Bitmap> images){
		this.context = c;
		this.images = images;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 * Returns the number of images being displayed
	 */
	public int getCount() {
		return images.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 * 
	 * Populates each element of the gridView with a separate image. Then inflates each of these
	 * and returns the fully populated gridView.
	 * 
	 */
	public View getView(int position, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;


			gridView = new View(context);

			// inflate image_helper layout
			gridView = inflater.inflate(R.layout.image_helper, null);


			// set images
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_image);

			imageView.setImageBitmap(images.get(position));

		return gridView;
	}

}
