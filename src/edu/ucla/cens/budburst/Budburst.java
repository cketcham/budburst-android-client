package edu.ucla.cens.budburst;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Budburst extends Activity {
	private static final String TAG = "Budburst";

	private static BudburstDatabaseManager dbManager;
	private static DownloadManager downloadManager;

	public static String BASE_PATH = "/sdcard/budburst/";
	public static String OBSERVATION_PATH = BASE_PATH + "observation/";
	public static String SPECIES_PATH = BASE_PATH + "species/";

	public static int MAX_PREDEFINED_SPECIES = 96;

	/**
	 * TODO:
	 * 
	 * <pre>
	 * Add budburst logo and images
	 * change headers for main page
	 * remove headers from add plant page
	 * add a way to sync rather than restarting the app
	 * fix image upload bug
	 * add login screen
	 * make plant info screen look nicer, with the correct text color
	 * 
	 * make the sync screen a floating window
	 * add ability to make one time observation
	 * add ability to delete plants / observations
	 * add ability to add notes to a certain plant
	 * </pre>
	 */

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//Starting point for application

		startActivity(new Intent(this, SplashScreen.class));

		// create references to the databaseManager and downloadManager
		// this can both be used from other activites by calling Budburst.getDownloadManager() or Budburst.getDatabaseManager(). They allow you to access the downloadManager or databaseManager.
		dbManager = new BudburstDatabaseManager(this);
		downloadManager = new DownloadManager();

		// Debugging code
		// PreferencesManager.letUserIn("android4", "android4", this);

		if (PreferencesManager.isFirstRun(this)) {

			// First Time stuff
			// make sure budburst directory is set
			if (!new File(BASE_PATH).exists())
				new File(BASE_PATH).mkdirs();
			if (!new File(OBSERVATION_PATH).exists())
				new File(OBSERVATION_PATH).mkdirs();
			if (!new File(SPECIES_PATH).exists())
				new File(SPECIES_PATH).mkdirs();

			// move species_images
			try {
				String[] images = getAssets().list("species_images");
				for (int i = 0; i < images.length; i++) {

					OutputStream outputStream = new FileOutputStream(SPECIES_PATH + images[i]);
					InputStream inputStream;

					byte[] buffer = new byte[1024];
					inputStream = getAssets().open("species_images/" + images[i]);
					while ((inputStream.read(buffer)) > 0) {
						outputStream.write(buffer);
					}
					inputStream.close();

					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// End first time stuff

		finish();
	}

	public static BudburstDatabaseManager getDatabaseManager() {
		return dbManager;
	}

	public static DownloadManager getDownloadManager() {
		return downloadManager;
	}

}