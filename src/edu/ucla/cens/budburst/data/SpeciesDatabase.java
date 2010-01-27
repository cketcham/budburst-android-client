package edu.ucla.cens.budburst.data;

import java.net.URL;
import java.util.ArrayList;

import edu.ucla.cens.budburst.data.Database.Row;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class SpeciesDatabase extends Database{

	private Context context;
	public static final String name = "Species";
	public static final String KEY_SPECIES_NAME = "species_name";
	public static final String KEY_COMMON_NAME = "common_name";
	public static final String KEY_ID = "_id";
	private static final String TAG = "SpeciesDatabase";
	
	public SpeciesDatabase(Context context) {
		super(((SQLiteOpenHelper)new StaticDatabaseHelper(context, name)), name, fields(), new SpeciesRow(context));
		this.context = context;
	}


	public static class SpeciesRow extends Row {
		public SpeciesRow(Context context) {
			super(context);
		}

		public String species_name;
		public String common_name;
		
		@Override
		public ContentValues vals() {
			ContentValues vals  = super.vals();

			vals.put(SpeciesDatabase.KEY_SPECIES_NAME, this.species_name);
			vals.put(SpeciesDatabase.KEY_COMMON_NAME, this.common_name);

			return vals;
		}

		@Override
		public int readCursor(Cursor c) {
			int next = super.readCursor(c);
			species_name = c.getString(next++);
			common_name = c.getString(next++);
			return next;
		}

		@Override
		public Row newRow() {
			return new SpeciesRow(context);
		}

		
	}


	public static String[] fields() {
		return new String[] { KEY_ID, KEY_SPECIES_NAME, KEY_COMMON_NAME };
	}


}

