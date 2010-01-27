package edu.ucla.cens.budburst;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.ucla.cens.budburst.data.Database;
import edu.ucla.cens.budburst.data.SpeciesDatabase;
import edu.ucla.cens.budburst.data.SpeciesDatabase.SpeciesRow;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

public class Budburst extends Activity {
    private static final String TAG = "Budburst";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SpeciesDatabase sdb = new SpeciesDatabase(this);
        SpeciesRow row = (SpeciesRow) sdb.find("_id=3 or _id=5").get(0);
        Log.d(TAG, row.common_name);
        row.common_name = "blah";
    }
    
	
}