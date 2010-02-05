package edu.ucla.cens.budburst.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database{
	public static final String KEY_ID = "_id";

	private SQLiteOpenHelper dbHelper;

	private String name;

	private Row rowInstance;
	private static SQLiteDatabase db;

	private static final String TAG = "Database";

	public Database(SQLiteOpenHelper helper, String name, Row row) {
		dbHelper = helper;
		this.name = name;
		this.rowInstance = row;
	}

	public Database openRead() throws SQLException {
		db = dbHelper.getReadableDatabase();

		return this;
	}


	public void close() {
			db.close();
	}

	public ArrayList<Row> find(String filter) {
		openRead();
		ArrayList<Row> ret = CursorToArrayList(db.query(name, fields(), filter, null, null, null, null));
		close();
		return ret;
	}
	
	public Row find(long id) {
		openRead();
		Cursor c = db.query(name, fields(), "_id=?", new String[] { String.valueOf(id) }, null, null, null);

		if (c.moveToFirst()) {
			rowInstance.readCursor(c);
		}

		c.close();
		close();
		return rowInstance;
	}

	public ArrayList<Row> CursorToArrayList(Cursor c) {
		ArrayList<Row> ret = new ArrayList<Row>();
		int numRows = c.getCount();

		c.moveToFirst();

		for (int i =0; i < numRows; ++i)
		{
			Row newRow = rowInstance.newRow();
			newRow.readCursor(c);
			ret.add(newRow);

			c.moveToNext();

		}
		c.close();			

		return ret;
	}
	
	public String[] fields() {
		ArrayList<String> ret = new ArrayList<String>();
		Field[] fields = rowInstance.getFields();
		for(int i=0;i<fields.length;i++) {
			ret.add(fields[i].getName());
		}
		return ret.toArray(new String[0]);
	}

	public String getName() {
		return name;
	}

	//finds all rows which have values for the name
	public ArrayList<Row> find(ArrayList<Row> array, String name) {
		Log.d(TAG,"finding for " + name);
		String filter = "";
		for(Iterator<Row> i = array.iterator();i.hasNext();)
			try {
				Row current = i.next();
				filter += "_id=" + current.getClass().getField(name).get(current);
				if(i.hasNext())
					filter += " OR ";
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return find(filter);
	}

}

