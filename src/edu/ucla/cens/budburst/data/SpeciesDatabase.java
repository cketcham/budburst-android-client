package edu.ucla.cens.budburst.data;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class SpeciesDatabase extends Database{
	private static final String TAG = "SpeciesDatabase";
	
	public static final String name = "Species";
	
	public SpeciesDatabase(Context context) {
		super(((SQLiteOpenHelper)new StaticDatabaseHelper(context, name)), name, new SpeciesRow());
	}

}

