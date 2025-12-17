package com.cache.service;


import com.cache.model.User;
import com.cache.repository.UserRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurentUserService {

    private UserRepository repository = new UserRepository();

    private Map<Integer, User> cache = new ConcurrentHashMap<>();


    public User getUser(int id) {
        return cache.computeIfAbsent(id, key -> {
            System.out.println("CACHE MISS -- DB CALL");
            return repository.gerUserById(key);// SQL Server call
        });
    }
}
