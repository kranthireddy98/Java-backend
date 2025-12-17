package com.cache.service;


import com.cache.cache.NaiveCache;
import com.cache.cache.TTLCache;
import com.cache.model.User;
import com.cache.repository.UserRepository;

public class UserService {

    private UserRepository repository = new UserRepository();

    private NaiveCache<Integer, User> cache = new NaiveCache<>();

    private TTLCache<Integer,User> cacheTTL = new TTLCache<>(3,5000);

    public User getUser(int id) {
        if (cache.contains(id)) {
            System.out.println("CACHE HIT");
            return cache.get(id);
        }


        System.out.println("CACHE MISS -- DB CALL");

        User user = repository.gerUserById(id);

        cache.put(id, user);
        return user;
    }

    public User getUserTTL(int id) {
        return  cacheTTL.get(id,key -> {
            System.out.println("DB CALL");
            return repository.gerUserById(id);
        });
    }

}
