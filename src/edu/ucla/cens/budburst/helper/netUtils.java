package edu.ucla.cens.budburst.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.ImageView;

public class netUtils {
	public static final String TAG = "NetUtils";

	/**
	 * @param stream InputStream of data from httpResponse.getContent()
	 * @return a String with the contents from the response.
	 */
	public static String generateString(InputStream stream) {
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader buffer = new BufferedReader(reader);
		StringBuilder sb = new StringBuilder();

		try {
			String cur;
			boolean begun = false;
			while ((cur = buffer.readLine()) != null) {
				if (begun)
					sb.append("\n");
				else
					begun = true;
				sb.append(cur);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String returnVal = sb.toString();
		return returnVal;
	}

	/**
	 * 
	 * @param ctx - pass this from your Activity or Service
	 * @return true if the phone has a connection to the Internet.
	 */
	public static boolean isConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] ni = cm.getAllNetworkInfo();
		boolean connected = false;
		for (int n = 0; n < ni.length; n++) {
			// Log.d(TAG,"Network "+ni[n].getTypeName() + " is "+ ni[n].getDetailedState().name() + "\n");
			if (ni[n].getDetailedState().name() == "CONNECTED")
				connected = true;
		}
		return connected;
	}

	/**
	 * 
	 */

	public static String[] postData(String url, List<NameValuePair> arguments) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url.toString());

		int status = 0;
		String responseVal = "";

		try {
			// Add your data

			// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			// nameValuePairs.add(new BasicNameValuePair("id", "12345"));
			// nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));

			httppost.setEntity(new UrlEncodedFormEntity(arguments));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();

			responseVal = netUtils.generateString(entity.getContent());

			// Get status.
			status = response.getStatusLine().getStatusCode();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		String[] response = new String[2];
		response[0] = Integer.toString(status);
		response[1] = responseVal;

		return response;
	}

	public static class DownloadImage extends AsyncTask<Object, Void, Drawable> {
		ImageView mImage = null;
		URL mUrl = null;

		@Override
		protected Drawable doInBackground(Object... params) {
			mImage = (ImageView) params[0];
			mUrl = (URL) params[1];

			Drawable d = null;
			try {
				d = Drawable.createFromStream((InputStream) mUrl.getContent(), "src");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return d;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			if (result != null) {
				mImage.setImageDrawable(result);
			}
		}

	}
}
