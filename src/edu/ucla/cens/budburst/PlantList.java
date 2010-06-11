package edu.ucla.cens.budburst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.ucla.cens.budburst.data.Row;
import edu.ucla.cens.budburst.helper.ImageAdapter;
import edu.ucla.cens.budburst.helper.SeparatedListAdapter;
import edu.ucla.cens.budburst.models.PlantRow;
import edu.ucla.cens.budburst.models.SiteRow;
import edu.ucla.cens.budburst.models.SpeciesRow;

public class PlantList extends ListActivity {

	private static final String TAG = "PlantList";

	private static final String ITEM_COMMON_NAME = "name";
	private static final String ITEM_SPECIES_NAME = "description";
	private static final String ITEM_IMG = "icon";

	private static final int MENU_ADD = 0;
	private static final int MENU_ONE = 1;
	private static final int MENU_SETTINGS = 2;
	private static final int MENU_SYNC = 3;

	protected static final int CONTEXT_REMOVE = 0;
	private static final int LOGIN_FINISHED = 0;

	Button button1;
	Button button2;
	Button button3;
	private BudburstDatabaseManager databaseManager;
	private LinkedList<Map<String,?>> data;

	//this listens to see if you clicked the logout button in the preferences. if you have, it should hide this view.
	private final BroadcastReceiver mLoggedInReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plant_list);

		button1 = (Button) this.findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// this is where you add the code for what happens on the button on left is clicked.
			}
		});

		button2 = (Button) this.findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// this is where you add the code for what happens on the button on right is clicked.
			}
		});
		
		//this gets the databaseManager associated with budburst
		//The BudburstDatabaseManager automatically creates all the databases that will be used in the application
		databaseManager = Budburst.getDatabaseManager();

		//this actually registers the reciever so it will be notified when the user logs out.
		registerReceiver(mLoggedInReceiver, new IntentFilter(Constants.INTENT_ACTION_LOGGED_OUT));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mLoggedInReceiver);
	}

	//helper function to create a species item in the list
	public Map<String,?> createItem(SpeciesRow species) {
		Map<String,String> item = new HashMap<String,String>();
		item.put(ITEM_COMMON_NAME, species.common_name);
		item.put(ITEM_SPECIES_NAME, species.species_name);
		item.put("icon", species.getImagePath());
		item.put("_id", species._id.toString());
		return item;
	}
	
	@Override
	public void onResume() {
		super.onResume();

		SeparatedListAdapter adapter = new SeparatedListAdapter(this);
		
		//this is the adapter to be shown
		data = new LinkedList<Map<String,?>>();

		//first get the sites. use those to populate the main screen grouped by site
		ArrayList<Row> sites = databaseManager.getDatabase("site").all();
		for(Iterator<Row> j = sites.iterator();j.hasNext();) {

			ArrayList<HashMap<String, String>> local_data = new ArrayList<HashMap<String, String>>();
			SiteRow current_site = (SiteRow) j.next();
			
			//Query the plant database to return all rows for the current site
			ArrayList<Row> plants = databaseManager.getDatabase("plant").find("site_id="+current_site._id);
			
			//for each of the rows returned create an entry in data which has information to populate your plant list
			for (Iterator<Row> i = plants.iterator(); i.hasNext();) {
				HashMap<String, String> map = new HashMap<String, String>();
				PlantRow current = (PlantRow) i.next();
				map.put("name", current.species().common_name);
				map.put("description", current.species().species_name);
				map.put("icon", current.species().getImagePath());
				map.put("_id", current._id.toString());
				local_data.add(map);
			}
			
			//this allows for the offset
			data.add(new HashMap<String, String>());
			//then add the data to the global
			data.addAll(local_data);

			adapter.addSection(current_site.name, new ImageAdapter(this, local_data, R.layout.list_item, new String[] { ITEM_COMMON_NAME, ITEM_SPECIES_NAME, ITEM_IMG },
					new int[] { R.id.name, R.id.description, R.id.icon }));
		}

		setListAdapter(adapter);

		// set for long clicks
		getListView().setOnCreateContextMenuListener(ContextMenuListener);

		// shows the username if there are no plants in the listview
		showUserName();
	}


	private final OnCreateContextMenuListener ContextMenuListener = new OnCreateContextMenuListener() {

		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			menu.setHeaderTitle("Plant Actions");
			// if you want to add any actions when you long press an item in the list you can add it here
			// menu.add(0, CONTEXT_REMOVE, 0, "Remove");
			// menu.add(0, CONTEXT_SHOW_ON_MAP, 1, "Show on map");
			// menu.add(0, CONTEXT_VIEW, 2, "View tag");
		}
	};

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo =
			(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
		//you should check for the cases you created in the function above and perform the action here.
//		case CONTEXT_REMOVE:
//			databaseManager.getDatabase("plant").find(item.id).remove();
//			this.adapter.notifyDataSetChanged();
//
//			break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//when a user clicks normal on an item in the plant list, it gets the id from the data array and starts the plantInfo activity which lets the user record an observation for that plant.
		Intent launchPlantIntent = new Intent(this, PlantInfo.class);
		launchPlantIntent.putExtra("PlantID", Long.parseLong((String) data.get(position).get("_id")));
		launchPlantIntent.putExtra("phase", "leaves");
		startActivity(launchPlantIntent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//These are the items that are shown when the user clicks
		menu.add(0, MENU_ADD, 0, "Add Plant").setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, MENU_SETTINGS, 2, "Settings").setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(0, MENU_SYNC, 3, "Sync").setIcon(android.R.drawable.ic_menu_rotate);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;

		switch (item.getItemId()) {
		case MENU_ADD:
			intent = new Intent(this, AddPlant.class);
			this.startActivity(intent);

			break;
		case MENU_SYNC:
			intent = new Intent(this, SyncDatabases.class);
			this.startActivity(intent);
			break;
		case MENU_SETTINGS:
			intent = new Intent(this, SettingsScreen.class);
			this.startActivity(intent);

			break;
		case MENU_ONE:
			// intent = new Intent(this, Help.class);
			// this.startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	// added by EG to try to learn how to do this... add name after "Hello"
	protected void showUserName() {
		// Display user name at top of screen
		TextView textView = (TextView) this.findViewById(R.id.hello_text);

		String username_string = new String(PreferencesManager.currentUser(this));
		textView.setText("Hello " + username_string + "!");

	}
}