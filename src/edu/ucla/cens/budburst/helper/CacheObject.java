package edu.ucla.cens.budburst.helper;

import android.util.Log;

public class CacheObject {

	private static final String TAG = "CacheObject";
	
	public Object object;
	public long expiry;
	
	public CacheObject(Object o) {
		this(o,Long.valueOf(5 * 60000));
	}
	
	public CacheObject(Object o, Long keepTime) {
		this.object = o;
		this.expiry = System.currentTimeMillis() + keepTime;
	}
	
	public Boolean isExpired() {
		return expiry < System.currentTimeMillis();
	}
	
	
}
