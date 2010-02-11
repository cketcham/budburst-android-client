package edu.ucla.cens.budburst;

import java.util.ArrayList;

import models.ObservationRow;
import models.PhenophaseRow;
import models.PlantRow;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlantInfo extends Activity {

	private static final String TAG = "plant_info";
	protected static final int PHOTO_CAPTURE_CODE = 0;

	private PlantRow plant;
	private PhenophaseRow phenophase;
	private ObservationRow observation;
	private int chrono;
	private int stageID;
	private String stageName;

	ArrayList<Button> buttonBar = new ArrayList<Button>();
	private BudburstDatabaseManager databaseManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		databaseManager = Budburst.getDatabaseManager();
		Bundle extras = getIntent().getExtras();
		chrono = extras.getInt("chrono", 0);
		stageID = extras.getInt("StageID", BudburstDatabaseManager.LEAVES);

		// map stageID to stage Name
		switch (stageID) {
		case BudburstDatabaseManager.LEAVES:
			stageName = "leaves";
			break;
		case BudburstDatabaseManager.FLOWERS:
			stageName = "flowers";
			break;
		case BudburstDatabaseManager.FRUITS:
			stageName = "fruits";
			break;
		}

		// get plant, and phenophases
		plant = (PlantRow) databaseManager.getDatabase("plant").find(extras.getLong("PlantID"));
		phenophase = (PhenophaseRow) plant.species().phenophases(stageName).get(chrono);
		observation = plant.observations(phenophase);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		setContentView(R.layout.sampleinfo);

		TextView name = (TextView) this.findViewById(R.id.name);
		TextView state = (TextView) this.findViewById(R.id.state);
		ImageView img = (ImageView) this.findViewById(R.id.image);

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

		Button last_stage = (Button) this.findViewById(R.id.backward_button);
		if (chrono > 0) {
			last_stage.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = getIntent();
					intent.putExtra("chrono", chrono - 1);

					startActivity(intent);
					finish();
				}
			});
		} else {
			// hide the back button
			last_stage.setVisibility(View.GONE);
		}

		Button next_stage = (Button) this.findViewById(R.id.forward_button);
		if (plant.species().phenophases(stageName).size() > chrono) {
			next_stage.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = getIntent();
					intent.putExtra("chrono", chrono + 1);

					startActivity(intent);
					finish();
				}
			});
		} else {
			next_stage.setVisibility(View.GONE);
		}

		name.setText(plant.species().common_name);
		// display image if there is one

		if (observation != null)
			img.setImageBitmap(BitmapFactory.decodeFile(observation.getImagePath()));
		state.setText(phenophase.name);

		Button replace_img = (Button) this.findViewById(R.id.replace_image);
		replace_img.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(PlantInfo.this, CapturePhoto.class);
				startActivityForResult(intent, PHOTO_CAPTURE_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
				Long image_id = data.getLongExtra("image_id", -1);
				if (image_id != -1) {
					ObservationRow obs = new ObservationRow();
					obs.species_id = plant.species_id;
					obs.phenophase_id = phenophase._id;
					obs.image_id = image_id;
					obs.site_id = plant.site_id;
					obs.put();
				}
			}
		}
	}
}
