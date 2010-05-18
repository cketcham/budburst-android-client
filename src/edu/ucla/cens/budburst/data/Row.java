package edu.ucla.cens.budburst.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.database.Cursor;
import edu.ucla.cens.budburst.Budburst;

public abstract class Row {
	private static final String TAG = "Row";
	public Long _id;

	public ContentValues vals() {
		ContentValues vals = new ContentValues();

		Field[] fields = getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				Object o = fields[i].get(this);
				if (o != null)
					vals.put(fields[i].getName(), o.toString());
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
	
	public Boolean isSaved() {
		return _id != null;
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
					fields[i].set(this, c.getString(i).equals("true"));
				else if (fields[i].getType().equals(Double.class))
					fields[i].set(this, c.getDouble(i));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		for (int i = 0; i < fields.length; i++) {
			if (Modifier.isPublic(fields[i].getModifiers()))
				ret.add(fields[i]);
		}
		return ret.toArray(new Field[0]);
	}

	// returns the name of the database for this row (just the name before Row)
	public String getName() {
		return this.getClass().getSimpleName().toLowerCase().split("row")[0];
	}

	public ArrayList<String> primaryKeys() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("_id");
		return ret;
	}

	protected Row hasOne(String name, Long id) {
		return Budburst.getDatabaseManager().getDatabase(name).find(id);
	}

	// returns has many for table name, on field match with value value, with optional filter
	protected ArrayList<Row> hasMany(String name, String match, String value, String filter) {
		ArrayList<Row> this_that = Budburst.getDatabaseManager().getDatabase(getName() + "_" + name).find(match + "=" + value);
		// for (Iterator<Row> i = this_that.iterator(); i.hasNext();) {
		// Log.d(TAG, i.next().toString());
		// }
		if (filter != null && !filter.trim().equals(""))
			filter += " AND ";
		return Budburst.getDatabaseManager().getDatabase(name).find(this_that, name + "_id", filter);
	}

	protected ArrayList<Row> hasMany(String name, String filter) {
		return hasMany(name, getName() + "_id", _id.toString(), filter);
	}

	protected ArrayList<Row> hasMany(String name) {
		return hasMany(name, "");
	}

	public void put() {
		((WritableDatabase) Budburst.getDatabaseManager().getDatabase(getName())).insertRow(this);
	}

	public ArrayList<NameValuePair> parameters() {
		ArrayList<NameValuePair> ret = new ArrayList<NameValuePair>();
		Field[] fields = getFields();

		for (int i = 0; i < fields.length; i++) {
			try {
				ret.add(new BasicNameValuePair(fields[i].getName(), fields[i].get(this).toString()));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		return ret;
	}

}