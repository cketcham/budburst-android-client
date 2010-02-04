package edu.ucla.cens.budburst.data;

import edu.ucla.cens.budburst.R;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class SpeciesDatabase extends Database{
	private static final String TAG = "SpeciesDatabase";
	
	public static final String name = "species";
	
	public SpeciesDatabase(Context context) {
		super(((SQLiteOpenHelper)new StaticDatabaseHelper(context, name, R.raw.species_db)), name, new SpeciesRow());
	}

}

