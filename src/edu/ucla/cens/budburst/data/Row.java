package edu.ucla.cens.budburst.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import edu.ucla.cens.budburst.Budburst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public abstract class Row {
	private static final String TAG = "Row";
	public Long _id;

	public ContentValues vals() {
		ContentValues vals = new ContentValues();

		Field[] fields = getFields();
		for(int i=0; i<fields.length;i++) {
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
	
	public abstract Row newRow();

	public void readCursor(Cursor c) {
		Field[] fields = getFields();
		for(int i=0; i<fields.length;i++) {
			try {
				if(fields[i].getType().equals(Long.class))
					fields[i].set(this, c.getLong(i));
				else if(fields[i].getType().equals(String.class))
					fields[i].set(this, c.getString(i));
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
	
	public String toString() {
		String ret = this.getClass().getSimpleName();
		Field[] fields = getClass().getFields();
		for(int i=0; i<fields.length;i++) {
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
		Field [] fields = this.getClass().getFields();
		for(int i=0;i<fields.length;i++)
			if(fields[i].getType() != ArrayList.class)
				ret.add(fields[i]);
		return ret.toArray(new Field[0]);
	}
	

	protected ArrayList<Row> hasMany(String name) {
		Log.d(TAG, "in db : " + getName()+"_"+name);
		Log.d(TAG, "find : " + getName()+"_id="+_id);
		ArrayList<Row> this_that = Budburst.getDatabaseManager().getDatabase(getName()+"_"+name).find(getName()+"_id="+_id);
		for(Iterator<Row> i = this_that.iterator();i.hasNext();) {
			Log.d(TAG, i.next().toString());
		}
		return Budburst.getDatabaseManager().getDatabase(name).find(this_that, name+"_id");
	}

	//returns the name of the database for this row (just the name before Row)
	public String getName() {
		return this.getClass().getSimpleName().toLowerCase().split("row")[0];
	}
	
	public void setupRelations() {}

}