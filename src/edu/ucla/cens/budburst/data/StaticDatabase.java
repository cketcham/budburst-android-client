package edu.ucla.cens.budburst.data;

import java.util.ArrayList;

import edu.ucla.cens.budburst.R;
import edu.ucla.cens.budburst.data.Database;
import edu.ucla.cens.budburst.data.StaticDatabaseHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class StaticDatabase extends Database{
	private static final String TAG = "StaticDatabase";

	public StaticDatabase(Context context, int resource, Row row) {
		super(((SQLiteOpenHelper)new StaticDatabaseHelper(context, row.getName(), resource)), row.getName(), row);
	}
}

