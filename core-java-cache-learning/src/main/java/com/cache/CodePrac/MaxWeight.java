package com.cache.CodePrac;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class MaxWeight {
    public static void main(String[] args) {

        //1. create a weighted cache (Total capacity:1000 units)
        Cache<Integer,byte[]> cache = Caffeine.newBuilder()
                .maximumWeight(1000)
                .weigher((Integer key, byte[] value) -> value.length)
                .evictionListener((key,value,cause) ->
                        System.out.println("Key: " + key+" evicted! Reason: " + cause))
                .recordStats()
                .build();

        System.out.println("--- Starting Lab ---");

        //2. put a "large" object (600 units)
        cache.put(1,new byte[600]);

        //3. put another "Large" object (500 units)
        //This exceeds 1000 total. watch the eviction listener!
        cache.put(2,new byte[500]);

        // Caffeine performs eviction asynchronously to stay fast.
        // We clean up manually for the demo to see the logs immediately.
        cache.cleanUp();

        System.out.println("Cache Size: " + cache.estimatedSize());
        System.out.println("Stats: " + cache.stats());

    }
}
