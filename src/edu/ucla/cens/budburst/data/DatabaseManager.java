package edu.ucla.cens.budburst.data;

import java.util.HashMap;

import models.PlantRow;

import android.content.Context;

public class DatabaseManager{
	private Context context;
	private HashMap<String,Integer> resources = new HashMap<String,Integer>();
	private HashMap<String,Row> rows = new HashMap<String,Row>();
	private HashMap<String,String> urls = new HashMap<String,String>();
	
	public DatabaseManager(Context context) {
		this.context = context;
	}
	
	public void createDatabase(String name, int resource, Row row) {
		resources.put(name, resource);
		rows.put(name, row);
	}

	public void createSyncableDatabase(String name, String jsonData, Row row) {
		urls.put(name, jsonData);
		rows.put(name, row);
	}
	
	public Database getDatabase(String name) {
		if(resources.containsKey(name))
			return new StaticDatabase(context, resources.get(name),rows.get(name));
		else if(urls.containsKey(name))
			return new SyncableDatabase(context, urls.get(name), rows.get(name));
		
		return null;
	}

}

