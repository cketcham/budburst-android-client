package edu.ucla.cens.budburst.helper;

import java.util.HashMap;

public class Cache {
	HashMap<Download, CacheObject> cache = new HashMap<Download, CacheObject>();

	public Object get(Download key) {
		if (containsKey(key))
			return cache.get(key).object;

		return null;
	}

	public void put(Download key, Object o) {
		// cache.put(key, new CacheObject(o));
	}

	public Boolean containsKey(Download key) {
		if (cache.containsKey(key) && !cache.get(key).isExpired())
			return true;

		return false;
	}

}
