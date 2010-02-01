package edu.ucla.cens.budburst.data;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public abstract class Row {
	private static final String TAG = "Row";
	public Long _id;

	public ContentValues vals() {
		ContentValues vals = new ContentValues();

		Field[] fields = getClass().getFields();
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
		Field[] fields = getClass().getFields();
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

}