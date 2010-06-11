package edu.ucla.cens.budburst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

	private static final int SPECIES_CREATED = 0;

	private BudburstDatabaseManager databaseManager;

	//helper function to create a species item in the list
	public Map<String,?> createItem(SpeciesRow species) {
		Map<String,String> item = new HashMap<String,String>();
		item.put(ITEM_COMMON_NAME, species.common_name);
		item.put(ITEM_SPECIES_NAME, species.species_name);
		item.put("icon", species.getImagePath());
		item.put("_id", species._id.toString());
		return item;
	}

	private final BroadcastReceiver mLoggedInReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};

	private LinkedList<Map<String, ?>> top_ten;
	private LinkedList<Map<String, ?>> all;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_plant);

		databaseManager = Budburst.getDatabaseManager();

		//you must first make a site, so we should check that there is at least 1 site
		//TODO: make a site on the phone
		if(databaseManager.getDatabase("site").all().size() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					getString(R.string.no_sites_message))
					.setPositiveButton("Make Site", new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialog, int id) {
							//if not prompt them to go to the web to make a new one
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri.parse(getString(R.string.budburst_url)));
							startActivity(intent);
							finish();
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialog, int id) {
							//if they don't want to make one, they can't add a plant
							finish();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();

		}


		//two lists to display in the listview
		top_ten = new LinkedList<Map<String,?>>();
		all = new LinkedList<Map<String,?>>();

		//Do top 10 plants
		for (Iterator<Row> i = databaseManager.getDatabase("species").find(toptenfilter).iterator(); i.hasNext();) {
			top_ten.add(createItem((SpeciesRow)i.next()));
		}

		//And all plants
		for (Iterator<Row> i = databaseManager.getDatabase("species").all().iterator(); i.hasNext();) {
			all.add(createItem((SpeciesRow)i.next()));
		}

		//Make an adapter to add Other category
		LinkedList<Map<String, ?>> otheritemmap = new LinkedList<Map<String,?>>();
		Map<String,String> otheritem = new HashMap<String,String>();
		otheritem.put(ITEM_COMMON_NAME, "Unknown Plant");
		otheritem.put("icon", Budburst.SPECIES_PATH + "999.jpg");
		otheritemmap.add(otheritem);

		// create our list and custom adapter
		// this will show each section with a header
		SeparatedListAdapter adapter = new SeparatedListAdapter(this);
		adapter.addSection("Top Ten", new ImageAdapter(this, top_ten, R.layout.list_item, new String[] { ITEM_COMMON_NAME, ITEM_SPECIES_NAME, ITEM_IMG },
				new int[] { R.id.name, R.id.description, R.id.icon }));
		adapter.addSection("Unknown", new ImageAdapter(this, otheritemmap, R.layout.list_item,  new String[] { ITEM_COMMON_NAME, ITEM_SPECIES_NAME, ITEM_IMG }, 
				new int[] { R.id.name, R.id.description, R.id.icon }));
		adapter.addSection("All Plants", new ImageAdapter(this, all, R.layout.list_item, new String[] { ITEM_COMMON_NAME, ITEM_SPECIES_NAME, ITEM_IMG },
				new int[] { R.id.name, R.id.description, R.id.icon }));

		setListAdapter(adapter);

		registerReceiver(mLoggedInReceiver, new IntentFilter(Constants.INTENT_ACTION_LOGGED_OUT));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mLoggedInReceiver);
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {


		//need to set the offsets. the position returned the the absolute position in the list (including the separators as items)
		//we need to check what the position is and calculate the offsets so we can actually index into the arrays correctly
		if(position <= 10) {
			position -= 1;
			// the user clicked in the list with the top ten plants so index into that array for the species id
			createPlant(Long.parseLong((String) top_ten.get(position).get("_id")));

		} else if (position == 12) {
			//this happens when a user clicks the unknown plant item.
			// TODO: edit the CreateSpecies activity to let the user actually create a
			// new species, it should return the species_id in the result intent
			
			startActivityForResult(new Intent(this,CreateSpecies.class),SPECIES_CREATED);

		} else {
			position -= 14;
			// the user clicked in the list with all the plants so index into that array for the species id
			createPlant(Long.parseLong((String) all.get(position).get("_id")));

		}
	}
	
	protected void createPlant(final Long species_id) {
		// First get the sites
		final ArrayList<Row> sites = databaseManager.getDatabase("site").all();
		ArrayList<String> site_names = new ArrayList<String>();
		for (Iterator<Row> i = sites.iterator(); i.hasNext();)
			site_names.add(((SiteRow) i.next()).name);
		final String[] siteNames = site_names.toArray(new String[0]);
		final int[] selectedNames = new int[siteNames.length];
		
		//let the user choose a site
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose Site");
		builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				PlantRow plant = new PlantRow();
				plant.species_id = species_id;

				//set the site id for that plant
				for (int i = 0; i < selectedNames.length; i++)
					if (selectedNames[i] == 1)
						plant.site_id = sites.get(i)._id;

				//put the plant into the db
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// You can use the requestCode to select between multiple child
		// activities you may have started. Here there is only one thing
		// we launch.
		Log.d(TAG, "onActivityResult");
		if (requestCode == SPECIES_CREATED) {
			createPlant(intent.getExtras().getLong("species_id"));
		}
	}

}