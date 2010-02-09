package edu.ucla.cens.budburst.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.ucla.cens.budburst.R;
import edu.ucla.cens.budburst.DownloadManager;
import edu.ucla.cens.budburst.data.Row;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class RowWithImageAdapter extends LazyAdapter {

	public RowWithImageAdapter(Context context, DownloadManager dm, ArrayList<HashMap<String, Object>> arrayList, List<Row> rows,
			int resource, String[] from, int[] to) {
		super(context, dm,  arrayList, resource, from, to);
		// TODO Auto-generated constructor stub

		for(Iterator<Row> i = rows.iterator();i.hasNext();) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for(int j=0; j< from.length ; j++) {
				try {
					map.put(from[j], i.next().getClass().getField(from[j]));
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			arrayList.add(map);
		}
	}




}