package edu.ucla.cens.budburst.data;

import java.util.ArrayList;

import edu.ucla.cens.budburst.data.SpeciesDatabase.SpeciesRow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database{
	public static final String KEY_ID = "_id";

	private SQLiteOpenHelper dbHelper;

	private String name;

	private String[] fields;

	private Row rowInstance;
	private static SQLiteDatabase db;

	private static final String TAG = "Database";

	public interface DatabaseRow {
		public ContentValues vals();
	}

	public abstract static class Row implements DatabaseRow{
		public Context context; 
		public Long id;

		public Row(Context context) {
			this.context = context;
		}

		public ContentValues vals() {
			ContentValues vals = new ContentValues();

			vals.put(Database.KEY_ID, this.id);

			return vals;
		}
		
		public abstract Row newRow();

		public int readCursor(Cursor c) {
			int next = 0;
			id = c.getLong(next++);
			return next;
		}

	}

	public Database(SQLiteOpenHelper helper, String name, String [] fields, Row row) {
		dbHelper = helper;
		this.name = name;
		this.fields = fields;
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
		ArrayList<Row> ret = CursorToArrayList(db.query(name, fields, filter, null, null, null, null));
		close();
		return ret;
	}
	
	public Row find(long id) {
		openRead();
		Cursor c = db.query(name, fields, "_id=?", new String[] { String.valueOf(id) }, null, null, null);

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
}

