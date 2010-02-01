package edu.ucla.cens.budburst.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import edu.ucla.cens.budburst.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class StaticDatabaseHelper extends SQLiteOpenHelper {
	Context ctx;
	StaticDatabaseHelper(Context ctx, String name) {
		super(ctx, name, null, 1);
		this.ctx = ctx;

		try {
			copyDatabase(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	private void copyDatabase(String name) throws IOException{

		OutputStream databaseOutputStream = new 
		FileOutputStream("/data/data/edu.ucla.cens.budburst/databases/" + name);
		InputStream databaseInputStream;

		byte[] buffer = new byte[1024];
		databaseInputStream = 
			ctx.getResources().openRawResource(R.raw.species_db);
		while ( (databaseInputStream.read(buffer)) > 0 ) {
			databaseOutputStream.write(buffer);
		}
		databaseInputStream.close();

		databaseOutputStream.flush();
		databaseOutputStream.close();
	}

}
