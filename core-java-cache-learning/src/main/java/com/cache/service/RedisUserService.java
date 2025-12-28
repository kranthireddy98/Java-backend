package com.cache.service;

import com.cache.model.User;
import com.cache.repository.UserRepository;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;

public class RedisUserService {

    private final String USER_KEY_PREFIX = "user:";

    private final UserRepository repository =new UserRepository();

    //Jedis connection
    private final Jedis jedis = new Jedis("localhost",6379);

    //TTL in seconds
    private static final int TTL_SECONDS = 300;

    public User getUser(int userId) throws SQLException {
        String key = USER_KEY_PREFIX + userId;

        // Check Redis

        String cachedValue = jedis.get(key);

        if(cachedValue !=null)
        {
            System.out.println("REDIS CACHE HIT");
            return deserialize(cachedValue);
        }

        //Cache miss --> DB call
        System.out.println("Redis Cache miss -> DB call");
        User user = repository.getUserById(userId);
        //put into redis with ttl
        jedis.setex(key,TTL_SECONDS,serialize(user));

        return user;
    }

    public User getIfPresent(int userId) {
        String key = USER_KEY_PREFIX + userId;
        String cachedValue = jedis.get(key);
        return cachedValue != null ? deserialize(cachedValue) : null;
    }


    public void put(int userId, User user) {
        jedis.setex(USER_KEY_PREFIX + userId, TTL_SECONDS, serialize(user));
    }

    private String serialize(User user) {
        return user.getId() + "," + user.getName();
    }

    private User deserialize(String cachedValue) {
        String[] parts = cachedValue.split(",");

        return new User(Integer.parseInt(parts[0]),parts[1]);
    }

    public void close() {
        jedis.close();
    }

    public void evictUser(int id) {
    }
}
