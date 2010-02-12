package edu.ucla.cens.budburst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import models.PlantRow;
import models.SpeciesRow;
import android.app.ListActivity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import edu.ucla.cens.budburst.data.Row;

public class AddPlant extends ListActivity {

	private SimpleAdapter adapter;
	private static final String TAG = "AddPlant";

	private static final String ITEM_COMMON_NAME = "name";
	private static final String ITEM_SPECIES_NAME = "description";
	private static final String ITEM_IMG = "icon";

	private LocationManager lManager;
	private BudburstDatabaseManager databaseManager;
	private ArrayList<HashMap<String, String>> data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dynamic_list);

		databaseManager = Budburst.getDatabaseManager();
		lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	}

	@Override
	public void onResume() {
		super.onResume();

		ArrayList<Row> plants = databaseManager.getDatabase("species").all();
		data = new ArrayList<HashMap<String, String>>();
		for (Iterator<Row> i = plants.iterator(); i.hasNext();) {
			HashMap<String, String> map = new HashMap<String, String>();
			SpeciesRow current = (SpeciesRow) i.next();
			map.put("name", current.common_name);
			map.put("description", current.species_name);
			map.put("icon", current.getImagePath());
			map.put("_id", current._id.toString());
			data.add(map);
		}

		adapter = new SimpleAdapter(this, data, R.layout.list_item, new String[] { ITEM_COMMON_NAME, ITEM_SPECIES_NAME, ITEM_IMG },
				new int[] { R.id.name, R.id.description, R.id.icon });

		setListAdapter(this.adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		PlantRow plant = new PlantRow();
		plant.latitude = lManager.getLastKnownLocation("gps").getLatitude();
		plant.longitude = lManager.getLastKnownLocation("gps").getLongitude();
		plant.species_id = Long.parseLong(data.get(position).get("_id"));

		// TODO:put in an actual site
		plant.site_id = databaseManager.getDatabase("site").all().get(0)._id;

		plant.put();

		finish();

	}

}