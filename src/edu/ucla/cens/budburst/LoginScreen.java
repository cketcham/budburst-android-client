package edu.ucla.cens.budburst;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends Activity {
	Button loginButton, registerButton;
	EditText usernameInput, passwordInput;
	Vibrator vibrator;

	private CharSequence username;
	private CharSequence password;
	private HttpPost httpRequest;
	private DefaultHttpClient httpClient;
	private ProgressDialog mProgressDialog;
	private AsyncTask<Void, Void, Boolean> mLoginTask;

	private ProgressDialog showProgressDialog() {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle(R.string.login_dialog_title);
			dialog.setMessage(getString(R.string.login_dialog_message));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			mProgressDialog = dialog;
		}
		mProgressDialog.show();
		return mProgressDialog;
	}

	private void dismissProgressDialog() {
		try {
			mProgressDialog.dismiss();
		} catch (IllegalArgumentException e) {
			// We don't mind. android cleared it for us.
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (mLoginTask != null) {
			mLoginTask.cancel(true);
		}
		return mLoginTask;
	}

	private class LoginTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return true;
			// try {
			// if(true) return true;
			// // Create new client.
			// HttpClient httpClient = new DefaultHttpClient();
			//
			// // Compile post data.
			// List<NameValuePair> data = new ArrayList<NameValuePair>();
			// data.add(new BasicNameValuePair("username", username.toString()));
			// data.add(new BasicNameValuePair("password", password.toString()));
			//
			// // Form request with post data.
			// httpRequest = new HttpPost(getString(R.string.attemptLoginURL));
			// httpRequest.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
			//
			// // Send request.
			// HttpResponse response = httpClient.execute(httpRequest);
			//
			// // Get message.
			// HttpEntity entity = response.getEntity();
			// String responseVal = netUtils.generateString(entity.getContent());
			//
			// // Get status.
			// int status = response.getStatusLine().getStatusCode();
			//
			// // Act on result.
			// if (status == 200) {
			// }
			//
			// if (entity != null) {
			// // Delete entity.
			// entity.consumeContent();
			// }
			//					
			// Log.d("httppost", responseVal);
			//
			// if (responseVal.contains("success"))
			// {
			// return true;
			// }
			// else
			// {
			// return false;
			// }
			//					
			// } catch (ClientProtocolException e) {
			// Log.e("httpPost", e.getMessage());
			// e.printStackTrace();
			// } catch (IOException e) {
			// Log.e("httpPost", e.getMessage());
			// e.printStackTrace();
			// } catch (Exception e) {
			// if (e.getMessage() != null)
			// Log.e("httpPost", e.getMessage());
			// e.printStackTrace();
			// }
			// return false;
		}

		@Override
		protected void onPostExecute(Boolean loggedin) {
			dismissProgressDialog();

			if (loggedin) {
				PreferencesManager.letUserIn(username.toString(), password.toString(), LoginScreen.this);
				gotoMainScreen();
			} else {
				Toast.makeText(LoginScreen.this, "Sorry username / password wrong.", Toast.LENGTH_SHORT).show();
				long[] pattern = { 0L, 200L, 100L, 600L };
				vibrator.vibrate(pattern, -1);

			}
		}

		@Override
		protected void onCancelled() {
			dismissProgressDialog();
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Re-task if the request was cancelled.
		mLoginTask = (LoginTask) getLastNonConfigurationInstance();
		if (mLoginTask != null && mLoginTask.isCancelled()) {
			mLoginTask = new LoginTask().execute();
		}
	}

	/** Everytime screen is redrawn, this function is called */
	@Override
	public void onResume() {
		super.onResume();

		// if(netUtils.isConnected(loginScreen.this))
		// {
		if (PreferencesManager.isUserIn(this)) {
			gotoMainScreen();
		} else {
			setContentView(R.layout.loginscreen);
			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			usernameInput = (EditText) this.findViewById(R.id.username_text);
			passwordInput = (EditText) this.findViewById(R.id.password_text);
			registerButton = (Button) this.findViewById(R.id.register_button);

			if (PreferencesManager.getDefaultUser(this) != PreferencesManager.no_user) {
				usernameInput.setText(PreferencesManager.getDefaultUser(this));
			}

			registerButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(getString(R.string.registerURL)));
					startActivity(intent);
				}
			});

			loginButton = (Button) this.findViewById(R.id.login_button);
			loginButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(usernameInput.getWindowToken(), 0);
					attemptLogin();
				}
			});
		}
		// }
		// else
		// {
		// setContentView(R.layout.loginscreen);
		//
		// // Warn the user that there is no internet.
		// AlertDialog.Builder builder = new AlertDialog.Builder(
		// loginScreen.this);
		// builder
		// .setMessage(
		// "Currently there is no network connection.  Please open up the web browser and see what message appears.  If the browser says you need to upgrade data plan, please contact UCLA.  Otherwise, just retry this when you have proper cell service.")
		// .setCancelable(false).setPositiveButton(
		// "Okay",
		// new DialogInterface.OnClickListener() {
		// public void onClick(
		// DialogInterface dialog, int id) {
		// loginScreen.this.finish();
		// }
		// });
		// AlertDialog alert = builder.create();
		// alert.show();
		//
		// }
	}

	private void attemptLogin() {
		// Grab username and password.
		username = usernameInput.getText();
		password = passwordInput.getText();

		mLoginTask = new LoginTask().execute();

	}

	private void gotoMainScreen() {

		// Intent campaignIntent = new Intent(LoginScreen.this,
		// // campaignScreen.class);
		// PlantList.class);
		// LoginScreen.this.startActivity(campaignIntent);

		this.finish();
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