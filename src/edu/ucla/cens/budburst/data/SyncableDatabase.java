package edu.ucla.cens.budburst.data;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.util.Log;

public class SyncableDatabase extends WritableDatabase{
	private static final String TAG = "StaticDatabase";

	public SyncableDatabase(Context context, String json_data, Row row) {
		super(new DatabaseHelper(context, row), row.getName(), row);
		try {
			JSONObject ret = new JSONObject(new JSONTokener(json_data));
			//check success status
			if(ret.getBoolean("success")) {

				JSONArray json = new JSONArray(ret.getString("results"));

				openWrite();
				insertRows(json);
				close();

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//adds samples to the database from a correctly formated json array
	public long insertRows(JSONArray json) {
		long rowid=-1;

		for(int i=0;i<json.length();i++) {
			JSONObject object;
			try {
				object = json.getJSONObject(i);
				Iterator keys = object.keys();
				ContentValues vals = new ContentValues();

				while(keys.hasNext()) {
					String key = keys.next().toString();
					String value = object.get(key).toString();

					vals.put(key,value);
				}

				rowid = insertRow(vals);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
				Log.d(TAG,"did not insert element");
			}

		}


		return rowid;
	}
}



