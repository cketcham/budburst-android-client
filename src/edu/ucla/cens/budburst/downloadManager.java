package edu.ucla.cens.budburst;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import edu.ucla.cens.budburst.helper.Cache;
import edu.ucla.cens.budburst.helper.Download;
import edu.ucla.cens.budburst.helper.Downloadable;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class downloadManager {

	private static final String TAG = "DownloadManager";
	public static final int CONSUME_INPUTSTREAM = 0;
	public static final int DOWNLOADED = 1;
	
	private Cache cache = new Cache();

	public downloadManager() {
		Log.d(TAG,"make download manager");
	}
	
	public void download(Downloadable context, int what, Download d) {
		Log.d(TAG,"start to download for " + context.toString());
		if(!cache.containsKey(d) || d.isVolitile()) {
			new DownloadTask().execute(new DownloadObject(context,what,d));
			cache.put(d, null);
		} else if(cache.get(d) != null) {
			Message msg = new Message();
			msg.what = what;
			msg.obj = cache.get(d);
			context.onDownloaded(msg, d);
		}
	}
	
	public Boolean has(Download d) {
		return cache.containsKey(d);
	}
	
	public Object get(Downloadable context, Download d) {
		if(!cache.containsKey(d)) {
			new DownloadTask().execute(new DownloadObject(context,0,d));
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
	
	public class DownloadTask extends AsyncTask<DownloadObject, Void, DownloadObject> {
		
		protected DownloadObject doInBackground(DownloadObject... downloadObjects) {
			Log.d(TAG,"start downloading");
			DownloadObject d = downloadObjects[0];
			try {
				
				// Create new client.
				HttpClient httpClient = new DefaultHttpClient();

				// Form request with post data.
				HttpPost httpRequest = new HttpPost(d.download.url);
				if(d.download.data != null)
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

			Log.d(TAG,"finished downloading");
			return d;
		}

		protected void onPostExecute(DownloadObject downloaded) {
			returnResult(downloaded);
		}
		
	}
	
	protected void returnResult(DownloadObject downloaded) {
		Message msg = new Message();
		msg.what = downloaded.what;
		
		//convert the input stream to a result if we need to
		if(downloaded.result == null) {
			msg.obj = downloaded.streamResult;
			downloaded.result = downloaded.context.consumeInputStream(msg);
			cache.put(downloaded.download,downloaded.result);
		}
		
		//then return the result
		msg.obj = downloaded.result;
		downloaded.context.onDownloaded(msg, downloaded.download);
	}
}

