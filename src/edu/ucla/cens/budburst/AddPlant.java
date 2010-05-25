package edu.ucla.cens.budburst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import edu.ucla.cens.budburst.data.Row;
import edu.ucla.cens.budburst.helper.ImageAdapter;
import edu.ucla.cens.budburst.helper.SeparatedListAdapter;
import edu.ucla.cens.budburst.models.PlantRow;
import edu.ucla.cens.budburst.models.SiteRow;
import edu.ucla.cens.budburst.models.SpeciesRow;

public class AddPlant extends ListActivity {

	private static final String TAG = "AddPlant";

	private static final String ITEM_COMMON_NAME = "name";
	private static final String ITEM_SPECIES_NAME = "description";
	private static final String ITEM_IMG = "icon";
	private static final int[] top_plants = { 70, 69, 45, 73, 59, 60, 19, 32, 34, 24 };
	private static final String toptenfilter = "_id=70 OR _id=69 OR _id=45 OR _id=73 OR _id=59 OR _id=60 OR _id=19 OR _id=32 OR _id=34 OR _id=24";

	private LocationManager lManager;
	private BudburstDatabaseManager databaseManager;
	private ArrayList<HashMap<String, String>> data;

	public Map<String,?> createItem(SpeciesRow species) {
		Map<String,String> item = new HashMap<String,String>();
		item.put(ITEM_COMMON_NAME, species.common_name);
		item.put(ITEM_SPECIES_NAME, species.species_name);
		item.put("icon", species.getImagePath());
		item.put("_id", species._id.toString());
		return item;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_plant);

		databaseManager = Budburst.getDatabaseManager();
		lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


		LinkedList<Map<String,?>> top_ten = new LinkedList<Map<String,?>>();
		LinkedList<Map<String,?>> all = new LinkedList<Map<String,?>>();

		//Do top 10 plants
		for (Iterator<Row> i = databaseManager.getDatabase("species").find(toptenfilter).iterator(); i.hasNext();) {
			top_ten.add(createItem((SpeciesRow)i.next()));
		}
		
		//And all plants
		for (Iterator<Row> i = databaseManager.getDatabase("species").all().iterator(); i.hasNext();) {
			all.add(createItem((SpeciesRow)i.next()));
		}
		
		// create our list and custom adapter
		SeparatedListAdapter adapter = new SeparatedListAdapter(this);
		adapter.addSection("Top Ten", new ImageAdapter(this, top_ten, R.layout.list_item, new String[] { ITEM_COMMON_NAME, ITEM_SPECIES_NAME, ITEM_IMG },
				new int[] { R.id.name, R.id.description, R.id.icon }));
		adapter.addSection("All Plants", new ImageAdapter(this, all, R.layout.list_item, new String[] { ITEM_COMMON_NAME, ITEM_SPECIES_NAME, ITEM_IMG },
				new int[] { R.id.name, R.id.description, R.id.icon }));

		setListAdapter(adapter);

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