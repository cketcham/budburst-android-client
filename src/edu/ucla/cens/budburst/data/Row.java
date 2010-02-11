package edu.ucla.cens.budburst.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import edu.ucla.cens.budburst.Budburst;

public abstract class Row {
	private static final String TAG = "Row";
	public Long _id;

	public ContentValues vals() {
		ContentValues vals = new ContentValues();

		Field[] fields = getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				vals.put(fields[i].getName(), fields[i].get(this).toString());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return vals;
	}

	public Row newRow() {
		try {
			return this.getClass().newInstance();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void readCursor(Cursor c) {
		Field[] fields = getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				if (fields[i].getType().equals(Long.class))
					fields[i].set(this, c.getLong(i));
				else if (fields[i].getType().equals(String.class))
					fields[i].set(this, c.getString(i));
				else if (fields[i].getType().equals(Boolean.class))
					fields[i].set(this, (c.getLong(i) == 1));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		setupRelations();
	}

	@Override
	public String toString() {
		String ret = this.getClass().getSimpleName();
		Field[] fields = getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				try {
					ret += " " + fields[i].getName() + ":" + fields[i].get(this);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}

	public Field[] getFields() {
		ArrayList<Field> ret = new ArrayList<Field>();
		Field[] fields = this.getClass().getFields();
		for (int i = 0; i < fields.length; i++)
			if (fields[i].getType() == Long.class || fields[i].getType() == String.class || fields[i].getType() == Boolean.class)
				ret.add(fields[i]);
		return ret.toArray(new Field[0]);
	}

	// returns the name of the database for this row (just the name before Row)
	public String getName() {
		return this.getClass().getSimpleName().toLowerCase().split("row")[0];
	}

	public void setupRelations() {
	}

	public ArrayList<String> primaryKeys() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("_id");
		return ret;
	}

	protected Row hasOne(String name, Long id) {
		return Budburst.getDatabaseManager().getDatabase(name).find(id);
	}

	protected ArrayList<Row> hasMany(String name) {
		Log.d(TAG, "in db : " + getName() + "_" + name);
		Log.d(TAG, "find : " + getName() + "_id=" + _id);
		ArrayList<Row> this_that = Budburst.getDatabaseManager().getDatabase(getName() + "_" + name).find(getName() + "_id=" + _id);
		for (Iterator<Row> i = this_that.iterator(); i.hasNext();) {
			Log.d(TAG, i.next().toString());
		}
		return Budburst.getDatabaseManager().getDatabase(name).find(this_that, name + "_id");
	}

	public void put() {
		((WritableDatabase) Budburst.getDatabaseManager().getDatabase(this)).insertRow(this);
	}

}