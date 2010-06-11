
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
		
		img = (ImageView) this.findViewById(R.id.image);

		databaseManager = Budburst.getDatabaseManager();
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
		
		View take_photo = this.findViewById(R.id.take_photo);
		take_photo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				File ld = new File(Budburst.OBSERVATION_PATH);
				if (ld.exists()) {
					if (!ld.isDirectory()) {
						// Should probably inform user ... hmm!
						PlantInfo.this.finish();
					}
				} else {
					if (!ld.mkdir()) {
						PlantInfo.this.finish();

					}
				}

				image_id = new Date().getTime();

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
			
			//set date
			TextView timestamp = (TextView) this.findViewById(R.id.timestamp_text);
			timestamp.setText(observation.time);
			
			//put the note in the edittext
			EditText note = (EditText) this.findViewById(R.id.notes);
			note.setText(observation.note);
			
		} else {
			this.findViewById(R.id.timestamp_text).setVisibility(View.GONE);
			this.findViewById(R.id.timestamp_label).setVisibility(View.GONE);
			
			TextView make_obs_text = (TextView) this.findViewById(R.id.make_obs_text);
			make_obs_text.setText("Make an Observation for this Phenophase");
			
			observation = new ObservationRow();
			observation.species_id = plant.species_id;
			observation.phenophase_id = phenophase._id;
			observation.site_id = plant.site_id;
		}

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

		//populate the buttonbar
		LinearLayout phenophaseBar = (LinearLayout) this.findViewById(R.id.phenophase_bar);

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

			if (chrono != phenophaseChrono)
				icon = overlay(icon, BitmapFactory.decodeResource(getResources(), R.drawable.translucent_gray));

			ObservationRow current_obs = plant.observations(current);
			if (current_obs != null && current_obs.isSaved())
				icon = overlay(icon, BitmapFactory.decodeResource(getResources(), R.drawable.check_mark));

			button.setImageBitmap(icon);
			phenophaseBar.addView(button);
		}


		Button save = (Button) this.findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { 
				EditText note = (EditText) findViewById(R.id.notes);

				observation.note = note.getText().toString();
				observation.time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				observation.put();
				finish();
			}
		});
		
		Button cancel = (Button) this.findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { 
				//should restart the activity?
				//startActivity(PlantInfo.this.getIntent());
				finish();
			}
		});

		registerReceiver(mLoggedInReceiver, new IntentFilter(Constants.INTENT_ACTION_LOGGED_OUT));

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//set the image because it was replaced
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
					
					observation.image_id = image_id;
					showReplaceRemovePhotoButtons();
				}
			}
		}
	}

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
