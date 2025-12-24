package com.cache;

import com.cache.model.User;
import com.cache.repository.UserRepository;
import com.cache.service.CaffeineUserService;
import com.cache.service.ConcurentUserService;
import com.cache.service.RedisUserService;
import com.cache.service.UserService;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException, SQLException {


        /*concurrentHashMap();
        userTTL();*/
        //caffeineUser();
        //asyncCaffeineUser();
        redisTest();

    }

    static void asyncCaffeineUser() throws InterruptedException {
        UserRepository repo = new UserRepository();

        ExecutorService executorService = Executors.newFixedThreadPool(25);


        CaffeineUserService caffeineUserService = new CaffeineUserService();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 25; i++) {
            executorService.submit(() -> {
                User user = caffeineUserService.asyncGetUser(1);
                System.out.println(Thread.currentThread().getName() + " --> " + user);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        long end = System.currentTimeMillis();


        System.out.println("Time take: " + (end - start) + " ms");

        caffeineUserService.printCacheStats();
        caffeineUserService.shutdown();
    }

    static void caffeineUser() throws InterruptedException {
        UserRepository repo = new UserRepository();

        ExecutorService executorService = Executors.newFixedThreadPool(25);


        CaffeineUserService caffeineUserService = new CaffeineUserService();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 25; i++) {
            executorService.submit(() -> {
                User user = caffeineUserService.getUser(1);
                System.out.println(Thread.currentThread().getName() + " --> " + user);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        long end = System.currentTimeMillis();


        System.out.println("Time take: " + (end - start) + " ms");


    }

    public static void redisTest() throws SQLException {

        RedisUserService redisUserService = new RedisUserService();

        redisUserService.getUser(1); // DB
        redisUserService.getUser(1); // Redis
        redisUserService.getUser(1); // Redis

        redisUserService.close();
    }


    static void concurrentHashMap() throws InterruptedException {
        UserRepository repo = new UserRepository();

        ExecutorService executorService = Executors.newFixedThreadPool(25);


        ConcurentUserService concurentUserService = new ConcurentUserService();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 25; i++) {
            executorService.submit(() -> {
                User user = concurentUserService.getUser(1);
                System.out.println(Thread.currentThread().getName() + " --> " + user);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        long end = System.currentTimeMillis();


        System.out.println("Time take: " + (end - start) + " ms");

    }
    static void userTTL() throws InterruptedException {
        UserRepository repo = new UserRepository();

        ExecutorService executorService = Executors.newFixedThreadPool(25);


        UserService service = new UserService();

        UserService userService = new UserService();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 25; i++) {
            executorService.submit(() -> {
                User user = userService.getUserTTL(1);
                System.out.println(Thread.currentThread().getName() + " --> " + user);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        long end = System.currentTimeMillis();


        System.out.println("Time take: " + (end - start) + " ms");
    }
}
