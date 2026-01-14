package com.cache.service;

import com.cache.model.User;
import com.cache.repository.UserRepository;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.sql.SQLException;
import java.util.concurrent.*;

public class CaffeineUserService {
    private UserRepository repository = new UserRepository();

    private final ExecutorService dbExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );


    private final Cache<Integer, User> cache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .expireAfterWrite(5, TimeUnit.MINUTES)
                    .build();

    public void shutdown() {
        dbExecutor.shutdown();
    }
    //Async Caffeine cache.
    private final AsyncLoadingCache<Integer,User> asyncCache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)//Eviction
                    .maximumWeight(20_000)
                    .expireAfterWrite(30,TimeUnit.MINUTES)//Hard TTL
                    .refreshAfterWrite(5,TimeUnit.MINUTES)// Refresh-ahead
                    .recordStats()//Metrics
                    .executor(dbExecutor)
                    .buildAsync(userId ->
                    {
                        System.out.println("CACHE MISS -> DB CALL ASYNC Caffeine");
                        return repository.getUserById(userId);
                    });

    public User asyncGetUser(int id)
    {
        return asyncCache.get(id).join();
    }
    public void evictUser (int id)
    {
        asyncCache.synchronous().invalidate(id);
    }

    public void printCacheStats()
    {
        System.out.println(asyncCache.synchronous().stats());
    }
    public User getUser(int id)
    {
        System.out.println(cache.stats());
        return cache.get(id,key ->{
            System.out.println("CACHE MISS -> DB CALL Caffeine");
            try {
                return repository.getUserById(key);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public User getIfPresent(int id) {
        return asyncCache.synchronous().getIfPresent(id);
    }

    public void put(int userId,User user)
    {
        asyncCache.synchronous().put(userId,user);
    }

    public void evict(int id) {
        asyncCache.synchronous().invalidate(id);
    }
}
