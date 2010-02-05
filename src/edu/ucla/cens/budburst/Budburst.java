package edu.ucla.cens.budburst;

import java.util.ArrayList;
import java.util.Iterator;

import models.PhenophaseRow;
import models.PlantRow;
import models.SpeciesPhenophaseRow;
import models.SpeciesRow;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import edu.ucla.cens.budburst.data.DatabaseManager;
import edu.ucla.cens.budburst.data.Row;

public class Budburst extends Activity {
    private static final String TAG = "Budburst";
    private static BudburstDatabaseManager dbManager;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        dbManager = new BudburstDatabaseManager(this);
        
//        SpeciesRow row = (SpeciesRow) dbManager.getDatabase("species").find(4);
//
//        Log.d(TAG, row.toString());
//        
//        for(Iterator<Row> i = row.phenophases.iterator(); i.hasNext();)
//        	Log.d(TAG, i.next()._id.toString());
//        
        PlantRow row = (PlantRow) dbManager.getDatabase("plant").find(1);
        Log.d(TAG, row.toString());
    }
    
    public static BudburstDatabaseManager getDatabaseManager() {
    	return dbManager;
    }
    
	
}