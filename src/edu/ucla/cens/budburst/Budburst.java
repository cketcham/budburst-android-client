package edu.ucla.cens.budburst;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import edu.ucla.cens.budburst.data.PhenophaseDatabase;
import edu.ucla.cens.budburst.data.Row;
import edu.ucla.cens.budburst.data.SpeciesDatabase;

public class Budburst extends Activity {
    private static final String TAG = "Budburst";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SpeciesDatabase sdb = new SpeciesDatabase(this);
        ArrayList<Row> rows = sdb.find("_id=3 or _id=5");
        for(Iterator<Row> i = rows.iterator(); i.hasNext();)
        	Log.d(TAG, i.next().toString());
        
        PhenophaseDatabase pdb = new PhenophaseDatabase(this);
        pdb.openRead();
        ArrayList<Row> prows = pdb.find("_id=3 or _id=5");
        for(Iterator<Row> i = prows.iterator(); i.hasNext();)
        	Log.d(TAG, i.next().toString());
    }
    
	
}