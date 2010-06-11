package edu.ucla.cens.budburst;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import edu.ucla.cens.budburst.helper.Cache;
import edu.ucla.cens.budburst.helper.Download;
import edu.ucla.cens.budburst.helper.Downloadable;
import edu.ucla.cens.budburst.helper.Uploadable;

public class DownloadManager {

	private static final String TAG = "DownloadManager";
	public static final int CONSUME_INPUTSTREAM = 0;
	public static final int DOWNLOADED = 1;

	private final Cache cache = new Cache();

	public DownloadManager() {
		Log.d(TAG, "make download manager");
	}
	
	// A simple downloadManager which can be used to download data from the web.
	// you get a reference to the downloadManager from any Activity using Budburst.getDownloadManager().
	// you can then call download, or upload with a download object d (which give the url and any parameters)
	// it will store the data in the cache so it doesn't always have to go to the web.
	
	
	//if you have an activity that you want to download content for, it shoud implement
	//downloadable. you will have two functions consumeInputstream, and onDownloaded.
	//consumeInputstream should return the inputstream returned by the http call into
	//an object that can be used a String for example.\
	//onDownloaded returns the object you created from consumeInputstream back to your activity
	//this is where you can actually use the dat that you downloaded.
	public void download(Downloadable context, int what, Download d) {
		Log.d(TAG, "start to download for " + context.toString());
		if (!cache.containsKey(d) || d.isVolitile()) {
			new DownloadTask().execute(new DownloadObject(context, what, d));
			cache.put(d, null);
		} else if (cache.get(d) != null) {
			Message msg = new Message();
			msg.what = what;
			msg.obj = cache.get(d);
			context.onDownloaded(msg, d);
		}
	}
	
	//the asynchronus upload works slightly differently. any activity which wants to use
	//upload should implement uploadable. that includes the function upload which is called
	//asyncronusly
	public void upload(Uploadable context, int what, Download d) {

		new UploadTask().execute(new UploadObject(context, what, d));

	}

	public Boolean has(Download d) {
		return cache.containsKey(d);
	}

	public Object get(Downloadable context, Download d) {
		if (!cache.containsKey(d)) {
			new DownloadTask().execute(new DownloadObject(context, 0, d));
			cache.put(d, null);
		}
		return cache.get(d);
	}

	public class DownloadObject {
		public Downloadable context;
		public int what;
		public Download download;

		public InputStream streamResult;
		public Object result;

		public DownloadObject(Downloadable context, int what, Download d) {
			this.context = context;
			this.what = what;
			this.download = d;
		}
	}

	public class UploadObject {
		public Uploadable context;
		public int what;
		public Download download;

		public InputStream streamResult;
		public Object result;

		public UploadObject(Uploadable context, int what, Download d) {
			this.context = context;
			this.what = what;
			this.download = d;
		}
	}

	// the actual download code which gets called asynchronously 
	public class DownloadTask extends AsyncTask<DownloadObject, Void, DownloadObject> {

		@Override
		protected DownloadObject doInBackground(DownloadObject... downloadObjects) {
			Log.d(TAG, "start downloading");
			DownloadObject d = downloadObjects[0];
			try {

				// Create new client.
				HttpClient httpClient = new DefaultHttpClient();

				// Form request with post data.
				HttpPost httpRequest = new HttpPost(d.download.url);
				if (d.download.data != null)
					httpRequest.setEntity(new UrlEncodedFormEntity(d.download.data, HTTP.UTF_8));

				// Send request.
				HttpResponse response = httpClient.execute(httpRequest);

				// Get message.
				HttpEntity entity = response.getEntity();
				Header a = response.getEntity().getContentType();

				// Get status.
				int status = response.getStatusLine().getStatusCode();

				// Act on result.
				if (status == 200) {
					d.streamResult = entity.getContent();
				}

			} catch (ClientProtocolException e) {
				Log.e("httpPost", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.e("httpPost", e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				if (e.getMessage() != null)
					Log.e("httpPost", e.getMessage());
				e.printStackTrace();
			}

			Log.d(TAG, "finished downloading");
			return d;
		}

		@Override
		protected void onPostExecute(DownloadObject downloaded) {
			returnResult(downloaded);
		}

	}

	//the actual upload asyncTask
	private class UploadTask extends AsyncTask<UploadObject, Void, UploadObject> {

		@Override
		protected UploadObject doInBackground(UploadObject... params) {
			params[0].context.upload(params[0].what, params[0].download);
			return params[0];
		}

	}

	protected void returnResult(DownloadObject downloaded) {
		Message msg = new Message();
		msg.what = downloaded.what;

		// convert the input stream to a result if we need to
		if (downloaded.result == null) {
			msg.obj = downloaded.streamResult;

			// TODO HACK
			// if
			// (downloaded.download.url.contains("http://cens.solidnetdns.com/~kmayoral/PBB/PBsite_CENS/images/"))
			// {
			// String s =
			// downloaded.download.url.split("http://cens.solidnetdns.com/~kmayoral/PBB/PBsite_CENS/images/")[1];
			// msg.arg2 = Integer.valueOf(s.split(".jpg")[0]);
			// }

			downloaded.result = downloaded.context.consumeInputStream(msg);
			cache.put(downloaded.download, downloaded.result);
		}

		// then return the result
		msg.obj = downloaded.result;
		downloaded.context.onDownloaded(msg, downloaded.download);
	}
}
