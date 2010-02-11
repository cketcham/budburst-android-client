package edu.ucla.cens.budburst;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import models.PhenophaseRow;
import models.PlantRow;
import models.SpeciesPhenophaseRow;
import models.SpeciesRow;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import edu.ucla.cens.budburst.data.DatabaseManager;
import edu.ucla.cens.budburst.data.Row;
import edu.ucla.cens.budburst.data.SyncableDatabase;
import edu.ucla.cens.budburst.helper.Download;
import edu.ucla.cens.budburst.helper.Downloadable;
import edu.ucla.cens.budburst.helper.netUtils;

public class SyncDatabases extends Activity implements Downloadable {
	private static final String TAG = "Budburst";
	private static final int DOWNLOADED_JSON = 0;
	private BudburstDatabaseManager dbManager;
	private DownloadManager downloadManager;
	
	private ArrayList<Download> downloads = new ArrayList<Download>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//TODO DEBUGING
		finish();

//		dbManager = Budburst.getDatabaseManager();
//		downloadManager = Budburst.getDownloadManager();
//		
//		String url = ((SyncableDatabase)dbManager.getDatabase("site")).getURL() + PreferencesManager.currentGETAuthParams(this);
//		Download d = new Download(url);
//		downloads.add(d);
//		downloadManager.download(this, DOWNLOADED_JSON, d);
//		
//		url = ((SyncableDatabase)dbManager.getDatabase("observation")).getURL() + PreferencesManager.currentGETAuthParams(this);
//		d = new Download(url);
//		downloads.add(d);
//		downloadManager.download(this, DOWNLOADED_JSON, d);
		
	}

	public Object consumeInputStream(Message msg) {
		Log.d(TAG, "consumeInputStream");
		switch(msg.what){
		case DOWNLOADED_JSON:
			Log.d(TAG, "in consumeInputStream");
			return netUtils.generateString((InputStream) msg.obj);
		}
		return null;
	}
	


	public void onDownloaded(Message msg, Download d) {
		switch(msg.what){
		case DOWNLOADED_JSON:
			String db ="";
			if(d.url.contains("get_my_sites"))
				db = "site";
			else if(d.url.contains("get_my_obs"))
				db = "observation";
			else if(d.url.contains("get_my_plants"))
				db = "plant";
			((SyncableDatabase)dbManager.getDatabase(db)).sync((String) msg.obj);
			
			if(d.url.contains("get_my_sites")) {
				//now we can start downloading plants since it depends on the sites:
				ArrayList<Row> sites = dbManager.getDatabase("site").all();
				for(Iterator<Row> i = sites.iterator();i.hasNext();) {
					String url = ((SyncableDatabase)dbManager.getDatabase("plant")).getURL() + PreferencesManager.currentGETAuthParams(this);
					url += "&site_id=" + i.next()._id;
					Download plantd = new Download(url);
					downloads.add(plantd);
					downloadManager.download(this, DOWNLOADED_JSON, plantd);
				}
			}
			
			downloads.remove(d);
			if(downloads.isEmpty())
				finish();
				
			break;
		}
		
	}



}