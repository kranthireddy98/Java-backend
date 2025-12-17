package com.cache.cache;


import java.util.HashMap;
import java.util.Map;

public class NaiveCache<K, V> {

    private Map<K, V> store = new HashMap<>();

    public V get(K key) {
        return store.get(key);
    }

    public void put(K key, V value) {
        store.put(key, value);
    }

    public boolean contains(K key) {
        return store.containsKey(key);
    }
}
