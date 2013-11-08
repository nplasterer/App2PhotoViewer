package com.mines.bossoplastererdiviness_photoviewer;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	Context context;
	ArrayList<Bitmap> images;
	
	public ImageAdapter(Context c, ArrayList<Bitmap> images){
		this.context = c;
		this.images = images;
	}

	public int getCount() {
		return images.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;


			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.image_helper, null);


			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_image);

			imageView.setImageBitmap(images.get(position));

		return gridView;
	}

}
