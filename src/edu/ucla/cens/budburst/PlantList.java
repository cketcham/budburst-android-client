package edu.ucla.cens.budburst;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import edu.ucla.cens.budburst.data.Row;
import edu.ucla.cens.budburst.helper.RowWithImageAdapter;


public class PlantList extends ListActivity {  

	private RowWithImageAdapter adapter;  
	private static final String TAG = "campaignList";

	private static final String ITEM_COMMON_NAME = "site_id";
	private static final String ITEM_SPECIES_NAME = "species_id";
	private static final String ITEM_IMG = "species_id";

	private static final int MENU_ADD = 0;
	private static final int MENU_ONE = 1;
	private static final int MENU_SETTINGS = 2;
	protected static final int CONTEXT_REMOVE = 0;


	Button button1;
	Button button2;
	Button button3;
	private BudburstDatabaseManager databaseManager;

	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.dynamic_list);

		databaseManager = Budburst.getDatabaseManager();
	}

	public void onResume() {
		super.onResume();

		ArrayList<Row> plants = databaseManager.getDatabase("plant").all();
		
		adapter = new RowWithImageAdapter(this, Budburst.getDownloadManager(), new ArrayList<HashMap<String,Object>>(), plants, R.layout.list_item, new String[] { ITEM_COMMON_NAME, ITEM_SPECIES_NAME, ITEM_IMG }, new int[] { R.id.name, R.id.description, R.id.icon });
		setListAdapter(this.adapter);  
		
		//set for long clicks
		getListView().setOnCreateContextMenuListener(ContextMenuListener);

		button1 = (Button)this.findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//		currentTask = new GrabCampaigns().execute("http://t5l-kullect.appspot.com/list?query=featured");
			}
		});

		button2 = (Button)this.findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//	currentTask = new GrabCampaigns().execute("http://t5l-kullect.appspot.com/list?query=new");
			}
		});

		button3 = (Button)this.findViewById(R.id.button3);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//	currentTask = new GrabCampaigns().execute("http://t5l-kullect.appspot.com/list?query=all");
			}
		});

	}  


	private OnCreateContextMenuListener ContextMenuListener = new OnCreateContextMenuListener(){

		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.setHeaderTitle("Plant Actions");
			menu.add(0, CONTEXT_REMOVE, 0, "Remove");
			//menu.add(0, CONTEXT_SHOW_ON_MAP, 1, "Show on map");
			//menu.add(0, CONTEXT_VIEW, 2, "View tag");						
		}
	};
	

//	@Override
//	public boolean onContextItemSelected(MenuItem item) { 
//		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//
//		switch (item.getItemId()) {
//		case CONTEXT_REMOVE:
//			pdb.openWrite();
//			pdb.deleteRow(menuInfo.id);
//			pdb.close();
//
//			this.adapter.changeCursor(pdb.getAllRows());
//
//			break;
//		}
//
//		return super.onContextItemSelected(item);
//	}
//
//
//	public class MyAdapter extends SimpleCursorAdapter { 
//		MyAdapter(Context context, int layout, Cursor cursor,String[] from, int[] to) { 
//			super(context, layout, cursor, from, to); 
//			setViewBinder(new MyViewBinder());
//		} 
//
//	} 
//
//	Hashtable<Long, Object> list = new Hashtable<Long, Object>();
//	public class MyViewBinder implements SimpleCursorAdapter.ViewBinder {
//		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//			//if we are not dealing with the image don't handle it
//			if(view instanceof ImageView) {
//				
//				//set the default icon
//				ImageView imView = (ImageView)view;
//				imView.setImageResource(R.drawable.default_icon);
//				
//				Object data = list.get(cursor.getLong(1));
//				if(data == null) {
//					list.put(cursor.getLong(1), "downloading");
//					new GetIconTask().execute(cursor.getLong(1));
//				} if(data instanceof Drawable) {
//					((ImageView) view).setImageDrawable((Drawable)data);
//				}
//				return true;
//			} else if(view instanceof TextView) {
//				PlantRow p = (PlantRow) pdb.CursorToRow(cursor);
//				pdb.openRead();
//				sdb.openRead();
//				((TextView) view).setText(p.species().getString(columnIndex));
//				sdb.close();
//				pdb.close();
//				return true;
//			}
//			
//			return false;
//		}
//	}
//	
//	protected final Handler listUpdated = new Handler() {
//		@Override
//		public void handleMessage(Message msg){
//			switch(msg.what) {
//			case constants.REFRESH:
//				//show no results if there are no items in the list
//				adapter.notifyDataSetChanged();
//			}
//		}
//	};
//	
//	private class GetIconTask extends AsyncTask<Long, Void, Void> {
//		String Error;
//
//		protected Void doInBackground(Long... id) {
//			try {
//				Log.d(TAG, "start downloading icon " + id[0]);
//				URL url = new URL(getString(R.string.get_image_url)+id[0]+".jpg");
//				InputStream is = (InputStream) url.getContent();
//				Drawable d = Drawable.createFromStream(is, "src");
//				Log.d(TAG, "id = " + id[0] + " draw= " + d);
//				if(d!=null)
//				list.put(id[0], d);
//				
//				listUpdated.sendEmptyMessage(constants.REFRESH);
//
//
//			} catch (MalformedURLException e) {
//				Error = e.getMessage();
//				cancel(true);
//			} catch (IOException e) {
//				Error = e.getMessage();
//				cancel(true);
//			}
//
//			return null;
//		}
//
//		protected void onPostExecute(Void unused) {
//			if (Error != null) {
//				Toast.makeText(plantList.this, Error, Toast.LENGTH_LONG).show();	
//			}
//		}
//	}
//	
//	@Override
//	protected void  onListItemClick  (ListView l, View v, int position, long id) {
//
//		Intent launchPlantIntent = new Intent(plantList.this, PlantInfo.class);
//		launchPlantIntent.putExtra("PlantID", id);
//		launchPlantIntent.putExtra("phase", "leaves");
//		plantList.this.startActivity(launchPlantIntent);
//
//	}
//
//
//
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, MENU_ADD, 0, "Add Plant").setIcon(android.R.drawable.ic_menu_add);
//		menu.add(0, MENU_ONE, 1, "One-Time Sample").setIcon(android.R.drawable.ic_menu_set_as);
//		menu.add(0, MENU_SETTINGS, 2, "Settings").setIcon(android.R.drawable.ic_menu_preferences);
//
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		Intent intent= null;
//
//		switch(item.getItemId()){
//		case MENU_ADD:
//			intent = new Intent(this, AddPlant.class);
//			this.startActivity(intent);
//
//			break;
//		case MENU_SETTINGS:
//			intent = new Intent(this, settingsScreen.class);
//			this.startActivity(intent);
//
//			break;
//		case MENU_ONE:
//			//intent = new Intent(this, Help.class);
//			//this.startActivity(intent);
//			break;
//		}
//
//		return super.onOptionsItemSelected(item);
//	}







}  