package com.cache.CodePrac;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class MaxSize {
    public static void main(String[] args) {

        Cache<Integer,String> cache = Caffeine.newBuilder()
                .maximumSize(10)
                .evictionListener((key,value,cause) ->
                        System.out.println("Key: " + key+" Value : "+value+" evicted! Reason: " + cause))
                .recordStats()
                .build();

        for (int i =0;i<10;i++){
            cache.put(i,"User: " +i);
        }


        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));
        System.out.println(cache.getIfPresent(0));


        cache.put(10,"User: 10");
        System.out.println("--- Starting Lab ---");

        System.out.println(cache.toString());
        cache.cleanUp();
        cache.put(1,"User: 1");
        System.out.println("Cache Size: " + cache.estimatedSize());
        System.out.println("Stats: " + cache.stats());
    }
}
