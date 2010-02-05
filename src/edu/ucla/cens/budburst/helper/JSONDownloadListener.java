package edu.ucla.cens.budburst.helper;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

public interface JSONDownloadListener{

	public String onItemDownloaded(String from, JSONObject jo);
	
}
