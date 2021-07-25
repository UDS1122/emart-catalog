package com.demo.uds.emartcatalog.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.demo.uds.emartcatalog.entity.Cart;

/*
 * Cache responsible for keeping the track of all the coins type and its respective available quantity
 * As user's request are entertained quantities will be updated.
 * Singleton instance that will be used throughout the application life time.
 */
public class CartCache {

	private static volatile Map<String, List<Cart>> CART_CACHE;
	
	//private constructor
	private CartCache(){
		//no-one should be able to call it
	}
	
	//Double Checking with lazy loading and thread-safety
	public static Map<String, List<Cart>> getCartCache(){
		if(CART_CACHE == null) {
            synchronized(CartCache.class) {
                // Double checking Singleton instance
                if(CART_CACHE == null) {
                	CART_CACHE = new ConcurrentHashMap<String, List<Cart>>();//for better performance in case of multiple reads and write
                }
            }
         }
		return CART_CACHE;
	}
}