package com.cache.service;


import com.cache.model.User;
import com.cache.repository.UserRepository;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurentUserService {

    private UserRepository repository = new UserRepository();

    private Map<Integer, User> cache = new ConcurrentHashMap<>();


    public User getUser(int id) {
        return cache.computeIfAbsent(id, key -> {
            System.out.println("CACHE MISS -- DB CALL");
            try {
                return repository.getUserById(key);// SQL Server call
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
