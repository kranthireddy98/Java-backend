package com.cache.CodePrac;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrencyContentionLab {

    public static void main(String[] args) throws InterruptedException {

        //1. create a cache with stats enabled
        Cache<Integer,String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .removalListener((Integer key,String value, RemovalCause cause) ->{
                    System.out.println("Key "+key+" was removed. Reason: "+cause);
                })
                .recordStats()
                .build();

        //2. Create a thread pool to simulate 50 concurrent users
        ExecutorService executor = Executors.newFixedThreadPool(50);

        System.out.println("---- Simulating High Load----");
        for (int i=0; i<10000;i++){
            final int key = i;
            executor.submit(() ->{
                cache.put(key,"Value_" + key);
                cache.getIfPresent(key);
                int[] ar = {1,2};
               // cache.getAll(ar);
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        //3. Look at the stats
        //Notice 'requestCount` vs how many items actually survived

        System.out.println("Final Cache Size: " + cache.estimatedSize());
        System.out.println("Maintenance happened? " + cache.stats());
     }
}
