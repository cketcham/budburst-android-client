package edu.ucla.cens.budburst;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

	protected static final int DOWNLOAD_DATA_FINISHED = 0;
	/* Fields */
	private final int SPLASH_DISPLAY_LENGHT = 1500;

	/* Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				SplashScreen.this.finish();
			}
		}, SPLASH_DISPLAY_LENGHT);

	}

	public void onPause(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Log.d("HI", "Pausing");
	}

	public void onStop(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Log.d("HI", "Stopping");
	}
}