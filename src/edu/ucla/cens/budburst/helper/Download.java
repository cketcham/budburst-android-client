package edu.ucla.cens.budburst.helper;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;

import android.os.Handler;

public class Download implements Comparable {
	public String url = "";
	public List<NameValuePair> data = new ArrayList<NameValuePair>();
	private Boolean isVolitile = false;
	public Handler handler;
	
	public Download(String url) {
		this.url = url;
	}
	
	public Download(String url, Boolean isVolitile) {
		this.url = url;
		this.isVolitile = isVolitile;
	}
	
	public Download(String url, List<NameValuePair> data) {
		this.url = url;
		this.data = data;
	}
	
	public Download(String url, List<NameValuePair> data, Boolean isVolitile) {
		this.url = url;
		this.data = data;
		this.isVolitile = isVolitile;
	}

	public String toString() {
		String ret = url;
		for(Iterator<NameValuePair> i = data.iterator();i.hasNext();) {
			NameValuePair current = i.next();
			ret += current.getName() + current.getValue();
		}
		
		return ret;
	}

	public int compareTo(Object another) {
		return this.toString().compareTo(((Download) another).toString());
	}
	
	public int hashCode() {
		return this.toString().hashCode();
	}

	public boolean equals(Object o) {
		return this.toString().equals(((Download) o).toString());
	}

	public boolean isVolitile() {
		return isVolitile ;
	}
}
