package edu.ucla.cens.budburst;

import java.util.ArrayList;
import java.util.Iterator;

import edu.ucla.cens.budburst.data.Row;

import models.ObservationRow;
import models.PhenophaseRow;
import models.PlantRow;
import models.SpeciesPhenophaseRow;
import models.SpeciesRow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Budburst extends Activity {
    private static final String TAG = "Budburst";
	private static final int DOWNLOADED_DATABASES = 0;
    private static BudburstDatabaseManager dbManager;
    private static DownloadManager downloadManager;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        dbManager = new BudburstDatabaseManager(this);
        downloadManager = new DownloadManager();
        
        PreferencesManager.letUserIn("android", "android", this);
        

//        

        startActivityForResult(new Intent(this, SyncDatabases.class), DOWNLOADED_DATABASES);
    }
    
    public static BudburstDatabaseManager getDatabaseManager() {
    	return dbManager;
    }
    
    public static DownloadManager getDownloadManager() {
    	return downloadManager;
    }
    
    @Override 
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == DOWNLOADED_DATABASES) {
            	ArrayList<Row> rows = (ArrayList<Row>) dbManager.getDatabase("plant").all();

            	PlantRow row = (PlantRow) rows.get(0);
            	Log.d(TAG,"the plant" + row.observations.toString());
//            	        
//            	        for(Iterator<Row> i = row.phenophases.iterator(); i.hasNext();)
//            	        	Log.d(TAG, i.next()._id.toString());
            }

	}
    
	
}