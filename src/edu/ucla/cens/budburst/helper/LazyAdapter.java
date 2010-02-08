package edu.ucla.cens.budburst.helper;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import edu.ucla.cens.budburst.R;
import edu.ucla.cens.budburst.DownloadManager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class LazyAdapter extends SimpleAdapter implements Downloadable{
	
	public LazyAdapter(Context context, DownloadManager dm, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		this.dm = dm;
		Log.d(TAG,"this is a " + this.toString());
	}

	protected static final int REFRESH = 0;
	private static final int DOWNLOADED_IMAGE = 0;
	private static final String TAG = "LazyAdapter";


	private DownloadManager dm;

	public void onDownloaded(Message msg, Download d) {
		Log.d(TAG, "onDownloaded");
		switch(msg.what){
		case DOWNLOADED_IMAGE:
			Log.d(TAG,"in onDownloaded");
			notifyDataSetChanged();
			break;
		}
	}

	public Object consumeInputStream(Message msg) {
		Log.d(TAG, "consumeInputStream");
		switch(msg.what){
		case DOWNLOADED_IMAGE:
			Log.d(TAG, "in consumeInputStream");
			return Drawable.createFromStream((InputStream) msg.obj, "src");
		}
		return null;
	}
	

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View ret = super.getView(position, convertView, parent);
		if (ret != null) {
			LazyListImageView iv = (LazyListImageView) ret.findViewById(R.id.icon);
			if (iv != null) {
				//Log.d(TAG,"get remote uri " + iv.getRemoteURI());
				Drawable drawable = (Drawable) dm.get(this, new Download(iv.getRemoteURI()));
				if(drawable!=null)
				iv.setImageDrawable(drawable);
			}
		}
		return ret;
	}

	@Override
	public void setViewImage(final ImageView image, final String value) {
		if (value != null && value.length() > 0 && image instanceof LazyListImageView) {
			LazyListImageView riv = (LazyListImageView) image;
			riv.setRemoteURI(value);
			if(!dm.has(new Download(value)))
				dm.download(this, DOWNLOADED_IMAGE, new Download(value));
			super.setViewImage(image, R.drawable.icon);
		} else {
			image.setVisibility(View.GONE);
		}
	}
	



}