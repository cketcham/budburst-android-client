package edu.ucla.cens.budburst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import models.PlantRow;
import models.SiteRow;
import models.SpeciesRow;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
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
		setContentView(R.layout.add_plant);

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

		// First get the site:
		final ArrayList<Row> sites = databaseManager.getDatabase("site").all();
		ArrayList<String> site_names = new ArrayList<String>();
		for (Iterator<Row> i = sites.iterator(); i.hasNext();)
			site_names.add(((SiteRow) i.next()).name);
		final String[] siteNames = site_names.toArray(new String[0]);
		final int[] selectedNames = new int[siteNames.length];
		final Long species_id = Long.parseLong(data.get(position).get("_id"));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose Site");
		builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				PlantRow plant = new PlantRow();
				plant.latitude = lManager.getLastKnownLocation("gps").getLatitude();
				plant.longitude = lManager.getLastKnownLocation("gps").getLongitude();
				plant.species_id = species_id;

				for (int i = 0; i < selectedNames.length; i++)
					if (selectedNames[i] == 1)
						plant.site_id = sites.get(i)._id;

				plant.put();
				finish();
			}
		});

		builder.setSingleChoiceItems(siteNames, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				for (int i = 0; i < selectedNames.length; i++)
					if (i == item)
						selectedNames[i] = 1;
					else
						selectedNames[i] = 0;
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

	}

}