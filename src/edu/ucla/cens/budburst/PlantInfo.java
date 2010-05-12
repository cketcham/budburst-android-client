
package edu.ucla.cens.budburst;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

	private PlantRow plant;
	private PhenophaseRow phenophase;
	private ObservationRow observation;
	private int chrono;
	private int stageID;
	private String stageName;

	ArrayList<Button> buttonBar = new ArrayList<Button>();
	private BudburstDatabaseManager databaseManager;
	protected Long image_id;

	private TextView name;
	private TextView state;
	private TextView phenophase_comment;
	private ImageView img;

	private EditText note;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sampleinfo);

		name = (TextView) this.findViewById(R.id.name);
		state = (TextView) this.findViewById(R.id.state);
		phenophase_comment = (TextView) this.findViewById(R.id.phenophase_info_text);
		img = (ImageView) this.findViewById(R.id.image);

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
			stageName = "flower";
			break;
		case BudburstDatabaseManager.FRUITS:
			stageName = "fruit";
			break;
		}

		// get plant, and phenophases
		plant = (PlantRow) databaseManager.getDatabase("plant").find(extras.getLong("PlantID"));
		phenophase = (PhenophaseRow) plant.species().phenophases(stageName).get(chrono);
		observation = plant.observations(phenophase);
		
		if(observation == null) {
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

		LinearLayout phenophaseBar = (LinearLayout) this.findViewById(R.id.phenophase_bar);

		final ArrayList<Row> phenophases = plant.species().phenophases(stageName);

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

			if (this.chrono != phenophaseChrono)
				icon = overlay(icon, BitmapFactory.decodeResource(getResources(), R.drawable.translucent_gray));

			if (observation!= null && observation.image_id != 0)
				icon = overlay(icon, BitmapFactory.decodeResource(getResources(), R.drawable.check_mark));

			button.setImageBitmap(icon);
			phenophaseBar.addView(button);
		}

		name.setText(plant.species().common_name);

		phenophase_comment.setText(((PhenophaseRow) phenophases.get(chrono)).getAboutText(plant.species().protocol_id));

		// display image if there is one
		if (observation != null)
			img.setImageBitmap(BitmapFactory.decodeFile(observation.getImagePath()));

		state.setText(phenophase.name);

		View replace_img = this.findViewById(R.id.replace_image);
		replace_img.setOnClickListener(new View.OnClickListener() {

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
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		if (image_id != null)
			savedInstanceState.putLong("image_id", image_id);

		if (note != null)
			savedInstanceState.putString("note", note.getText().toString());

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.d(TAG, "restore instance state");
		image_id = savedInstanceState.getLong("image_id");

		note = makeNoteEditText();
		note.setText(savedInstanceState.getString("note"));
	}

	protected EditText makeNoteEditText() {
		EditText ret = new EditText(this);
		ret.setLines(4);
		ret.setGravity(Gravity.TOP);
		return ret;
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

				if (image_id != null) {

					observation.image_id = image_id;
					observation.put();

					img.setImageDrawable(Drawable.createFromPath(observation.getImagePath()));
				}
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD_NOTE, 0, "Set Note").setIcon(android.R.drawable.ic_menu_edit);

		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public Dialog onCreateDialog(int id) {
		AlertDialog alert = null;
		switch(id) {
		case MENU_ADD_NOTE:

			if(note == null) {
				note =  makeNoteEditText();
				note.setText(observation.note);
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(note).setTitle("Set Note").setCancelable(true)
			.setPositiveButton("Save", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					observation.note = note.getText().toString();
					observation.put();
				}
			})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					note.setText(observation.note);
				}
			});

			alert = builder.create();
			break;
		} 
		return alert;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD_NOTE:

			this.showDialog(MENU_ADD_NOTE);
		}

		return super.onOptionsItemSelected(item);
	}

}
