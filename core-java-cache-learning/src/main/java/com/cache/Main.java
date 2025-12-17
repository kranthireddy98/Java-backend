package com.cache;

import com.cache.model.User;
import com.cache.repository.UserRepository;
import com.cache.service.ConcurentUserService;
import com.cache.service.UserService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {


        UserRepository repo = new UserRepository();

        ExecutorService executorService = Executors.newFixedThreadPool(25);


        UserService service = new UserService();

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

        userTTL();
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
