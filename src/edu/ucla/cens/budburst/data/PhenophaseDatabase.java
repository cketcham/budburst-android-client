package edu.ucla.cens.budburst.data;

import edu.ucla.cens.budburst.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class PhenophaseDatabase extends Database{
	private static final String TAG = "PhenophaseDatabase";
	
	public static final String name = "phenophase";
	
	public PhenophaseDatabase(Context context) {
		super(((SQLiteOpenHelper)new StaticDatabaseHelper(context, name, R.raw.phenophase_db)), name, new PhenophaseRow());
	}

}

