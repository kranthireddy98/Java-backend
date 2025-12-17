package com.cache.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class TTLCache <K,V>{

    private final Map<K,CacheEntry<V>> store = new ConcurrentHashMap<>();

    private final int maxSize;
    private final long ttlMillis;

    public TTLCache(int maxSize,long ttlMillis)
    {
        this.maxSize= maxSize;
        this.ttlMillis = ttlMillis;
    }

    public V get(K key, Function<K,V> loader)
    {
        CacheEntry<V> entry= store.get(key);

        if(entry!=null && !entry.isExpired())
        {
            System.out.println("CACHE HIT");

            return entry.getValue();
        }

        V value = loader.apply(key);

        if(store.size()>=maxSize)
        {
            evictOne();
        }
        store.put(key,new CacheEntry<>(value,ttlMillis));

        return value;
    }

    private void evictOne() {

        K ketToRemove = store.keySet().iterator().next();
        store.remove(ketToRemove);
        System.out.println("EVICTED KEY: " + ketToRemove);

    }
}
