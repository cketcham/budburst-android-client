package edu.ucla.cens.budburst.helper;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

// Speeds up list view by caching the image in memory
public class ImageAdapter extends SimpleAdapter {

	public ImageAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
	}
	
	HashMap<String,SoftReference<Drawable>> images = new HashMap<String,SoftReference<Drawable>>();


	@Override
	public void setViewImage(final ImageView image, final String value) {
		if (value != null && value.length() > 0 && image instanceof ImageView) {
			Drawable d = null;
			if(images.containsKey(value)) {
				d = images.get(value).get();
				image.setImageDrawable(d);
			} 
			if(d == null) {
				d = Drawable.createFromPath(value);
				images.put(value, new SoftReference<Drawable>(d));
				image.setImageDrawable(d);
			}
			//super.setViewImage(image, value);
		} else {
			image.setVisibility(View.GONE);
		}
	}

}