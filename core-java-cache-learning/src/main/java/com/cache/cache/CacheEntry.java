package com.cache.cache;

public class CacheEntry <V>{

    private final V value;
    private final long expiryTime;

    public CacheEntry(V value,long ttlMillis)
    {
        this.value=value;
        this.expiryTime = System.currentTimeMillis()+ttlMillis;
    }

    public V getValue()
    {
        return value;
    }

    public boolean isExpired()
    {
        return System.currentTimeMillis() > expiryTime;
    }

}
