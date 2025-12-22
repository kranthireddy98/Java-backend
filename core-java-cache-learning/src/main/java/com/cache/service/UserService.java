package com.cache.service;


import com.cache.cache.NaiveCache;
import com.cache.cache.TTLCache;
import com.cache.model.User;
import com.cache.repository.UserRepository;

import java.sql.SQLException;

public class UserService {

    private UserRepository repository = new UserRepository();

    private NaiveCache<Integer, User> cache = new NaiveCache<>();

    private TTLCache<Integer,User> cacheTTL = new TTLCache<>(3,5000);

    public User getUser(int id) throws SQLException {
        if (cache.contains(id)) {
            System.out.println("CACHE HIT");
            return cache.get(id);
        }


        System.out.println("CACHE MISS -- DB CALL");

        User user = repository.getUserById(id);

        cache.put(id, user);
        return user;
    }

    public User getUserTTL(int id) {
        return  cacheTTL.get(id,key -> {
            System.out.println("DB CALL");
            try {
                return repository.getUserById(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
