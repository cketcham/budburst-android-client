
package edu.ucla.cens.budburst;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.ucla.cens.budburst.data.Row;
import edu.ucla.cens.budburst.models.ObservationRow;
import edu.ucla.cens.budburst.models.PhenophaseRow;
import edu.ucla.cens.budburst.models.PlantRow;

public class PlantInfo extends Activity {

	private static final String TAG = "plant_info";
	protected static final int PHOTO_CAPTURE_CODE = 0;
	private static final int MENU_ADD_NOTE = 0;

	private ObservationRow observation;

	ArrayList<Button> buttonBar = new ArrayList<Button>();
	private BudburstDatabaseManager databaseManager;
	protected Long image_id;

	private ImageView img;

	//this listens to see if you clicked the logout button in the preferences. if you have, it should hide this view.
	private final BroadcastReceiver mLoggedInReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sampleinfo);
		
		// this is the view that would display the observations image
		img = (ImageView) this.findViewById(R.id.image);

		// get the databaseManager
		databaseManager = Budburst.getDatabaseManager();
		
		// when this activity is called some extra data is passed so we know which plant we should be looking at, and which phenophase we want to look at.
		Bundle extras = getIntent().getExtras();
		int chrono = extras.getInt("chrono", 0);
		int stageID = extras.getInt("StageID", BudburstDatabaseManager.LEAVES);


		// get plant, phenophases, and observation models
		PlantRow plant = (PlantRow) databaseManager.getDatabase("plant").find(extras.getLong("PlantID"));
		ArrayList<Row> phenophases = plant.species().phenophases(stageID);
		PhenophaseRow phenophase = (PhenophaseRow) phenophases.get(chrono);
		observation = plant.observations(phenophase);

		
		//set name
		TextView name = (TextView) this.findViewById(R.id.name);
		name.setText(plant.species().common_name);
		
		//set phenophase description
		TextView phenophase_comment = (TextView) this.findViewById(R.id.phenophase_info_text);
		phenophase_comment.setText(phenophase.getAboutText(plant.species().protocol_id));

		//set phenophase state text
		TextView state = (TextView) this.findViewById(R.id.state);
		state.setText(phenophase.name);
		
		//set the button to take the photo
		View take_photo = this.findViewById(R.id.take_photo);
		take_photo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//just make a random_id for the image now. this will be the file name.
				image_id = new Date().getTime();

				//start the built-in camera, it will return in onActivityResult
				Intent mediaCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				mediaCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Budburst.OBSERVATION_PATH, image_id + ".jpg")));
				startActivityForResult(mediaCaptureIntent, PHOTO_CAPTURE_CODE);
			}
		});
		
		View remove_photo = this.findViewById(R.id.no_photo);
		remove_photo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// 0 means there should be no image associated with this observation
				image_id = new Long(0);
				observation.image_id = image_id;
				showReplaceRemovePhotoButtons();
			}
		});
		
		if(observation != null) {
			//this means an observation has been taken, so we should show the date it was taken and any note if there was one.
			
			//set date
			TextView timestamp = (TextView) this.findViewById(R.id.timestamp_text);
			timestamp.setText(observation.time);
			
			//put the note in the edittext
			EditText note = (EditText) this.findViewById(R.id.notes);
			note.setText(observation.note);
			
		} else {
			//since no observation has been taken yet, we should not show the date
			this.findViewById(R.id.timestamp_text).setVisibility(View.GONE);
			this.findViewById(R.id.timestamp_label).setVisibility(View.GONE);
			
			TextView make_obs_text = (TextView) this.findViewById(R.id.make_obs_text);
			make_obs_text.setText("Make an Observation for this Phenophase");
			
			//and we should make a new observation for this plant and phenophase
			//but it isnt saved to the db yet. you need to call put() to actually save it.
			observation = new ObservationRow();
			observation.species_id = plant.species_id;
			observation.phenophase_id = phenophase._id;
			observation.site_id = plant.site_id;
		}

		//the leaves button
		buttonBar.add((Button) this.findViewById(R.id.button1));
		buttonBar.get(0).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putExtra("StageID", BudburstDatabaseManager.LEAVES);
				intent.putExtra("chrono", 0);

				startActivity(intent);
				finish();
			}
		});

		//The Flowers button
		buttonBar.add((Button) this.findViewById(R.id.button2));
		buttonBar.get(1).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putExtra("StageID", BudburstDatabaseManager.FLOWERS);
				intent.putExtra("chrono", 0);

				startActivity(intent);
				finish();
			}
		});

		//The Fruits button
		buttonBar.add((Button) this.findViewById(R.id.button3));
		buttonBar.get(2).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putExtra("StageID", BudburstDatabaseManager.FRUITS);
				intent.putExtra("chrono", 0);

				startActivity(intent);
				finish();
			}
		});

		// set selected button
		buttonBar.get(stageID).setSelected(true);

		//populate the buttonbar on the bottom with phenophases
		LinearLayout phenophaseBar = (LinearLayout) this.findViewById(R.id.phenophase_bar);

		//for each of the phenophases go through and set the image and make it a button
		for (Iterator<Row> i = phenophases.iterator(); i.hasNext();) {
			final PhenophaseRow current = (PhenophaseRow) i.next();
			final int phenophaseChrono = phenophases.indexOf(current);
			ImageView button = new ImageView(this);
			// button.setImageBitmap(Drawable.createFromPath(pathName)
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = getIntent();
					intent.putExtra("chrono", phenophaseChrono);
					startActivity(intent);
					finish();
				}
			});
			button.setPadding(1, 0, 1, 0);

			Bitmap icon = overlay(BitmapFactory.decodeStream(current.getImageStream(this, plant.species().protocol_id)));

			//if it is not the current phenophase, grey out the icon
			if (chrono != phenophaseChrono)
				icon = overlay(icon, BitmapFactory.decodeResource(getResources(), R.drawable.translucent_gray));

			//if this observation has been taken, display the checkmark
			ObservationRow current_obs = plant.observations(current);
			if (current_obs != null && current_obs.isSaved())
				icon = overlay(icon, BitmapFactory.decodeResource(getResources(), R.drawable.check_mark));

			button.setImageBitmap(icon);
			phenophaseBar.addView(button);
		}

		//the button to press to actually save the observation to the db.
		Button save = (Button) this.findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { 
				EditText note = (EditText) findViewById(R.id.notes);

				//get the current note, and set the time.
				observation.note = note.getText().toString();
				observation.time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				
				//actually commit the data to the database
				observation.put();
				finish();
			}
		});
		
		//the button to press to cancel any changes
		Button cancel = (Button) this.findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { 
				//should restart the activity?
				//startActivity(PlantInfo.this.getIntent());
				//TODO: there might be an image leak here, if the user has taken a new photo, and then cancels, the image will probably still be on the sdcard.
				//the new observation has not been commited so just finish
				finish();
			}
		});

		//this actually registers the reciever so it will be notified when the user logs out.
		registerReceiver(mLoggedInReceiver, new IntentFilter(Constants.INTENT_ACTION_LOGGED_OUT));

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//set the image because it was replaced, (but not saved in the db yet)
        if(image_id != null) {
        	observation.image_id = image_id;
        }
		
		//show replace image/add image/remove image stuff
		showReplaceRemovePhotoButtons();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mLoggedInReceiver);
	}

	// just a helper function to overylay a bunch of bitmaps on eachother.
	private Bitmap overlay(Bitmap... bitmaps) {
		if (bitmaps.length == 0)
			return null;

		Bitmap bmOverlay = Bitmap.createBitmap(bitmaps[0].getWidth(), bitmaps[0].getHeight(), bitmaps[0].getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		for (int i = 0; i < bitmaps.length; i++)
			canvas.drawBitmap(bitmaps[i], new Matrix(), null);
		return bmOverlay;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		//we should save the image_id incase the activity needs to be restarted
		if (image_id != null)
			savedInstanceState.putLong("image_id", image_id);

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.d(TAG, "restore instance state");
		if(savedInstanceState.containsKey("image_id"))
			image_id = savedInstanceState.getLong("image_id");
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// You can use the requestCode to select between multiple child
		// activities you may have started. Here there is only one thing
		// we launch.
		Log.d(TAG, "onActivityResult");
		if (requestCode == PHOTO_CAPTURE_CODE) {

			// This is a standard resultCode that is sent back if the
			// activity doesn't supply an explicit result. It will also
			// be returned if the activity failed to launch.
			if (resultCode == RESULT_CANCELED) {
				Log.d(TAG, "Photo returned canceled code.");
				Toast.makeText(this, "Picture cancelled.", Toast.LENGTH_SHORT).show();
			} else {
				
				if (image_id != null) {
					//the picture went through, update the observation, but don't commit it yet.
					
					observation.image_id = image_id;
					showReplaceRemovePhotoButtons();
				}
			}
		}
	}

	//helper function to deal with the different states of the add image, replace image, remove image buttons and the actual observation image
	private void showReplaceRemovePhotoButtons() {
		TextView no_photo_text = (TextView) this.findViewById(R.id.no_photo_text);
		TextView replace_photo_text = (TextView) this.findViewById(R.id.take_photo_text);
		View remove_photo = this.findViewById(R.id.no_photo);

		img.setImageBitmap(null);
		
		if(observation != null && observation.hasImage()) {
			img.setImageBitmap(BitmapFactory.decodeFile(observation.getImagePath()));

			no_photo_text.setText("Remove Photo");
			remove_photo.setVisibility(View.VISIBLE);
			replace_photo_text.setText("Replace Photo");
		} else {
			remove_photo.setVisibility(View.GONE);
			replace_photo_text.setText("Add Photo");
		}
	}
}
