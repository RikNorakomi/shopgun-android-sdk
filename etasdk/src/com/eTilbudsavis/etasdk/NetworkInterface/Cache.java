package com.eTilbudsavis.etasdk.NetworkInterface;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.eTilbudsavis.etasdk.NetworkInterface.Request.Method;

public class Cache implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TAG = "EtaCache";
	
	private Map<String, Item> mItems;
	
	public Cache() {
		mItems = Collections.synchronizedMap(new HashMap<String, Cache.Item>());
	}
	
	public void clear() {
		mItems.clear();
	}
	
	public void put(Request<?> request, Response<?> response) {
		
        // If the request is cachable
        if (request.getMethod() == Method.GET && request.isCachable() && !request.isCacheHit() && response.cache != null) {
        	request.addEvent("add-response-to-cache");
			mItems.putAll(response.cache);
        }
        
	}
	
	public Cache.Item get(String key) {
		
		Cache.Item c = mItems.get(key);
		if (c == null) {
			return null;
		} else if (c.isExpired()) {
			mItems.remove(key);
			return null;
		}
		return c;
		
	}
	
	public static class Item {
		
		// Time of insertion
		public final long expires;
		public final Object object;
		
		public Item(Object o, long ttl) {
			this.expires = System.currentTimeMillis() + ttl;
			this.object = o;
		}
		
		/**
		 * Returns true if the Item is still valid.
		 * 
		 * this is based on the time to live factor
		 * @return
		 */
		public boolean isExpired() {
			return expires < System.currentTimeMillis();
		}
		
	}
	
}
