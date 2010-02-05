package edu.ucla.cens.budburst.data;

import java.util.HashMap;

import android.content.Context;

public class DatabaseManager{
	private Context context;
	private HashMap<String,Integer> resources = new HashMap<String,Integer>();
	private HashMap<String,Row> rows = new HashMap<String,Row>();
	
	public DatabaseManager(Context context) {
		this.context = context;
	}
	
	public void createDatabase(String name, int resource, Row row) {
		resources.put(name, resource);
		rows.put(name, row);
	}
	
	public StaticDatabase getDatabase(String name) {
		return new StaticDatabase(context, resources.get(name),rows.get(name));
	}

}

