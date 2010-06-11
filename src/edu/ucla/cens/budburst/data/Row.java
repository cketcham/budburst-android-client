package edu.ucla.cens.budburst.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.database.Cursor;
import edu.ucla.cens.budburst.Budburst;

//base class that all models extend from
public abstract class Row {
	private static final String TAG = "Row";
	public Long _id;

	//automatically calculates the fields and values using the public fields of the object
	//probably do not need to extend
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

	//creates a new instance of the current type of row
	//probably do not need to extend
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

	//the _id will be set only after it is put in the database
	public Boolean isSaved() {
		return _id != null;
	}

	//reads in the cursor returned from the database to create a model
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

	// can be used to automatically say that one model should be "linked" to another model
	// for example every plant hasOne species associated with it. you can look at PlantRow to see how it is used.
	protected Row hasOne(String name, Long id) {
		return Budburst.getDatabaseManager().getDatabase(name).find(id);
	}

	//similar to has one, but used for models which have many of another model
	//returns hasMany for table name, on field match with value value, with optional filter
	//uses a RelationRow to do the maping with an optional filter
	//look at SpeciesRow, PhenophaseRow, and SpeciesPhenophaseRow for an example of how it
	//is used
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

	//actually commits the data to the database
	public void put() {
		//run onDelete in case we are replacing a row
		//Ex. we need to delete an old image
		if(_id != null)
			((WritableDatabase) Budburst.getDatabaseManager().getDatabase(getName())).find(_id).onDelete();
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

	//should be extended if there are things that need to be cleaned up when a model is deleted
	//such as removing an image that was taken.
	public void onDelete() {}

}