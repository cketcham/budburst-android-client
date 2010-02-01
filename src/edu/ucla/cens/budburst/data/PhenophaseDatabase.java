package edu.ucla.cens.budburst.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class PhenophaseDatabase extends Database{
	private static final String TAG = "PhenophaseDatabase";
	
	public static final String name = "Phenophase";
	
	public PhenophaseDatabase(Context context) {
		super(((SQLiteOpenHelper)new StaticDatabaseHelper(context, name)), name, new PhenophaseRow());
	}

}

