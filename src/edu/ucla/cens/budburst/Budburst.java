package edu.ucla.cens.budburst;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Budburst extends Activity {
	private static final String TAG = "Budburst";
	private static final int DOWNLOADED_DATABASES = 0;
	private static final int FINISHED = 1;
	private static BudburstDatabaseManager dbManager;
	private static DownloadManager downloadManager;
	public static String IMAGE_PATH = "/sdcard/budburst/";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		dbManager = new BudburstDatabaseManager(this);
		downloadManager = new DownloadManager();

		PreferencesManager.letUserIn("android", "android", this);

		// make sure budburst directory is set
		if (!new File(IMAGE_PATH).exists())
			new File(IMAGE_PATH).mkdirs();

		startActivityForResult(new Intent(this, SyncDatabases.class), DOWNLOADED_DATABASES);
	}

	public static BudburstDatabaseManager getDatabaseManager() {
		return dbManager;
	}

	public static DownloadManager getDownloadManager() {
		return downloadManager;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case DOWNLOADED_DATABASES:
			startActivityForResult(new Intent(this, PlantList.class), FINISHED);
			break;
		case FINISHED:
			finish();
			break;
		}

	}

}