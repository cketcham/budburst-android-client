package edu.ucla.cens.budburst.data;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WritableDatabase extends Database{
	private static final String TAG = "WritableDatabase";

	private static Object dbLock = new Object();
	private static boolean databaseOpen = false;
	
	private SQLiteOpenHelper dbHelper;

	private Row rowInstance;
	
	public WritableDatabase(SQLiteOpenHelper helper, String name, Row row) {
		super(helper, name, row);
		dbHelper = helper;
		rowInstance = row;
	}

	public Database openWrite() throws SQLException {

		synchronized (dbLock) {
			while (databaseOpen) {
				try {
					dbLock.wait();
				} catch (InterruptedException e) {
				}

			}

			databaseOpen = true;
			db = dbHelper.getWritableDatabase();

			return this;
		}
	}
	
	public long insertRow(Row row) {
		return insertRow(row.vals());
	}
	
	public long insertRow(ContentValues vals) {
		long rowid = db.insert(rowInstance.getName(), null, vals);
		return rowid;
	}

}



