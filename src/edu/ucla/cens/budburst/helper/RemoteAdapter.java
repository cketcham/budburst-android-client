package edu.ucla.cens.budburst.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.ucla.cens.budburst.R;
import edu.ucla.cens.budburst.DownloadManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class RemoteAdapter extends LazyAdapter implements Downloadable{
	
	private static final int DOWNLOADED_JSON = 1;
	private static final String TAG = "RemoteAdapter";
	public static final String ITEM_JSON = "json";
	private List<HashMap<String, String>> list;

	String[] from;
	Boolean isDownloading;
	Activity context;
	private DownloadManager dm;
	
	//used to set more complex views from the data that is downloaded
	private JSONDownloadListener listener;
	private Download current_download;

	public RemoteAdapter(Activity context, DownloadManager dm, ArrayList<HashMap<String,String>> list, Download d,
			int resource, String[] from, int[] to) {
		super(context, dm, list, resource, from, to);

		this.from = from;
		this.context = context;
		this.dm = dm;
		this.list = list;
		
		startDownload(d);
	}
	
	public void startDownload(Download d) {
		current_download = d;
		isDownloading = true;
		context.findViewById(R.id.downloading).setVisibility(View.VISIBLE);
		dm.download(this, DOWNLOADED_JSON, d);
	}

	@Override
	public void onDownloaded(Message msg, Download d) {
		switch(msg.what){
		case DOWNLOADED_JSON:
			if(!d.equals(current_download))
				break;
			list.clear();
			try {
				String Content = (String) msg.obj;
				Log.d(TAG,"Content = " + Content);
				JSONObject ret = new JSONObject(new JSONTokener(Content));
				//check success status
				if(!ret.get("type").equals("error")) {

					JSONArray json = new JSONArray(ret.getString("value"));

					//populate list
					for(int i=0;i<json.length();i++) {
						JSONObject jo = json.getJSONObject(i);
						HashMap<String, String> item = new HashMap<String, String>();  
						for(int j=0;j<from.length;j++) {
							if(jo.has(from[j])) {
								item.put(from[j], jo.getString(from[j]));
							} else {
								item.put(from[j], listener.onItemDownloaded(from[j], jo));
							}
						}
						item.put(this.ITEM_JSON, jo.toString());
						
						list.add(item); 
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			isDownloading = false;
			context.findViewById(R.id.downloading).setVisibility(View.GONE);

			
			Log.d(TAG,"notify updated " + list.size());
			notifyDataSetChanged();
			break;
			default: super.onDownloaded(msg, d);
		}
	}

	@Override
	public Object consumeInputStream(Message msg) {
		Log.d(TAG, "consumeInputStream");
		switch(msg.what){
		case DOWNLOADED_JSON:
			Log.d(TAG, "in consumeInputStream");
			return netUtils.generateString((InputStream) msg.obj);
		}
		return super.consumeInputStream(msg);
	}
	
    public boolean isEmpty() {
    	//so it doesn't show empty view
    	if(isDownloading) {
    		return false;
    	}
        return getCount() == 0;
    }

	public void setItemDownloadedListener(JSONDownloadListener listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
	}
}