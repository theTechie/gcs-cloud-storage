package com.iit.cloudstorageapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemCacheHelper {
	public static final int CACHE_FILE_MAX_SIZE = 102400; // 100 KB
	
	private static MemcacheService c = MemcacheServiceFactory
			.getMemcacheService();
	

	public static boolean containsKey(Object key) {
		return c.contains(key);
	}

	public static Object get(Object key) {
		return c.get(key);
	}

	// returns only the cached key/object pairs
	public static Map<String, Object> getAll(Collection<String> keys) {
		return c.getAll(keys);
	}

	public static void put(Object key, Object value) {
		c.put(key, value);
	}

	public static void putAll(Map<String, Object> values) {
		c.putAll(values);
	}

	public static boolean remove(Object key) {
		return c.delete(key);
	}
	
	public static void removeAll(ArrayList<String> keys) {
		c.deleteAll(keys);
	}
	
	public static long getCacheSizeElem() {
		return c.getStatistics().getItemCount();
	}
	
	public static double getCacheSizeMB() {
		return c.getStatistics().getTotalItemBytes() / 1e6;
	}
}
